package kddjavatoolchain.DataProcess;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
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

    public static void CourseHadStudents(EnrollmentLog e)
    {
        String course_id = e.getCourse_id();

        int answer = ComputeCourses.courses.get(course_id).getStudents_num();

        e.getFeatures().add((float) answer / 100.0f);
    }

    public static void CourseDropouttedStudents(EnrollmentLog e)
    {
        String course_id = e.getCourse_id();

        int answer = ComputeCourses.courses.get(course_id).getDropouts();

        e.getFeatures().add((float) answer / 100.0f);
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

        e.getFeatures().add(durationMap.getOrDefault("nagivate", new LongAdder()).floatValue() / 100.0f);
        e.getFeatures().add(durationMap.getOrDefault("page_close", new LongAdder()).floatValue() / 100.0f);
        e.getFeatures().add(durationMap.getOrDefault("access", new LongAdder()).floatValue() / 100.0f);
        e.getFeatures().add(durationMap.getOrDefault("video", new LongAdder()).floatValue() / 100.0f);
        e.getFeatures().add(durationMap.getOrDefault("wiki", new LongAdder()).floatValue() / 100.0f);
        e.getFeatures().add(durationMap.getOrDefault("problem", new LongAdder()).floatValue() / 100.0f);
        e.getFeatures().add(durationMap.getOrDefault("discussion", new LongAdder()).floatValue() / 100.0f);
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

        List<Float> to49d = e.getTimeSeriesFeatures();

        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                to49d.add((float) matrix[i][j]);
            }
        }
    }

    public static void Basic7x7NormMatrix(EnrollmentLog e)
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

        List<Float> to49d = e.getTimeSeriesFeatures();

        for (int i = 0; i < 7; i++)
        {
            float sum = (float) Arrays.stream(matrix[i])
                    .asDoubleStream()
                    .sum();

            for (int j = 0; j < 7; j++)
            {
                if (sum > 0.0)
                {
                    to49d.add(((float) matrix[i][j]) / sum);
                }
                else
                {
                    to49d.add(1.0f / 7.0f);
                }

            }
        }
    }

    public static void Duration7x7Matrix(EnrollmentLog e)
    {
        long[][] matrix = new long[7][7];
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

        List<Instant> timeLine = e.getTimeLine();

        int listSize = events.size();
        for (int i = 0; i < listSize; i++)
        {
            if (i + 1 == listSize)
            {
                break;
            }
            else
            {
                long duration = Duration.between(timeLine.get(i), timeLine.get(i + 1)).getSeconds();

                if (duration <= 0)
                {
                    duration = 0;
                }

                if (duration > 3600)
                {
                    duration = 0;
                }

                matrix[events.get(i)][events.get(i + 1)] += duration;
            }
        }

        List<Float> to196d = e.getTimeSeriesFeatures();

        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                to196d.add((float) matrix[i][j]);
            }
        }
    }

    public static void Duration7x7NormMatrix(EnrollmentLog e)
    {
        long[][] matrix = new long[7][7];
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

        List<Instant> timeLine = e.getTimeLine();

        int listSize = events.size();
        for (int i = 0; i < listSize; i++)
        {
            if (i + 1 == listSize)
            {
                break;
            }
            else
            {
                long duration = Duration.between(timeLine.get(i), timeLine.get(i + 1)).getSeconds();

                if (duration <= 0)
                {
                    duration = 0;
                }

                if (duration > 3600)
                {
                    duration = 0;
                }

                matrix[events.get(i)][events.get(i + 1)] += duration;
            }
        }

        List<Float> to196d = e.getTimeSeriesFeatures();

        for (int i = 0; i < 7; i++)
        {
            float sum = (float) Arrays.stream(matrix[i])
                    .sum();

            for (int j = 0; j < 7; j++)
            {
                if (sum > 0.0)
                {
                    to196d.add(((float) matrix[i][j]) / sum * 1.0f);
                }
                else
                {
                    to196d.add(1.0f / 7.0f);
                }

            }
        }
    }

    private static class TimeActionVector
    {

        public final float x;
        public final float y;

        public TimeActionVector(float x, float y)
        {
            this.x = x;
            this.y = y;
        }
    }

    public static void OffsetTimeSeries(EnrollmentLog e)
    {
        // (x, y) -> (time offset, action offset)
        List<String> logs = e.getSortedLogs();
//        List<TimeActionVector> vectors = new ArrayList<>();
        List<Float> vectors;

        List<Float> events
                = logs
                .stream()
                .sequential()
                .map(str -> str.split(","))
                .map(sarr -> sarr[3])
                .map(event ->
                        {
                            switch (event)
                            {
                                case "nagivate":
                                    return 0.0f;
                                case "page_close":
                                    return 0.1f;
                                case "access":
                                    return 0.2f;
                                case "video":
                                    return 0.3f;
                                case "wiki":
                                    return 0.5f;
                                case "problem":
                                    return 0.8f;
                                case "discussion":
                                    return 1.3f;
                                default:
                                    return -1.0f;
                            }
                })
                .collect(Collectors.toList());

        int timeLength = logs.size();
        int targetLength = 400;

//        for (int i = 0; i < timeLength; i++)
//        {
////            vectors.add(new TimeActionVector(1.0f, events.get(i)));
//            vectors.add(events.get(i));
//        }
        vectors = events;

        float[] offsets = new float[targetLength];

        if (timeLength > targetLength)
        {
            float each = (float) timeLength / (float) targetLength;
            for (int i = 0; i < targetLength; i++)
            {
                int nextOne;
                if (((int) ((i + 1) * each)) >= timeLength)
                {
                    nextOne = timeLength - 1;
                }
                else
                {
                    nextOne = (int) ((i + 1) * each);
                }

//                float _y = vectors.subList(i * each, nextRange).stream().map(v -> v.y).reduce(0.0f, (y1, y2) -> (y1 + y2));
                float _y = vectors.subList((int) (i * each), nextOne).stream().reduce(0.0f, (y1, y2) -> (y1 + y2));
                offsets[i] = _y / each;
            }
        }
        else if (timeLength < targetLength)
        {
            float each = (float) timeLength / (float) targetLength;
            for (int i = 0; i < targetLength; i++)
            {
                int nextOne;
                if (((int) (i * each)) >= timeLength)
                {
                    nextOne = timeLength - 1;
                }
                else
                {
                    nextOne = (int) (i * each);
                }
                offsets[i] = vectors.get(nextOne);
            }
        }
        else if (timeLength == targetLength)
        {
            for (int i = 0; i < timeLength; i++)
            {
                offsets[i] = vectors.get(i);
            }
        }

        List<Float> toOffsetD = e.getTimeSeriesFeatures();

        for (int i = 0; i < targetLength; i++)
        {
            toOffsetD.add(offsets[i]);
        }
    }
}
