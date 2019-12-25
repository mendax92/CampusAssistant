package com.zhiqi.campusassistant.core.upload.entity;

import com.ming.base.bean.BaseJsonData;

import java.util.List;

/**
 * Created by ming on 2017/2/26.
 */

public abstract class BaseUploadBean implements BaseJsonData {

    public abstract UploadType getUploadType();

    public abstract List<String> getUploadFiles();

    public abstract void setUploadFiles(List<String> uploadFiles);
}
