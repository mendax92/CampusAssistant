package com.zhiqi.campusassistant.core.payment.entity;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2017/7/23.
 * 学生支付费用信息
 */

public class StudentExpense implements BaseJsonData {

    public long expense_id;

    public String expense_name;

    public double amount;
}
