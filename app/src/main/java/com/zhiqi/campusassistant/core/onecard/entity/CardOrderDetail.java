package com.zhiqi.campusassistant.core.onecard.entity;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by minh on 18-2-2.
 * 一卡通账单详情
 */

public class CardOrderDetail implements BaseJsonData {
    public long comsume_id;
    public String comment;
    public String topup_amount;
    public String type_name;
    public String order_no;
    public String payment_time;
    public String balance;
}
