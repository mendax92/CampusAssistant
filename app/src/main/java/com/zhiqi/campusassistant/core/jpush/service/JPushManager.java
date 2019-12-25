package com.zhiqi.campusassistant.core.jpush.service;

import android.app.Notification;
import android.content.Context;

import com.ming.base.http.HttpManager;
import com.ming.base.util.Log;
import com.ming.base.util.NetworkUtil;
import com.ming.base.util.RxUtil;
import com.zhiqi.campusassistant.BuildConfig;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.http.OnHttpFilterCallback;
import com.zhiqi.campusassistant.common.utils.AppPreference;
import com.zhiqi.campusassistant.core.jpush.api.JPushApiService;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;

/**
 * Created by ming on 2017/3/22.
 * jPush管理
 */

public class JPushManager {

    private static final String TAG = "JPushManager";

    private static JPushManager instance;

    private boolean initialized = false;

    private Context mContext;

    private JPushApiService pushApiService;

    private JPushManager(Context context, JPushApiService apiService) {
        this.mContext = context.getApplicationContext();
        this.pushApiService = apiService;
        init();
    }

    public static void init(Context context, JPushApiService apiService) {
        if (instance == null) {
            synchronized (JPushManager.class) {
                if (instance == null) {
                    instance = new JPushManager(context, apiService);
                }
            }
        }
    }

    public static JPushManager getInstance() {
        if (instance == null) {
            throw new NullPointerException("Please init JPushManager at first...");
        }
        return instance;
    }

    private void init() {
        if (isEnabled()) {
            initialized = true;
            BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(mContext);
            builder.statusBarDrawable = R.drawable.jpush_notification_icon;
            builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                    | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
            builder.notificationDefaults = Notification.DEFAULT_SOUND
                    | Notification.DEFAULT_VIBRATE
                    | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
            JPushInterface.setPushNotificationBuilder(1, builder);
            JPushInterface.setDebugMode(BuildConfig.DEBUG);
            JPushInterface.init(mContext);
            reportRegistrationId();
        }
    }

    public void enable(boolean enabled) {
        AppPreference.getInstance(mContext).setJPushAvailable(enabled);
        if (enabled) {
            resumePush();
        } else {
            stopPush();
        }
    }

    public boolean isEnabled() {
        return AppPreference.getInstance(mContext).isJPushAvailable();
    }

    private void stopPush() {
        JPushInterface.stopPush(mContext);
    }

    private void resumePush() {
        if (!initialized) {
            init();
        } else {
            JPushInterface.resumePush(mContext);
        }
    }

    public String getRegistrationID() {
        return JPushInterface.getRegistrationID(mContext);
    }

    public void reportRegistrationId() {
        reportRegistrationId(getRegistrationID());
    }


    /**
     * 上报服务器
     *
     * @param regId
     */
    public void reportRegistrationId(final String regId) {
        if (!LoginManager.getInstance().isLogin()) {
            Log.w(TAG, "User hasn't login.");
            return;
        }
        if (!isEnabled()) {
            Log.i(TAG, "JPush is disabled.");
            return;
        }
        Log.i(TAG, "regId:" + regId);
        Observable<BaseResultData> observable = pushApiService.pushUserRegistrationId(regId);
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData>(mContext) {
            @Override
            public void onSuccess(BaseResultData result) {
                Log.i(TAG, "push registration id to server success");
            }

            @Override
            public void onFailure(int errorCode, String message) {
                Log.e(TAG, "push registration id to server failed, errorCode : " + errorCode + ", error message:" + message);
                if (NetworkUtil.isNetworkAvailable(mContext)) {
                    Log.i(TAG, "After 60 second retry report registration id.");
                    RxUtil.postOnIoThread(new Runnable() {
                        @Override
                        public void run() {
                            reportRegistrationId(regId);
                        }
                    }, 60 * 1000);
                }
            }
        });
    }
}
