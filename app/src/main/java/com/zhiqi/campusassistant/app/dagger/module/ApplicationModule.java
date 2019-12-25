package com.zhiqi.campusassistant.app.dagger.module;

import com.zhiqi.campusassistant.app.AssistantApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Edmin on 2016/8/29 0029.
 */
@Module
public class ApplicationModule {

    private AssistantApplication mApplication;

    public ApplicationModule(AssistantApplication application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    public AssistantApplication provideApplicationContext() {
        return mApplication;
    }
}
