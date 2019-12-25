package com.zhiqi.campusassistant.core.jpush.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ming.base.util.Log;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ming on 2017/3/22.
 * JPush接受者
 */

public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPushReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "Receive Registration Id : " + regId);
            JPushManager.getInstance().reportRegistrationId(regId);
        }
    }
}
