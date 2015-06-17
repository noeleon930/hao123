package kddjavatoolchain.DataFormat;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Noel
 */
public class Student
{

    private final String student_id;
    private final List<Course> courses;
    private int courses_num;
    private int dropouts;

    public Student(String student_id, List<Course> courses)
    {
        this.student_id = student_id;
        this.courses = courses;
        this.courses_num = courses.size();
    }

    public Student(String student_id)
    {
        this.student_id = student_id;
        this.courses = new ArrayList<>();
        this.courses_num = courses.size();
    }

    public String getStudent_id()
    {
        return student_id;
    }

    public int getDropouts()
    {
        return dropouts;
    }

    public void setDropouts(int dropouts)
    {
        this.dropouts = dropouts;
    }

    public List<Course> getCourses()
    {
        return courses;
    }

    public int getCourses_num()
    {
        return courses_num;
    }
}
