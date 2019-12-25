package com.zhiqi.campusassistant.core.repair.entity;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2016/12/23.
 * 维修信息
 */

public class RepairInfo implements BaseJsonData {

    public long id;

    public RepairStatus status;

    public String status_name;

    public String apply_time;

    public int repair_type;

    public String type_name;

    public String detail;

    public String appointment;

    public String location;

    public String applicant_name;
}
