package com.zhiqi.campusassistant.core.app.entity;

import com.ming.base.bean.BaseJsonData;
import com.zhiqi.campusassistant.core.message.entity.ModuleType;

/**
 * Created by ming on 2017/3/11.
 * app列表信息
 */

public class AppInfo implements BaseJsonData {

    public ModuleType module_id;

    public String module_name;

    public String detail_information;

    public int module_category;

    public String module_icon;

    public int badge;

    public boolean is_active;

    public boolean is_release;
}
