package com.zhiqi.campusassistant.core.leave.entity;

import com.ming.base.bean.BaseJsonData;
import com.ming.base.util.GsonUtils;

/**
 * Created by Edmin on 2016/11/13 0013.
 */

public class LeaveInfo implements BaseJsonData {

    public long id;

    public String type_name;

    public String applicant_name;

    public String start_time;

    public String end_time;

    public float total_days;

    public String reason;

    public String apply_time;

    public String approver;

    public LeaveStatus status;

    public String status_name;

    public String department;

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
