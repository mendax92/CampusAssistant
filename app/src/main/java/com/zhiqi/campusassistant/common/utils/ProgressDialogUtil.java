package com.zhiqi.campusassistant.common.utils;

import android.app.Activity;
import android.support.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ming.base.util.RxUtil;
import com.zhiqi.campusassistant.R;

/**
 * Created by ming on 2016/11/23.
 * 加载对话框工具类
 */

public class ProgressDialogUtil {

    private static MaterialDialog progressDialog;

    public static void show(Activity activity, @StringRes int msgRes) {
        show(activity, activity.getString(msgRes));
    }

    public static void show(Activity activity, CharSequence msg) {
        if (progressDialog == null) {
            progressDialog = new MaterialDialog.Builder(activity)
                    .autoDismiss(false)
                    .cancelable(false)
                    .content(msg)
                    .progress(true, 0)
                    .show();
        } else {
            progressDialog.setContent(msg);
        }
    }

    public static void dismiss() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        progressDialog = null;
    }

    public static void error(String msg) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setContent(msg);
            progressDialog.setProgressComplete(R.drawable.ic_tip_error);
            RxUtil.postOnMainThread(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, 1500);
        }
    }

    public static void success(String msg) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setContent(msg);
            progressDialog.setProgressComplete(R.drawable.ic_tip_sucess);
            RxUtil.postOnMainThread(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, 1500);
        }
    }
}
