package com.zhiqi.campusassistant.core.bedroom.dagger.component;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.bedroom.dagger.module.BedRoomModule;
import com.zhiqi.campusassistant.ui.bedroom.activity.BedChooseActivity;
import com.zhiqi.campusassistant.ui.bedroom.activity.BedRoomInfoActivity;

import dagger.Component;

/**
 * Created by ming on 2017/7/30.
 * 宿舍床位组件
 */
@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = BedRoomModule.class)
public interface BedRoomComponent {

    void inject(BedRoomInfoActivity activity);

    void inject(BedChooseActivity activity);

}
