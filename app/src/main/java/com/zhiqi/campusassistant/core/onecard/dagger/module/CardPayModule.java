package com.zhiqi.campusassistant.core.onecard.dagger.module;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.core.onecard.api.CardPayApiService;
import com.zhiqi.campusassistant.core.onecard.presenter.CardPayPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2018/1/21.
 * 一卡通支付module
 */
@Module
public class CardPayModule {

    @Provides
    @ActivityScoped
    CardPayPresenter provideCardPayPresenter(AssistantApplication content, CardPayApiService apiService, AppDaoSession daoSession) {
        return new CardPayPresenter(content, apiService, daoSession);
    }
}
