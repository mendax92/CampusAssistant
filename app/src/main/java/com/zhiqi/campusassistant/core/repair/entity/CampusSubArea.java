package com.zhiqi.campusassistant.core.repair.entity;

import com.ming.base.bean.BaseJsonData;

import java.util.List;

/**
 * Created by ming on 2017/1/19.
 * 校区信息列表
 */

public class CampusSubArea implements BaseJsonData {

    public int version;

    public List<CampusDistrict> items;
}
