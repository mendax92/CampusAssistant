package com.zhiqi.campusassistant.core.payment.entity;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.zhiqi.campusassistant.R;

/**
 * Created by Edmin on 2016/10/30 0030.
 */

public enum PayType {
    @SerializedName("1")
    WECHAT(1),
    @SerializedName("2")
    ALIPAY(2),
    @SerializedName("3")
    UNIONPAY(3),
    @SerializedName("4")
    APPLEPAY(4);

    /**
     * 微信支付利息
     */
    public static final double PAY_WECHAT_RATE = 0.006;

    /**
     * 支付宝利息
     */
    public static final double PAY_ALI_RATE = 0.01;

    private int value;

    PayType(int value) {
        this.value = value;
    }

    public double getPayRate() {
        return this == PayType.WECHAT ? PAY_WECHAT_RATE : PAY_ALI_RATE;
    }

    public int getValue() {
        return value;
    }

    public String getPayName(Context context) {
        switch (this) {
            case WECHAT:
                return context.getString(R.string.pay_wxpay);
            case ALIPAY:
                return context.getString(R.string.pay_alipay);
            case UNIONPAY:
                return context.getString(R.string.pay_union);
            case APPLEPAY:
                return context.getString(R.string.pay_apple);
        }
        return null;
    }

    public static PayType formatValue(int value) {
        switch (value) {
            case 1:
                return WECHAT;
            case 2:
                return ALIPAY;
            case 3:
                return UNIONPAY;
            case 4:
                return APPLEPAY;
        }
        return null;
    }
}
