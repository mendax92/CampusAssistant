package com.zhiqi.campusassistant.core.user.entity;

import com.ming.base.bean.BaseJsonData;
import com.ming.base.util.GsonUtils;

import java.util.List;

/**
 * Created by ming on 2016/11/30.
 */

public class ContactsList implements BaseJsonData {

    public List<UserInfo> persons;

    public List<DepartmentInfo> children;

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
