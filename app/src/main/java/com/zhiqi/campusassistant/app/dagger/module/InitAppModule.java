package com.zhiqi.campusassistant.app.dagger.module;

import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.app.dagger.presenter.InitManager;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.http.CloupusApiAdapter;
import com.zhiqi.campusassistant.common.http.LongApiAdapter;
import com.zhiqi.campusassistant.core.appsetting.api.AppSettingApiService;
import com.zhiqi.campusassistant.core.appsetting.presenter.AppUpgradePresenter;
import com.zhiqi.campusassistant.core.upload.api.UploadApiService;
import com.zhiqi.campusassistant.core.upload.service.UploadManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2016/11/21.
 * 初始化管理
 */

@Module
public class InitAppModule {

    @Provides
    AppUpgradePresenter provideAppUpgradePresenter(AssistantApplication context, AppSettingApiService apiService) {
        return new AppUpgradePresenter(context, apiService);
    }

    @Singleton
    @Provides
    InitManager provideInitManager(AssistantApplication context, AppDaoSession daoSession, AppUpgradePresenter settingPresenter, CloupusApiAdapter cloupusApiAdapter, LongApiAdapter longApiAdapter) {
        return new InitManager(context, daoSession, settingPresenter, cloupusApiAdapter, longApiAdapter);
    }

    @Singleton
    @Provides
    UploadManager provideUploadManager(UploadApiService service) {
        return new UploadManager(service);
    }
}
