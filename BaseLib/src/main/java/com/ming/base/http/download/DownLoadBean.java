package com.ming.base.http.download;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2017/1/7.
 * 下载基础类
 */
public class DownLoadBean implements BaseJsonData {

    public String downloadUrl;

    public String filePath;

    public long downloadSize;

    public long fileSize;

    public String tempFile;

    public DownloadStatus status = DownloadStatus.Waiting;

    public Object tag;

    public DownLoadBean() {
    }

    public DownLoadBean(String downloadUrl, String filePath) {
        this.downloadUrl = downloadUrl;
        this.filePath = filePath;
    }

    public int getProgress() {
        if (fileSize > 0) {
            return (int) (((double) downloadSize / fileSize) * 100);
        }
        return 0;
    }

}
