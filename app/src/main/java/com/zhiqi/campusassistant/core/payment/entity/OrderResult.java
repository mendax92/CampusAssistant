package com.zhiqi.campusassistant.core.payment.entity;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2017/7/23.
 * 支付订单结果
 */

public class OrderResult implements BaseJsonData {

    public long order_id;

    public PayStatus order_status;

    public String description;
}
