package com.zhiqi.campusassistant.core.payment.entity;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 17-8-4.
 * 已缴费信息
 */

public class SelfPaidInfo implements BaseJsonData {

    public long order_id;

    public long expense_id;

    public String expense_name;

    public double amount;

    public PayStatus order_status;

    public String status_name;

    public String payment_time;
}
