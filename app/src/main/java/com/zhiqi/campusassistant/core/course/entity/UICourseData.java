package com.zhiqi.campusassistant.core.course.entity;

import com.ming.base.bean.BaseJsonData;
import com.ming.base.util.GsonUtils;

import java.util.List;

/**
 * Created by ming on 2016/12/16.
 * ui课程表信息
 */

public class UICourseData implements BaseJsonData {

    /**
     * 一共多少周
     */
    public int weekCount;

    /**
     * 当前属于第几周
     */
    public int currentWeek;

    /**
     * 一天的课数
     */
    public int lessonCount;

    /**
     * 本周一时间
     */
    public long mondayTime;

    /**
     * 是否显示周末课程
     */
    public boolean showEndWeek;

    /**
     * 开始的周一时间
     */
    public long startMonday;

    /**
     * 课程信息
     */
    public List<UICourseInfo> courseInfos;

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
