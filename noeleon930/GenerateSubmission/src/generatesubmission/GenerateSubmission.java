package generatesubmission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Noel
 */
public class GenerateSubmission
{

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException
    {
        String pathSeparator = File.separator;

        Path sample_submit = FileSystems.getDefault().getPath(""
                + ".."
                + pathSeparator
                + ".."
                + pathSeparator
                + "data_kdd"
                + pathSeparator
                + "sampleSubmission.csv");

        Path test_predict = FileSystems.getDefault().getPath(""
                + ".."
                + pathSeparator
                + "ConvertToFeatureMatrixJava"
                + pathSeparator
                + "output.txt");

        List<String> enrollments_to_submit
                = Files.lines(sample_submit, StandardCharsets.UTF_8)
                .sequential()
                .collect(Collectors.toList());

        List<String> from_predict_probList
                = Files.lines(test_predict, StandardCharsets.UTF_8)
                .sequential()
                .collect(Collectors.toList());

        int listSize = enrollments_to_submit.size();
        if (listSize != from_predict_probList.size())
        {
            System.out.println("Error! Number of Lines not matched.");
        }
        else
        {
            try (Writer writer = new OutputStreamWriter(new FileOutputStream("randomForest.csv"), "UTF-8"))
            {
                for (int i = 0; i < listSize; i++)
                {
                    String out = ""
                            + enrollments_to_submit.get(i).split(",")[0]
                            + ","
                            + from_predict_probList.get(i).split("  ")[1].split("]")[0];

                    if (out.endsWith("."))
                    {
                        out = out + "0";
                    }

                    writer.write(out + "\n");
                    writer.flush();
                }

                // Close output file stream
                writer.close();
            }

        }

    }

}
