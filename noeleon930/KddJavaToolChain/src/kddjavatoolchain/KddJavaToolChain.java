package kddjavatoolchain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import kddjavatoolchain.DataFormat.EnrollmentLog;
import kddjavatoolchain.DataFormat.Module;
import kddjavatoolchain.DataProcess.ComputeCourses;
import kddjavatoolchain.DataProcess.ComputeEnrollments;
import kddjavatoolchain.DataProcess.ComputeStudents;
import kddjavatoolchain.DataProcess.ExtractFeatures;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

/**
 *
 * @author Noel
 */
public class KddJavaToolChain
{

    private static Path kdd_data_home_path;
    private static Path train_log_train_path;
    private static Path train_truth_train_path;
    private static Path train_enrollment_train_path;
    private static Path test_log_test_path;
    private static Path test_enrollment_test_path;

    private static List<String> train_log_train_list;
    private static List<String> test_log_test_list;

    private static List<String> train_enrollment_train_list;
    private static List<String> test_enrollment_test_list;
    private static List<String> total_enrollment_total_list;

    private static Map<Integer, Integer> train_truth_train_map;

    private static Map<Integer, EnrollmentLog> train_enrollments_map;
    private static Map<Integer, EnrollmentLog> test_enrollments_map;

    private static Map<String, Module> modules_list;

