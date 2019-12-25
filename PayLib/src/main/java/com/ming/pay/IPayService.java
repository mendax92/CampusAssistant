package com.ming.pay;

import android.content.Context;

/**
 * Created by ming on 17-7-11.
 * 支付服务
 */

public interface IPayService {

    void init(Context context, String appId);

    /**
     * app是否已经安装
     *
     * @return
     */
    boolean isAppInstalled();

    /**
     * 支付
     *
     * @param request     支付请求
     * @param onPayResult 支付回调
     * @return
     * @throws PayException
     */
    void pay(IPayRequest<?> request, IOnPayResult onPayResult) throws PayException;

    /**
     * 检测结果
     */
    void checkResult();
}
