package kddjavatoolchain.DataFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 *
 * @author Noel
 */
public class Student
{

    private final String student_id;
    private final List<Course> courses;
    private final Set<Integer> coursesID;
    private final Set<String> timeline;
    private final List<String> sortedtimeline;
    private final int courses_num;
    private int dropouts;

    public Student(String student_id, List<Course> courses)
    {
        this.student_id = student_id;
        this.courses = courses;
        this.coursesID = new ConcurrentSkipListSet<>();
        this.timeline = new ConcurrentSkipListSet<>();
        this.sortedtimeline = new ArrayList<>();
        this.courses_num = courses.size();
    }

    public Student(String student_id)
    {
        this.student_id = student_id;
        this.courses = null;
        this.coursesID = null;
        this.timeline = null;
        this.sortedtimeline = null;
        this.courses_num = 0;
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

    public Set<String> getTimeline()
    {
        return timeline;
    }

    public void importToSortedTimeline()
    {
        this.timeline
                .stream()
                .sorted((s1, s2) -> s1.compareTo(s2))
                .sequential()
                .forEachOrdered(s ->
                        {
                            this.sortedtimeline.add(s);
                });
    }

    public List<String> getSortedtimeline()
    {
        return sortedtimeline;
    }

    public Set<Integer> getCoursesID()
    {
        return coursesID;
    }
}
