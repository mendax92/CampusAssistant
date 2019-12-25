package com.zhiqi.campusassistant.core.location.dagger.module;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.core.location.api.CampusLocationApiService;
import com.zhiqi.campusassistant.core.location.presenter.CampusLocationPresenter;
import com.zhiqi.campusassistant.core.location.presenter.LocationPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 17-8-10.
 * 定位module
 */
@Module
public class LocationModule {

    @Provides
    @ActivityScoped
    LocationPresenter provideLocationPresenter(AssistantApplication context) {
        return new LocationPresenter(context);
    }

    @Provides
    @ActivityScoped
    CampusLocationPresenter provideCampusLocationPresenter(AssistantApplication context, CampusLocationApiService apiService, AppDaoSession daoSession) {
        return new CampusLocationPresenter(context, apiService, daoSession);
    }
}
