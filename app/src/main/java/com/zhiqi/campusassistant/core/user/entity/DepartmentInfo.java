package com.zhiqi.campusassistant.core.user.entity;

import com.ming.base.bean.BaseJsonData;
import com.ming.base.util.GsonUtils;

/**
 * Created by ming on 2016/10/20.
 */

public class DepartmentInfo implements BaseJsonData {

    public int id;

    public String org_logo;

    public String org_name;

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
