package com.zhiqi.campusassistant.core.upload.model;

import com.ming.base.bean.BaseJsonData;
import com.zhiqi.campusassistant.core.upload.entity.BaseUploadBean;

import java.util.List;
import java.util.Map;

/**
 * Created by ming on 2017/2/26.
 * 上传任务（功用模块）
 */
public class UploadTask<T extends BaseUploadBean> implements BaseJsonData {

    private String url;

    private boolean wholeCommit;

    private List<UploadSingleTask> singleTasks;

    private Map<String, String> params;

    private String uploadKey;

    private String result;

    private T entity;

    private long progress;

    private long length;

    private UploadStatus status;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getWholeCommit() {
        return this.wholeCommit;
    }

    public void setWholeCommit(boolean wholeCommit) {
        this.wholeCommit = wholeCommit;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getUploadKey() {
        return this.uploadKey;
    }

    public void setUploadKey(String uploadKey) {
        this.uploadKey = uploadKey;
    }

    public List<UploadSingleTask> getSingleTasks() {
        return singleTasks;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }


    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public UploadStatus getStatus() {
        return status;
    }

    public void setStatus(UploadStatus status) {
        this.status = status;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setSingleTasks(List<UploadSingleTask> singleTasks) {
        this.singleTasks = singleTasks;
    }

}
