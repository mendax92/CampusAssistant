package com.zhiqi.campusassistant.core.message.dagger.module;

import com.ming.base.dagger.FragmentScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.core.message.api.MessageApiService;
import com.zhiqi.campusassistant.core.message.presenter.MessagePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2016/12/20.
 * 消息 module
 */
@Module
public class MessageModule {

    @FragmentScoped
    @Provides
    MessagePresenter provideMessagePresenter(AssistantApplication context, MessageApiService apiService, AppDaoSession daoSession) {
        return new MessagePresenter(context, apiService, daoSession);
    }
}
