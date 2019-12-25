package com.zhiqi.campusassistant.core.scores.entity;

import com.ming.base.bean.BaseJsonData;

import java.util.List;

/**
 * Created by ming on 2017/5/11.
 * 学期信息
 */

public class SemesterInfo implements BaseJsonData {

    /**
     * 学年
     */
    public List<Semester> school_years;

    /**
     * 学期
     */
    public List<Semester> semester;


    public class Semester implements BaseJsonData {

        public String id;

        public String type_name;

        public int seq;
    }
}
