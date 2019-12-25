package com.ming.pay;

import com.tencent.mm.opensdk.modelpay.PayReq;

/**
 * Created by ming on 2017/7/22.
 * 微信支付请求
 */

public class WeChatRequest implements IPayRequest<PayReq> {

    private PayReq req;

    public WeChatRequest(PayReq req) {
        this.req = req;
    }

    @Override
    public PayReq getRequest() {
        return req;
    }
}
