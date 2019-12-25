package com.zhiqi.campusassistant.core.course.entity;

import com.ming.base.bean.BaseJsonData;
import com.ming.base.widget.TwoDimensionalLayout;

/**
 * Created by ming on 2016/12/16.
 */

public class UICourseInfo extends TwoDimensionalLayout.BaseDataItem<CourseInfo> implements BaseJsonData {

    public UICourseInfo(CourseInfo info) {
        this.item = info;
        this.placeSize = info.class_end - info.class_start + 1;
    }
}
