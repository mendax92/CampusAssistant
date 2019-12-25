package com.zhiqi.campusassistant.core.entrance.dagger.component;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.entrance.dagger.module.EntranceModule;
import com.zhiqi.campusassistant.core.location.dagger.module.LocationModule;
import com.zhiqi.campusassistant.ui.entrance.activity.EntranceGuideActivity;

import dagger.Component;

/**
 * Created by ming on 2017/8/16.
 * 报到指南component
 */
@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = {EntranceModule.class, LocationModule.class})
public interface EntranceComponent {

    void inject(EntranceGuideActivity activity);
}
