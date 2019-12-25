package com.zhiqi.campusassistant.core.scores.dagger.component;

import com.ming.base.dagger.ActivityScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.scores.dagger.module.ScoresModule;
import com.zhiqi.campusassistant.ui.scores.activity.ScoresDetailActivity;
import com.zhiqi.campusassistant.ui.scores.activity.ScoresListActivity;
import com.zhiqi.campusassistant.ui.scores.fragment.ScoresListFragment;

import dagger.Component;

/**
 * Created by ming on 2017/5/12.
 * 成绩查询组件
 */

@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = ScoresModule.class)
public interface ScoresComponent {

    void inject(ScoresListActivity activity);

    void inject(ScoresListFragment fragment);

    void inject(ScoresDetailActivity activity);
}
