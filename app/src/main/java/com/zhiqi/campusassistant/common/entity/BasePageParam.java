package com.zhiqi.campusassistant.common.entity;

import com.google.gson.annotations.SerializedName;
import com.ming.base.bean.BaseJsonData;
import com.zhiqi.campusassistant.config.AppConfigs;

/**
 * Created by ming on 2017/2/12.
 * 基础分页参数
 */

public class BasePageParam implements BaseJsonData {

    @SerializedName("page_no")
    public int pageNo;

    @SerializedName("page_size")
    public int pageSize = AppConfigs.DEFAULT_PAGE_SIZE;
}
