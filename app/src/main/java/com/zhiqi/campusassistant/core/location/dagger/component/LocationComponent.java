package com.zhiqi.campusassistant.core.location.dagger.component;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.location.dagger.module.LocationModule;
import com.zhiqi.campusassistant.ui.location.activity.LocationActivity;

import dagger.Component;

/**
 * Created by ming on 17-8-10.
 */
@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = LocationModule.class)
public interface LocationComponent {

    void inject(LocationActivity activity);
}
