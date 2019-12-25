package com.zhiqi.campusassistant.core.repair.entity;

import com.ming.base.bean.BaseJsonData;

import java.util.List;

/**
 * Created by ming on 2017/1/18.
 * 维修类型项目
 */

public class RepairItem implements BaseJsonData {

    public int version;

    public List<RepairType> items;

}
