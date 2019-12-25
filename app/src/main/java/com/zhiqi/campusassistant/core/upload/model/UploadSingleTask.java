package com.zhiqi.campusassistant.core.upload.model;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2017/2/26.
 * 上传任务(功用模块)
 */

public class UploadSingleTask implements BaseJsonData {

    private String filePath;

    private String md5;

    private long parentId;

    private String result;

    private UploadStatus status;

    private long length;

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getParentId() {
        return this.parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public UploadStatus getStatus() {
        return this.status;
    }

    public void setStatus(UploadStatus status) {
        this.status = status;
    }

    public long getLength() {
        return this.length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
