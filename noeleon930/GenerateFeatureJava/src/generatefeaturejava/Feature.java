package generatefeaturejava;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Noel in hao123 >w<
 */
public class Feature implements Serializable
{

    /*
     * Basic events : 
     * [nagivate, access, problem, page_close, video, discussion, wiki]
     *
     * naViGate ~~~
     */
    private final int enrollment_id;

    private int nagivate_times;
    private int access_times;
    private int problem_times;
    private int page_close_times;
    private int video_times;
    private int discussion_times;
    private int wiki_times;

    private float nagivate_times_norm;
    private float access_times_norm;
    private float problem_times_norm;
    private float page_close_times_norm;
    private float video_times_norm;
    private float discussion_times_norm;
    private float wiki_times_norm;

    private int result;

    public void setResult(int result)
    {
        this.result = result;
    }

    public Feature(int enrollment_id, List<String> logs)
    {
        this.enrollment_id = enrollment_id;

        this.nagivate_times = 0;
        this.access_times = 0;
        this.problem_times = 0;
        this.page_close_times = 0;
        this.video_times = 0;
        this.discussion_times = 0;
        this.wiki_times = 0;

        logs.stream().forEach((log) ->
        {
            this.EatData(log);
        });

        this.reCalculate();
    }

    private void EatData(String data)
    {
        String event = data.split(",")[3];

        switch (event)
        {
            case "nagivate":
                this.nagivate_times += 1;
                break;
            case "access":
                this.access_times += 1;
                break;
            case "problem":
                this.problem_times += 1;
                break;
            case "page_close":
                this.page_close_times += 1;
                break;
            case "video":
                this.video_times += 1;
                break;
            case "discussion":
                this.discussion_times += 1;
                break;
            case "wiki":
                this.wiki_times += 1;
                break;
        }
    }

    private void reCalculate()
    {
        float total_times
                = this.nagivate_times
                + this.access_times
                + this.problem_times
                + this.page_close_times
                + this.video_times
                + this.discussion_times
                + this.wiki_times;

        this.nagivate_times_norm = this.nagivate_times / total_times;
        this.access_times_norm = this.access_times / total_times;
        this.problem_times_norm = this.problem_times / total_times;
        this.page_close_times_norm = this.page_close_times / total_times;
        this.video_times_norm = this.video_times / total_times;
        this.discussion_times_norm = this.discussion_times / total_times;
        this.wiki_times_norm = this.wiki_times / total_times;
    }

    public int getEnrollment_id()
    {
        return enrollment_id;
    }

    public int getNagivate_times()
    {
        return nagivate_times;
    }

    public int getAccess_times()
    {
        return access_times;
    }

    public int getProblem_times()
    {
        return problem_times;
    }

    public int getPage_close_times()
    {
        return page_close_times;
    }

    public int getVideo_times()
    {
        return video_times;
    }

    public int getDiscussion_times()
    {
        return discussion_times;
    }

    public int getWiki_times()
    {
        return wiki_times;
    }

    public float getNagivate_times_norm()
    {
        return nagivate_times_norm;
    }

    public float getAccess_times_norm()
    {
        return access_times_norm;
    }

    public float getProblem_times_norm()
    {
        return problem_times_norm;
    }

    public float getPage_close_times_norm()
    {
        return page_close_times_norm;
    }

    public float getVideo_times_norm()
    {
        return video_times_norm;
    }

    public float getDiscussion_times_norm()
    {
        return discussion_times_norm;
    }

    public float getWiki_times_norm()
    {
        return wiki_times_norm;
    }

    public int getResult()
    {
        return result;
    }
}
