package com.ming.pay.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ming.pay.PayPlatform;
import com.ming.pay.PayServiceFactory;
import com.ming.pay.WeChatService;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * 微信回调activity，必须有${package}.wxapi.WXPayEntryActivity继承该类
 */
public class BaseWXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "BaseWXPayEntryActivity";

    private WeChatService service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        service = (WeChatService) PayServiceFactory.getPayService(PayPlatform.WECHAT);
        service.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        service.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        service.onResp(resp);
    }
}