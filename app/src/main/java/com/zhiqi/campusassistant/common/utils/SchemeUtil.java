package com.zhiqi.campusassistant.common.utils;

import android.content.Context;

/**
 * Created by ming on 2017/3/31.
 * scheme帮助类
 */

public class SchemeUtil {

    /**
     * 获取scheme url
     *
     * @param context
     * @param host
     * @return
     */
    public static String getSchemeUrl(Context context, String host) {
        return context.getPackageName() + "://" + host;
    }
}
