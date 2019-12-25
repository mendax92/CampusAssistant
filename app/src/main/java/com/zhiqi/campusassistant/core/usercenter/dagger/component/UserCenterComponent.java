package com.zhiqi.campusassistant.core.usercenter.dagger.component;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.login.dagger.module.LoginPresenterModule;
import com.zhiqi.campusassistant.core.usercenter.dagger.module.UserCenterModule;
import com.zhiqi.campusassistant.ui.user.activity.SettingActivity;
import com.zhiqi.campusassistant.ui.user.activity.UserFeedbackActivity;

import dagger.Component;

/**
 * Created by ming on 2017/3/17.
 * 用户中心
 */
@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = {UserCenterModule.class, LoginPresenterModule.class})
public interface UserCenterComponent {

    void inject(UserFeedbackActivity activity);

    void inject(SettingActivity activity);
}
