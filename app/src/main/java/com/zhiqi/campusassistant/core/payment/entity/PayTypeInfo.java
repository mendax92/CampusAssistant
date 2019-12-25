package com.zhiqi.campusassistant.core.payment.entity;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2017/7/23.
 * 支付类型信息
 */

public class PayTypeInfo implements BaseJsonData {

    public PayType payment_type;

    public String payment_type_name;

    public float service_rate;

}
