package generatefeaturejava;

import java.util.List;

/**
 *
 * @author Noel in hao123 >w<
 */
public class Feature
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

    public int getResult()
    {
        return result;
    }
}
