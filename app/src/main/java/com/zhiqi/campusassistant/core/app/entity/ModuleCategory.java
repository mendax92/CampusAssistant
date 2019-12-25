package com.zhiqi.campusassistant.core.app.entity;

import com.ming.base.bean.BaseJsonData;

import java.util.List;

/**
 * Created by ming on 2017/5/2.
 */

public class ModuleCategory extends BannerInfos implements BaseJsonData {

    public int module_category;

    public String module_category_name;

    public List<AppInfo> list;
}
