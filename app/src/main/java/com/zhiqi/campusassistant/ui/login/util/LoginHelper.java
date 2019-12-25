package com.zhiqi.campusassistant.ui.login.util;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ming.base.activity.BaseActivity;
import com.ming.base.util.RxUtil;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.core.usercenter.entity.VerifyFunction;
import com.zhiqi.campusassistant.ui.login.activity.BindPhoneActivity;

/**
 * Created by minh on 18-2-7.
 * 登陆帮助类
 */

public class LoginHelper {

    public static boolean checkBindPhone(final BaseActivity activity, @StringRes int msgRes, final Runnable runnable) {
        return checkBindPhone(activity, activity.getString(msgRes), runnable);
    }

    /**
     * 检查是否绑定了手机
     *
     * @param activity 当前activity
     * @param msg      提示信息
     * @param runnable 回调
     * @return true已经绑定；false未绑定，将会进去绑定手机号码界面
     */
    public static boolean checkBindPhone(final BaseActivity activity, CharSequence msg, final Runnable runnable) {
        LoginUser loginUser = LoginManager.getInstance().getLoginUser();
        if (loginUser != null && !TextUtils.isEmpty(loginUser.getPhone())) {
            RxUtil.postOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
            return true;
        }
        new MaterialDialog.Builder(activity)
                .title(R.string.common_prompt)
                .content(msg)
                .positiveText(R.string.user_go_to_bind_phone)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        activity.addOnActivityResultListener(AppConstant.ACTIVITY_REQUEST_VERIFY_CODE, new BaseActivity.OnActivityResultListener() {
                            @Override
                            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                                activity.removeOnActivityResultListener(this);
                                if (Activity.RESULT_OK == resultCode && runnable != null) {
                                    runnable.run();
                                }
                            }
                        });
                        Intent intent = new Intent(activity, BindPhoneActivity.class);
                        intent.putExtra(AppConstant.EXTRA_BIND_PHONE_OPT, VerifyFunction.BindPhone.getValue());
                        activity.startActivityForResult(intent, AppConstant.ACTIVITY_REQUEST_VERIFY_CODE);
                    }
                })
                .negativeText(R.string.common_cancel)
                .build()
                .show();
        return false;
    }
}
