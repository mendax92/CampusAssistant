package com.zhiqi.campusassistant.core.upload.service;

import com.zhiqi.campusassistant.core.upload.entity.BaseUploadBean;
import com.zhiqi.campusassistant.core.upload.model.UploadTask;

import io.reactivex.ObservableEmitter;

/**
 * Created by ming on 2017/2/27.
 * 上传步骤
 */

public interface UploadStep {

    <T extends BaseUploadBean> void onNext(UploadTask<T> task, ObservableEmitter<? super UploadTask<T>> subscriber);
}
