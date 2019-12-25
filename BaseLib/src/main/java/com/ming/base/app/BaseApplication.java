package com.ming.base.app;

import android.content.ComponentCallbacks2;
import android.support.multidex.MultiDexApplication;

import com.ming.base.activity.ActivityManager;

/**
 * Created by minh on 18-2-8.
 */

public class BaseApplication extends MultiDexApplication {

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN == level) {
            ActivityManager.getInstance().bringToBackground();
        }
    }
}
