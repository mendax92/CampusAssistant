package com.zhiqi.campusassistant.core.repair.entity;

import com.ming.base.bean.BaseJsonData;

import java.util.List;

/**
 * Created by ming on 2017/1/19.
 * 校区信息
 */

public class CampusDistrict implements BaseJsonData {

    public int district_id;

    public String value;

    public List<CampusLocation> subareas;

    public static class CampusLocation implements BaseJsonData {

        public int location_id;

        public String value;
    }
}
