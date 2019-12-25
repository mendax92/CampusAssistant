package com.zhiqi.campusassistant.core.message.entity;

import com.google.gson.annotations.JsonAdapter;
import com.ming.base.bean.BaseJsonData;
import com.ming.base.util.GsonUtils;

import java.util.List;

/**
 * Created by ming on 2016/12/20.
 * 消息模版信息
 */
@JsonAdapter(ModuleConverter.class)
public class ModuleInfo implements BaseJsonData {

    public ModuleType module_id;

    public String module_name;

    public String update_time;

    public int weeks;

    public String datetime;

    public List<? extends BaseJsonData> data;

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
