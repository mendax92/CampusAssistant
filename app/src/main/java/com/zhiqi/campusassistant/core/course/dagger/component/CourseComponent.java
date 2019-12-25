package com.zhiqi.campusassistant.core.course.dagger.component;

import com.ming.base.dagger.ActivityScoped;
import com.ming.base.dagger.FragmentScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.course.dagger.module.CourseModule;
import com.zhiqi.campusassistant.ui.course.activity.CourseDetailActivity;
import com.zhiqi.campusassistant.ui.main.fragment.CourseFragment;

import dagger.Component;

/**
 * Created by ming on 2016/12/16.
 */
@ActivityScoped
@Component(dependencies = ApplicationComponent.class, modules = CourseModule.class)
public interface CourseComponent {

    void inject(CourseFragment fragment);

    void inject(CourseDetailActivity activity);
}
