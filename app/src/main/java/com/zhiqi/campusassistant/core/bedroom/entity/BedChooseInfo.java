package com.zhiqi.campusassistant.core.bedroom.entity;

import com.ming.base.bean.BaseJsonData;

import java.util.List;

/**
 * Created by ming on 2017/8/26.
 * 床位选择信息
 */

public class BedChooseInfo implements BaseJsonData {

    public long room_id;

    public String type_name;

    public int live_count;

    public String room;

    public boolean is_up_down;

    public List<BedInfo> beds;

}
