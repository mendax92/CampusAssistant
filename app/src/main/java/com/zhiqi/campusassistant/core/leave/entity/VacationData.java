package com.zhiqi.campusassistant.core.leave.entity;

import com.zhiqi.campusassistant.common.entity.DataVersion;

import java.util.List;

/**
 * Created by ming on 2017/3/8.
 */

public class VacationData extends DataVersion {

    public int version;

    public List<VacationType> items;
}
