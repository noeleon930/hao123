package kddjavatoolchain.DataFormat;

import java.io.Serializable;

/**
 *
 * @author Noel
 */
public class Module implements Serializable
{

    private final String course_id;
    private final String module_id;
    private final String category;

    public Module(String course_id, String module_id, String category)
    {
        this.course_id = course_id;
        this.module_id = module_id;
        this.category = category;
    }

    public Module(String module_id)
    {
        this.course_id = "";
        this.module_id = module_id;
        this.category = "";
    }

    public String getCourse_id()
    {
        return course_id;
    }

    public String getModule_id()
    {
        return module_id;
    }

    public String getCategory()
    {
        return category;
    }
}
