package com.zhiqi.campusassistant.core.payment.entity;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2017/8/13.
 * 支付详情
 */

public class OrderDetail implements BaseJsonData {

    public long order_id;

    public String expense_name;

    public double amount;

    public PayStatus order_status;

    public String status_name;

    public String payment_time;

    public String student_no;

    public String real_name;

    public String class_name;

    public String order_no;

    public String service_tell;

    public String payment_type_name;
}
