package com.zhiqi.campusassistant.core.upload;

import android.content.Context;

import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.core.leave.entity.LeaveRequest;
import com.zhiqi.campusassistant.core.lost.entity.LostApplyRequest;
import com.zhiqi.campusassistant.core.repair.entity.RepairApplyRequest;
import com.zhiqi.campusassistant.core.upload.entity.BaseUploadBean;
import com.zhiqi.campusassistant.core.upload.presenter.UploadPresenter;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/3/4.
 * 上传服务
 */

public class UploadServer extends BaseUploadServer {

    public UploadServer(Context context, UploadPresenter uploadPresenter) {
        super(context, uploadPresenter, registerProvider());
    }

    /**
     * 注册API接口提供者
     *
     * @return
     */
    private static IApiServiceProvider registerProvider() {
        return new IApiServiceProvider() {
            @Override
            public <T extends BaseUploadBean> Observable<BaseResultData<List<String>>> provideApiObserver(T request, boolean isUpdate) {
                ApplicationComponent component = AssistantApplication.getInstance().getApplicationComponent();
                switch (request.getUploadType()) {
                    case Maintenance:
                        if (isUpdate) {
                            return component.getRepairApiService().updateRepairApply((RepairApplyRequest) request);
                        } else {
                            return component.getRepairApiService().requestRepairApply((RepairApplyRequest) request);
                        }
                    case Vacation:
                        if (isUpdate) {
                            return component.getLeaveApiService().updateLeaveApply((LeaveRequest) request);
                        } else {
                            return component.getLeaveApiService().requestLeaveApply((LeaveRequest) request);
                        }
                    case LOST:
                        return component.getLostFoundApiService().requestLostApply((LostApplyRequest) request);
                }
                return null;

            }
        };

    }
}
