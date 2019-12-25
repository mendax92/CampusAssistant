package com.zhiqi.campusassistant.core.usercenter.dagger.module;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.core.usercenter.api.SecurityApiService;
import com.zhiqi.campusassistant.core.usercenter.presenter.SecurityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2017/3/23.
 * 安全中心module
 */
@Module
public class SecurityModule {

    @ActivityScoped
    @Provides
    SecurityPresenter provideSecurityPresenter(AssistantApplication context, SecurityApiService apiService) {
        return new SecurityPresenter(context, apiService);
    }
}
