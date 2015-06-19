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
                    + ",course_about_num"
                    + ",course_chapter_num"
                    + ",course_course_num"
                    + ",course_course_info_num"
                    + ",course_html_num"
                    + ",course_outlink_num"
                    + ",course_problem_num"
                    + ",course_sequential_num"
                    + ",course_static_tab_num"
                    + ",course_vertical_num"
                    + ",course_video_num"
                    + ",course_combinedopenended_num"
                    + ",course_peergrading_num"
                    + ",course_discussion_num"
                    + ",course_dictation_num"
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
                    + ",nagivate_duration"
                    + ",page_close_duration"
                    + ",access_duration"
                    + ",video_duration"
                    + ",wiki_duration"
                    + ",problem_duration"
                    + ",discussion_duration"
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

                                    tmpLine = tmpLine + "," + String.valueOf(raw7sum) + "," + String.valueOf(enrollment15sum) + "," + String.valueOf(duration7sum);

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
                    + ",course_about_num"
                    + ",course_chapter_num"
                    + ",course_course_num"
                    + ",course_course_info_num"
                    + ",course_html_num"
                    + ",course_outlink_num"
                    + ",course_problem_num"
                    + ",course_sequential_num"
                    + ",course_static_tab_num"
                    + ",course_vertical_num"
                    + ",course_video_num"
                    + ",course_combinedopenended_num"
                    + ",course_peergrading_num"
                    + ",course_discussion_num"
                    + ",course_dictation_num"
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
                    + ",nagivate_duration"
                    + ",page_close_duration"
                    + ",access_duration"
                    + ",video_duration"
                    + ",wiki_duration"
                    + ",problem_duration"
                    + ",discussion_duration"
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

                                    tmpLine = tmpLine + "," + String.valueOf(raw7sum) + "," + String.valueOf(enrollment15sum) + "," + String.valueOf(duration7sum);

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
                    + ",course_about_num"
                    + ",course_chapter_num"
                    + ",course_course_num"
                    + ",course_course_info_num"
                    + ",course_html_num"
                    + ",course_outlink_num"
                    + ",course_problem_num"
                    + ",course_sequential_num"
                    + ",course_static_tab_num"
                    + ",course_vertical_num"
                    + ",course_video_num"
                    + ",course_combinedopenended_num"
                    + ",course_peergrading_num"
                    + ",course_discussion_num"
                    + ",course_dictation_num"
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
                    + ",nagivate_duration"
                    + ",page_close_duration"
                    + ",access_duration"
                    + ",video_duration"
                    + ",wiki_duration"
                    + ",problem_duration"
                    + ",discussion_duration\n");
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
                                float cors15_sum = fl.subList(9, 24).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float enr15_sum = fl.subList(24, 39).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float time7_sum = fl.subList(39, 46).stream().reduce(0.0f, (f1, f2) -> f1 + f2);

                                String raw7 = fl.subList(0, 7).stream().sequential()
                                .map(f -> f / raw7_sum)
                                .map(f -> Float.isNaN(f) ? 1.0f / 7.0f : f)
                                .map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String cors15 = fl.subList(9, 24).stream().sequential()
                                .map(f -> f / cors15_sum)
                                .map(f -> Float.isNaN(f) ? 1.0f / 15.0f : f)
                                .map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String enr15 = fl.subList(24, 39).stream().sequential()
                                .map(f -> f / enr15_sum)
                                .map(f -> Float.isNaN(f) ? 1.0f / 15.0f : f)
                                .map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String time7 = fl.subList(39, 46).stream().sequential()
                                .map(f -> f / time7_sum)
                                .map(f -> Float.isNaN(f) ? 1.0f / 7.0f : f)
                                .map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String total = Arrays.asList(raw7, cors15, enr15, time7).stream().sequential().collect(Collectors.joining(","));
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
                    + ",course_about_num"
                    + ",course_chapter_num"
                    + ",course_course_num"
                    + ",course_course_info_num"
                    + ",course_html_num"
                    + ",course_outlink_num"
                    + ",course_problem_num"
                    + ",course_sequential_num"
                    + ",course_static_tab_num"
                    + ",course_vertical_num"
                    + ",course_video_num"
                    + ",course_combinedopenended_num"
                    + ",course_peergrading_num"
                    + ",course_discussion_num"
                    + ",course_dictation_num"
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
                    + ",nagivate_duration"
                    + ",page_close_duration"
                    + ",access_duration"
                    + ",video_duration"
                    + ",wiki_duration"
                    + ",problem_duration"
                    + ",discussion_duration\n");
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
                                float cors15_sum = fl.subList(9, 24).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float enr15_sum = fl.subList(24, 39).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float time7_sum = fl.subList(39, 46).stream().reduce(0.0f, (f1, f2) -> f1 + f2);

                                String raw7 = fl.subList(0, 7).stream().sequential()
                                .map(f -> f / raw7_sum)
                                .map(f -> Float.isNaN(f) ? 1.0f / 7.0f : f)
                                .map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String cors15 = fl.subList(9, 24).stream().sequential()
                                .map(f -> f / cors15_sum)
                                .map(f -> Float.isNaN(f) ? 1.0f / 15.0f : f)
                                .map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String enr15 = fl.subList(24, 39).stream().sequential()
                                .map(f -> f / enr15_sum)
                                .map(f -> Float.isNaN(f) ? 1.0f / 15.0f : f)
                                .map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String time7 = fl.subList(39, 46).stream().sequential()
                                .map(f -> f / time7_sum)
                                .map(f -> Float.isNaN(f) ? 1.0f / 7.0f : f)
                                .map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String total = Arrays.asList(raw7, cors15, enr15, time7).stream().sequential().collect(Collectors.joining(","));
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
