package com.ming.base.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * 渠道工具类
 */
public class ChannelUtil {
    private static final String PREFERENCE_CHANNEL = "PREFERENCE_CHANNEL";
    public static String KEY_CHANNEL = "channel";

    public static String getAppChannel(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_CHANNEL, Context.MODE_PRIVATE);
        String channel = sp.getString(KEY_CHANNEL, null);
        if (channel == null) {
            channel = getChannelFromPkg(context);
            sp.edit().putString(KEY_CHANNEL, channel).commit();
        }
        return channel;
    }

    private static String getChannelFromPkg(Context context) {
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = info != null ? info.metaData : null;
        return bundle != null ? bundle.getString(KEY_CHANNEL) : null;
    }
}
