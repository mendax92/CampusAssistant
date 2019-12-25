package com.zhiqi.campusassistant.common.utils;

import android.content.Context;

import com.ming.base.util.QPreference;

/**
 * Created by ming on 2017/3/22.
 */

public class AppPreference {

    private static final String SHARE_PREFERENCES_NAME = "app_data";

    private static final String JPUSH_AVAILABLE = "jpush_availabel";

    private static final String LOGIN_ACCOUNT = "login_account";

    private static AppPreference instance;

    private QPreference preference;

    private AppPreference() {
    }

    private AppPreference(Context context) {
        preference = new QPreference(context.getApplicationContext(), SHARE_PREFERENCES_NAME);
    }

    public static AppPreference getInstance(Context context) {
        if (instance == null) {
            synchronized (AppPreference.class) {
                if (instance == null) {
                    instance = new AppPreference(context);
                }
            }
        }
        return instance;
    }

    public void setJPushAvailable(boolean available) {
        preference.setBoolean(JPUSH_AVAILABLE, available);
    }

    public boolean isJPushAvailable() {
        return preference.getBoolean(JPUSH_AVAILABLE, true);
    }

    public void setLoginAccount(String account) {
        preference.setString(LOGIN_ACCOUNT, account);
    }

    public String getLoginAccount() {
        return preference.getString(LOGIN_ACCOUNT, null);
    }
}
