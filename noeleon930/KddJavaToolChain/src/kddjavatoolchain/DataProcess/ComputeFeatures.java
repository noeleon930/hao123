package kddjavatoolchain.DataProcess;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
                .sequential()
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

    public static void CourseHadStudentsPercent(EnrollmentLog e)
    {
        String course_id = e.getCourse_id();

        int sum = ComputeCourses.courses.get(course_id).getStudents_num();
        int dropouts = ComputeCourses.courses.get(course_id).getDropouts();

        e.getFeatures().add((((float) dropouts) / ((float) sum)) * 100.f);
    }

    public static void CourseDropouttedStudents(EnrollmentLog e)
    {
        String course_id = e.getCourse_id();

        int answer = ComputeCourses.courses.get(course_id).getDropouts();

        e.getFeatures().add((float) Math.log(answer));
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
                .sequential()
                .map((m) ->
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
                            return m;
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

        e.getFeatures().add((float) Math.log(durationMap.getOrDefault("nagivate", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(durationMap.getOrDefault("page_close", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(durationMap.getOrDefault("access", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(durationMap.getOrDefault("video", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(durationMap.getOrDefault("wiki", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(durationMap.getOrDefault("problem", new LongAdder()).longValue() + 1));
        e.getFeatures().add((float) Math.log(durationMap.getOrDefault("discussion", new LongAdder()).longValue() + 1));
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
                = e.getSortedLogs()
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

//    public static void Basic7x7FinalState(EnrollmentLog e)
//    {
//        double[][] matrix = new double[7][7];
//        for (int i = 0; i < 7; i++)
//        {
//            for (int j = 0; j < 7; j++)
//            {
//                matrix[i][j] = 0.0;
//            }
//        }
//
//        List<Integer> events
//                = e.getSortedLogs()
//                .stream()
//                .sequential()
//                .map(str -> str.split(",")[3])
//                .map(event ->
//                        {
//                            switch (event)
//                            {
//                                case "nagivate":
//                                    return 0;
//                                case "page_close":
//                                    return 1;
//                                case "access":
//                                    return 2;
//                                case "video":
//                                    return 3;
//                                case "wiki":
//                                    return 4;
//                                case "problem":
//                                    return 5;
//                                case "discussion":
//                                    return 6;
//                                default:
//                                    return -1;
//                            }
//                })
//                .collect(Collectors.toList());
//
//        int listSize = events.size();
//        for (int i = 0; i < listSize; i++)
//        {
//            if (i + 1 == listSize)
//            {
//                break;
//            }
//            else
//            {
//                matrix[events.get(i)][events.get(i + 1)] += 1.0;
//            }
//        }
//
////        List<Float> to49d = e.getTimeSeriesFeatures();
//        for (int i = 0; i < 7; i++)
//        {
//            double sum = 0.0;
//            for (int j = 0; j < 7; j++)
//            {
//                sum = sum + matrix[i][j];
//            }
//
//            for (int j = 0; j < 7; j++)
//            {
//                double tmp = matrix[i][j] / sum;
//
//                if (sum <= 0.0)
//                {
//                    tmp = 1.0 / 7.0;
//                }
//
//                matrix[i][j] = tmp;
//            }
//
////            sum = 0.0;
////            for (int j = 0; j < 7; j++)
////            {
////                sum = sum + matrix[i][j];
////            }
////
////            Double d = sum;
////            if (d.intValue() != 1)
////            {
////                System.out.println(sum);
////            }
//        }
//
//        double[] startState = new double[]
//        {
//            1, 0, 0, 0, 0, 0, 0
//        };
//
//        double[] finalState = new double[]
//        {
//            0, 0, 0, 0, 0, 0, 0
//        };
//
//        for (int k = 0; k < 20; k++)
//        {
//            for (int i = 0; i < 7; i++)
//            {
//                double put = 0.0;
//
//                for (int j = 0; j < 7; j++)
//                {
//                    put = put + matrix[i][j] * startState[j];
//                }
//
//                finalState[i] = put;
//            }
//
//            for (int i = 0; i < 7; i++)
//            {
//                startState[i] = finalState[i];
//            }
//        }
//
//        for (int i = 0; i < 7; i++)
//        {
//            e.getFeatures().add((float) finalState[i]);
//        }
//    }
//
//    public static void Duration7x7Matrix(EnrollmentLog e)
//    {
//        long[][] matrix = new long[7][7];
//        for (int i = 0; i < 7; i++)
//        {
//            for (int j = 0; j < 7; j++)
//            {
//                matrix[i][j] = 0;
//            }
//        }
//
//        List<Integer> events
//                = e.getSortedLogs()
//                .stream()
//                .sequential()
//                .map(str -> str.split(",")[3])
//                .map(event ->
//                        {
//                            switch (event)
//                            {
//                                case "nagivate":
//                                    return 0;
//                                case "page_close":
//                                    return 1;
//                                case "access":
//                                    return 2;
//                                case "video":
//                                    return 3;
//                                case "wiki":
//                                    return 4;
//                                case "problem":
//                                    return 5;
//                                case "discussion":
//                                    return 6;
//                                default:
//                                    return -1;
//                            }
//                })
//                .collect(Collectors.toList());
//
//        List<Instant> timeLine = e.getTimeLine();
//
//        int listSize = events.size();
//        for (int i = 0; i < listSize; i++)
//        {
//            if (i + 1 == listSize)
//            {
//                break;
//            }
//            else
//            {
//                long duration = Duration.between(timeLine.get(i), timeLine.get(i + 1)).getSeconds();
//
//                if (duration <= 0)
//                {
//                    duration = 0;
//                }
//
//                if (duration > 3600)
//                {
//                    duration = 0;
//                }
//
//                matrix[events.get(i)][events.get(i + 1)] += duration;
//            }
//        }
//
//        List<Float> to196d = e.getTimeSeriesFeatures();
//
//        for (int i = 0; i < 7; i++)
//        {
//            for (int j = 0; j < 7; j++)
//            {
//                to196d.add((float) matrix[i][j]);
//            }
//        }
//    }
//
//    public static void Duration7x7NormMatrix(EnrollmentLog e)
//    {
//        long[][] matrix = new long[7][7];
//        for (int i = 0; i < 7; i++)
//        {
//            for (int j = 0; j < 7; j++)
//            {
//                matrix[i][j] = 0;
//            }
//        }
//
//        List<Integer> events
//                = e.getSortedLogs()
//                .stream()
//                .sequential()
//                .map(str -> str.split(",")[3])
//                .map(event ->
//                        {
//                            switch (event)
//                            {
//                                case "nagivate":
//                                    return 0;
//                                case "page_close":
//                                    return 1;
//                                case "access":
//                                    return 2;
//                                case "video":
//                                    return 3;
//                                case "wiki":
//                                    return 4;
//                                case "problem":
//                                    return 5;
//                                case "discussion":
//                                    return 6;
//                                default:
//                                    return -1;
//                            }
//                })
//                .collect(Collectors.toList());
//
//        List<Instant> timeLine = e.getTimeLine();
//
//        int listSize = events.size();
//        for (int i = 0; i < listSize; i++)
//        {
//            if (i + 1 == listSize)
//            {
//                break;
//            }
//            else
//            {
//                long duration = Duration.between(timeLine.get(i), timeLine.get(i + 1)).getSeconds();
//
//                if (duration <= 0)
//                {
//                    duration = 0;
//                }
//
//                if (duration > 3600)
//                {
//                    duration = 0;
//                }
//
//                matrix[events.get(i)][events.get(i + 1)] += duration;
//            }
//        }
//
//        List<Float> to196d = e.getTimeSeriesFeatures();
//
//        for (int i = 0; i < 7; i++)
//        {
//            float sum = (float) Arrays.stream(matrix[i])
//                    .sum();
//
//            for (int j = 0; j < 7; j++)
//            {
//                if (sum > 0.0)
//                {
//                    to196d.add(((float) matrix[i][j]) / sum * 1.0f);
//                }
//                else
//                {
//                    to196d.add(1.0f / 7.0f);
//                }
//
//            }
//        }
//    }
//
//    private static class TimeActionVector
//    {
//
//        public final float x;
//        public final float y;
//
//        public TimeActionVector(float x, float y)
//        {
//            this.x = x;
//            this.y = y;
//        }
//    }
//
//    public static void OffsetTimeSeries(EnrollmentLog e)
//    {
//        // (x, y) -> (time offset, action offset)
//        List<String> logs = e.getSortedLogs();
////        List<TimeActionVector> vectors = new ArrayList<>();
//        List<Float> vectors;
//
//        List<Float> events
//                = logs
//                .stream()
//                .sequential()
//                .map(str -> str.split(","))
//                .map(sarr -> sarr[3])
//                .map(event ->
//                        {
//                            switch (event)
//                            {
//                                case "nagivate":
//                                    return 0.0f;
//                                case "page_close":
//                                    return 0.1f;
//                                case "access":
//                                    return 0.2f;
//                                case "video":
//                                    return 0.3f;
//                                case "wiki":
//                                    return 0.5f;
//                                case "problem":
//                                    return 0.8f;
//                                case "discussion":
//                                    return 1.3f;
//                                default:
//                                    return -1.0f;
//                            }
//                })
//                .collect(Collectors.toList());
//
//        int timeLength = logs.size();
//        int targetLength = 400;
//
////        for (int i = 0; i < timeLength; i++)
////        {
//////            vectors.add(new TimeActionVector(1.0f, events.get(i)));
////            vectors.add(events.get(i));
////        }
//        vectors = events;
//
//        float[] offsets = new float[targetLength];
//
//        if (timeLength > targetLength)
//        {
//            float each = (float) timeLength / (float) targetLength;
//            for (int i = 0; i < targetLength; i++)
//            {
//                int nextOne;
//                if (((int) ((i + 1) * each)) >= timeLength)
//                {
//                    nextOne = timeLength - 1;
//                }
//                else
//                {
//                    nextOne = (int) ((i + 1) * each);
//                }
//
////                float _y = vectors.subList(i * each, nextRange).stream().map(v -> v.y).reduce(0.0f, (y1, y2) -> (y1 + y2));
//                float _y = vectors.subList((int) (i * each), nextOne).stream().reduce(0.0f, (y1, y2) -> (y1 + y2));
//                offsets[i] = _y / each;
//            }
//        }
//        else if (timeLength < targetLength)
//        {
//            float each = (float) timeLength / (float) targetLength;
//            for (int i = 0; i < targetLength; i++)
//            {
//                int nextOne;
//                if (((int) (i * each)) >= timeLength)
//                {
//                    nextOne = timeLength - 1;
//                }
//                else
//                {
//                    nextOne = (int) (i * each);
//                }
//                offsets[i] = vectors.get(nextOne);
//            }
//        }
//        else if (timeLength == targetLength)
//        {
//            for (int i = 0; i < timeLength; i++)
//            {
//                offsets[i] = vectors.get(i);
//            }
//        }
//
//        List<Float> toOffsetD = e.getTimeSeriesFeatures();
//
//        for (int i = 0; i < targetLength; i++)
//        {
//            toOffsetD.add(offsets[i]);
//        }
//    }
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

        e.getFeatures().add((float) availableScore);
        e.getFeatures().add((float) totalScore);
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

//        long HourCount = e.getTimeLine()
//                .stream()
//                .map(in -> in.toString().split(":")[0])
//                .distinct()
//                .count();
//
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
//        long courseStartToStuEnd = Duration.between(startTime, stuEndTime).toDays();
//        long stuStartToCourseEnd = Duration.between(stuStartTime, endTime).toDays();
//

        Map<String, Long> typeCount = e.getTimeLine()
                .stream()
                .map(in -> in.toString())
                .map(hms -> hms.split(":")[0])
                .distinct()
                .map(str -> str.split("T")[1])
                .map(h -> Integer.parseInt(h))
                .map(h ->
                        {
                            if (h >= 0 && h <= 5)
                            {
                                return "night";
                            }
                            else if (h > 5 && h <= 13)
                            {
                                return "morning";
                            }
                            else if (h > 13 && h <= 21)
                            {
                                return "afternoon";
                            }
                            else
                            {
                                return "night";
                            }
                })
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()));

        // Activating days
        e.getFeatures().add((float) DateCount);
//        e.getFeatures().add((float) HourCount);

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
        e.getFeatures().add((float) beforeCourseEnd);
//        e.getFeatures().add((float) courseStartToStuEnd);
//        e.getFeatures().add((float) stuStartToCourseEnd);

        // Student active type in morning afternoon night
        e.getFeatures().add((float) typeCount.getOrDefault("morning", 0L));
        e.getFeatures().add((float) typeCount.getOrDefault("afternoon", 0L));
        e.getFeatures().add((float) typeCount.getOrDefault("night", 0L));
    }

    public static void CourseTimeLinesForSpecialDays(EnrollmentLog e)
    {
        Instant stuEndTime = e.getTimeLine().get(e.getTimeLine().size() - 1);

        Instant exam;

        float answer;

        int stuEndTimeYear = Integer.parseInt(stuEndTime.toString().split("T")[0].split("-")[0]);
        int stuEndTimeMonth = Integer.parseInt(stuEndTime.toString().split("T")[0].split("-")[1]);

        if (stuEndTimeMonth >= 9 && stuEndTimeMonth <= 11)
        {
            exam = Instant.parse(stuEndTimeYear + "-11-20T12:00:00Z");
        }
        else if (stuEndTimeMonth == 12)
        {
            exam = Instant.parse((stuEndTimeYear + 1) + "-01-20T12:00:00Z");
        }
        else if (stuEndTimeMonth == 1)
        {
            exam = Instant.parse(stuEndTimeYear + "-01-20T12:00:00Z");
        }
        else if (stuEndTimeMonth == 2)
        {
            exam = Instant.parse(stuEndTimeYear + "-02-18T12:00:00Z");
        }
        else if (stuEndTimeMonth > 2 && stuEndTimeMonth <= 4)
        {
            exam = Instant.parse(stuEndTimeYear + "-04-20T12:00:00Z");
        }
        else if (stuEndTimeMonth > 4 && stuEndTimeMonth <= 6)
        {
            exam = Instant.parse(stuEndTimeYear + "-06-20T12:00:00Z");
        }
        else if (stuEndTimeMonth == 7)
        {
            exam = Instant.parse(stuEndTimeYear + "-07-20T12:00:00Z");
        }
        else
        {
            exam = Instant.parse(stuEndTimeYear + "-08-20T12:00:00Z");
        }

        answer = (float) Math.abs(Duration.between(stuEndTime, exam).toDays());

        e.getFeatures().add(answer);
    }
}
