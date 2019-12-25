package com.zhiqi.campusassistant.core.payment.dagger.module;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.core.payment.api.SelfPayApiService;
import com.zhiqi.campusassistant.core.payment.presenter.SelfPayPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2017/7/23.
 * 自助支付module
 */
@Module
public class SelfPayModule {

    @ActivityScoped
    @Provides
    SelfPayPresenter provideSelfPayPresenter(AssistantApplication context, SelfPayApiService apiService) {
        return new SelfPayPresenter(context, apiService);
    }
}
