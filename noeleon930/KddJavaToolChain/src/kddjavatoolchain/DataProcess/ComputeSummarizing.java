package kddjavatoolchain.DataProcess;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Collectors;
import kddjavatoolchain.DataFormat.EnrollmentLog;

/**
 *
 * @author Noel
 */
public class ComputeSummarizing
{

    public static void Compute()
    {
        ConcurrentHashMap<Integer, DoubleAdder> surMap0 = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer, DoubleAdder> surMap1 = new ConcurrentHashMap<>();

        ConcurrentHashMap<Integer, EnrollmentLog> trainMap = new ConcurrentHashMap<>(kddjavatoolchain.KddJavaToolChain.getTrain_enrollments_map());

        ConcurrentMap<Integer, List<EnrollmentLog>> resultToEnrollmentMap
                = trainMap.entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .collect(Collectors.groupingByConcurrent(e -> e.getResult()));

        resultToEnrollmentMap.get(1)
                .parallelStream()
                .map(er -> er.getFeatures())
                .map(fl -> fl.subList(0, 7))
                .forEach(ft ->
                        {
                            for (int i = 0; i < 7; i++)
                            {
                                surMap1.putIfAbsent(i, new DoubleAdder());
                                surMap1.get(i).add(ft.get(i));
                            }
                });

        resultToEnrollmentMap.get(0)
                .parallelStream()
                .map(er -> er.getFeatures())
                .map(fl -> fl.subList(0, 7))
                .forEach(ft ->
                        {
                            for (int i = 0; i < 7; i++)
                            {
                                surMap0.putIfAbsent(i, new DoubleAdder());
                                surMap0.get(i).add(ft.get(i));
                            }
                });

        trainMap.entrySet()
                .stream()
                .sequential()
                .limit(10)
                .map(e -> e.getValue())
                .parallel()
                .sorted((e1, e2) -> e1.getID() - e2.getID())
                .sequential()
                .map(e -> e.getResult())
                .forEachOrdered(System.out::println);

        surMap0.entrySet().stream().sequential().forEachOrdered(e -> System.out.print(e.getKey() + ":" + e.getValue().floatValue() + ", "));
        System.out.println("\n----");
        surMap1.entrySet().stream().sequential().forEachOrdered(e -> System.out.print(e.getKey() + ":" + e.getValue().floatValue() + ", "));
        System.out.println(" ");
    }

    public static void GetLonggestLogLength()
    {
        Optional<Integer> train_max = kddjavatoolchain.KddJavaToolChain
                .getTrain_enrollments_map()
                .entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .map(e -> e.getRawLogs())
                .map(e -> e.size())
                .max((size1, size2) -> Integer.compare(size1, size2));

        Optional<Integer> test_max = kddjavatoolchain.KddJavaToolChain
                .getTest_enrollments_map()
                .entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .map(e -> e.getRawLogs())
                .map(e -> e.size())
                .max((size1, size2) -> Integer.compare(size1, size2));

        System.out.println(train_max.get() + ", " + test_max.get());

    }
}
