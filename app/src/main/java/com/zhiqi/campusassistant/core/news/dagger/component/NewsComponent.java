package com.zhiqi.campusassistant.core.news.dagger.component;

import com.ming.base.dagger.FragmentScoped;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.core.news.dagger.module.NewsPresenterModule;
import com.zhiqi.campusassistant.ui.news.activity.NewsActivity;
import com.zhiqi.campusassistant.ui.news.fragment.NewsFragment;

import dagger.Component;

/**
 * Created by ming on 2017/3/12.
 * 新闻组件
 */
@FragmentScoped
@Component(dependencies = ApplicationComponent.class, modules = NewsPresenterModule.class)
public interface NewsComponent {

    void inject(NewsActivity activity);

    void inject(NewsFragment fragment);
}
