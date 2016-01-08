package kddjavatoolchain.OutputToFile;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MakeFirstRow
{

    public static String has()
    {
        return ""
                + "nagivate"
                + ",page_close"
                + ",access"
                + ",video"
                + ",wiki"
                + ",problem"
                + ",discussion"
                + ",unk_nagivate"
                + ",unk_page_close"
                + ",unk_access"
                + ",unk_video"
                + ",unk_wiki"
                + ",unk_problem"
                + ",unk_discussion"
                + ",lt2wk_nagivate"
                + ",lt2wk_page_close"
                + ",lt2wk_access"
                + ",lt2wk_video"
                + ",lt2wk_wiki"
                + ",lt2wk_problem"
                + ",lt2wk_discussion"
                + ",unk_lt2wk_nagivate"
                + ",unk_lt2wk_page_close"
                + ",unk_lt2wk_access"
                + ",unk_lt2wk_video"
                + ",unk_lt2wk_wiki"
                + ",unk_lt2wk_problem"
                + ",unk_lt2wk_discussion"
                + ",ltwk_nagivate"
                + ",ltwk_page_close"
                + ",ltwk_access"
                + ",ltwk_video"
                + ",ltwk_wiki"
                + ",ltwk_problem"
                + ",ltwk_discussion"
                + ",unk_ltwk_nagivate"
                + ",unk_ltwk_page_close"
                + ",unk_ltwk_access"
                + ",unk_ltwk_video"
                + ",unk_ltwk_wiki"
                + ",unk_ltwk_problem"
                + ",unk_ltwk_discussion"
                + ",courses_student_had_othercourse"
                + ",enrollment_about_num"
                + ",enrollment_chapter_num"
                + ",enrollment_course_num"
                + ",enrollment_course_info_num"
                + ",enrollment_html_num"
                + ",enrollment_outlink_num"
                + ",enrollment_problem_num"
                + ",enrollment_sequential_num"
                + ",enrollment_static_tab_num"
                + ",enrollment_vertical_num"
                + ",enrollment_video_num"
                + ",enrollment_combinedopenended_num"
                + ",enrollment_peergrading_num"
                + ",enrollment_discussion_num"
                + ",enrollment_dictation_num"
                + ",enrollment_unknown_num"
                + ",nagivate_duration"
                + ",page_close_duration"
                + ",access_duration"
                + ",video_duration"
                + ",wiki_duration"
                + ",problem_duration"
                + ",discussion_duration"
                + ",unk_nagivate_duration"
                + ",unk_page_close_duration"
                + ",unk_access_duration"
                + ",unk_video_duration"
                + ",unk_wiki_duration"
                + ",unk_problem_duration"
                + ",unk_discussion_duration"
                + ",student_logginThisCourse_score"
                + ",student_logginDays_score"
                + ",isfirstcourse"
                + ",islastcourse"
                + ",sametime_courses"
                + ",monday"
                + ",tuesday"
                + ",wednesday"
                + ",thursday"
                + ",friday"
                + ",saturday"
                + ",sunday"
                + ",afterCourseStart"
                + ",activedays"
                + ",beforeCourseEnd"
                + ",loggin_morning_1"
                + ",loggin_morning_2"
                + ",loggin_afternoon_1"
                + ",loggin_afternoon_2"
                + ",loggin_night_1"
                + ",loggin_night_2"
                + ",to_special_days"
                + ",server_access"
                + ",server_problem"
                + ",browser_access"
                + ",browser_problem"
                + ",unk_server_access"
                + ",unk_server_problem"
                + ",unk_browser_access"
                + ",unk_browser_problem"
                + ",studrop"
                + ",coursedrop"
                + "\n";
    }

    public static String empty()
    {
        return "";
    }

    public static String index()
    {
        int size
                = kddjavatoolchain.KddJavaToolChain.getTrain_enrollments_map().get(1).getFeatures().size();

        String indexes
                = IntStream.range(0, size)
                .mapToObj(i -> i + "")
                .collect(Collectors.joining(","));

        indexes = indexes + "\n";

        return indexes;
    }
}
