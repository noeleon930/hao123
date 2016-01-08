package kddjavatoolchain.DataProcess;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kddjavatoolchain.DataFormat.EnrollmentLog;

/**
 *
 * @author Noel
 */
public class ComputeSummarizing
{

    public static void Compute()
    {
        DayToScoreMap();
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

    public static void ServerAndBrowser()
    {
        Map<Integer, EnrollmentLog> train_enrollments_map
                = kddjavatoolchain.KddJavaToolChain.getTrain_enrollments_map();

        Map<Integer, EnrollmentLog> test_enrollments_map
                = kddjavatoolchain.KddJavaToolChain.getTest_enrollments_map();

        Set<String> fromTrain
                = train_enrollments_map.entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .map(enrlg -> enrlg.getRawLogs())
                .flatMap(logs -> logs.stream())
                .map(str -> str.split(","))
                .map(sarr -> sarr[2] + "," + sarr[3])
                .distinct()
                .collect(Collectors.toSet());

        Set<String> fromTest
                = test_enrollments_map.entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .map(enrlg -> enrlg.getRawLogs())
                .flatMap(logs -> logs.stream())
                .map(str -> str.split(","))
                .map(sarr -> sarr[2] + "," + sarr[3])
                .distinct()
                .collect(Collectors.toSet());

        Set<String> fromAll = new LinkedHashSet<>();
        fromAll.addAll(fromTrain);
        fromAll.addAll(fromTest);

        fromAll.stream().forEach(System.out::println);
    }

    public static void GetEnrollmentLogsDays()
    {
        Map<Integer, EnrollmentLog> train_enrollments_map
                = kddjavatoolchain.KddJavaToolChain.getTrain_enrollments_map();

        Map<Integer, EnrollmentLog> test_enrollments_map
                = kddjavatoolchain.KddJavaToolChain.getTest_enrollments_map();

        long asLong
                = train_enrollments_map.entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .map(enrlg -> enrlg.getTimeLine())
                .mapToLong(times -> Duration.between(times.get(0), times.get(times.size() - 1)).toDays())
                .max()
                .getAsLong();

        long asLong1
                = test_enrollments_map.entrySet()
                .parallelStream()
                .map(e -> e.getValue())
                .map(enrlg -> enrlg.getTimeLine())
                .mapToLong(times -> Duration.between(times.get(0), times.get(times.size() - 1)).toDays())
                .max()
                .getAsLong();

        System.out.println(asLong + ", " + asLong1);
    }

    public static void TestReduceToAList()
    {
        EnrollmentLog e
                = kddjavatoolchain.KddJavaToolChain.getTrain_enrollments_map().get(1);

        List<String> collectedLogs
                = e.getSortedLogs()
                .stream()
                .map(log -> log.split(","))
                .map(sarr -> sarr[1] + "," + sarr[2] + "," + sarr[3] + "," + sarr[4])
                .collect(Collectors.toList());

        List<String> filteredLogs = new ArrayList<>();

        int listSize = collectedLogs.size();
        for (int i = 0; i < listSize; i++)
        {
            filteredLogs.add(collectedLogs.get(i));

            String current = collectedLogs.get(i).split(",", 2)[1];

            for (int j = i + 1; j < listSize; j++)
            {
                String next = collectedLogs.get(j).split(",", 2)[1];

                if (!current.equals(next))
                {
                    i = j - 1;
                    break;
                }
            }
        }

        filteredLogs.forEach(str -> System.out.println(str));
    }

    public static void DayToScoreMap()
    {
        EnrollmentLog e = kddjavatoolchain.KddJavaToolChain.getTrain_enrollments_map().get(1);

        Map<String, String> DayToEventsMap
                = e.getUniquedLogs()
                .stream()
                .map(log -> log.split(","))
                .map(sarr -> sarr[1].split("T")[0] + "," + sarr[3])
                .map(DayAndEvents -> DayAndEvents.split(","))
                .collect(Collectors.toMap(DayAndEvents -> DayAndEvents[0], DayAndEvents -> DayAndEvents[1], (s1, s2) -> s1 + "," + s2));

        Map<String, Integer> DayToScoreMap
                = DayToEventsMap
                .entrySet()
                .stream()
                .collect(Collectors
                        .toMap(
                                ee -> ee.getKey(),
                                ee ->
                                {
                                    int score
                                    = Stream
                                    .of(ee.getValue().split(","))
                                    .mapToInt(s ->
                                            {
                                                int to;

                                                switch (s)
                                                {
                                                    case "nagivate":
                                                        to = 1;
                                                        break;
                                                    case "page_close":
                                                        to = 1;
                                                        break;
                                                    case "access":
                                                        to = 2;
                                                        break;
                                                    case "video":
                                                        to = 2;
                                                        break;
                                                    case "wiki":
                                                        to = 4;
                                                        break;
                                                    case "problem":
                                                        to = 6;
                                                        break;
                                                    case "discussion":
                                                        to = 8;
                                                        break;
                                                    default:
                                                        to = 0;
                                                        break;
                                                }

                                                return to;
                                    })
                                    .sum();

                                    return score;
                                })
                );

        DayToScoreMap.entrySet().stream().forEach(ee -> System.out.println(ee.getKey() + ":" + ee.getValue()));
    }
}
