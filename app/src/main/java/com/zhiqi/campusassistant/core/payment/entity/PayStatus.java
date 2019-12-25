package com.zhiqi.campusassistant.core.payment.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ming on 17-8-8.
 * 支付状态
 */

public enum PayStatus {

    /**
     * 不支持
     */
    @SerializedName("-5")
    Unsupport(-5),

    /**
     * 取消支付
     */
    @SerializedName("-2")
    Cancel(-2),

    /**
     * 未支付
     */
    @SerializedName("0")
    Unpaid(0),

    /**
     * 支付成功
     */
    @SerializedName("1")
    Success(1),

    /**
     * 支付失败
     */
    @SerializedName("2")
    Error(2);

    private int value;

    PayStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PayStatus formatValue(int value) {
        switch (value) {
            case -5:
                return Unsupport;
            case -2:
                return Cancel;
            case 0:
                return Unpaid;
            case 1:
                return Success;
            case 2:
                return Error;
        }
        return null;
    }

}
