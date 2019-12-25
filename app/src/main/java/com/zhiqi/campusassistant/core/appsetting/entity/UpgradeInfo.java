package com.zhiqi.campusassistant.core.appsetting.entity;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2017/5/21.
 * 升级信息
 */

public class UpgradeInfo implements BaseJsonData {

    public String update_log;

    public String path;

    public boolean forced_update;

    public int version_id;
}
