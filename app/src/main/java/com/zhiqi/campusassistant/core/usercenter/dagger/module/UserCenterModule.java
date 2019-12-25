package com.zhiqi.campusassistant.core.usercenter.dagger.module;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.core.usercenter.api.UserCenterApiService;
import com.zhiqi.campusassistant.core.usercenter.presenter.UserCenterPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2017/3/17.
 * 用户中心module
 */
@Module
public class UserCenterModule {

    @ActivityScoped
    @Provides
    UserCenterPresenter provideUserCenterPresenter(AssistantApplication context, UserCenterApiService apiService, AppDaoSession daoSession) {
        return new UserCenterPresenter(context, apiService, daoSession);
    }
}
