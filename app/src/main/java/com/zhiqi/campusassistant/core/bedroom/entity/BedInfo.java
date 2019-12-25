package com.zhiqi.campusassistant.core.bedroom.entity;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2017/8/26.
 * 床位信息
 */

public class BedInfo implements BaseJsonData {

    public long bed_id;

    public String bed_name;

    public int bed_type;

    public boolean selected;
}
