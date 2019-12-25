package com.zhiqi.campusassistant.app;

import com.ming.base.app.BaseApplication;
import com.ming.base.util.Log;
import com.zhiqi.campusassistant.BuildConfig;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.app.dagger.component.DaggerApplicationComponent;
import com.zhiqi.campusassistant.app.dagger.module.ApplicationModule;
import com.zhiqi.campusassistant.app.dagger.module.DatabaseModule;
import com.zhiqi.campusassistant.app.dagger.module.HttpServiceModule;
import com.zhiqi.campusassistant.app.dagger.module.InitAppModule;
import com.zhiqi.campusassistant.app.dagger.presenter.InitManager;

import javax.inject.Inject;

/**
 * Created by Edmin on 2016/8/29 0029.
 */
public class AssistantApplication extends BaseApplication {

    private ApplicationComponent appComponent;

    private static AssistantApplication instance;

    private boolean hasCheckUpgrade = false;

    @Inject
    InitManager mInitManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //wwz
//        Log.setDebug(BuildConfig.DEBUG);
        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .databaseModule(new DatabaseModule())
                .initAppModule(new InitAppModule())
                .httpServiceModule(new HttpServiceModule())
                .build();
        appComponent.inject(this);
        init();
    }

    private void init() {
        mInitManager.initData(appComponent.getJPushApiService());
    }

    /**
     * 检测更新
     */
    public void checkUpgrade() {
        if (!hasCheckUpgrade) {
            hasCheckUpgrade = true;
            mInitManager.checkUpgrade();
        }
    }

    /**
     * 取消所有http请求
     */
    public void cancelHttpRequest() {
        mInitManager.cancelHttpRequest();
    }

    public static AssistantApplication getInstance() {
        return instance;
    }

    public ApplicationComponent getApplicationComponent() {
        return appComponent;
    }
}
