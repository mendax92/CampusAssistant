package com.ming.base.http.download;

/**
 * Created by ming on 2017/1/7.
 * 下载状态
 */

public enum DownloadStatus {

    /**
     * 等待
     */
    Waiting(0),

    /**
     * 准备
     */
    Prepare(1),

    /**
     * 正在下载
     */
    Downloading(2),

    /**
     * 完成
     */
    Finish(3);

    private int value;

    DownloadStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
