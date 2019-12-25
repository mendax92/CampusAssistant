package com.zhiqi.campusassistant.common.entity;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by Edmin on 2016/10/15
 * HTTP 基本数据
 */

public class BaseResultData<T> implements BaseJsonData {

    public String message;

    public int error_code;

    public T data;
}
