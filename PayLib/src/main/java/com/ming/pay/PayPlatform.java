package com.ming.pay;

/**
 * Created by ming on 17-7-11.
 * 支付平台
 */

public enum PayPlatform {

    WECHAT(1),
    ALIPAY(2);

    private int value;

    PayPlatform(int value) {
        this.value = value;
    }

    public static PayPlatform formatValue(int value) {
        switch (value) {
            case 1:
                return WECHAT;
            case 2:
                return ALIPAY;
        }
        return null;
    }
}
