package com.zhiqi.campusassistant.core.payment.entity;

import com.ming.base.bean.BaseJsonData;

import java.util.List;

/**
 * Created by ming on 2017/7/23.
 * 消费信息
 */

public class ExpenseInfo implements BaseJsonData {

    public long expense_id;

    public String expense_name;

    public double amount;

    public List<PayTypeInfo> support_payment_type;

}
