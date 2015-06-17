package kddjavatoolchain.DataFormat;

/**
 *
 * @author Noel
 */
public class Enrollment
{

    private final int enrollemt_id;
    private final String username;
    private final String course_id;
    private int result;

    public Enrollment(int enrollemt_id, String username, String course_id)
    {
        this.enrollemt_id = enrollemt_id;
        this.username = username;
        this.course_id = course_id;
    }

    public int getResult()
    {
        return result;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    public int getEnrollemt_id()
    {
        return enrollemt_id;
    }

    public String getUsername()
    {
        return username;
    }

    public String getCourse_id()
    {
        return course_id;
    }

}
