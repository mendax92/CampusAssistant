package com.zhiqi.campusassistant.core.payment.entity;

import com.ming.base.bean.BaseJsonData;
import com.tencent.mm.opensdk.modelpay.PayReq;

/**
 * Created by ming on 2017/7/23.
 * 订单信息
 */

public class OrderInfo implements BaseJsonData {

    public String appId;

    public String nonceStr;

    public String partnerId;

    public String paySign;

    public String pkg;

    public String prepayId;

    public String timeStamp;

    public PayReq convert2WechatReq() {
        PayReq req = new PayReq();
        req.appId = appId;
        req.nonceStr = nonceStr;
        req.partnerId = partnerId;
        req.prepayId = prepayId;
        req.packageValue = pkg;
        req.sign = paySign;
        req.timeStamp = timeStamp;
        return req;
    }
}
