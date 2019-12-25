package com.zhiqi.campusassistant.core.onecard.entity;

import com.ming.base.bean.BaseJsonData;
import com.ming.base.util.GsonUtils;
import com.zhiqi.campusassistant.core.payment.entity.PayType;

/**
 * Created by ming on 2018/1/28.
 * 支付请求信息
 */

public class CardPayRequest implements BaseJsonData {

    public String topup_amount;

    public String service_fee;

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
