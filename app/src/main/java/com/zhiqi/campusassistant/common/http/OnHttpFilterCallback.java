package com.zhiqi.campusassistant.common.http;

import android.content.Context;
import android.content.Intent;

import com.ming.base.activity.ActivityManager;
import com.ming.base.http.interfaces.OnHttpCallback;
import com.ming.base.util.NetworkUtil;
import com.ming.base.util.RxUtil;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.core.security.manager.SecurityManager;
import com.zhiqi.campusassistant.ui.launch.activity.NavigationActivity;

/**
 * Created by Edmin on 2016/11/6 0006.
 * 回掉拦截过滤
 */

public abstract class OnHttpFilterCallback<T extends BaseResultData> implements OnHttpCallback<T> {

    private Context mContext;

    public OnHttpFilterCallback(Context context) {
        this.mContext = context;
    }

    @Override
    public void onFinished(T result) {
        if (result != null) {
            if (HttpErrorCode.SUCCESS == result.error_code) {
                onSuccess(result);
            } else {
                onFailure(result);
                onFailure(result.error_code, result.message);
                handleErrorCode(result.error_code);
            }
        } else {
            onFailure(HttpErrorCode.ERROR_EXCEPTION, mContext.getString(R.string.common_tip_server_no_result));
        }
    }

    public void onFailure(T result) {
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        int tipRes = R.string.common_tip_network_error;
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            tipRes = R.string.common_tip_no_network;
        }
        onFailure(HttpErrorCode.ERROR_EXCEPTION, mContext.getString(tipRes));
    }

    private void handleErrorCode(int errorCode) {
        switch (errorCode) {
            case HttpErrorCode.ERROR_TIMESTAMP:
                // 如果与服务器存在时间差则查询服务器时间
                SecurityManager.getInstance().queryServerTime();
                break;
            case HttpErrorCode.ERROR_TOKEN_INVALID:
                // 跳转登录界面
                LoginManager.getInstance().onLogout();
                if (RxUtil.isUIThread()) {
                    gotoLogin();
                } else {
                    RxUtil.postOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            gotoLogin();
                        }
                    });
                }
                break;
        }
    }

    private void gotoLogin() {
        ActivityManager.getInstance().popAllActivity();
        if (!ActivityManager.getInstance().isRunningBackground()) {
            Intent intent = new Intent(mContext, NavigationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.getApplicationContext().startActivity(intent);
        }
    }

    public abstract void onSuccess(T result);

    public abstract void onFailure(int errorCode, String message);
}
