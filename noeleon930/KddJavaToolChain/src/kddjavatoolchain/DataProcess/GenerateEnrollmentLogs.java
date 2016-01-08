package kddjavatoolchain.DataProcess;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kddjavatoolchain.DataFormat.EnrollmentLog;

/**
 *
 * @author Noel
 */
public class GenerateEnrollmentLogs
{

    public static Map<Integer, EnrollmentLog> GenerateEnrollmentClass(List<String> inputList)
    {
        Map<Integer, EnrollmentLog> IdToEnrollmentMap
                = inputList
                .parallelStream()
                .collect(Collectors.groupingByConcurrent(s -> Integer.parseInt(s.split(",")[0])))
                .entrySet()
                .parallelStream()
                .map(e -> new EnrollmentLog(e.getKey(), e.getValue()))
                .collect(Collectors.toConcurrentMap(f -> f.getID(), Function.identity()));

        return IdToEnrollmentMap;
    }

    public static void ImportTruthToEnrollments(Map<Integer, Integer> truthMap, Map<Integer, EnrollmentLog> enrollmentMap)
    {
        truthMap.entrySet()
                .parallelStream()
                .forEach((e) ->
                        {
                            enrollmentMap.get(e.getKey()).setResult(e.getValue());
                });
    }

    public static void ImportEnrollmentStudentAndCourseIdtoEnrollments(List<String> enrollmentList, Map<Integer, EnrollmentLog> enrollmentMap)
    {
        enrollmentList
                .parallelStream()
                .map(s -> s.split(","))
                .forEach(s ->
                        {
                            enrollmentMap.get(Integer.parseInt(s[0])).setUsername(s[1]);
                            enrollmentMap.get(Integer.parseInt(s[0])).setCourse_id(s[2]);
                });
    }
}