    private static void p(String in)
    {
        System.out.println(in);
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException
    {
        // 1. Read kdd_data
        // 2. Extract features (as some kinda list of enrollments)
        // 3. Convert to feature matrix for various platforms or tools (like java-ml or scikit-learn)
        // 3.5. Train and Testing
        // 4. Generate the submission
        //
        for (String arg : args)
        {
            if (null != arg)
            {
                switch (arg)
                {
                    case "-use-cache":
                        DeSerializeThem();
                        break;
                    case "-show-features":
                        PrintThemOut();
                        break;
                    case "-output-csv":
                        OutputAsCSV();
                        OutputAsCSV_PercentVer();
                        break;
                    case "-check-data":
                        SetEnvironment();
                        LoadEnrollmentFiles();
                        LoadTruthFile();
                        CheckData();
                        break;
                    case "-default":
                        SetEnvironment();
                        LoadLogFiles();
                        LoadEnrollmentFiles();
                        LoadTruthFile();
                        LoadModulesFile();
                        ComputeStudents.Compute();
                        ComputeCourses.Compute();
                        ComputeEnrollments.Compute();
                        ExtractFeaturesFromFiles();
                        SerializeThem();
                        DestroyObjects();
                        break;
                }
            }
        }
    }

    private static void SetEnvironment()
    {
        p("Set Environment...");

        String ps = File.separator;

        kdd_data_home_path = FileSystems
                .getDefault()
                .getPath(".." + ps + ".." + ps + "data_kdd");

        train_log_train_path
                = kdd_data_home_path.resolve("train" + ps + "log_train.csv");

        train_truth_train_path
                = kdd_data_home_path.resolve("train" + ps + "truth_train.csv");

        train_enrollment_train_path
                = kdd_data_home_path.resolve("train" + ps + "enrollment_train.csv");

        test_log_test_path
                = kdd_data_home_path.resolve("test" + ps + "log_test.csv");

        test_enrollment_test_path
                = kdd_data_home_path.resolve("test" + ps + "enrollment_test.csv");

        p("Set Environment Completed...");
    }

    private static void LoadLogFiles() throws IOException
    {
        p("Load Log Files...");

        List<String> train_log_train_lines = Files.lines(train_log_train_path, StandardCharsets.UTF_8)
                .sequential()
                .skip(1)
                .sequential()
                .parallel()
                .collect(Collectors.toList());

        List<String> test_log_test_lines = Files.lines(test_log_test_path, StandardCharsets.UTF_8)
                .sequential()
                .skip(1)
                .sequential()
                .parallel()
                .collect(Collectors.toList());

        train_log_train_list
                = train_log_train_lines
                .parallelStream()
                .collect(Collectors.toList());

        test_log_test_list
                = test_log_test_lines
                .parallelStream()
                .collect(Collectors.toList());

        train_log_train_lines = null;
        test_log_test_lines = null;
        System.gc();

        p("Load Log Files Completed...");
    }

    private static void LoadEnrollmentFiles() throws IOException
    {
        p("Load Enrollment Files...");

        List<String> tmp_train_enrollment_train_list
                = Files.lines(train_enrollment_train_path, StandardCharsets.UTF_8)
                .sequential()
                .skip(1)
                .sequential()
                .parallel()
                .collect(Collectors.toList());

        List<String> tmp_test_enrollment_test_list
                = Files.lines(test_enrollment_test_path, StandardCharsets.UTF_8)
                .sequential()
                .skip(1)
                .sequential()
                .parallel()
                .collect(Collectors.toList());

        train_enrollment_train_list
                = tmp_train_enrollment_train_list
                .parallelStream()
                .collect(Collectors.toList());

        test_enrollment_test_list
                = tmp_test_enrollment_test_list
                .parallelStream()
                .collect(Collectors.toList());

        // Concat them
        total_enrollment_total_list = new ArrayList<>();
        total_enrollment_total_list.addAll(train_enrollment_train_list);
        total_enrollment_total_list.addAll(test_enrollment_test_list);

        tmp_test_enrollment_test_list = null;
        tmp_test_enrollment_test_list = null;
        System.gc();

        p("Load Enrollment Files Completed...");
    }

    private static void LoadTruthFile() throws IOException
    {
        p("Load Truth File...");

        train_truth_train_map
                = Files.lines(train_truth_train_path, StandardCharsets.UTF_8)
                .parallel()
                .map(s -> s.split(","))
                .collect(Collectors.toConcurrentMap(s -> Integer.parseInt(s[0]), s -> Integer.parseInt(s[1])));

        p("Load Truth File Completed...");
    }

    private static void ExtractFeaturesFromFiles()
    {
        p("Extract Features From Files...");

        train_enrollments_map = ExtractFeatures.GenerateEnrollmentClass(train_log_train_list);
        test_enrollments_map = ExtractFeatures.GenerateEnrollmentClass(test_log_test_list);

        // Import truth to enrollments
        ExtractFeatures.ImportTruthToEnrollments(train_truth_train_map, train_enrollments_map);

        // Import students and course data to train and test
        ExtractFeatures.ImportEnrollmentStudentAndCourseIdtoEnrollments(train_enrollment_train_list, train_enrollments_map);
        ExtractFeatures.ImportEnrollmentStudentAndCourseIdtoEnrollments(test_enrollment_test_list, test_enrollments_map);

        // Do Feature Extraction!
        train_enrollments_map.entrySet().parallelStream().forEach(e ->
        {
            e.getValue().GenerateFeatures();
        });

        test_enrollments_map.entrySet().parallelStream().forEach(e ->
        {
            e.getValue().GenerateFeatures();
        });

        p("Extract Features From Files Completed...");
    }

    private static void SerializeThem() throws FileNotFoundException, IOException
    {
        p("Serialize Them...");

        try (FSTObjectOutput objOut1 = new FSTObjectOutput(new FileOutputStream("train_enrollment_map.obj")))
        {
            objOut1.writeObject(train_enrollments_map);
        }

        try (FSTObjectOutput objOut2 = new FSTObjectOutput(new FileOutputStream("test_enrollment_map.obj")))
        {
            objOut2.writeObject(test_enrollments_map);
        }

        p("Serialize Them Completed...");
    }

    private static void DeSerializeThem() throws IOException, ClassNotFoundException
    {
        p("De-Serialize Them...");

        try (FSTObjectInput objIn1 = new FSTObjectInput(new FileInputStream("train_enrollment_map.obj")))
        {
            train_enrollments_map = (Map<Integer, EnrollmentLog>) objIn1.readObject();
        }

        try (FSTObjectInput objIn2 = new FSTObjectInput(new FileInputStream("test_enrollment_map.obj")))
        {
            test_enrollments_map = (Map<Integer, EnrollmentLog>) objIn2.readObject();
        }

        p("De-Serialize Them Completed...");
    }

    private static void LoadModulesFile() throws IOException
    {
        p("Load Modules File...");

        Path object_path = kdd_data_home_path.resolve("object.csv");

        List<String> lines = Files.lines(object_path, StandardCharsets.UTF_8)
                .sequential()
                .skip(1)
                .sequential()
                .parallel()
                .collect(Collectors.toList());

        Set<String> Uniques = lines.parallelStream()
                .map(str -> str.split(","))
                .map(sarr -> sarr[0] + " : " + sarr[1] + " : " + sarr[2])
                .distinct()
                .parallel()
                .collect(Collectors.toSet());
//
//        final Set<String> courses = lines.parallelStream()
//                .map(str -> str.split(",")[0])
//                .distinct()
//                .collect(Collectors.toSet());
//
//        courses.stream().forEachOrdered(System.out::println);
//
//        final Set<String> categories = Files.lines(object_path, StandardCharsets.UTF_8)
//                .skip(1)
//                .parallel()
//                .map(str -> str.split(",")[2])
//                .distinct()
//                .collect(Collectors.toSet());
//
//        final Set<String> modules_list = Files.lines(object_path, StandardCharsets.UTF_8)
//                .skip(1)
//                .parallel()
//                .map(str -> str.split(","))
//                .map(sarr -> sarr[1] + " " + sarr[3])
//                .map(str -> str.endsWith(" ") ? str.substring(0, str.length() - 1) : str)
//                .map(s -> s.split(" "))
//                .flatMap(Arrays::stream)
//                .parallel()
//                .distinct()
//                .parallel()
//                .collect(Collectors.toSet());
//
        modules_list = Uniques
                .parallelStream()
                .map(str -> str.split(" : "))
                .map(sarr -> new Module(sarr[0], sarr[1], sarr[2]))
                .collect(Collectors.toConcurrentMap(m -> m.getModule_id(), Function.identity()));

        lines = null;
        Uniques = null;
        System.gc();

        p("Load Modules File Compeleted...");
    }

    private static void PrintThemOut()
    {
        p("Print Them Out...");

        train_enrollments_map
                .entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .sorted((e1, e2) -> e1.getID() - e2.getID())
                .sequential()
                .forEachOrdered(e -> System.out.println(e.getFeatures()));

        p("-*-*-*-*-*-*-*-*-*-*-*-");

        test_enrollments_map
                .entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .sorted((e1, e2) -> e1.getID() - e2.getID())
                .sequential()
                .forEachOrdered(e -> System.out.println(e.getFeatures()));

        p("Print Them Out Completed...");
    }

    private static void PrintThemOut(String time)
    {
        if (!"timelines".equals(time))
        {
            return;
        }

        p("Print Them Out...");

        train_enrollments_map
                .entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .sorted((e1, e2) -> e1.getID() - e2.getID())
                .sequential()
                .forEachOrdered(e -> System.out.println(e.getID() + ":" + e.getTimeLine()));

        p("Print Them Out Completed...");
    }

    private static void OutputAsCSV() throws UnsupportedEncodingException, FileNotFoundException, IOException
    {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("train_feature.csv"), "UTF-8"))
        {
            writer.write("nagivate_times,page_close_times,video_times,access_times,wiki_times,problem_times,discussion_times,courses_student_had,courses_student_dropout,students_course_had,students_course_dropout,course_about_num,course_chapter_num,course_course_num,course_course_info_num,course_html_num,course_outlink_num,course_problem_num,course_sequential_num,course_static_tab_num,course_vertical_num,course_video_num,course_combinedopenended_num,course_peergrading_num,course_discussion_num,course_dictation_num,enrollment_about_num,enrollment_chapter_num,enrollment_course_num,enrollment_course_info_num,enrollment_html_num,enrollment_outlink_num,enrollment_problem_num,enrollment_sequential_num,enrollment_static_tab_num,enrollment_vertical_num,enrollment_video_num,enrollment_combinedopenended_num,enrollment_peergrading_num,enrollment_discussion_num,enrollment_dictation_num,nagivate_duration,page_close_duration,video_duration,access_duration,wiki_duration,problem_duration,discussion_duration\n");

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
            writer.write("nagivate_times,page_close_times,video_times,access_times,wiki_times,problem_times,discussion_times,courses_student_had,courses_student_dropout,students_course_had,students_course_dropout,course_about_num,course_chapter_num,course_course_num,course_course_info_num,course_html_num,course_outlink_num,course_problem_num,course_sequential_num,course_static_tab_num,course_vertical_num,course_video_num,course_combinedopenended_num,course_peergrading_num,course_discussion_num,course_dictation_num,enrollment_about_num,enrollment_chapter_num,enrollment_course_num,enrollment_course_info_num,enrollment_html_num,enrollment_outlink_num,enrollment_problem_num,enrollment_sequential_num,enrollment_static_tab_num,enrollment_vertical_num,enrollment_video_num,enrollment_combinedopenended_num,enrollment_peergrading_num,enrollment_discussion_num,enrollment_dictation_num,nagivate_duration,page_close_duration,video_duration,access_duration,wiki_duration,problem_duration,discussion_duration\n");

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

    private static void OutputAsCSV_PercentVer() throws UnsupportedEncodingException, FileNotFoundException, IOException
    {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("train_feature_percentage.csv"), "UTF-8"))
        {
            writer.write("nagivate_times,page_close_times,video_times,access_times,wiki_times,problem_times,discussion_times,courses_student_dropout,students_course_dropout,course_about_num,course_chapter_num,course_course_num,course_course_info_num,course_html_num,course_outlink_num,course_problem_num,course_sequential_num,course_static_tab_num,course_vertical_num,course_video_num,course_combinedopenended_num,course_peergrading_num,course_discussion_num,course_dictation_num,enrollment_about_num,enrollment_chapter_num,enrollment_course_num,enrollment_course_info_num,enrollment_html_num,enrollment_outlink_num,enrollment_problem_num,enrollment_sequential_num,enrollment_static_tab_num,enrollment_vertical_num,enrollment_video_num,enrollment_combinedopenended_num,enrollment_peergrading_num,enrollment_discussion_num,enrollment_dictation_num,nagivate_duration,page_close_duration,video_duration,access_duration,wiki_duration,problem_duration,discussion_duration\n");
            writer.flush();

            train_enrollments_map
                    .entrySet()
                    .parallelStream()
                    .map(e -> e.getValue())
                    .map(e -> e.getFeatures())
                    .sequential()
                    .forEachOrdered(fl ->
                            {
                                float raw7_sum = fl.subList(0, 7).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float stu2_sum = fl.subList(7, 9).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float cors2_sum = fl.subList(9, 11).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float cors15_sum = fl.subList(11, 26).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float enr15_sum = fl.subList(26, 41).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float time7_sum = fl.subList(41, 48).stream().reduce(0.0f, (f1, f2) -> f1 + f2);

                                String raw7 = fl.subList(0, 7).stream().sequential().map(f -> f / raw7_sum).map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));
                                String stu2 = fl.subList(8, 9).stream().sequential().map(f -> f / stu2_sum).map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));
                                String cors2 = fl.subList(10, 11).stream().sequential().map(f -> f / cors2_sum).map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));
                                String cors15 = fl.subList(11, 26).stream().sequential().map(f -> f / cors15_sum).map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));
                                String enr15 = fl.subList(26, 41).stream().sequential().map(f -> f / enr15_sum).map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));
                                String time7 = fl.subList(41, 48).stream().sequential().map(f -> f / time7_sum).map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String total = Arrays.asList(raw7, stu2, cors2, cors15, enr15, time7).stream().sequential().collect(Collectors.joining(","));
                                total = total.replace("NaN", "0.0").replace("E", "e");

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
            writer.write("nagivate_times,page_close_times,video_times,access_times,wiki_times,problem_times,discussion_times,courses_student_dropout,students_course_dropout,course_about_num,course_chapter_num,course_course_num,course_course_info_num,course_html_num,course_outlink_num,course_problem_num,course_sequential_num,course_static_tab_num,course_vertical_num,course_video_num,course_combinedopenended_num,course_peergrading_num,course_discussion_num,course_dictation_num,enrollment_about_num,enrollment_chapter_num,enrollment_course_num,enrollment_course_info_num,enrollment_html_num,enrollment_outlink_num,enrollment_problem_num,enrollment_sequential_num,enrollment_static_tab_num,enrollment_vertical_num,enrollment_video_num,enrollment_combinedopenended_num,enrollment_peergrading_num,enrollment_discussion_num,enrollment_dictation_num,nagivate_duration,page_close_duration,video_duration,access_duration,wiki_duration,problem_duration,discussion_duration\n");
            writer.flush();

            test_enrollments_map
                    .entrySet()
                    .parallelStream()
                    .map(e -> e.getValue())
                    .map(e -> e.getFeatures())
                    .sequential()
                    .forEachOrdered(fl ->
                            {
                                float raw7_sum = fl.subList(0, 7).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float stu2_sum = fl.subList(7, 9).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float cors2_sum = fl.subList(9, 11).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float cors15_sum = fl.subList(11, 26).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float enr15_sum = fl.subList(26, 41).stream().reduce(0.0f, (f1, f2) -> f1 + f2);
                                float time7_sum = fl.subList(41, 48).stream().reduce(0.0f, (f1, f2) -> f1 + f2);

                                String raw7 = fl.subList(0, 7).stream().sequential().map(f -> f / raw7_sum).map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));
                                String stu2 = fl.subList(8, 9).stream().sequential().map(f -> f / stu2_sum).map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));
                                String cors2 = fl.subList(10, 11).stream().sequential().map(f -> f / cors2_sum).map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));
                                String cors15 = fl.subList(11, 26).stream().sequential().map(f -> f / cors15_sum).map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));
                                String enr15 = fl.subList(26, 41).stream().sequential().map(f -> f / enr15_sum).map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));
                                String time7 = fl.subList(41, 48).stream().sequential().map(f -> f / time7_sum).map(f -> String.valueOf(f)).sequential().collect(Collectors.joining(","));

                                String total = Arrays.asList(raw7, stu2, cors2, cors15, enr15, time7).stream().sequential().collect(Collectors.joining(","));
                                total = total.replace("NaN", "0.0").replace("E", "e");

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

    private static void CheckData()
    {
        if (!(train_enrollments_map.size() == train_enrollment_train_list.size()
                && train_enrollment_train_list.size() == train_truth_train_map.size()))
        {
            p("Check 1 fail");
        }

        if (!(test_enrollments_map.size() == test_enrollment_test_list.size()))
        {
            p("Check 2 fail");
        }
    }

    private static void DestroyObjects()
    {
        p("Destroy Objects...");

        train_log_train_list = null;
        test_log_test_list = null;
        train_enrollment_train_list = null;
        test_enrollment_test_list = null;
        train_enrollments_map = null;
        test_enrollments_map = null;
        System.gc();

        p("Destroy Objects Completed...");
    }

    public static Map<String, Module> getModules_map()
    {
        return modules_list;
    }

    public static List<String> getTotal_enrollment_total_list(boolean train_only)
    {
        if (train_only)
        {
            return train_enrollment_train_list;
        }
        else
        {
            return total_enrollment_total_list;
        }
    }

    public static Map<Integer, Integer> getTrain_truth_train_map()
    {
        return train_truth_train_map;
    }

}
