package com.zhiqi.campusassistant.core.repair.dagger.module;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.core.repair.api.RepairApiService;
import com.zhiqi.campusassistant.core.repair.presenter.RepairApplyPresenter;
import com.zhiqi.campusassistant.core.repair.presenter.RepairPresenter;
import com.zhiqi.campusassistant.core.upload.UploadServer;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2017/1/14.
 * 维修module
 */
@Module
public class RepairModule {

    @ActivityScoped
    @Provides
    RepairApplyPresenter provideRepairApplyPresenter(AssistantApplication context, RepairApiService apiService, UploadServer uploadServer) {
        return new RepairApplyPresenter(context, apiService, uploadServer);
    }

    @ActivityScoped
    @Provides
    RepairPresenter provideRepairPresenter(AssistantApplication context, RepairApiService apiService, AppDaoSession daoSession) {
        return new RepairPresenter(context, apiService, daoSession);
    }
}
