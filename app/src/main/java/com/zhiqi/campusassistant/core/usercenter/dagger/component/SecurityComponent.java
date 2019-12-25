package com.zhiqi.campusassistant.core.usercenter.dagger.component;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.usercenter.dagger.module.SecurityModule;
import com.zhiqi.campusassistant.ui.login.activity.BindPhoneActivity;
import com.zhiqi.campusassistant.ui.user.activity.ResetPayPwdActivity;
import com.zhiqi.campusassistant.ui.login.activity.ResetPwdActivity;
import com.zhiqi.campusassistant.ui.login.activity.VerifyCodeActivity;
import com.zhiqi.campusassistant.ui.user.activity.ChangePasswordActivity;
import com.zhiqi.campusassistant.ui.user.activity.ChangePayPwdActivity;

import dagger.Component;

/**
 * Created by ming on 2017/3/23.
 * 安全中心component
 */

@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = SecurityModule.class)
public interface SecurityComponent {

    void inject(VerifyCodeActivity activity);

    void inject(BindPhoneActivity activity);

    void inject(ResetPwdActivity activity);

    void inject(ChangePasswordActivity activity);

    void inject(ChangePayPwdActivity activity);

    void inject(ResetPayPwdActivity activity);
}
