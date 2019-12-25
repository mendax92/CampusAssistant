package com.zhiqi.campusassistant.core.notice.dagger.module;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.core.notice.api.NoticeApiService;
import com.zhiqi.campusassistant.core.notice.presenter.NoticePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2017/5/3.
 * 通知公告module
 */
@Module
public class NoticeModule {

    @ActivityScoped
    @Provides
    NoticePresenter provideNoticePresenter(AssistantApplication context, NoticeApiService apiService, AppDaoSession daoSession) {
        return new NoticePresenter(context, apiService, daoSession);
    }
}
