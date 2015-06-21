package kddjavatoolchain.OutputToFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import kddjavatoolchain.DataFormat.EnrollmentLog;
import kddjavatoolchain.KddJavaToolChain;

/**
 *
 * @author Noel
 */
public class OutputAsCsv
{

    private static final Map<Integer, EnrollmentLog> train_enrollments_map = kddjavatoolchain.KddJavaToolChain.getTrain_enrollments_map();
    private static final Map<Integer, EnrollmentLog> test_enrollments_map = kddjavatoolchain.KddJavaToolChain.getTest_enrollments_map();

    public static void RawCSV() throws UnsupportedEncodingException, FileNotFoundException, IOException
    {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("train_feature.csv"), "UTF-8"))
        {
            writer.write("nagivate_times"
                    + ",page_close_times"
                    + ",access_times"
                    + ",video_times"
                    + ",wiki_times"
                    + ",problem_times"
                    + ",discussion_times"
                    + ",courses_student_had"
                    + ",students_course_had"
                    + ",enrollment_about_num"
                    + ",enrollment_chapter_num"
                    + ",enrollment_course_num"
                    + ",enrollment_course_info_num"
                    + ",enrollment_html_num"
                    + ",enrollment_outlink_num"
                    + ",enrollment_problem_num"
                    + ",enrollment_sequential_num"
                    + ",enrollment_static_tab_num"
                    + ",enrollment_vertical_num"
                    + ",enrollment_video_num"
                    + ",enrollment_combinedopenended_num"
                    + ",enrollment_peergrading_num"
                    + ",enrollment_discussion_num"
                    + ",enrollment_dictation_num"
                    + ",enrollment_unknown_num"
                    + ",nagivate_duration"
                    + ",page_close_duration"
                    + ",access_duration"
                    + ",video_duration"
                    + ",wiki_duration"
                    + ",problem_duration"
                    + ",discussion_duration"
                    + ",student_logginThisCourse_score"
                    + ",student_logginDays_score"
                    + ",activating_days"
                    + ",activating_hours"
                    + ",total_event_times"
                    + ",total_enrollment_module_num"
                    + ",total_duration_times"
                    + "\n");
            writer.flush();

            train_enrollments_map
                    .entrySet()
                    .parallelStream()
                    .map(e -> e.getValue())
                    .sorted((e1, e2) -> e1.getID() - e2.getID())
                    .sequential()
                    .forEachOrdered(e ->
                            {
                                try
                                {
                                    String tmpLine
                                    = e
                                    .getFeatures()
                                    .stream()
                                    .sequential()
                                    .map(f -> String.valueOf(f))
                                    .collect(Collectors.joining(","));

                                    float raw7sum
                                    = e
                                    .getFeatures()
                                    .subList(0, 7)
                                    .stream()
                                    .collect(Collectors.summingDouble(f -> (double) f))
                                    .floatValue();

                                    float enrollment16sum
                                    = e
                                    .getFeatures()
                                    .subList(9, 24)
                                    .stream()
                                    .collect(Collectors.summingDouble(f -> (double) f))
                                    .floatValue();

                                    float duration7sum
                                    = e
                                    .getFeatures()
                                    .subList(25, 32)
                                    .stream()
                                    .collect(Collectors.summingDouble(f -> (double) f))
                                    .floatValue();

                                    tmpLine = tmpLine + "," + String.valueOf(raw7sum) + "," + String.valueOf(enrollment16sum) + "," + String.valueOf(duration7sum);

                                    writer.write(tmpLine + "\n");
                                    writer.flush();
                                }
                                catch (IOException ex)
                                {
                                    Logger.getLogger(KddJavaToolChain.class.getName()).log(Level.SEVERE, null, ex);
                                }
                    });

            writer.close();
        }

        try (Writer writer = new OutputStreamWriter(new FileOutputStream("test_feature.csv"), "UTF-8"))
        {
            writer.write("nagivate_times"
                    + ",page_close_times"
                    + ",access_times"
                    + ",video_times"
                    + ",wiki_times"
                    + ",problem_times"
                    + ",discussion_times"
                    + ",courses_student_had"
                    + ",students_course_had"
                    + ",enrollment_about_num"
                    + ",enrollment_chapter_num"
                    + ",enrollment_course_num"
                    + ",enrollment_course_info_num"
                    + ",enrollment_html_num"
                    + ",enrollment_outlink_num"
                    + ",enrollment_problem_num"
                    + ",enrollment_sequential_num"
                    + ",enrollment_static_tab_num"
                    + ",enrollment_vertical_num"
                    + ",enrollment_video_num"
                    + ",enrollment_combinedopenended_num"
                    + ",enrollment_peergrading_num"
                    + ",enrollment_discussion_num"
                    + ",enrollment_dictation_num"
                    + ",enrollment_unknown_num"
                    + ",nagivate_duration"
                    + ",page_close_duration"
                    + ",access_duration"
                    + ",video_duration"
                    + ",wiki_duration"
                    + ",problem_duration"
                    + ",discussion_duration"
                    + ",student_logginThisCourse_score"
                    + ",student_logginDays_score"
                    + ",activating_days"
                    + ",activating_hours"
                    + ",total_event_times"
                    + ",total_enrollment_module_num"
                    + ",total_duration_times"
                    + "\n");
            writer.flush();

            test_enrollments_map
                    .entrySet()
                    .parallelStream()
                    .map(e -> e.getValue())
                    .sorted((e1, e2) -> e1.getID() - e2.getID())
                    .sequential()
                    .forEachOrdered(e ->
                            {
                                try
                                {
                                    String tmpLine
                                    = e
                                    .getFeatures()
                                    .stream()
                                    .sequential()
                                    .map(f -> String.valueOf(f))
                                    .collect(Collectors.joining(","));

                                    float raw7sum
                                    = e
                                    .getFeatures()
                                    .subList(0, 7)
                                    .stream()
                                    .collect(Collectors.summingDouble(f -> (double) f))
                                    .floatValue();

                                    float enrollment16sum
                                    = e
                                    .getFeatures()
                                    .subList(9, 24)
                                    .stream()
                                    .collect(Collectors.summingDouble(f -> (double) f))
                                    .floatValue();

                                    float duration7sum
                                    = e
                                    .getFeatures()
                                    .subList(25, 32)
                                    .stream()
                                    .collect(Collectors.summingDouble(f -> (double) f))
                                    .floatValue();

                                    tmpLine = tmpLine + "," + String.valueOf(raw7sum) + "," + String.valueOf(enrollment16sum) + "," + String.valueOf(duration7sum);

                                    writer.write(tmpLine + "\n");
                                    writer.flush();
                                }
                                catch (IOException ex)
                                {
                                    Logger.getLogger(KddJavaToolChain.class.getName()).log(Level.SEVERE, null, ex);
                                }
                    });

            writer.close();
        }
    }

    public static void PercentCSV() throws UnsupportedEncodingException, FileNotFoundException, IOException
    {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("train_feature_percentage.csv"), "UTF-8"))
        {
            writer.write("nagivate_times"
                    + ",page_close_times"
                    + ",access_times"
                    + ",video_times"
                    + ",wiki_times"
                    + ",problem_times"
                    + ",discussion_times"
                    + ",enrollment_about_num"
                    + ",enrollment_chapter_num"
                    + ",enrollment_course_num"
                    + ",enrollment_course_info_num"
                    + ",enrollment_html_num"
                    + ",enrollment_outlink_num"
                    + ",enrollment_problem_num"
                    + ",enrollment_sequential_num"
                    + ",enrollment_static_tab_num"
                    + ",enrollment_vertical_num"
                    + ",enrollment_video_num"
                    + ",enrollment_combinedopenended_num"
                    + ",enrollment_peergrading_num"
                    + ",enrollment_discussion_num"
                    + ",enrollment_dictation_num"
                    + ",enrollment_unknown_num"
                    + ",nagivate_duration"
                    + ",page_close_duration"
                    + ",access_duration"
                    + ",video_duration"
                    + ",wiki_duration"
                    + ",problem_duration"
                    + ",discussion_duration"
                    + ",student_logginDays_score"
                    + "\n");
            writer.flush();

            train_enrollments_map
                    .entrySet()
                    .parallelStream()
                    .map(e -> e.getValue())
                    .sorted((e1, e2) -> e1.getID() - e2.getID())
                    .map(e -> e.getFeatures())
                    .sequential()
                    .forEachOrdered(fl ->
                            {
                                float raw7_sum = fl.subList(0, 7).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float enr16_sum = fl.subList(9, 25).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float time7_sum = fl.subList(25, 32).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float loggindays2_sum = fl.subList(33, 34).stream().reduce(0.0f, (f1, f2) -> f1 + f2);

                                String raw7 = fl.subList(0, 7).stream().sequential()
                                .map(f -> f / raw7_sum)
                                .map(f -> Float.isNaN(f) ? 1.0f / 7.0f : f)
                                .map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String enr16 = fl.subList(9, 25).stream().sequential()
                                .map(f -> f / enr16_sum)
                                .map(f -> Float.isNaN(f) ? 1.0f / 16.0f : f)
                                .map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String time7 = fl.subList(25, 32).stream().sequential()
                                .map(f -> f / time7_sum)
                                .map(f -> Float.isNaN(f) ? 1.0f / 7.0f : f)
                                .map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String loggindays2 = fl.subList(32, 33).stream().sequential()
                                .map(f -> f / loggindays2_sum)
                                .map(f -> Float.isNaN(f) ? 1.0f / 2.0f : f)
                                .map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String total = Arrays.asList(raw7, enr16, time7, loggindays2).stream().sequential().collect(Collectors.joining(","));
                                total = total.replace("E", "e");

                                try
                                {
                                    writer.write(total + "\n");
                                    writer.flush();
                                }
                                catch (IOException ex)
                                {
                                    Logger.getLogger(KddJavaToolChain.class.getName()).log(Level.SEVERE, null, ex);
                                }
                    });

            writer.close();
        }

        try (Writer writer = new OutputStreamWriter(new FileOutputStream("test_feature_percentage.csv"), "UTF-8"))
        {
            writer.write("nagivate_times"
                    + ",page_close_times"
                    + ",access_times"
                    + ",video_times"
                    + ",wiki_times"
                    + ",problem_times"
                    + ",discussion_times"
                    + ",enrollment_about_num"
                    + ",enrollment_chapter_num"
                    + ",enrollment_course_num"
                    + ",enrollment_course_info_num"
                    + ",enrollment_html_num"
                    + ",enrollment_outlink_num"
                    + ",enrollment_problem_num"
                    + ",enrollment_sequential_num"
                    + ",enrollment_static_tab_num"
                    + ",enrollment_vertical_num"
                    + ",enrollment_video_num"
                    + ",enrollment_combinedopenended_num"
                    + ",enrollment_peergrading_num"
                    + ",enrollment_discussion_num"
                    + ",enrollment_dictation_num"
                    + ",enrollment_unknown_num"
                    + ",nagivate_duration"
                    + ",page_close_duration"
                    + ",access_duration"
                    + ",video_duration"
                    + ",wiki_duration"
                    + ",problem_duration"
                    + ",discussion_duration"
                    + ",student_logginDays_score"
                    + "\n");
            writer.flush();

            test_enrollments_map
                    .entrySet()
                    .parallelStream()
                    .map(e -> e.getValue())
                    .sorted((e1, e2) -> e1.getID() - e2.getID())
                    .map(e -> e.getFeatures())
                    .sequential()
                    .forEachOrdered(fl ->
                            {
                                float raw7_sum = fl.subList(0, 7).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float enr16_sum = fl.subList(9, 25).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float time7_sum = fl.subList(25, 32).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float loggindays2_sum = fl.subList(33, 34).stream().reduce(0.0f, (f1, f2) -> f1 + f2);

                                String raw7 = fl.subList(0, 7).stream().sequential()
                                .map(f -> f / raw7_sum)
                                .map(f -> Float.isNaN(f) ? 1.0f / 7.0f : f)
                                .map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String enr16 = fl.subList(9, 25).stream().sequential()
                                .map(f -> f / enr16_sum)
                                .map(f -> Float.isNaN(f) ? 1.0f / 16.0f : f)
                                .map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String time7 = fl.subList(25, 32).stream().sequential()
                                .map(f -> f / time7_sum)
                                .map(f -> Float.isNaN(f) ? 1.0f / 7.0f : f)
                                .map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String loggindays2 = fl.subList(32, 33).stream().sequential()
                                .map(f -> f / loggindays2_sum)
                                .map(f -> Float.isNaN(f) ? 1.0f / 2.0f : f)
                                .map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String total = Arrays.asList(raw7, enr16, time7, loggindays2).stream().sequential().collect(Collectors.joining(","));
                                total = total.replace("E", "e");

                                try
                                {
                                    writer.write(total + "\n");
                                    writer.flush();
                                }
                                catch (IOException ex)
                                {
                                    Logger.getLogger(KddJavaToolChain.class.getName()).log(Level.SEVERE, null, ex);
                                }
                    });

            writer.close();
        }

    }

    public static void TimeseriesCSV() throws UnsupportedEncodingException, FileNotFoundException, IOException
    {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("train_feature_timeseries.csv"), "UTF-8"))
        {
            train_enrollments_map
                    .entrySet()
                    .parallelStream()
                    .map(e -> e.getValue())
                    .sorted((e1, e2) -> e1.getID() - e2.getID())
                    .map(e -> e.getTimeSeriesFeatures())
                    .sequential()
                    .forEachOrdered(tsf ->
                            {

                                String total
                                = tsf
                                .stream()
                                .map(f -> String.valueOf(f))
                                .collect(Collectors.joining(","));

                                try
                                {
                                    writer.write(total + "\n");
                                    writer.flush();
                                }
                                catch (IOException ex)
                                {
                                    Logger.getLogger(KddJavaToolChain.class.getName()).log(Level.SEVERE, null, ex);
                                }
                    });

            writer.close();
        }

        try (Writer writer = new OutputStreamWriter(new FileOutputStream("test_feature_timeseries.csv"), "UTF-8"))
        {
            test_enrollments_map
                    .entrySet()
                    .parallelStream()
                    .map(e -> e.getValue())
                    .sorted((e1, e2) -> e1.getID() - e2.getID())
                    .map(e -> e.getTimeSeriesFeatures())
                    .sequential()
                    .forEachOrdered(tsf ->
                            {

                                String total
                                = tsf
                                .stream()
                                .map(f -> String.valueOf(f))
                                .collect(Collectors.joining(","));

                                try
                                {
                                    writer.write(total + "\n");
                                    writer.flush();
                                }
                                catch (IOException ex)
                                {
                                    Logger.getLogger(KddJavaToolChain.class.getName()).log(Level.SEVERE, null, ex);
                                }
                    });

            writer.close();
        }
    }

    public static void RawPlusTimeseriesCSV() throws UnsupportedEncodingException, FileNotFoundException, IOException
    {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("train_feature_raw_and_timeseries.csv"), "UTF-8"))
        {
            train_enrollments_map
                    .entrySet()
                    .parallelStream()
                    .map(e -> e.getValue())
                    .sorted((e1, e2) -> e1.getID() - e2.getID())
                    .sequential()
                    .forEachOrdered(e ->
                            {

                                String timeLine
                                = e
                                .getTimeSeriesFeatures()
                                .stream()
                                .map(f -> String.valueOf(f))
                                .collect(Collectors.joining(","));

                                String rawLine
                                = e
                                .getFeatures()
                                .stream()
                                .sequential()
                                .map(f -> String.valueOf(f))
                                .collect(Collectors.joining(","));

                                float raw7sum
                                = e
                                .getFeatures()
                                .subList(0, 7)
                                .stream()
                                .collect(Collectors.summingDouble(f -> (double) f))
                                .floatValue();

                                float enrollment15sum
                                = e
                                .getFeatures()
                                .subList(24, 39)
                                .stream()
                                .collect(Collectors.summingDouble(f -> (double) f))
                                .floatValue();

                                float duration7sum
                                = e
                                .getFeatures()
                                .subList(39, 46)
                                .stream()
                                .collect(Collectors.summingDouble(f -> (double) f))
                                .floatValue();

                                rawLine = rawLine + "," + String.valueOf(raw7sum) + "," + String.valueOf(enrollment15sum) + "," + String.valueOf(duration7sum);

                                try
                                {
                                    writer.write(rawLine + "," + timeLine + "\n");
                                    writer.flush();
                                }
                                catch (IOException ex)
                                {
                                    Logger.getLogger(KddJavaToolChain.class.getName()).log(Level.SEVERE, null, ex);
                                }
                    });

            writer.close();
        }

        try (Writer writer = new OutputStreamWriter(new FileOutputStream("test_feature_raw_and_timeseries.csv"), "UTF-8"))
        {
            test_enrollments_map
                    .entrySet()
                    .parallelStream()
                    .map(e -> e.getValue())
                    .sorted((e1, e2) -> e1.getID() - e2.getID())
                    .sequential()
                    .forEachOrdered(e ->
                            {

                                String timeLine
                                = e
                                .getTimeSeriesFeatures()
                                .stream()
                                .map(f -> String.valueOf(f))
                                .collect(Collectors.joining(","));

                                String rawLine
                                = e
                                .getFeatures()
                                .stream()
                                .sequential()
                                .map(f -> String.valueOf(f))
                                .collect(Collectors.joining(","));

                                float raw7sum
                                = e
                                .getFeatures()
                                .subList(0, 7)
                                .stream()
                                .collect(Collectors.summingDouble(f -> (double) f))
                                .floatValue();

                                float enrollment15sum
                                = e
                                .getFeatures()
                                .subList(24, 39)
                                .stream()
                                .collect(Collectors.summingDouble(f -> (double) f))
                                .floatValue();

                                float duration7sum
                                = e
                                .getFeatures()
                                .subList(39, 46)
                                .stream()
                                .collect(Collectors.summingDouble(f -> (double) f))
                                .floatValue();

                                rawLine = rawLine + "," + String.valueOf(raw7sum) + "," + String.valueOf(enrollment15sum) + "," + String.valueOf(duration7sum);

                                try
                                {
                                    writer.write(rawLine + "," + timeLine + "\n");
                                    writer.flush();
                                }
                                catch (IOException ex)
                                {
                                    Logger.getLogger(KddJavaToolChain.class.getName()).log(Level.SEVERE, null, ex);
                                }
                    });

            writer.close();

        }
    }
}
