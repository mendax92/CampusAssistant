package com.zhiqi.campusassistant.core.lost.entity;

import com.ming.base.bean.BaseJsonData;
import com.zhiqi.campusassistant.common.entity.DataVersion;

import java.util.List;

/**
 * Created by ming on 2017/5/8.
 * 失物类型
 */

public class LostTypeVersion extends DataVersion {

    public int version;

    public List<LostType> items;

    public static class LostType implements BaseJsonData {

        public int id;

        public String type_name;

        public int seq;
    }
}
