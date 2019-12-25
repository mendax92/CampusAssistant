package com.ming.base.activity;

import android.app.Activity;

import com.ming.base.util.RxUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * activity管理
 *
 * @author Edmin
 */
public class ActivityManager {

    private static List<Activity> activityList = new CopyOnWriteArrayList<Activity>();
    private static ActivityManager instance;

    private boolean newTask = false;

    private boolean isRunningBackground = false;

    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public void bringToBackground() {
        isRunningBackground = true;
    }

    public void bringToForeground() {
        isRunningBackground = false;
    }

    public boolean isRunningBackground() {
        return isRunningBackground;
    }

    // 退出栈顶
    public void popActivity(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);
        }
    }

    // 获得当前栈顶
    public Activity getCurrentActivity() {
        Activity activity = null;
        if (!activityList.isEmpty()) {
            activity = activityList.get(activityList.size() - 1);
        }
        return activity;
    }

    // 当前Activity推入栈中
    public void pushActivity(Activity activity) {
        if (activityList == null || newTask) {
            activityList = new CopyOnWriteArrayList<Activity>();
            newTask = false;
        }
        activityList.add(activity);
    }

    // 退出所有Activity
    public void popAllActivity() {
        final List<Activity> oldActitys = activityList;
        newTask = true;
        RxUtil.postOnMainThread(new Runnable() {
            @Override
            public void run() {
                for (Activity activity : oldActitys) {
                    activity.finish();
                    activityList.remove(activity);
                }
            }
        });
    }

    public int getActivitySize() {
        return activityList == null ? 0 : activityList.size();
    }
}
