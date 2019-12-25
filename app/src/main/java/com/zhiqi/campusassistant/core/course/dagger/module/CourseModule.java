package com.zhiqi.campusassistant.core.course.dagger.module;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.core.course.api.CourseApiService;
import com.zhiqi.campusassistant.core.course.presenter.CoursePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2016/12/16.
 */
@Module
public class CourseModule {

    @ActivityScoped
    @Provides
    CoursePresenter provideCoursePresenter(AssistantApplication context, CourseApiService apiService, AppDaoSession daoSession) {
        return new CoursePresenter(context, apiService, daoSession);
    }
}
