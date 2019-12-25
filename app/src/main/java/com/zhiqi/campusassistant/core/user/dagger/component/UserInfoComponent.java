package com.zhiqi.campusassistant.core.user.dagger.component;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.user.dagger.module.UserInfoModule;
import com.zhiqi.campusassistant.ui.contacts.activity.BaseContactsActivity;
import com.zhiqi.campusassistant.ui.contacts.activity.UserInfoActivity;

import dagger.Component;

/**
 * Created by ming on 2016/11/30.
 */
@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = UserInfoModule.class)
public interface UserInfoComponent {

    void inject(UserInfoActivity activity);

    void inject(BaseContactsActivity activity);
}
