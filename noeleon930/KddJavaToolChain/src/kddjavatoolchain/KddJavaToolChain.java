package kddjavatoolchain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import kddjavatoolchain.DataFormat.EnrollmentLog;
import kddjavatoolchain.DataFormat.Module;
import kddjavatoolchain.DataProcess.ComputeCourses;
import kddjavatoolchain.DataProcess.ComputeEnrollments;
import kddjavatoolchain.DataProcess.ComputeStudents;
import kddjavatoolchain.DataProcess.ComputeSummarizing;
import kddjavatoolchain.DataProcess.GenerateEnrollmentLogs;
import kddjavatoolchain.OutputToFile.OutputAsCsv;
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
    private static Path course_date_course_path;

    private static List<String> train_log_train_list;
    private static List<String> test_log_test_list;

    private static List<String> train_enrollment_train_list;
    private static List<String> test_enrollment_test_list;
    private static List<String> total_enrollment_total_list;

    private static Map<Integer, Integer> train_truth_train_map;

    private static Map<Integer, EnrollmentLog> train_enrollments_map;
    private static Map<Integer, EnrollmentLog> test_enrollments_map;

    private static Map<String, Module> modules_list;

    private static ConcurrentMap<String, String> course_date_course_map;

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
                        break;
                    case "-check-data":
                        SetEnvironment();
                        LoadEnrollmentFiles();
                        LoadTruthFile();
                        CheckData();
                        break;
                    case "-surm":
                        ComputeSummarizing.Compute();
                        break;
                    case "-serializefiles":
                        SetEnvironment();
                        LoadLogFiles();
                        LoadEnrollmentFiles();
                        LoadTruthFile();
                        LoadModulesFile();
                        LoadEnrollmentLog();
                        LoadCourseDate();
                        SerializeThem();
                        break;
                    case "-default":
                        ExtractFeaturesFromFiles();
                        OutputAsCSV();
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

    private static void LoadEnrollmentLog()
    {
        p("Load Enrollment Log...");

        train_enrollments_map = GenerateEnrollmentLogs.GenerateEnrollmentClass(train_log_train_list);
        test_enrollments_map = GenerateEnrollmentLogs.GenerateEnrollmentClass(test_log_test_list);

        // Import truth to enrollments
        GenerateEnrollmentLogs.ImportTruthToEnrollments(train_truth_train_map, train_enrollments_map);

        // Import students and course data to train and test
        GenerateEnrollmentLogs.ImportEnrollmentStudentAndCourseIdtoEnrollments(train_enrollment_train_list, train_enrollments_map);
        GenerateEnrollmentLogs.ImportEnrollmentStudentAndCourseIdtoEnrollments(test_enrollment_test_list, test_enrollments_map);

        p("Load Enrollment Log Completed...");
    }

    private static void LoadCourseDate() throws IOException
    {
        course_date_course_path
                = kdd_data_home_path.resolve("date.csv");

        course_date_course_map
                = Files.lines(course_date_course_path, StandardCharsets.UTF_8)
                .skip(1)
                .sequential()
                .map(str -> str.split(","))
                .collect(Collectors.toConcurrentMap(sarr -> sarr[0], sarr -> sarr[1] + "," + sarr[2]));
    }

    private static void ExtractFeaturesFromFiles()
    {
        p("Extract Features From Files...");

        p("-> Computing Students...");
        ComputeStudents.Compute();

        p("-> Computing Courses...");
        ComputeCourses.Compute();

        p("-> Computing Enrollments...");
        ComputeEnrollments.Compute();

        p("-> Computing Students' Timeline...");
        ComputeStudents.ComputeTimeline();

        p("-> Computing Courses' Timeline...");
        ComputeCourses.ComputeTimeline();

        p("-> Feature Extraction for Train Data...");
        train_enrollments_map.entrySet().parallelStream().forEach(e ->
        {
            e.getValue().GenerateFeatures();
            // e.getValue().GenerateTimeSeriesFeatures();
        });
        p("-> Feature Extraction for Test Data...");
        test_enrollments_map.entrySet().parallelStream().forEach(e ->
        {
            e.getValue().GenerateFeatures();
            // e.getValue().GenerateTimeSeriesFeatures();
        });

        p("Extract Features From Files Completed...");
    }

    private static void SerializeThem() throws FileNotFoundException, IOException
    {
        p("Serialize Them...");

        try (FSTObjectOutput objOut = new FSTObjectOutput(new FileOutputStream("train_enrollments_map.obj")))
        {
            objOut.writeObject(train_enrollments_map);
        }

        try (FSTObjectOutput objOut = new FSTObjectOutput(new FileOutputStream("test_enrollments_map.obj")))
        {
            objOut.writeObject(test_enrollments_map);
        }

        try (FSTObjectOutput objOut = new FSTObjectOutput(new FileOutputStream("train_log_train_list.obj")))
        {
            objOut.writeObject(train_log_train_list);
        }

        try (FSTObjectOutput objOut = new FSTObjectOutput(new FileOutputStream("test_log_test_list.obj")))
        {
            objOut.writeObject(test_log_test_list);
        }

        try (FSTObjectOutput objOut = new FSTObjectOutput(new FileOutputStream("train_enrollment_train_list.obj")))
        {
            objOut.writeObject(train_enrollment_train_list);
        }

        try (FSTObjectOutput objOut = new FSTObjectOutput(new FileOutputStream("test_enrollment_test_list.obj")))
        {
            objOut.writeObject(test_enrollment_test_list);
        }

        try (FSTObjectOutput objOut = new FSTObjectOutput(new FileOutputStream("total_enrollment_total_list.obj")))
        {
            objOut.writeObject(total_enrollment_total_list);
        }

        try (FSTObjectOutput objOut = new FSTObjectOutput(new FileOutputStream("train_truth_train_map.obj")))
        {
            objOut.writeObject(train_truth_train_map);
        }

        try (FSTObjectOutput objOut = new FSTObjectOutput(new FileOutputStream("modules_list.obj")))
        {
            objOut.writeObject(modules_list);
        }

        try (FSTObjectOutput objOut = new FSTObjectOutput(new FileOutputStream("course_date_course_map.obj")))
        {
            objOut.writeObject(course_date_course_map);
        }

        p("Serialize Them Completed...");
    }

    private static void DeSerializeThem() throws IOException, ClassNotFoundException
    {
        p("De-Serialize Them...");

        try (FSTObjectInput objIn = new FSTObjectInput(new FileInputStream("train_enrollments_map.obj")))
        {
            train_enrollments_map = (Map<Integer, EnrollmentLog>) objIn.readObject();
        }

        try (FSTObjectInput objIn = new FSTObjectInput(new FileInputStream("test_enrollments_map.obj")))
        {
            test_enrollments_map = (Map<Integer, EnrollmentLog>) objIn.readObject();
        }

        try (FSTObjectInput objIn = new FSTObjectInput(new FileInputStream("total_enrollment_total_list.obj")))
        {
            total_enrollment_total_list = (List<String>) objIn.readObject();
        }

        try (FSTObjectInput objIn = new FSTObjectInput(new FileInputStream("train_truth_train_map.obj")))
        {
            train_truth_train_map = (Map<Integer, Integer>) objIn.readObject();
        }

        try (FSTObjectInput objIn = new FSTObjectInput(new FileInputStream("modules_list.obj")))
        {
            modules_list = (Map<String, Module>) objIn.readObject();
        }

        try (FSTObjectInput objIn = new FSTObjectInput(new FileInputStream("course_date_course_map.obj")))
        {
            course_date_course_map = (ConcurrentMap<String, String>) objIn.readObject();
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
                .forEachOrdered(e -> System.out.println(e.getSortedLogs()));

        p("-*-*-*-*-*-*-*-*-*-*-*-");

        test_enrollments_map
                .entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .sorted((e1, e2) -> e1.getID() - e2.getID())
                .sequential()
                .forEachOrdered(e -> System.out.println(e.getSortedLogs()));

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

    private static void OutputAsCSV() throws FileNotFoundException, IOException
    {
        p("Output As CSV...");

        OutputAsCsv.RawCSV();
        OutputAsCsv.Timeline30CSV();

        p("Output As CSV Completed...");
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

    public static Map<Integer, EnrollmentLog> getTrain_enrollments_map()
    {
        return train_enrollments_map;
    }

    public static Map<Integer, EnrollmentLog> getTest_enrollments_map()
    {
        return test_enrollments_map;
    }

    public static ConcurrentMap<String, String> getCourse_date_course_map()
    {
        return course_date_course_map;
    }

}
