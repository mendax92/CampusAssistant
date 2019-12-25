package com.zhiqi.campusassistant.core.course.entity;

import com.ming.base.bean.BaseJsonData;

import java.util.List;

/**
 * Created by ming on 2016/12/16.
 * 课程数据
 */

public class CourseData implements BaseJsonData {

    public long start_date;

    public int lessons;

    public int weeks;

    public List<CourseInfo> courses;

}
