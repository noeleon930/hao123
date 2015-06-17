package kddjavatoolchain.DataProcess;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

        courses = new LinkedHashMap<>();

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
}
