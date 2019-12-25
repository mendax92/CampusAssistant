package com.zhiqi.campusassistant.core.lost.dagger.component;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.lost.dagger.module.LostFoundModule;
import com.zhiqi.campusassistant.core.upload.dagger.module.UploadModule;
import com.zhiqi.campusassistant.ui.lost.activity.LostApplyActivity;
import com.zhiqi.campusassistant.ui.lost.activity.MyLostActivity;
import com.zhiqi.campusassistant.ui.lost.fragment.LostListFragment;

import dagger.Component;

/**
 * Created by ming on 2017/5/6.
 * 失物招领
 */
@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = {LostFoundModule.class, UploadModule.class})
public interface LostFoundComponent {

    void inject(LostListFragment fragment);

    void inject(MyLostActivity activity);

    void inject(LostApplyActivity activity);
}
