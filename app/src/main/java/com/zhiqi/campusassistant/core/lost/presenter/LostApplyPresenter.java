package com.zhiqi.campusassistant.core.lost.presenter;

import android.content.Context;
import android.webkit.URLUtil;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.presenter.SimplePresenter;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.core.lost.api.LostFoundApiService;
import com.zhiqi.campusassistant.core.lost.entity.LostApplyRequest;
import com.zhiqi.campusassistant.core.upload.OnUploadListener;
import com.zhiqi.campusassistant.core.upload.UploadServer;
import com.zhiqi.campusassistant.core.upload.entity.UploadType;
import com.zhiqi.campusassistant.core.upload.model.UploadTask;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/5/10.
 * 失物招领申请
 */

public class LostApplyPresenter extends SimplePresenter {


    private LostFoundApiService mApiService;

    private UploadServer uploadServer;

    public LostApplyPresenter(Context context, LostFoundApiService apiService, UploadServer uploadServer) {
        super(context);
        this.mApiService = apiService;
        this.uploadServer = uploadServer;
    }

    /**
     * 失物招领申请
     *
     * @param requestBody
     * @param view
     * @return 是否上传文件
     */
    public void requestLostApply(LostApplyRequest requestBody, final IRequestView view, OnUploadListener<LostApplyRequest> listener) {
        if (requestBody == null) {
            return;
        }
        List<String> images = requestBody.images;
        if (images != null && !images.isEmpty()) {
            for (String image : images) {
                if (!URLUtil.isNetworkUrl(image)) {
                    requestBody.uploadType = UploadType.LOST;
                    uploadServer.upload(requestBody, false, listener);
                    return;
                }
            }
        }
        Observable<BaseResultData<List<String>>> observable = mApiService.requestLostApply(requestBody);
        request(observable, view);
    }

    /**
     * 上传重试
     *
     * @param task
     * @param listener
     */
    public void retryUpload(UploadTask<LostApplyRequest> task, OnUploadListener<LostApplyRequest> listener) {
        uploadServer.retryUpload(task, false, listener);
    }

    public void clearCache(UploadTask<LostApplyRequest> task) {
        uploadServer.deleteTask(task);
    }

}
