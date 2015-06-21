package kddjavatoolchain.DataProcess;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import kddjavatoolchain.DataFormat.Course;
import kddjavatoolchain.DataFormat.EnrollmentLog;
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

    public static void ComputeTimeline()
    {
        ConcurrentMap<String, List<EnrollmentLog>> StudentToTrainEnrollmentMap
                = kddjavatoolchain.KddJavaToolChain
                .getTrain_enrollments_map()
                .entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .collect(Collectors.groupingByConcurrent(e -> e.getUsername()));

        ConcurrentMap<String, List<EnrollmentLog>> StudentToTestEnrollmentMap
                = kddjavatoolchain.KddJavaToolChain
                .getTest_enrollments_map()
                .entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .collect(Collectors.groupingByConcurrent(e -> e.getUsername()));

        ConcurrentMap<String, List<String>> StudentToDatesTrainMap
                = StudentToTrainEnrollmentMap
                .entrySet()
                .parallelStream()
                .collect(Collectors.toConcurrentMap(
                                e -> e.getKey(),
                                e -> e.getValue()
                                .stream()
                                .map(enrlg -> enrlg.getSortedLogs())
                                .flatMap(logs -> logs.stream().map(log -> log.split(",")[1].split("T")[0]).distinct())
                                .distinct()
                                .collect(Collectors.toList())
                        ));

        ConcurrentMap<String, List<String>> StudentToDatesTestMap
                = StudentToTestEnrollmentMap
                .entrySet()
                .parallelStream()
                .collect(Collectors.toConcurrentMap(
                                e -> e.getKey(),
                                e -> e.getValue()
                                .stream()
                                .map(enrlg -> enrlg.getSortedLogs())
                                .flatMap(logs -> logs.stream().map(log -> log.split(",")[1].split("T")[0]).distinct())
                                .distinct()
                                .collect(Collectors.toList())
                        ));

//        StudentToDatesTrainMap.entrySet().stream().sequential().forEachOrdered(e -> System.out.println(e.getKey() + " : " + e.getValue()));
//        StudentToDatesTestMap.entrySet().stream().sequential().forEachOrdered(e -> System.out.println(e.getKey() + " : " + e.getValue()));
//        
        StudentToDatesTrainMap
                .entrySet()
                .parallelStream()
                .forEach(e -> students.get(e.getKey()).getTimeline().addAll(e.getValue()));

        StudentToDatesTestMap
                .entrySet()
                .parallelStream()
                .forEach(e -> students.get(e.getKey()).getTimeline().addAll(e.getValue()));

//        students.entrySet()
//                .stream()
//                .sequential()
//                .map(e -> e.getValue())
//                .forEachOrdered(stu ->
//                        {
//                            System.out.print(stu.getStudent_id() + " : ");
//
//                            stu.getTimeline()
//                            .stream()
//                            .sequential()
//                            .forEachOrdered(s -> System.out.print(s + " "));
//                });
        students.entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .forEach(stu -> stu.importToSortedTimeline());
    }
}
