package com.zhiqi.campusassistant.core.leave.dagger.module;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.core.leave.api.LeaveApiService;
import com.zhiqi.campusassistant.core.leave.presenter.LeaveApplyPresenter;
import com.zhiqi.campusassistant.core.leave.presenter.LeavePresenter;
import com.zhiqi.campusassistant.core.upload.UploadServer;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2017/1/1.
 * 请假presenter module
 */
@Module
public class LeavePresenterModule {

    @Provides
    @ActivityScoped
    LeavePresenter provideLeavePresenter(AssistantApplication context, LeaveApiService apiService, AppDaoSession daoSession) {
        return new LeavePresenter(context, apiService, daoSession);
    }

    @Provides
    @ActivityScoped
    LeaveApplyPresenter provideLeaveApplyPresenter(AssistantApplication context, LeaveApiService apiService, UploadServer uploadServer) {
        return new LeaveApplyPresenter(context, apiService, uploadServer);
    }
}
