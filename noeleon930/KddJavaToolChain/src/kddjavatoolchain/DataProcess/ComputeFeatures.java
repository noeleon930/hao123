package kddjavatoolchain.DataProcess;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import kddjavatoolchain.DataFormat.EnrollmentLog;
import kddjavatoolchain.DataFormat.Module;

/**
 *
 * @author Noel
 */
public class ComputeFeatures
{

    public static void BasicSevenFeatures(EnrollmentLog e)
    {
        Map<String, Long> feature7_map
                = e.getSortedLogs()
                .stream()
                .sequential()
                .map(log -> log.split(",")[3])
                .collect(Collectors.groupingBy(event -> event, Collectors.counting()));

        e.getFeatures().add((float) feature7_map.getOrDefault("nagivate", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("page_close", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("video", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("access", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("wiki", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("problem", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("discussion", 0L));
    }

    public static void StudentHadCourses(EnrollmentLog e)
    {
        String student_id = e.getUsername();

        int answer = ComputeStudents.students.get(student_id).getCourses_num();

        e.getFeatures().add((float) answer);
    }

    public static void StudentDropouttedCourses(EnrollmentLog e)
    {
        String student_id = e.getUsername();

        int answer = ComputeStudents.students.get(student_id).getDropouts();

        e.getFeatures().add((float) answer);
    }

    public static void CourseHadStudents(EnrollmentLog e)
    {
        String course_id = e.getCourse_id();

        int answer = ComputeCourses.courses.get(course_id).getStudents_num();

        e.getFeatures().add((float) answer / 10.0f);
    }

    public static void CourseDropouttedStudents(EnrollmentLog e)
    {
        String course_id = e.getCourse_id();

        int answer = ComputeCourses.courses.get(course_id).getDropouts();

        e.getFeatures().add((float) answer / 10.0f);
    }

    public static void CourseObjectFeatures(EnrollmentLog e)
    {
        Map<String, Long> objectCategories_map
                = ComputeCourses.courses.get(e.getCourse_id())
                .getModules()
                .stream()
                .collect(Collectors.groupingBy(m -> m.getCategory(), Collectors.counting()));

        e.getFeatures().add((float) objectCategories_map.getOrDefault("about", 0L));
        e.getFeatures().add((float) objectCategories_map.getOrDefault("chapter", 0L));
        e.getFeatures().add((float) objectCategories_map.getOrDefault("course", 0L));
        e.getFeatures().add((float) objectCategories_map.getOrDefault("course_info", 0L));
        e.getFeatures().add((float) objectCategories_map.getOrDefault("html", 0L));
        e.getFeatures().add((float) objectCategories_map.getOrDefault("outlink", 0L));
        e.getFeatures().add((float) objectCategories_map.getOrDefault("problem", 0L));
        e.getFeatures().add((float) objectCategories_map.getOrDefault("sequential", 0L));
        e.getFeatures().add((float) objectCategories_map.getOrDefault("static_tab", 0L));
        e.getFeatures().add((float) objectCategories_map.getOrDefault("vertical", 0L));
        e.getFeatures().add((float) objectCategories_map.getOrDefault("video", 0L));
        e.getFeatures().add((float) objectCategories_map.getOrDefault("combinedopenended", 0L));
        e.getFeatures().add((float) objectCategories_map.getOrDefault("peergrading", 0L));
        e.getFeatures().add((float) objectCategories_map.getOrDefault("discussion", 0L));
        e.getFeatures().add((float) objectCategories_map.getOrDefault("dictation", 0L));
    }

    public static void LastLoginSevenFeatures(EnrollmentLog e)
    {
        // I'm fucked up here QAQ
    }

    public static void ModulesAccessed(EnrollmentLog e)
    {
        List<Module> modules_list = e.getModules();
        Map<String, Module> modules_map = kddjavatoolchain.KddJavaToolChain.getModules_map();

        Map<String, Integer> countsMap = new LinkedHashMap<>();

        modules_list
                .stream()
                .filter((m) -> (modules_map.containsKey(m.getModule_id())))
                .sequential()
                .map((m) ->
                        {
                            countsMap.putIfAbsent(modules_map.get(m.getModule_id()).getCategory(), 0);
                            return m;
                })
                .forEachOrdered((m) ->
                        {
                            countsMap.computeIfPresent(modules_map.get(m.getModule_id()).getCategory(), (k, v) -> v + 1);
                });

        e.getFeatures().add((float) countsMap.getOrDefault("about", 0));
        e.getFeatures().add((float) countsMap.getOrDefault("chapter", 0));
        e.getFeatures().add((float) countsMap.getOrDefault("course", 0));
        e.getFeatures().add((float) countsMap.getOrDefault("course_info", 0));
        e.getFeatures().add((float) countsMap.getOrDefault("html", 0));
        e.getFeatures().add((float) countsMap.getOrDefault("outlink", 0));
        e.getFeatures().add((float) countsMap.getOrDefault("problem", 0));
        e.getFeatures().add((float) countsMap.getOrDefault("sequential", 0));
        e.getFeatures().add((float) countsMap.getOrDefault("static_tab", 0));
        e.getFeatures().add((float) countsMap.getOrDefault("vertical", 0));
        e.getFeatures().add((float) countsMap.getOrDefault("video", 0));
        e.getFeatures().add((float) countsMap.getOrDefault("combinedopenended", 0));
        e.getFeatures().add((float) countsMap.getOrDefault("peergrading", 0));
        e.getFeatures().add((float) countsMap.getOrDefault("discussion", 0));
        e.getFeatures().add((float) countsMap.getOrDefault("dictation", 0));
    }

    public static void SevenFeaturesDuration(EnrollmentLog e)
    {
        List<Instant> timeline = e.getTimeLine();
        List<String> sortedlogs = e.getSortedLogs();

        ConcurrentHashMap<String, LongAdder> durationMap = new ConcurrentHashMap<>();

        int listSize = timeline.size();
        if (listSize != sortedlogs.size())
        {
            System.out.println("Fucked up! : " + e.getID());
            System.exit(1688);
        }

        for (int i = 0; i < listSize; i++)
        {
            String event = sortedlogs.get(i).split(",")[3];

            if (i + 1 == listSize)
            {
                durationMap.putIfAbsent(event, new LongAdder());
                durationMap.get(event).add(0);
            }
            else
            {
                long second = Duration.between(timeline.get(i), timeline.get(i + 1)).getSeconds();

                if (second < 0)
                {
                    second = 0;
                }

                if (second > 3600)
                {
                    second = 0;
                }

                durationMap.putIfAbsent(event, new LongAdder());
                durationMap.get(event).add(second);
            }
        }

        e.getFeatures().add(durationMap.getOrDefault("nagivate", new LongAdder()).floatValue() / 10.0f);
        e.getFeatures().add(durationMap.getOrDefault("page_close", new LongAdder()).floatValue() / 10.0f);
        e.getFeatures().add(durationMap.getOrDefault("video", new LongAdder()).floatValue() / 10.0f);
        e.getFeatures().add(durationMap.getOrDefault("access", new LongAdder()).floatValue() / 10.0f);
        e.getFeatures().add(durationMap.getOrDefault("wiki", new LongAdder()).floatValue() / 10.0f);
        e.getFeatures().add(durationMap.getOrDefault("problem", new LongAdder()).floatValue() / 10.0f);
        e.getFeatures().add(durationMap.getOrDefault("discussion", new LongAdder()).floatValue() / 10.0f);
    }
}
