package com.zhiqi.campusassistant.core.upload;

import com.zhiqi.campusassistant.core.upload.entity.BaseUploadBean;
import com.zhiqi.campusassistant.core.upload.model.UploadTask;

/**
 * Created by ming on 2017/5/10.
 * 上传回调
 */

public interface OnUploadListener<T extends BaseUploadBean> {
    void onSuccess(UploadTask<T> task, String message);

    void onFailed(UploadTask<T> task, int errorCode, String msg);
}
