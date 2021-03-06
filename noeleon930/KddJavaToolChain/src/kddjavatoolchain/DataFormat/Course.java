package kddjavatoolchain.DataFormat;

import java.time.Instant;
import java.util.List;

/**
 *
 * @author Noel
 */
public class Course
{

    private final String course_id;
    private final List<Module> modules;
    private final List<Student> students;
    private final int students_num;
    private int dropouts;

    private Instant startTime;
    private Instant endTime;

    public Course(String course_id, List<Module> modules, List<Student> students)
    {
        this.course_id = course_id;
        this.modules = modules;
        this.students = students;
        this.students_num = students.size();
    }

    public Course(String course_id)
    {
        this.course_id = course_id;
        this.modules = null;
        this.students = null;
        this.students_num = 0;
    }

    public String getCourse_id()
    {
        return course_id;
    }

    public List<Module> getModules()
    {
        return modules;
    }

    public List<Student> getStudents()
    {
        return students;
    }

    public int getStudents_num()
    {
        return students_num;
    }

    public int getDropouts()
    {
        return dropouts;
    }

    public void setDropouts(int dropouts)
    {
        this.dropouts = dropouts;
    }

    public Instant getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Instant startTime)
    {
        this.startTime = startTime;
    }

    public Instant getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Instant endTime)
    {
        this.endTime = endTime;
    }
}
