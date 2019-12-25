package com.zhiqi.campusassistant.core.scores.entity;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2017/5/13.
 * 成绩详情
 */

public class ScoreDetail implements BaseJsonData {

    public String course_type;

    public String course_name;

    public String examine_mode;

    public String score;

    public String credits;

    public String school_year;

    public String semester;

    public String teacher;

    public String usual_score;

    public String final_score;

    public String retest_score;

    public String rebuild_score;
}
