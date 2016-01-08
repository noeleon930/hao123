package kddjavatoolchain.DataFormat;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kddjavatoolchain.DataProcess.ComputeFeatures;

/**
 *
 * @author Noel
 */
public class EnrollmentLog implements Serializable
{

    private final int ID;
    private final List<Float> Features;
    private final List<Float> TimeSeriesFeatures;
    private final List<String> RawLogs;
    private final List<String> SortedLogs;
    private final List<String> UniquedLogs;
    private final List<Instant> TimeLine;
    private final List<Module> Modules;

    private int Result;
    private int[] timeline30scores;

    private String username;
    private String course_id;

    public EnrollmentLog(int ID, List<String> RawLogs)
    {
        this.ID = ID;
        this.Features = new ArrayList<>();
        this.TimeSeriesFeatures = new ArrayList<>();
        this.RawLogs = RawLogs;
        this.TimeLine = new ArrayList<>();
        this.SortedLogs = GenerateSortedLogsAndTimeLine();
        this.UniquedLogs = GenerateUniquedLogs();
        this.Modules = GenerateModules();
        this.Result = -1;
    }

    private List<String> GenerateSortedLogsAndTimeLine()
    {
        return this.RawLogs
                .stream()
                .sorted((log1, log2) -> log1.split(",")[1].compareTo(log2.split(",")[1]))
                .sequential()
                .peek(log -> this.TimeLine.add(Instant.parse(log.split(",")[1] + "Z")))
                .collect(Collectors.toList());
    }

    private List<String> GenerateUniquedLogs()
    {
        List<String> tmpLogs = new ArrayList<>();

        int listSize = this.SortedLogs.size();
        for (int i = 0; i < listSize; i++)
        {
            String tmp = this.SortedLogs.get(i);

            tmpLogs.add(tmp);

            String current = tmp.split(",", 3)[2];

            for (int j = i + 1; j < listSize; j++)
            {
                String next = this.SortedLogs.get(j).split(",", 3)[2];

                if (!current.equals(next))
                {
                    i = j - 1;
                    break;
                }
            }
        }

        return tmpLogs;
    }

    private List<Module> GenerateModules()
    {
        return this.RawLogs
                .stream()
                .map(str -> str.split(",")[4])
                .map(str -> new Module(str))
                .collect(Collectors.toList());
    }

    public void GenerateFeatures()
    {
        // Basic raw freq of 7 log features
        // ComputeFeatures.BasicSevenFeatures(this); // 7
        // ComputeFeatures.LastTwoWeekSevenFeatures(this); // 7
        // ComputeFeatures.LastWeekSevenFeatures(this); // 7
        ComputeFeatures.BasicSevenKnownFeatures(this); //7
        ComputeFeatures.BasicSevenUnknownFeatures(this); //7
        ComputeFeatures.LastTwoWeekSevenKnownFeatures(this); //7
        ComputeFeatures.LastTwoWeekSevenUnknownFeatures(this); //7
        ComputeFeatures.LastWeekSevenKnownFeatures(this); //7
        ComputeFeatures.LastWeekSevenUnknownFeatures(this); //7
        // How many courses did this student take? 
        ComputeFeatures.StudentHadOtherCourses(this); // 1
        // How many courses did this student drop?
        // ComputeFeatures.StudentDropouttedOtherCourses(this); // 1
        // How many students did this courses contain?
        // ComputeFeatures.CourseHadStudentsPercent(this);
        // How many students in this courses dropped?
        // ComputeFeatures.CourseDropouttedStudents(this); // 1
        // Basic raw freq of each course (like how many chapters, how many videos, how many basics..)
        // ComputeFeatures.CourseObjectFeatures(this);
        // Basic raw freq of 7 log features in last login
        // The Module this enrollment actually had
        ComputeFeatures.ModulesAccessed(this); // 16
        // The duration of 7 log features
        ComputeFeatures.DurationSevenFeatures(this); // 7
        // >w<
        ComputeFeatures.StudentTimeLines(this); // 5
        // Days and hours
        ComputeFeatures.CourseTimeLines(this); // 16
        // Special days
        ComputeFeatures.CourseTimeLinesForSpecialDays(this); // 1
        // Server or Browser
        ComputeFeatures.AccessAndProblemKnownFromServerOrBrowser(this); //4
        ComputeFeatures.AccessAndProblemUnknownFromServerOrBrowser(this); //4
        // Dropout Test
        ComputeFeatures.StudentDropouttedOtherCourses(this);
        ComputeFeatures.CourseDropouttedStudents(this);
        // Last two weeks active days
        // ComputeFeatures.LastTwoWeekActiveDays(this);
        // Last week active days
        // ComputeFeatures.LastWeekActiveDays(this);
        ComputeFeatures.LastTwoWeekActiveDaysMinusLastWeekActiveDays(this);
        ComputeFeatures.LastWeekActiveDays(this);
        // 30 days timeline
        ComputeFeatures.EnrollmentTimeLine30(this);
        // 7X7!
        ComputeFeatures.Basic7x7Matrix(this);
    }

    public void GenerateTimeSeriesFeatures()
    {
//        ComputeFeatures.Basic7x7Matrix(this);
//        ComputeFeatures.Basic7x7FinalState(this);
//        ComputeFeatures.Duration7x7Matrix(this);
//        ComputeFeatures.Duration7x7NormMatrix(this);
//        ComputeFeatures.OffsetTimeSeries(this);
    }

    public int getID()
    {
        return ID;
    }

    public List<Float> getFeatures()
    {
        return Features;
    }

    public List<String> getRawLogs()
    {
        return RawLogs;
    }

    public List<String> getSortedLogs()
    {
        return SortedLogs;
    }

    public List<Instant> getTimeLine()
    {
        return TimeLine;
    }

    public int getResult()
    {
        return Result;
    }

    public void setResult(int Result)
    {
        this.Result = Result;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getCourse_id()
    {
        return course_id;
    }

    public void setCourse_id(String course_id)
    {
        this.course_id = course_id;
    }

    public List<Module> getModules()
    {
        return Modules;
    }

    public List<Float> getTimeSeriesFeatures()
    {
        return TimeSeriesFeatures;
    }

    public List<String> getUniquedLogs()
    {
        return UniquedLogs;
    }

    public int[] getTimeline30scores()
    {
        return timeline30scores;
    }

    public void setTimeline30scores(int[] timeline30scores)
    {
        this.timeline30scores = timeline30scores;
    }
}
