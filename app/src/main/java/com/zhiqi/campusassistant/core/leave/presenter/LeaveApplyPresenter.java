package com.zhiqi.campusassistant.core.leave.presenter;

import android.content.Context;
import android.webkit.URLUtil;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.presenter.SimplePresenter;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.core.leave.api.LeaveApiService;
import com.zhiqi.campusassistant.core.leave.entity.LeaveRequest;
import com.zhiqi.campusassistant.core.upload.OnUploadListener;
import com.zhiqi.campusassistant.core.upload.UploadServer;
import com.zhiqi.campusassistant.core.upload.entity.UploadType;
import com.zhiqi.campusassistant.core.upload.model.UploadTask;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/3/9.
 * 请假申请
 */

public class LeaveApplyPresenter extends SimplePresenter {

    private LeaveApiService mApiService;

    private UploadServer uploadServer;

    public LeaveApplyPresenter(Context context, LeaveApiService apiService, UploadServer uploadServer) {
        super(context);
        this.mApiService = apiService;
        this.uploadServer = uploadServer;
    }

    /**
     * 报修申请
     *
     * @param requestBody
     * @param view
     * @return 是否上传文件
     */
    public void requestLeaveApply(LeaveRequest requestBody, final IRequestView view, OnUploadListener<LeaveRequest> listener) {
        if (requestBody == null) {
            return;
        }
        List<String> images = requestBody.images;
        if (images != null && !images.isEmpty()) {
            for (String image : images) {
                if (!URLUtil.isNetworkUrl(image)) {
                    requestBody.uploadType = UploadType.Vacation;
                    uploadServer.upload(requestBody, false, listener);
                    return;
                }
            }
        }
        Observable<BaseResultData<List<String>>> observable = mApiService.requestLeaveApply(requestBody);
        request(observable, view);
    }

    /**
     * 修改维修申请
     *
     * @param requestBody
     * @param view
     */
    public void updateLeaveApply(LeaveRequest requestBody, final IRequestView view, OnUploadListener<LeaveRequest> listener) {
        if (requestBody == null) {
            return;
        }
        List<String> images = requestBody.images;
        if (images != null && !images.isEmpty()) {
            for (String image : images) {
                if (!URLUtil.isNetworkUrl(image)) {
                    requestBody.uploadType = UploadType.Vacation;
                    uploadServer.upload(requestBody, true, listener);
                    return;
                }
            }
        }
        Observable<BaseResultData<List<String>>> observable = mApiService.updateLeaveApply(requestBody);
        request(observable, view);
    }

    /**
     * 上传重试
     *
     * @param task
     * @param isUpload
     * @param listener
     */
    public void retryUpload(UploadTask<LeaveRequest> task, boolean isUpload, OnUploadListener<LeaveRequest> listener) {
        uploadServer.retryUpload(task, isUpload, listener);
    }

    public void clearCache(UploadTask<LeaveRequest> task) {
        uploadServer.deleteTask(task);
    }
}
