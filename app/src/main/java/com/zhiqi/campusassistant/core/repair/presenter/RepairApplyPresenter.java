package com.zhiqi.campusassistant.core.repair.presenter;

import android.content.Context;
import android.webkit.URLUtil;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.presenter.SimplePresenter;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.core.repair.api.RepairApiService;
import com.zhiqi.campusassistant.core.repair.entity.RepairApplyRequest;
import com.zhiqi.campusassistant.core.upload.OnUploadListener;
import com.zhiqi.campusassistant.core.upload.UploadServer;
import com.zhiqi.campusassistant.core.upload.entity.UploadType;
import com.zhiqi.campusassistant.core.upload.model.UploadTask;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/1/14.
 * 维修申请presenter，主要处理跟上传相关
 */

public class RepairApplyPresenter extends SimplePresenter {

    private RepairApiService mApiService;

    private UploadServer uploadServer;

    public RepairApplyPresenter(Context context, RepairApiService apiService, UploadServer uploadServer) {
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
    public void requestRepairApply(RepairApplyRequest requestBody, final IRequestView view, OnUploadListener<RepairApplyRequest> listener) {
        if (requestBody == null) {
            return;
        }
        List<String> images = requestBody.images;
        if (images != null && !images.isEmpty()) {
            for (String image : images) {
                if (!URLUtil.isNetworkUrl(image)) {
                    requestBody.uploadType = UploadType.Maintenance;
                    uploadServer.upload(requestBody, false, listener);
                    return;
                }
            }
        }
        Observable<BaseResultData<List<String>>> observable = mApiService.requestRepairApply(requestBody);
        request(observable, view);
    }

    /**
     * 修改维修申请
     *
     * @param requestBody
     * @param view
     */
    public void updateRepairApply(RepairApplyRequest requestBody, final IRequestView view, OnUploadListener<RepairApplyRequest> listener) {
        if (requestBody == null) {
            return;
        }
        List<String> images = requestBody.images;
        if (images != null && !images.isEmpty()) {
            for (String image : images) {
                if (!URLUtil.isNetworkUrl(image)) {
                    requestBody.uploadType = UploadType.Maintenance;
                    uploadServer.upload(requestBody, true, listener);
                    return;
                }
            }
        }
        Observable<BaseResultData<List<String>>> observable = mApiService.updateRepairApply(requestBody);
        request(observable, view);
    }

    /**
     * 上传重试
     *
     * @param task
     * @param isUpload
     * @param listener
     */
    public void retryUpload(UploadTask<RepairApplyRequest> task, boolean isUpload, OnUploadListener<RepairApplyRequest> listener) {
        uploadServer.retryUpload(task, isUpload, listener);
    }

    public void clearCache(UploadTask<RepairApplyRequest> task) {
        uploadServer.deleteTask(task);
    }
}
