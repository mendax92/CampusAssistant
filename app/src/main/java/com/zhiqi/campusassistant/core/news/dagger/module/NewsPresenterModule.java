package com.zhiqi.campusassistant.core.news.dagger.module;

import com.ming.base.dagger.FragmentScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.core.news.api.NewsApiService;
import com.zhiqi.campusassistant.core.news.presenter.NewsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2017/3/12.
 * 新闻module
 */

@Module
public class NewsPresenterModule {

    @FragmentScoped
    @Provides
    NewsPresenter provideNewsPresenter(AssistantApplication context, NewsApiService apiService, AppDaoSession daoSession) {
        return new NewsPresenter(context, apiService, daoSession);
    }
}
