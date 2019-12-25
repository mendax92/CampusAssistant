package com.zhiqi.campusassistant.core.bedroom.entity;

import com.google.gson.annotations.JsonAdapter;
import com.ming.base.bean.BaseJsonData;
import com.ming.base.gson.JsonTypeDeserializer;
import com.ming.base.gson.JsonTypeInfo;
import com.ming.base.gson.SubType;

/**
 * Created by ming on 2017/7/30.
 * 床位宿舍信息
 */

@JsonAdapter(JsonTypeDeserializer.class)
@JsonTypeInfo(property = "is_selected", subType = {@SubType(value = BedRoomDetail.class, property = "true")})
public class BedRoomInfo implements BaseJsonData {

    public long room_id;

    public String image;

    public String room;

    public String location;

    public String type_name;

    public boolean is_selected;
}
