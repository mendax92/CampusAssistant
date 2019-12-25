package com.zhiqi.campusassistant.core.message.dagger.component;

import com.ming.base.dagger.FragmentScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.message.dagger.module.MessageModule;
import com.zhiqi.campusassistant.ui.main.fragment.MessageFragment;

import dagger.Component;

/**
 * Created by ming on 2016/12/20.
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = MessageModule.class)
public interface MessageComponent {

    void inject(MessageFragment fragment);
}
