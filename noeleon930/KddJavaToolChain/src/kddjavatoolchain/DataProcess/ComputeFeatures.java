package kddjavatoolchain.DataProcess;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import kddjavatoolchain.DataFormat.Course;
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
                .map(log -> log.split(",")[3])
                .collect(Collectors.groupingBy(event -> event, Collectors.counting()));

        e.getFeatures().add((float) feature7_map.getOrDefault("nagivate", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("page_close", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("access", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("video", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("wiki", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("problem", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("discussion", 0L));
    }

    public static void BasicSevenKnownFeatures(EnrollmentLog e)
    {
        Map<String, Module> modules_map = kddjavatoolchain.KddJavaToolChain.getModules_map();

        Map<String, Long> feature7_map
                = e.getSortedLogs()
                .stream()
                .filter(log -> modules_map.get(log.split(",")[4]) != null)
                .map(log -> log.split(",")[3])
                .collect(Collectors.groupingBy(event -> event, Collectors.counting()));

        e.getFeatures().add((float) feature7_map.getOrDefault("nagivate", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("page_close", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("access", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("video", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("wiki", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("problem", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("discussion", 0L));
    }

    public static void BasicSevenUnknownFeatures(EnrollmentLog e)
    {
        Map<String, Module> modules_map = kddjavatoolchain.KddJavaToolChain.getModules_map();

        Map<String, Long> feature7_map
                = e.getSortedLogs()
                .stream()
                .filter(log -> modules_map.get(log.split(",")[4]) == null)
                .map(log -> log.split(",")[3])
                .collect(Collectors.groupingBy(event -> event, Collectors.counting()));

        e.getFeatures().add((float) feature7_map.getOrDefault("nagivate", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("page_close", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("access", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("video", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("wiki", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("problem", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("discussion", 0L));
    }

    public static void StudentHadOtherCourses(EnrollmentLog e)
    {
        String student_id = e.getUsername();

        int answer = ComputeStudents.students.get(student_id).getCourses_num();
        answer = answer - 1;

        e.getFeatures().add((float) answer);
    }

    public static void StudentDropouttedOtherCourses(EnrollmentLog e)
    {
        String student_id = e.getUsername();

        int answer = ComputeStudents.students.get(student_id).getDropouts();
        answer = answer - e.getResult() + 1;

        e.getFeatures().add((float) Math.log10(Math.log10(answer) + 10));
    }

    public static void CourseDropouttedStudents(EnrollmentLog e)
    {
        String course_id = e.getCourse_id();

        int answer = ComputeCourses.courses.get(course_id).getDropouts();
        answer = answer - 1;

        e.getFeatures().add((float) Math.log10(Math.log10(answer)));
//        e.getFeatures().add((float) answer);
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

    public static void LastWeekSevenFeatures(EnrollmentLog e)
    {
        Instant oneWeekAgo = e.getTimeLine().get(e.getTimeLine().size() - 1).minusSeconds(604800);

        Map<String, Long> feature7_map
                = e.getSortedLogs()
                .stream()
                .filter(log -> Instant.parse(log.split(",")[1] + "Z").isAfter(oneWeekAgo))
                .map(log -> log.split(",")[3])
                .collect(Collectors.groupingBy(event -> event, Collectors.counting()));

        e.getFeatures().add((float) feature7_map.getOrDefault("nagivate", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("page_close", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("access", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("video", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("wiki", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("problem", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("discussion", 0L));
    }

    public static void LastWeekSevenKnownFeatures(EnrollmentLog e)
    {
        Instant oneWeekAgo = e.getTimeLine().get(e.getTimeLine().size() - 1).minusSeconds(604800);

        Map<String, Module> modules_map = kddjavatoolchain.KddJavaToolChain.getModules_map();

        Map<String, Long> feature7_map
                = e.getSortedLogs()
                .stream()
                .filter(log -> modules_map.get(log.split(",")[4]) != null)
                .filter(log -> Instant.parse(log.split(",")[1] + "Z").isAfter(oneWeekAgo))
                .map(log -> log.split(",")[3])
                .collect(Collectors.groupingBy(event -> event, Collectors.counting()));

        e.getFeatures().add((float) feature7_map.getOrDefault("nagivate", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("page_close", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("access", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("video", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("wiki", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("problem", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("discussion", 0L));
    }

    public static void LastWeekSevenUnknownFeatures(EnrollmentLog e)
    {
        Instant oneWeekAgo = e.getTimeLine().get(e.getTimeLine().size() - 1).minusSeconds(604800);

        Map<String, Module> modules_map = kddjavatoolchain.KddJavaToolChain.getModules_map();

        Map<String, Long> feature7_map
                = e.getSortedLogs()
                .stream()
                .filter(log -> modules_map.get(log.split(",")[4]) == null)
                .filter(log -> Instant.parse(log.split(",")[1] + "Z").isAfter(oneWeekAgo))
                .map(log -> log.split(",")[3])
                .collect(Collectors.groupingBy(event -> event, Collectors.counting()));

        e.getFeatures().add((float) feature7_map.getOrDefault("nagivate", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("page_close", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("access", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("video", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("wiki", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("problem", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("discussion", 0L));
    }

    public static void LastTwoWeekSevenFeatures(EnrollmentLog e)
    {
        Instant twoWeekAgo = e.getTimeLine().get(e.getTimeLine().size() - 1).minusSeconds(1209600);

        Map<String, Long> feature7_map
                = e.getSortedLogs()
                .stream()
                .filter(log -> Instant.parse(log.split(",")[1] + "Z").isAfter(twoWeekAgo))
                .map(log -> log.split(",")[3])
                .collect(Collectors.groupingBy(event -> event, Collectors.counting()));

        e.getFeatures().add((float) feature7_map.getOrDefault("nagivate", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("page_close", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("access", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("video", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("wiki", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("problem", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("discussion", 0L));
    }

    public static void LastTwoWeekSevenKnownFeatures(EnrollmentLog e)
    {
        Instant twoWeekAgo = e.getTimeLine().get(e.getTimeLine().size() - 1).minusSeconds(1209600);

        Map<String, Module> modules_map = kddjavatoolchain.KddJavaToolChain.getModules_map();

        Map<String, Long> feature7_map
                = e.getSortedLogs()
                .stream()
                .filter(log -> modules_map.get(log.split(",")[4]) != null)
                .filter(log -> Instant.parse(log.split(",")[1] + "Z").isAfter(twoWeekAgo))
                .map(log -> log.split(",")[3])
                .collect(Collectors.groupingBy(event -> event, Collectors.counting()));

        e.getFeatures().add((float) feature7_map.getOrDefault("nagivate", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("page_close", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("access", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("video", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("wiki", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("problem", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("discussion", 0L));
    }

    public static void LastTwoWeekSevenUnknownFeatures(EnrollmentLog e)
    {
        Instant twoWeekAgo = e.getTimeLine().get(e.getTimeLine().size() - 1).minusSeconds(1209600);

        Map<String, Module> modules_map = kddjavatoolchain.KddJavaToolChain.getModules_map();

        Map<String, Long> feature7_map
                = e.getSortedLogs()
                .stream()
                .filter(log -> modules_map.get(log.split(",")[4]) == null)
                .filter(log -> Instant.parse(log.split(",")[1] + "Z").isAfter(twoWeekAgo))
                .map(log -> log.split(",")[3])
                .collect(Collectors.groupingBy(event -> event, Collectors.counting()));

        e.getFeatures().add((float) feature7_map.getOrDefault("nagivate", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("page_close", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("access", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("video", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("wiki", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("problem", 0L));
        e.getFeatures().add((float) feature7_map.getOrDefault("discussion", 0L));
    }

    public static void ModulesAccessed(EnrollmentLog e)
    {
        List<Module> modules_list = e.getModules();
        Map<String, Module> modules_map = kddjavatoolchain.KddJavaToolChain.getModules_map();

        Map<String, Integer> countsMap = new LinkedHashMap<>();

        modules_list
                .stream()
                .sequential()
                .peek((m) ->
                        {
                            String theCategory;

                            if (modules_map.get(m.getModule_id()) == null)
                            {
                                theCategory = "unknown";
                            }
                            else
                            {
                                theCategory = modules_map.get(m.getModule_id()).getCategory();
                            }

                            countsMap.putIfAbsent(theCategory, 0);
                })
                .forEachOrdered((m) ->
                        {
                            String theCategory;

                            if (modules_map.get(m.getModule_id()) == null)
                            {
                                theCategory = "unknown";
                            }
                            else
                            {
                                theCategory = modules_map.get(m.getModule_id()).getCategory();
                            }

                            countsMap.computeIfPresent(theCategory, (k, v) -> v + 1);
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
        e.getFeatures().add((float) countsMap.getOrDefault("unknown", 0));
    }

    public static void DurationSevenFeatures(EnrollmentLog e)
    {
        Map<String, Module> modules_map = kddjavatoolchain.KddJavaToolChain.getModules_map();

        List<Instant> timeline = e.getTimeLine();
        List<String> sortedlogs = e.getSortedLogs();

        ConcurrentHashMap<String, LongAdder> knownDurationMap = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, LongAdder> unknownDurationMap = new ConcurrentHashMap<>();
//        ConcurrentHashMap<String, LongAdder> durationMap = new ConcurrentHashMap<>();

        int listSize = timeline.size();
        if (listSize != sortedlogs.size())
        {
            System.out.println("Fucked up! : " + e.getID());
            System.exit(1688);
        }

        for (int i = 0; i < listSize; i++)
        {
            String event = sortedlogs.get(i).split(",")[3];
            String module = sortedlogs.get(i).split(",")[4];

//            if (i + 1 == listSize)
//            {
//                durationMap.putIfAbsent(event, new LongAdder());
//                durationMap.get(event).add(0);
//            }
//            else
//            {
//                long second = Duration.between(timeline.get(i), timeline.get(i + 1)).getSeconds();
//
//                if (second < 0)
//                {
//                    second = 0;
//                }
//
//                if (second > 3600)
//                {
//                    second = 0;
//                }
//
//                durationMap.putIfAbsent(event, new LongAdder());
//                durationMap.get(event).add(second);
//            }
            if (modules_map.get(module) != null)
            {
                if (i + 1 == listSize)
                {
                    knownDurationMap.putIfAbsent(event, new LongAdder());
                    knownDurationMap.get(event).add(0);
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

                    knownDurationMap.putIfAbsent(event, new LongAdder());
                    knownDurationMap.get(event).add(second);
                }
            }
            else
            {
                if (i + 1 == listSize)
                {
                    unknownDurationMap.putIfAbsent(event, new LongAdder());
                    unknownDurationMap.get(event).add(0);
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

                    unknownDurationMap.putIfAbsent(event, new LongAdder());
                    unknownDurationMap.get(event).add(second);
                }
            }
        }

//        e.getFeatures().add((float) Math.log(durationMap.getOrDefault("nagivate", new LongAdder()).longValue() + 1));
//        e.getFeatures().add((float) Math.log(durationMap.getOrDefault("page_close", new LongAdder()).longValue() + 1));
//        e.getFeatures().add((float) Math.log(durationMap.getOrDefault("access", new LongAdder()).longValue() + 1));
//        e.getFeatures().add((float) Math.log(durationMap.getOrDefault("video", new LongAdder()).longValue() + 1));
//        e.getFeatures().add((float) Math.log(durationMap.getOrDefault("wiki", new LongAdder()).longValue() + 1));
//        e.getFeatures().add((float) Math.log(durationMap.getOrDefault("problem", new LongAdder()).longValue() + 1));
//        e.getFeatures().add((float) Math.log(durationMap.getOrDefault("discussion", new LongAdder()).longValue() + 1));
//
//        double total
//                = Math.log(durationMap.getOrDefault("nagivate", new LongAdder()).longValue() + 1)
//                + Math.log(durationMap.getOrDefault("page_close", new LongAdder()).longValue() + 1)
//                + Math.log(durationMap.getOrDefault("access", new LongAdder()).longValue() + 1)
//                + Math.log(durationMap.getOrDefault("video", new LongAdder()).longValue() + 1)
//                + Math.log(durationMap.getOrDefault("wiki", new LongAdder()).longValue() + 1)
//                + Math.log(durationMap.getOrDefault("problem", new LongAdder()).longValue() + 1)
//                + Math.log(durationMap.getOrDefault("discussion", new LongAdder()).longValue() + 1);
//
//        e.getFeatures().add((float) total);
        e.getFeatures().add((float) Math.log(knownDurationMap.getOrDefault("nagivate", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(knownDurationMap.getOrDefault("page_close", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(knownDurationMap.getOrDefault("access", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(knownDurationMap.getOrDefault("video", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(knownDurationMap.getOrDefault("wiki", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(knownDurationMap.getOrDefault("problem", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(knownDurationMap.getOrDefault("discussion", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(unknownDurationMap.getOrDefault("nagivate", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(unknownDurationMap.getOrDefault("page_close", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(unknownDurationMap.getOrDefault("access", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(unknownDurationMap.getOrDefault("video", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(unknownDurationMap.getOrDefault("wiki", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(unknownDurationMap.getOrDefault("problem", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(unknownDurationMap.getOrDefault("discussion", new LongAdder()).longValue() + 1));
    }

    public static void Basic7x7Matrix(EnrollmentLog e)
    {
        int[][] matrix = new int[7][7];
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                matrix[i][j] = 0;
            }
        }

        List<Integer> events
                = e.getUniquedLogs()
                .stream()
                .sequential()
                .map(str -> str.split(",")[3])
                .map(event ->
                        {
                            switch (event)
                            {
                                case "nagivate":
                                    return 0;
                                case "page_close":
                                    return 1;
                                case "access":
                                    return 2;
                                case "video":
                                    return 3;
                                case "wiki":
                                    return 4;
                                case "problem":
                                    return 5;
                                case "discussion":
                                    return 6;
                                default:
                                    return -1;
                            }
                })
                .collect(Collectors.toList());

        int listSize = events.size();
        for (int i = 0; i < listSize; i++)
        {
            if (i + 1 == listSize)
            {
                break;
            }
            else
            {
                matrix[events.get(i)][events.get(i + 1)] += 1;
            }
        }

        List<Float> to49d = e.getFeatures();

        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                to49d.add((float) Math.log(matrix[i][j] + 1));
            }
        }
    }

    public static void StudentTimeLines(EnrollmentLog e)
    {
        String student_id = e.getUsername();

        Set<String> DateSet = e.getSortedLogs()
                .stream()
                .map(s -> s.split(",")[1].split("T")[0])
                .distinct()
                .collect(Collectors.toSet());

        List<String> studenttimeline = ComputeStudents.students.get(student_id).getSortedtimeline();
        Map<Integer, Boolean> DateMap = new LinkedHashMap<>();

        int timelineSize = studenttimeline.size();
        for (int i = 0; i < timelineSize; i++)
        {
            if (DateSet.contains(studenttimeline.get(i)))
            {
                DateMap.putIfAbsent(i + 1, Boolean.TRUE);
            }
        }

        int totalScore = IntStream.rangeClosed(1, timelineSize).sum();
        int availableScore = DateMap.entrySet()
                .stream()
                .mapToInt(entry -> entry.getKey())
                .sum();

        // Check if the course student's first one or last one
        float isfirst = 0.0f;
        float islast = 0.0f;

        if (e.getSortedLogs()
                .get(0)
                .split(",")[1]
                .split("T")[0]
                .equals(
                        studenttimeline
                        .get(0)
                ))
        {
            isfirst = 1.0f;
        }
        else if (e.getSortedLogs()
                .get(
                        e.getSortedLogs().size() - 1
                )
                .split(",")[1]
                .split("T")[0]
                .equals(
                        studenttimeline
                        .get(
                                studenttimeline.size() - 1
                        )
                ))
        {
            islast = 1.0f;
        }

        // Count how many courses did this student take
        EnrollmentLog thisCourse
                = ComputeStudents.studentsToEnrollmentLogMap.get(student_id)
                .stream()
                .filter(enrlg -> enrlg.getCourse_id().equals(e.getCourse_id()))
                .findFirst().get();

        long sameDaysCoursesCounts
                = ComputeStudents.studentsToEnrollmentLogMap.get(student_id)
                .stream()
                .filter(enrlg -> !enrlg.getCourse_id().equals(e.getCourse_id()))
                .map(enrlg -> enrlg.getTimeLine())
                .map(times ->
                        {
                            if (times.get(0).isBefore(thisCourse.getTimeLine().get(0)))
                            {
                                if (!times.get(times.size() - 1).isBefore(thisCourse.getTimeLine().get(0)))
                                {
                                    return 1;
                                }
                            }
                            else
                            {
                                if (!times.get(0).isAfter(thisCourse.getTimeLine().get(thisCourse.getTimeLine().size() - 1)))
                                {
                                    return 1;
                                }
                            }

                            return 0;
                })
                .filter(i -> i == 1)
                .count();

        e.getFeatures().add((float) availableScore);
        e.getFeatures().add((float) totalScore);
        e.getFeatures().add(isfirst);
        e.getFeatures().add(islast);
        e.getFeatures().add((float) sameDaysCoursesCounts);

    }

    public static void CourseTimeLines(EnrollmentLog e)
    {
        String course_id = e.getCourse_id();
        Course theCourse = ComputeCourses.courses.get(course_id);

        Instant startTime = theCourse.getStartTime();
        Instant endTime = theCourse.getEndTime();

        long DateCount = e.getTimeLine()
                .stream()
                .map(in -> in.toString().split("T")[0])
                .distinct()
                .count();

        Map<String, Long> weekdaysCount = e.getTimeLine()
                .stream()
                .map(in -> in.toString().split("T")[0])
                .distinct()
                .map(str -> Instant.parse(str + "T06:00:00Z"))
                .map(in -> LocalDateTime.ofInstant(in, ZoneId.of("Z")).toLocalDate())
                .collect(Collectors.groupingBy(date -> date.getDayOfWeek().toString(), Collectors.counting()));

        Instant stuStartTime = e.getTimeLine().get(0);
        Instant stuEndTime = e.getTimeLine().get(e.getTimeLine().size() - 1);
        long afterCourseStart = Duration.between(startTime, stuStartTime).toDays();
        long beforeCourseEnd = Duration.between(stuEndTime, endTime).toDays();

        Map<String, Long> typeCount = e.getTimeLine()
                .stream()
                .map(in -> in.toString())
                .map(hms -> hms.split(":")[0])
                .distinct()
                .map(str -> str.split("T")[1])
                .map(h -> Integer.parseInt(h))
                .map(h ->
                        {
                            if (h >= 2 && h <= 5)
                            {
                                return "night_2";
                            }
                            else if (h > 5 && h <= 9)
                            {
                                return "morning_1";
                            }
                            else if (h > 9 && h <= 13)
                            {
                                return "morning_2";
                            }
                            else if (h > 13 && h <= 17)
                            {
                                return "afternoon_1";
                            }
                            else if (h > 17 && h <= 21)
                            {
                                return "afternoon_2";
                            }
                            else
                            {
                                return "night_1";
                            }
                })
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()));

        // Week days counts
        e.getFeatures().add((float) weekdaysCount.getOrDefault("MONDAY", 0L));
        e.getFeatures().add((float) weekdaysCount.getOrDefault("TUESDAY", 0L));
        e.getFeatures().add((float) weekdaysCount.getOrDefault("WEDNESDAY", 0L));
        e.getFeatures().add((float) weekdaysCount.getOrDefault("THURSDAY", 0L));
        e.getFeatures().add((float) weekdaysCount.getOrDefault("FRIDAY", 0L));
        e.getFeatures().add((float) weekdaysCount.getOrDefault("SATURDAY", 0L));
        e.getFeatures().add((float) weekdaysCount.getOrDefault("SUNDAY", 0L));

        // Student active in Course available days
        e.getFeatures().add((float) afterCourseStart);
        e.getFeatures().add((float) DateCount);
        e.getFeatures().add((float) beforeCourseEnd);

        // Student active type in morning afternoon night
        e.getFeatures().add((float) typeCount.getOrDefault("morning_1", 0L));
        e.getFeatures().add((float) typeCount.getOrDefault("morning_2", 0L));
        e.getFeatures().add((float) typeCount.getOrDefault("afternoon_1", 0L));
        e.getFeatures().add((float) typeCount.getOrDefault("afternoon_2", 0L));
        e.getFeatures().add((float) typeCount.getOrDefault("night_1", 0L));
        e.getFeatures().add((float) typeCount.getOrDefault("night_2", 0L));
    }

    public static void CourseTimeLinesForSpecialDays(EnrollmentLog e)
    {
        Instant stuEndTime = e.getTimeLine().get(e.getTimeLine().size() - 1);

        Instant exam;

        float answer;

        int stuEndTimeYear = Integer.parseInt(stuEndTime.toString().split("T")[0].split("-")[0]);
        int stuEndTimeMonth = Integer.parseInt(stuEndTime.toString().split("T")[0].split("-")[1]);

        if (stuEndTimeMonth > 9 && stuEndTimeMonth <= 11)
        {
            exam = Instant.parse((stuEndTimeYear + 1) + "-01-06T12:00:00Z");
        }
        else if (stuEndTimeMonth == 12)
        {
            exam = Instant.parse((stuEndTimeYear + 1) + "-01-06T12:00:00Z");
        }
        else if (stuEndTimeMonth == 1)
        {
            exam = Instant.parse(stuEndTimeYear + "-01-30T12:00:00Z");
        }
        else if (stuEndTimeMonth == 2)
        {
            exam = Instant.parse(stuEndTimeYear + "-02-23T12:00:00Z");
        }
        else if (stuEndTimeMonth > 2 && stuEndTimeMonth <= 4)
        {
            exam = Instant.parse(stuEndTimeYear + "-06-22T12:00:00Z");
        }
        else if (stuEndTimeMonth > 4 && stuEndTimeMonth <= 6)
        {
            exam = Instant.parse(stuEndTimeYear + "-06-22T12:00:00Z");
        }
        else if (stuEndTimeMonth == 7)
        {
            exam = Instant.parse(stuEndTimeYear + "-07-20T12:00:00Z");
        }
        else if (stuEndTimeMonth == 9)
        {
            exam = Instant.parse(stuEndTimeYear + "-09-15T12:00:00Z");
        }
        else
        {
            exam = Instant.parse(stuEndTimeYear + "-08-10T12:00:00Z");
        }

        answer = (float) Math.abs(Duration.between(stuEndTime, exam).toDays());

        e.getFeatures().add(answer);
    }

    public static void AccessAndProblemKnownFromServerOrBrowser(EnrollmentLog e)
    {
        Map<String, Module> modules_map = kddjavatoolchain.KddJavaToolChain.getModules_map();

        Map<String, Long> collectted
                = e.getRawLogs()
                .stream()
                .filter(log -> modules_map.get(log.split(",")[4]) != null)
                .map(str -> str.split(","))
                .map(sarr -> sarr[2] + "," + sarr[3])
                .collect(Collectors.groupingBy(event -> event, Collectors.counting()));

        e.getFeatures().add((float) collectted.getOrDefault("server,access", 0L));
        e.getFeatures().add((float) collectted.getOrDefault("server,problem", 0L));
        e.getFeatures().add((float) collectted.getOrDefault("browser,access", 0L));
        e.getFeatures().add((float) collectted.getOrDefault("browser,problem", 0L));
    }

    public static void AccessAndProblemUnknownFromServerOrBrowser(EnrollmentLog e)
    {
        Map<String, Module> modules_map = kddjavatoolchain.KddJavaToolChain.getModules_map();

        Map<String, Long> collectted
                = e.getRawLogs()
                .stream()
                .filter(log -> modules_map.get(log.split(",")[4]) == null)
                .map(str -> str.split(","))
                .map(sarr -> sarr[2] + "," + sarr[3])
                .collect(Collectors.groupingBy(event -> event, Collectors.counting()));

        e.getFeatures().add((float) collectted.getOrDefault("server,access", 0L));
        e.getFeatures().add((float) collectted.getOrDefault("server,problem", 0L));
        e.getFeatures().add((float) collectted.getOrDefault("browser,access", 0L));
        e.getFeatures().add((float) collectted.getOrDefault("browser,problem", 0L));
    }

    public static void EnrollmentTimeLine30(EnrollmentLog e)
    {
        Map<String, String> DayToEventsMap
                = e.getUniquedLogs()
                .stream()
                .map(log -> log.split(","))
                .map(sarr -> sarr[1].split("T")[0] + "," + sarr[3])
                .map(DayAndEvents -> DayAndEvents.split(","))
                .collect(Collectors.toMap(DayAndEvents -> DayAndEvents[0], DayAndEvents -> DayAndEvents[1], (s1, s2) -> s1 + "," + s2));

        Map<String, Integer> DayToScoreMap
                = DayToEventsMap
                .entrySet()
                .stream()
                .collect(Collectors
                        .toMap(
                                ee -> ee.getKey(),
                                ee ->
                                {
                                    int score
                                    = Stream
                                    .of(ee.getValue().split(","))
                                    .mapToInt(s ->
                                            {
                                                int to;

                                                switch (s)
                                                {
                                                    case "nagivate":
                                                        to = 1;
                                                        break;
                                                    case "page_close":
                                                        to = 1;
                                                        break;
                                                    case "access":
                                                        to = 2;
                                                        break;
                                                    case "video":
                                                        to = 2;
                                                        break;
                                                    case "wiki":
                                                        to = 4;
                                                        break;
                                                    case "problem":
                                                        to = 6;
                                                        break;
                                                    case "discussion":
                                                        to = 8;
                                                        break;
                                                    default:
                                                        to = 0;
                                                        break;
                                                }

                                                return to;
                                    })
                                    .sum();

                                    return score;
                                })
                );

        Course theCourse;
        Instant startTime;
        Instant endTime;

        theCourse = ComputeCourses.courses.get(e.getCourse_id());
        startTime = theCourse.getStartTime();
        endTime = theCourse.getEndTime();

        int days = 0;
        int[] scores = new int[30];

        Instant tmp = startTime;
        while (tmp.isBefore(endTime))
        {
            if (DayToScoreMap.containsKey(tmp.toString().split("T")[0]))
            {
                scores[days] = DayToScoreMap.get(tmp.toString().split("T")[0]);
            }
            else
            {
                scores[days] = 0;
            }

            days++;
            tmp = tmp.plusSeconds(86400);
        }

        e.setTimeline30scores(Arrays.copyOf(scores, scores.length));

        Arrays.stream(scores)
                .forEachOrdered(score -> e.getFeatures().add((float) Math.log(score + 1)));

    }

    public static void LagsAndPlots(EnrollmentLog e)
    {

    }

    public static void LastTwoWeekActiveDays(EnrollmentLog e)
    {
        Instant twoWeekAgo = e.getTimeLine().get(e.getTimeLine().size() - 1).minusSeconds(1209600);

        long count
                = e.getSortedLogs()
                .stream()
                .filter(log -> Instant.parse(log.split(",")[1] + "Z").isAfter(twoWeekAgo))
                .map(log -> log.split(","))
                .map(sarr -> sarr[1])
                .map(time -> time.split("T")[0])
                .distinct()
                .count();

        e.getFeatures().add((float) count);
    }

    public static void LastWeekActiveDays(EnrollmentLog e)
    {
        Instant oneWeekAgo = e.getTimeLine().get(e.getTimeLine().size() - 1).minusSeconds(604800);

        long count
                = e.getSortedLogs()
                .stream()
                .filter(log -> Instant.parse(log.split(",")[1] + "Z").isAfter(oneWeekAgo))
                .map(log -> log.split(","))
                .map(sarr -> sarr[1])
                .map(time -> time.split("T")[0])
                .distinct()
                .count();

        e.getFeatures().add((float) count);
    }

    public static void LastTwoWeekActiveDaysMinusLastWeekActiveDays(EnrollmentLog e)
    {
        Instant twoWeekAgo = e.getTimeLine().get(e.getTimeLine().size() - 1).minusSeconds(1209600);

        long countTwoWeek
                = e.getSortedLogs()
                .stream()
                .filter(log -> Instant.parse(log.split(",")[1] + "Z").isAfter(twoWeekAgo))
                .map(log -> log.split(","))
                .map(sarr -> sarr[1])
                .map(time -> time.split("T")[0])
                .distinct()
                .count();

        Instant oneWeekAgo = e.getTimeLine().get(e.getTimeLine().size() - 1).minusSeconds(604800);

        long countOneWeek
                = e.getSortedLogs()
                .stream()
                .filter(log -> Instant.parse(log.split(",")[1] + "Z").isAfter(oneWeekAgo))
                .map(log -> log.split(","))
                .map(sarr -> sarr[1])
                .map(time -> time.split("T")[0])
                .distinct()
                .count();

        e.getFeatures().add((float) (countTwoWeek - countOneWeek));
    }
}
