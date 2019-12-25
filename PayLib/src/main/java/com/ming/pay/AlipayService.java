package com.ming.pay;

import android.content.Context;

/**
 * Created by ming on 17-7-11.
 * 支付宝服务
 */

public class AlipayService implements IPayService {

    private String appId;

    @Override
    public void init(Context context, String appId) {
        this.appId = appId;
    }

    @Override
    public boolean isAppInstalled() {
        return false;
    }

    @Override
    public void pay(IPayRequest<?> request, IOnPayResult onPayResult) throws PayException {

    }

    @Override
    public void checkResult() {

    }
}
