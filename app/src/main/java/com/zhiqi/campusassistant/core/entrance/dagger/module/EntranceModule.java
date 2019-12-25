package com.zhiqi.campusassistant.core.entrance.dagger.module;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.core.entrance.api.EntranceApiService;
import com.zhiqi.campusassistant.core.entrance.presenter.EntrancePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2017/8/16.
 * 报道指南module
 */
@Module
public class EntranceModule {

    @ActivityScoped
    @Provides
    EntrancePresenter provideEntrancePresenter(AssistantApplication context, EntranceApiService apiService, AppDaoSession daoSession) {
        return new EntrancePresenter(context, apiService, daoSession);
    }
}
