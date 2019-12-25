package com.zhiqi.campusassistant.core.notice.dagger.component;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.notice.dagger.module.NoticeModule;
import com.zhiqi.campusassistant.ui.notice.activity.NoticeActivity;

import dagger.Component;

/**
 * Created by ming on 2017/5/3.
 * 通知公告组建
 */
@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = NoticeModule.class)
public interface NoticeComponent {

    void inject(NoticeActivity activity);

}
