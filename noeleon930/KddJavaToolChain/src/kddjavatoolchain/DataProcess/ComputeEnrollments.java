package kddjavatoolchain.DataProcess;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.stream.Collectors;
import kddjavatoolchain.DataFormat.Course;
import kddjavatoolchain.DataFormat.Enrollment;
import kddjavatoolchain.DataFormat.Student;

/**
 *
 * @author Noel
 */
public class ComputeEnrollments
{

    public static Map<Integer, Enrollment> enrollments;

    public static void Compute()
    {
        List<String> enrollmentList = kddjavatoolchain.KddJavaToolChain.getTotal_enrollment_total_list(false);
        Map<Integer, Integer> truthMap = kddjavatoolchain.KddJavaToolChain.getTrain_truth_train_map();

        enrollments
                = enrollmentList
                .parallelStream()
                .map(str -> str.split(","))
                .map(sarr -> new Enrollment(Integer.parseInt(sarr[0]), sarr[1], sarr[2]))
                .collect(Collectors.toConcurrentMap(e -> e.getEnrollemt_id(), Function.identity()));

        enrollments
                .entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .forEach(e -> e.setResult(truthMap.getOrDefault(e.getEnrollemt_id(), -1)));

        ComputeDropoutForStudentsAndCourses();
    }

    public static void ComputeDropoutForStudentsAndCourses()
    {
        Map<String, Student> tempStuMap = ComputeStudents.students;
        Map<String, Course> tempCourseMap = ComputeCourses.courses;

        ConcurrentHashMap<String, LongAdder> StudentToDropoutMap = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, LongAdder> CourseToDropoutMap = new ConcurrentHashMap<>();

        enrollments
                .entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .forEach(e ->
                        {
                            if (e.getResult() == 1)
                            {
                                StudentToDropoutMap.putIfAbsent(e.getUsername(), new LongAdder());
                                StudentToDropoutMap.get(e.getUsername()).increment();

                                CourseToDropoutMap.putIfAbsent(e.getCourse_id(), new LongAdder());
                                CourseToDropoutMap.get(e.getCourse_id()).increment();
                            }
                });

        StudentToDropoutMap.entrySet().parallelStream().forEach(e ->
        {
            tempStuMap.get(e.getKey()).setDropouts(e.getValue().intValue());
        });

        CourseToDropoutMap.entrySet().parallelStream().forEach(e ->
        {
            tempCourseMap.get(e.getKey()).setDropouts(e.getValue().intValue());
        });

    }

}
