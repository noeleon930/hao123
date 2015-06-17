package converttofeaturematrixjava;

import generatefeaturejava.Feature;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

/**
 *
 * @author Noel
 */
public class ConvertToFeatureMatrixJava
{

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        // Open serialized obj (enrollment_feature_list)
        FileInputStream objFileIn = new FileInputStream("enrollment_feature_list.obj");
        ObjectInputStream objIn = new ObjectInputStream(objFileIn);
        FileInputStream objFileIn2 = new FileInputStream("enrollment_feature_test_list.obj");
        ObjectInputStream objIn2 = new ObjectInputStream(objFileIn2);

        // Here we go
        final List<Feature> enrollment_feature_list = (List<Feature>) objIn.readObject();
        final List<Feature> enrollment_feature_test_list = (List<Feature>) objIn2.readObject();

        // Closing I/O
        objIn.close();
        objFileIn.close();
        objIn2.close();
        objFileIn2.close();

        // Open output file stream for ann.js
        // And write stuff into it
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("ann.js"), "UTF-8"))
        {
            // Write head part
            writer.write("var brain = require(\"brain\");\n"
                    + "var net = new brain.NeuralNetwork();\n"
                    + "\n"
                    + "var train_data = [");
            writer.flush();

            // Iterating enrollment_feature_list
            int listSize = enrollment_feature_list.size();
            for (int i = 0; i < listSize; i++)
            {
                // The Feature obj here
                Feature f = enrollment_feature_list.get(i);

                // String to be filled into json data
                String forInput;
                String forOutput;

                // Features
                forInput = "nagivate:" + f.getNagivate_times_norm() + ", "
                        + "acess:" + f.getAccess_times_norm() + ", "
                        + "problem:" + f.getProblem_times_norm() + ", "
                        + "page_close:" + f.getPage_close_times_norm() + ", "
                        + "video:" + f.getVideo_times_norm() + ", "
                        + "dicussion:" + f.getDiscussion_times_norm() + ", "
                        + "wiki:" + f.getWiki_times_norm();

                // Result (drop or not drop)
                forOutput = (f.getResult() == 1) ? "drop: 1" : "nodrop: 1";

                // Handle first, last, other one
                if (i == 0)
                {
                    writer.write("{ input:{ " + forInput + " }, output:{ " + forOutput + " } },\n");
                    writer.flush();
                }
                else if (i + 1 >= listSize)
                {

                    writer.write("                  { input:{ " + forInput + " }, output:{ " + forOutput + " } }");
                    writer.flush();
                }
                else
                {
                    writer.write("                  { input:{ " + forInput + " }, output:{ " + forOutput + " } },\n");
                    writer.flush();
                }
            }

            // Write run part
            writer.write("];\n"
                    + "\n"
                    + "net.train(train_data,\n"
                    + "{\n"
                    + "    errorThresh: 0.001,\n"
                    + "    iterations: 16888888888,\n"
                    + "    log: true,\n"
                    + "    logPeriod: 10,\n"
                    + "    learningRate: 0.5\n"
                    + "});\n"
                    + "\n"
                    + "var output = net.run(\n"
                    + "{\n"
                    + "});\n"
                    + "\n"
                    + "console.log(output)\n");
            writer.flush();

            // Close output file stream
            writer.close();
        }

        // Open output file stream for ELMjava
        // And write stuff into it
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("kdd_feature_train_java"), "UTF-8"))
        {
            // Write head part (feature matrix height, columns, classes)
            writer.write(enrollment_feature_list.size() + " " + 8 + " " + 2 + "\n");
            writer.flush();

            // Iterating enrollment_feature_list
            int listSize = enrollment_feature_list.size();
            for (int i = 0; i < listSize; i++)
            {
                // The Feature obj here
                Feature f = enrollment_feature_list.get(i);

                // Features
                String forOutput
                        = ((float) f.getResult()) + " "
                        + f.getNagivate_times_norm() + " "
                        + f.getAccess_times_norm() + " "
                        + f.getProblem_times_norm() + " "
                        + f.getPage_close_times_norm() + " "
                        + f.getVideo_times_norm() + " "
                        + f.getDiscussion_times_norm() + " "
                        + f.getWiki_times_norm() + "\n";

                writer.write(forOutput);
                writer.flush();
            }

            // Close output file stream
            writer.close();
        }

        // Open output file stream for scikit-learn
        // And write stuff into it
        try (Writer writer = new OutputStreamWriter(new FileOutputStream("random_forest.py"), "UTF-8"))
        {
            // Write [ to train
            writer.write("x = [");
            writer.flush();

            // Iterating enrollment_feature_list
            int listSize = enrollment_feature_list.size();
            for (int i = 0; i < listSize; i++)
            {
                // The Feature obj here
                Feature f = enrollment_feature_list.get(i);

                // Features
                String forOutput
                        = "[" + f.getTotal_times_norm() + ", "
                        + f.getNagivate_times_norm() + ", "
                        + f.getAccess_times_norm() + ", "
                        + f.getProblem_times_norm() + ", "
                        + f.getPage_close_times_norm() + ", "
                        + f.getVideo_times_norm() + ", "
                        + f.getDiscussion_times_norm() + ", "
                        + f.getWiki_times_norm() + "],\n";

                // Special for last one
                if (i == listSize - 1)
                {
                    forOutput
                            = "[" + f.getTotal_times_norm() + ", "
                            + f.getNagivate_times_norm() + ", "
                            + f.getAccess_times_norm() + ", "
                            + f.getProblem_times_norm() + ", "
                            + f.getPage_close_times_norm() + ", "
                            + f.getVideo_times_norm() + ", "
                            + f.getDiscussion_times_norm() + ", "
                            + f.getWiki_times_norm() + "]";
                }

                writer.write(forOutput);
                writer.flush();
            }

            // Write ] to train
            writer.write("]\n");
            writer.flush();

            // Write [ to truth
            writer.write("y = [");
            writer.flush();

            // Iterating enrollment_feature_list
            for (int i = 0; i < listSize; i++)
            {
                // The Feature obj here
                Feature f = enrollment_feature_list.get(i);

                // Features
                String forOutput = f.getResult() + ", ";

                // Special for last one
                if (i == listSize - 1)
                {
                    forOutput = f.getResult() + "";
                }

                writer.write(forOutput);
                writer.flush();
            }

            // Write ]to truth
            writer.write("]\n");
            writer.flush();

            // Write [ to test
            writer.write("testX = [");
            writer.flush();

            listSize = enrollment_feature_test_list.size();
            for (int i = 0; i < listSize; i++)
            {
                // The Feature obj here
                Feature f = enrollment_feature_test_list.get(i);

                // Features
                String forOutput
                        = "[" + f.getTotal_times_norm() + ", "
                        + f.getNagivate_times_norm() + ", "
                        + f.getAccess_times_norm() + ", "
                        + f.getProblem_times_norm() + ", "
                        + f.getPage_close_times_norm() + ", "
                        + f.getVideo_times_norm() + ", "
                        + f.getDiscussion_times_norm() + ", "
                        + f.getWiki_times_norm() + "],\n";

                // Special for last one
                if (i == listSize - 1)
                {
                    forOutput
                            = "[" + f.getTotal_times_norm() + ", "
                            + f.getNagivate_times_norm() + ", "
                            + f.getAccess_times_norm() + ", "
                            + f.getProblem_times_norm() + ", "
                            + f.getPage_close_times_norm() + ", "
                            + f.getVideo_times_norm() + ", "
                            + f.getDiscussion_times_norm() + ", "
                            + f.getWiki_times_norm() + "]";
                }

                writer.write(forOutput);
                writer.flush();
            }

            // Write ] to test
            writer.write("]\n");
            writer.flush();

            // Write process
            writer.write("from sklearn.ensemble import RandomForestClassifier\n"
                    + "\n"
                    + "clf = RandomForestClassifier(n_estimators=4000, n_jobs=-1)\n"
                    + "clf = clf.fit(x, y)\nfileOutput = open(\'output.txt\', \'w\')\nresults = clf.predict_proba(testX)\nfor result in results:\n\tfileOutput.write(str(result)+\'\\n\')\nfileOutput.close()\n");
            writer.flush();

            // Close output file stream
            writer.close();
        }
    }
}
