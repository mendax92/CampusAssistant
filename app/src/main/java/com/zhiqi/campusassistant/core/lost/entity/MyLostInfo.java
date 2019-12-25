package com.zhiqi.campusassistant.core.lost.entity;

import com.ming.base.bean.BaseJsonData;
import com.zhiqi.campusassistant.common.entity.ImageData;

import java.util.List;

/**
 * Created by ming on 2017/5/7.
 * 我的失物信息
 */

public class MyLostInfo implements BaseJsonData {

    public int id;

    public int lost_type;

    public LostApplyType publish_type;

    public String type_name;

    public String content;

    public String lost_name;

    public String publish_day;

    public String publish_month;

    public List<ImageData> images;

}
