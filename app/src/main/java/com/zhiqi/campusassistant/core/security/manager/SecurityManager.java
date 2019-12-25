package com.zhiqi.campusassistant.core.security.manager;

import android.content.Context;

import com.ming.base.http.HttpManager;
import com.ming.base.http.interfaces.OnHttpCallback;
import com.ming.base.util.Log;
import com.ming.base.util.TimeUtil;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.security.api.HttpTimestampService;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ming on 2016/11/21.
 * 安全管理，主要同步服务器时间差
 */

public class SecurityManager {

    private static final String TAG = "SecurityManager";

    private static SecurityManager instance;

    private long timeGap;

    private volatile boolean isRunning = false;

    private SecurityManager() {
    }

    public static SecurityManager getInstance() {
        if (instance == null) {
            synchronized (SecurityManager.class) {
                if (instance == null) {
                    return instance = new SecurityManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取服务器时间
     */
    public void queryServerTime() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        AssistantApplication.getInstance().cancelHttpRequest();
        HttpTimestampService service = AssistantApplication.getInstance().getApplicationComponent().getHttpTimestampService();
        Observable<BaseResultData<Map<String, Long>>> observable = service.getServerTime();
        HttpManager.subscribe(observable, new OnHttpCallback<BaseResultData<Map<String, Long>>>() {
            @Override
            public void onFinished(BaseResultData<Map<String, Long>> result) {
                isRunning = false;
                if (HttpErrorCode.SUCCESS == result.error_code) {
                    Map<String, Long> time = result.data;
                    if (time != null && !time.isEmpty()) {
                        long serverTime = time.get("server_time");
                        Log.i(TAG, "serverTime:" + serverTime);
                        timeGap = TimeUtil.getSimpleCurrentTime() - serverTime;
                        Log.i(TAG, "timeGap:" + timeGap);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                isRunning = false;
                t.printStackTrace();
            }
        });
    }

    /**
     * 获取跟服务器的时间差
     *
     * @return
     */
    public long getTimeGap() {
        return timeGap;
    }
}
