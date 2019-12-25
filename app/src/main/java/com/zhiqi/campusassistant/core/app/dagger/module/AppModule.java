package com.zhiqi.campusassistant.core.app.dagger.module;

import com.ming.base.dagger.FragmentScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.core.app.api.AppApiService;
import com.zhiqi.campusassistant.core.app.presenter.AppPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2017/3/11.
 */
@Module
public class AppModule {

    @FragmentScoped
    @Provides
    AppPresenter provideAppPresenter(AssistantApplication context, AppApiService apiService, AppDaoSession daoSession) {
        return new AppPresenter(context, apiService, daoSession);
    }
}
