package com.zhiqi.campusassistant.core.login.dagger.component;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.login.dagger.module.LoginPresenterModule;
import com.zhiqi.campusassistant.ui.login.activity.LoginActivity;

import dagger.Component;

/**
 * Created by Edmin on 2016/11/5 0005.
 */

@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = LoginPresenterModule.class)
public interface LoginComponent {

    void inject(LoginActivity activity);
}
