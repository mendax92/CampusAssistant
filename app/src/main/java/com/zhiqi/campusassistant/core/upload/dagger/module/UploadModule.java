package com.zhiqi.campusassistant.core.upload.dagger.module;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.core.leave.api.LeaveApiService;
import com.zhiqi.campusassistant.core.repair.api.RepairApiService;
import com.zhiqi.campusassistant.core.upload.UploadServer;
import com.zhiqi.campusassistant.core.upload.presenter.UploadPresenter;
import com.zhiqi.campusassistant.core.upload.service.UploadManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2017/3/4.
 */
@Module
public class UploadModule {

    @ActivityScoped
    @Provides
    UploadPresenter provideUploadPresenter(UploadManager manager) {
        return new UploadPresenter(manager);
    }

    @ActivityScoped
    @Provides
    UploadServer provideUploadServer(AssistantApplication context, UploadPresenter presenter) {
        return new UploadServer(context, presenter);
    }
}
