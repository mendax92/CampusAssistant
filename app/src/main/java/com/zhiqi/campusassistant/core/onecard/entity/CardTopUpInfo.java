package com.zhiqi.campusassistant.core.onecard.entity;

import com.ming.base.bean.BaseJsonData;
import com.zhiqi.campusassistant.core.payment.entity.PayTypeInfo;

import java.util.List;

/**
 * Created by ming on 2018/1/27.
 * 一卡通充值信息
 */

public class CardTopUpInfo implements BaseJsonData {

    public String comment;

    public String balance;

    public float max_amount;

    public List<TopUpAccount> topup_amounts;

    public List<PayTypeInfo> support_payment_type;
}
