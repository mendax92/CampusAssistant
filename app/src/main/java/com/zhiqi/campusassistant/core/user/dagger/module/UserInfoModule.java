package com.zhiqi.campusassistant.core.user.dagger.module;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.core.user.api.UserInfoApiService;
import com.zhiqi.campusassistant.core.user.presenter.UserInfoPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2016/11/30.
 * 用户信息及联系人module
 */

@Module
public class UserInfoModule {

    @ActivityScoped
    @Provides
    UserInfoPresenter provideContactsPresenter(AssistantApplication cotext, UserInfoApiService apiService, AppDaoSession daoSession) {
        return new UserInfoPresenter(cotext, apiService, daoSession);
    }
}
