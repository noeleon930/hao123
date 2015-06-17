package generatefeaturejava;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author Noel in hao123 >w<
 */
public class GenerateFeatureJava
{

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException
    {
        // Get start time
        Instant startTime = Instant.now();

        // Get the path seperator of the current system (ex: windows -> \ , linux -> /)
        String pathSeparator = File.separator;

        // Get the data source file's path ../../data_kdd/train/log_train.csv
        Path log_train = FileSystems.getDefault().getPath(""
                + ".."
                + pathSeparator
                + ".."
                + pathSeparator
                + "data_kdd"
                + pathSeparator
                + "train"
                + pathSeparator
                + "log_train.csv");

        // Load them into a list (parallel lazy evaluation)
        final List<String> logList;
        logList = Files
                .lines(log_train, StandardCharsets.UTF_8)
                .skip(1)
                .parallel()
                .collect(Collectors.toList());

        // Get end time
        Instant endTime = Instant.now();

        // Measure elapsed time
        Duration timeGone;
        timeGone = Duration.between(startTime, endTime);

        // Print elapsed time and size of data
        p((timeGone.toMillis() / 1000) + " sec in loading data.");
        p(logList.size() + " lines loaded.");

        // Fetch each enrollment's features And measure time
        p("Start to fetch each enrollment's features...");

        startTime = Instant.now();

        // Data structures to store data
        Map<Integer, List<String>> enrollment_log_map;
        Map<Integer, Feature> enrollment_feature_map;
        List<Feature> enrollment_feature_list;

        // Create (id -> logs) map (parallel lazy evaluation)
        // split(",")[0] is the id column, and use it to grouping
        enrollment_log_map = logList
                .parallelStream()
                .collect(Collectors.groupingBy(s -> Integer.valueOf(s.split(",")[0])));

        // Create (id -> features) map (parallel lazy evaluation)
        enrollment_feature_map = enrollment_log_map
                .entrySet()
                .parallelStream()
                .map(e -> new Feature(e.getKey(), e.getValue()))
                .collect(Collectors.toMap(f -> f.getEnrollment_id(), Function.identity()));

        // Load truth table
        // truth_train.csv path
        Path truth_train = FileSystems.getDefault().getPath(""
                + ".."
                + pathSeparator
                + ".."
                + pathSeparator
                + "data_kdd"
                + pathSeparator
                + "train"
                + pathSeparator
                + "truth_train.csv");

        // forEach in lines of truth_train (parallel evaluation)
        // setResult for each enrollment
        Files.lines(truth_train, StandardCharsets.UTF_8).parallel().forEach(s ->
        {
            String tmp[] = s.split(",");

            // id,result in tmp
            int id = Integer.valueOf(tmp[0]);
            int result = Integer.valueOf(tmp[1]);

            enrollment_feature_map.get(id).setResult(result);
        });

        // Create (features) list (parallel lazy evaluation)
        enrollment_feature_list = enrollment_feature_map
                .entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .sorted((f1, f2) -> f1.getEnrollment_id() - f2.getEnrollment_id())
                .collect(Collectors.toList());

        endTime = Instant.now();

        timeGone = Duration.between(startTime, endTime);
        p((timeGone.toMillis() / 1000) + " sec in fetching each enrollment's features.");

        // Output to json (one is prettified and one is minified)
        p("Start writing to json.");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(enrollment_feature_list);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("enrollment_feature.json"), "UTF-8"))
        {
            writer.write(json);
            writer.flush();
            writer.close();
        }

        Gson gson_min = new Gson();
        String json_min = gson_min.toJson(enrollment_feature_list);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("enrollment_feature.min.json"), "UTF-8"))
        {
            writer.write(json_min);
            writer.flush();
            writer.close();
        }

        p("Finish writing to json.");

        // Serialize enrollment_feature_list!
        FileOutputStream objFileOut;
        ObjectOutputStream objOut;

        objFileOut = new FileOutputStream("enrollment_feature_list.obj");
        objOut = new ObjectOutputStream(objFileOut);

        objOut.writeObject(enrollment_feature_list);
        objOut.close();
        objFileOut.close();

        forTestData();
    }

    // Print things to the console
    private static void p(String in)
    {
        System.out.println(in);
    }

    // For Test Data
    private static void forTestData() throws IOException
    {
        p("Start to fetch from Test Data");

        // Get start time
        Instant startTime = Instant.now();

        // Get the path seperator of the current system (ex: windows -> \ , linux -> /)
        String pathSeparator = File.separator;

        // Get the data source file's path ../../data_kdd/train/log_train.csv
        Path log_test = FileSystems.getDefault().getPath(""
                + ".."
                + pathSeparator
                + ".."
                + pathSeparator
                + "data_kdd"
                + pathSeparator
                + "test"
                + pathSeparator
                + "log_test.csv");

        // Load them into a list (parallel lazy evaluation)
        final List<String> logList;
        logList = Files
                .lines(log_test, StandardCharsets.UTF_8)
                .skip(1)
                .parallel()
                .collect(Collectors.toList());

        // Get end time
        Instant endTime = Instant.now();

        // Measure elapsed time
        Duration timeGone;
        timeGone = Duration.between(startTime, endTime);

        // Print elapsed time and size of data
        p((timeGone.toMillis() / 1000) + " sec in loading data.");
        p(logList.size() + " lines loaded.");

        // Fetch each enrollment's features And measure time
        p("Start to fetch each enrollment's features...");

        startTime = Instant.now();

        // Data structures to store data
        Map<Integer, List<String>> enrollment_log_map;
        Map<Integer, Feature> enrollment_feature_map;
        List<Feature> enrollment_feature_list;

        // Create (id -> logs) map (parallel lazy evaluation)
        // split(",")[0] is the id column, and use it to grouping
        enrollment_log_map = logList
                .parallelStream()
                .collect(Collectors.groupingBy(s -> Integer.valueOf(s.split(",")[0])));

        // Create (id -> features) map (parallel lazy evaluation)
        enrollment_feature_map = enrollment_log_map
                .entrySet()
                .parallelStream()
                .map(e -> new Feature(e.getKey(), e.getValue()))
                .collect(Collectors.toMap(f -> f.getEnrollment_id(), Function.identity()));

        // Create (features) list (parallel lazy evaluation)
        enrollment_feature_list = enrollment_feature_map
                .entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .sorted((f1, f2) -> f1.getEnrollment_id() - f2.getEnrollment_id())
                .collect(Collectors.toList());

        endTime = Instant.now();

        timeGone = Duration.between(startTime, endTime);
        p((timeGone.toMillis() / 1000) + " sec in fetching each enrollment's features.");

        // Output to json (one is prettified and one is minified)
        p("Start writing to json.");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(enrollment_feature_list);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("enrollment_feature_test.json"), "UTF-8"))
        {
            writer.write(json);
            writer.flush();
            writer.close();
        }

        Gson gson_min = new Gson();
        String json_min = gson_min.toJson(enrollment_feature_list);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("enrollment_feature_test.min.json"), "UTF-8"))
        {
            writer.write(json_min);
            writer.flush();
            writer.close();
        }

        p("Finish writing to json.");

        // Serialize enrollment_feature_list!
        FileOutputStream objFileOut;
        ObjectOutputStream objOut;

        objFileOut = new FileOutputStream("enrollment_feature_test_list.obj");
        objOut = new ObjectOutputStream(objFileOut);

        objOut.writeObject(enrollment_feature_list);
        objOut.close();
        objFileOut.close();
    }

}
