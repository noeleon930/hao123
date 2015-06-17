package kddjavatoolchain.DataProcess;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kddjavatoolchain.DataFormat.Course;
import kddjavatoolchain.DataFormat.Student;
import kddjavatoolchain.KddJavaToolChain;

/**
 *
 * @author Noel
 */
public class ComputeStudents
{

    public static Map<String, Student> students;

    private static class tempStudentRow
    {

        public String student_id;
        public String course_id;

        public tempStudentRow(String student_id, String course_id)
        {
            this.student_id = student_id;
            this.course_id = course_id;
        }
    }

    public static void Compute()
    {
        Map<String, List<tempStudentRow>> tempMap = KddJavaToolChain
                .getTotal_enrollment_total_list(false)
                .parallelStream()
                .map(str -> str.split(","))
                .map(sarr -> new tempStudentRow(sarr[1], sarr[2]))
                .collect(Collectors.groupingByConcurrent(tsr -> tsr.student_id));

        students = new LinkedHashMap<>();

        tempMap.entrySet().stream().forEach((e) ->
        {
            List<Course> c = new ArrayList<>();
            e.getValue().stream().forEach((tsr) ->
            {
                c.add(new Course(tsr.course_id));
            });
            students.put(e.getKey(), new Student(e.getKey(), c));
        });
    }
}
