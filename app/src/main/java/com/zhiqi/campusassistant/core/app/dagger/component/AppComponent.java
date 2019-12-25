package com.zhiqi.campusassistant.core.app.dagger.component;

import com.ming.base.dagger.FragmentScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.app.dagger.module.AppModule;
import com.zhiqi.campusassistant.ui.main.fragment.AppFragment;

import dagger.Component;

/**
 * Created by ming on 2017/3/11.
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = AppModule.class)
public interface AppComponent {

    void inject(AppFragment fragment);

}
