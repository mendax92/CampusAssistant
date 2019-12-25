package com.zhiqi.campusassistant.core.login.dagger.module;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.core.login.api.LoginApiService;
import com.zhiqi.campusassistant.core.login.presenter.LoginPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Edmin on 2016/11/5 0005.
 * 登录module
 */
@Module
public class LoginPresenterModule {

    @Provides
    @ActivityScoped
    LoginPresenter provideLoginPresenter(AssistantApplication context, LoginApiService apiService, AppDaoSession daoSession) {
        return new LoginPresenter(context, apiService, daoSession);
    }
}
