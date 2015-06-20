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
    private final List<Instant> TimeLine;
    private final List<Module> Modules;

    private int Result;

    private String username;
    private String course_id;

    public EnrollmentLog(int ID, List<String> RawLogs)
    {
        this.ID = ID;
        this.Features = new ArrayList<>();
        this.TimeSeriesFeatures = new ArrayList<>();
        this.RawLogs = RawLogs;
        this.TimeLine = new ArrayList<>();
        this.SortedLogs = GenerateSortedLogsAndTimeLine(10);
        this.Modules = GenerateModules();
        this.Result = -1;
    }

    private List<String> GenerateSortedLogsAndTimeLine()
    {
        int listSize = this.RawLogs.size();
        return this.RawLogs
                .stream()
                .sorted((log1, log2) -> log2.split(",")[1].compareTo(log1.split(",")[1]))
                .sequential()
                .map((log) ->
                        {
                            this.TimeLine.add(Instant.parse(log.split(",")[1] + "Z"));
                            return log;
                })
                .collect(Collectors.toList());
    }

    private List<String> GenerateSortedLogsAndTimeLine(int factor)
    {
        int listSize = this.RawLogs.size();
        return this.RawLogs
                .stream()
                .sorted((log1, log2) -> log2.split(",")[1].compareTo(log1.split(",")[1]))
                .sequential()
                .limit(listSize / factor + 1)
                .sorted((log1, log2) -> log1.split(",")[1].compareTo(log2.split(",")[1]))
                .sequential()
                .map((log) ->
                        {
                            this.TimeLine.add(Instant.parse(log.split(",")[1] + "Z"));
                            return log;
                })
                .collect(Collectors.toList());
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
        ComputeFeatures.BasicSevenFeatures(this); // 7
        // How many courses did this student take? 
        ComputeFeatures.StudentHadCourses(this); // 1
        // How many courses did this student drop?
        // ComputeFeatures.StudentDropouttedCourses(this); // 1
        // How many students did this courses contain?
        ComputeFeatures.CourseHadStudents(this); // 1
        // How many students in this courses dropped?
        // ComputeFeatures.CourseDropouttedStudents(this); // 1
        // Basic raw freq of each course (like how many chapters, how many videos, how many basics..)
        ComputeFeatures.CourseObjectFeatures(this); // 15
        // Basic raw freq of 7 log features in last login
        // The Module this enrollment actually had
        ComputeFeatures.ModulesAccessed(this); // 15
        // The duration of 7 log features
        ComputeFeatures.SevenFeaturesDuration(this); // 7
    }

    public void GenerateTimeSeriesFeatures()
    {
        ComputeFeatures.Basic7x7Matrix(this);
        ComputeFeatures.Basic7x7NormMatrix(this);
        ComputeFeatures.Duration7x7Matrix(this);
        ComputeFeatures.Duration7x7NormMatrix(this);
        ComputeFeatures.OffsetTimeSeries(this);
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
}
