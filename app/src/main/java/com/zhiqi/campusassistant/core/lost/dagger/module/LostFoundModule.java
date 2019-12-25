package com.zhiqi.campusassistant.core.lost.dagger.module;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.core.lost.api.LostFoundApiService;
import com.zhiqi.campusassistant.core.lost.presenter.LostApplyPresenter;
import com.zhiqi.campusassistant.core.lost.presenter.LostFoundPresenter;
import com.zhiqi.campusassistant.core.upload.UploadServer;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2017/5/6.
 * 失物招领 Module
 */
@Module
public class LostFoundModule {

    @Provides
    @ActivityScoped
    LostFoundPresenter provideLostFoundPresenter(AssistantApplication context, LostFoundApiService apiService, AppDaoSession daoSession) {
        return new LostFoundPresenter(context, apiService, daoSession);
    }

    @Provides
    @ActivityScoped
    LostApplyPresenter provideLostApplyPresenter(AssistantApplication context, LostFoundApiService apiService, UploadServer uploadServer) {
        return new LostApplyPresenter(context, apiService, uploadServer);
    }
}
