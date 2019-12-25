package com.zhiqi.campusassistant.common.utils;

import android.content.Context;
import android.widget.Toast;

import com.zhiqi.campusassistant.common.ui.widget.AppToast;

/**
 * Created by ming on 2016/10/8.
 */
public class ToastUtil {

    private static Toast mToast;

    public static void show(Context context, int msgRes) {
        show(context, context.getString(msgRes));
    }

    public static void show(Context context, String msg) {
        if (mToast == null) {
            mToast = AppToast.showToast(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.show();
        }
    }

    public static void show(Context context, int msgRes, int iconRes) {
        show(context, context.getString(msgRes), iconRes);
    }

    public static void show(Context context, String msg, int iconRes) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = AppToast.showToast(context.getApplicationContext(), iconRes, msg, Toast.LENGTH_SHORT);
    }
}
