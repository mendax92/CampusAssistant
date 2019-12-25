package com.zhiqi.campusassistant.core.lost.entity;

import com.ming.base.bean.BaseJsonData;
import com.zhiqi.campusassistant.common.entity.ImageData;

import java.util.List;

/**
 * Created by ming on 2017/5/5.
 * 失物信息
 */

public class LostInfo implements BaseJsonData {

    public int id;

    public String user_name;

    public String avatar;

    public String faculty;

    public String content;

    public int lost_type;

    public String lost_name;

    public String phone;

    public String publish_time;


    /**
     * 图片数据
     */
    public List<ImageData> images;

}
