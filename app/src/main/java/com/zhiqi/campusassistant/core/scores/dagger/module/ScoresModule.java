package com.zhiqi.campusassistant.core.scores.dagger.module;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.core.scores.api.ScoresApiService;
import com.zhiqi.campusassistant.core.scores.presenter.ScoresPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2017/5/12.
 * 成绩module
 */

@Module
public class ScoresModule {

    @ActivityScoped
    @Provides
    ScoresPresenter provideScoresPresenter(AssistantApplication context, ScoresApiService apiService, AppDaoSession daoSession) {
        return new ScoresPresenter(context, apiService, daoSession);
    }
}
