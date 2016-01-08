package kddjavatoolchain.DataProcess;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import kddjavatoolchain.DataFormat.Course;
import kddjavatoolchain.DataFormat.Module;
import kddjavatoolchain.DataFormat.Student;
import kddjavatoolchain.KddJavaToolChain;

/**
 *
 * @author Noel
 */
public class ComputeCourses
{

    public static Map<String, Course> courses;

    private static class tempCourseRow
    {

        public String course_id;
        public String student_id;

        public tempCourseRow(String course_id, String student_id)
        {
            this.course_id = course_id;
            this.student_id = student_id;
        }
    }

    public static void Compute()
    {
        Map<String, List<Module>> tempModuleMap = KddJavaToolChain
                .getModules_map()
                .entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .collect(Collectors.groupingByConcurrent(mo -> mo.getCourse_id()));

        Map<String, List<tempCourseRow>> tempCourseMap = KddJavaToolChain
                .getTotal_enrollment_total_list(false)
                .parallelStream()
                .map(str -> str.split(","))
                .map(sarr -> new tempCourseRow(sarr[2], sarr[1]))
                .collect(Collectors.groupingByConcurrent(tsr -> tsr.course_id));

        courses = new ConcurrentHashMap<>();

        if (tempModuleMap.size() != tempCourseMap.size())
        {
            System.exit(42);
        }

        tempCourseMap.entrySet().stream().forEach((e) ->
        {
            List<Student> s = new ArrayList<>();
            e.getValue().stream().forEach((tcr) ->
            {
                s.add(new Student(tcr.student_id));
            });
            courses.put(e.getKey(), new Course(e.getKey(), tempModuleMap.get(e.getKey()), s));
        });
    }

    public static void ComputeTimeline()
    {
        ConcurrentMap<String, String> course_date_course_map
                = kddjavatoolchain.KddJavaToolChain.getCourse_date_course_map();

        course_date_course_map
                .entrySet()
                .stream()
                .forEach(e -> courses.get(e.getKey()).setStartTime(Instant.parse(e.getValue().split(",")[0] + "T00:00:01Z")));

        course_date_course_map
                .entrySet()
                .stream()
                .forEach(e -> courses.get(e.getKey()).setEndTime(Instant.parse(e.getValue().split(",")[1] + "T23:59:59Z")));

        //
        //        Map<Integer, EnrollmentLog> train_enrollments_map = kddjavatoolchain.KddJavaToolChain.getTrain_enrollments_map();
        //        Map<Integer, EnrollmentLog> test_enrollments_map = kddjavatoolchain.KddJavaToolChain.getTest_enrollments_map();
        //
        //        Map<Integer, EnrollmentLog> total_enrollments_map = new ConcurrentHashMap<>();
        //        total_enrollments_map.putAll(train_enrollments_map);
        //        total_enrollments_map.putAll(test_enrollments_map);
        //
        //        ConcurrentMap<String, List<Instant>> CourseToInstantTrain
        //                = total_enrollments_map.entrySet()
        //                .parallelStream()
        //                .map(e -> e.getValue())
        //                .collect(Collectors.toConcurrentMap(
        //                                enrlg -> enrlg.getCourse_id(),
        //                                enrlg -> enrlg.getTimeLine(),
        //                                (tl1, tl2) ->
        //                                {
        //                                    List<Instant> tmpInstants = new ArrayList<>();
        //                                    tmpInstants.addAll(tl1);
        //                                    tmpInstants.addAll(tl2);
        //                                    return tmpInstants;
        //                                }));
        //
        //        Map<String, Course> coursesMap = ComputeCourses.courses;
        //
        //        CourseToInstantTrain.entrySet()
        //                .parallelStream()
        //                .forEach(e ->
        //                        {
        //                            Instant startTime = e.getValue().stream().min((in1, in2) -> in1.compareTo(in2)).get();
        //                            Instant endTime = e.getValue().stream().max((in1, in2) -> in1.compareTo(in2)).get();
        //
        //                            coursesMap.get(e.getKey()).setStartTime(startTime);
        //                            coursesMap.get(e.getKey()).setEndTime(endTime);
        //                });
//        courses.entrySet()
//                .stream()
//                .sequential()
//                .forEachOrdered(e -> System.out.println(e.getKey() + " : " + e.getValue().getStartTime() + " -> " + e.getValue().getEndTime()));
    }
}
