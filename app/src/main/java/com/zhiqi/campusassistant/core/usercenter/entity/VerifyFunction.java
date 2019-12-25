package com.zhiqi.campusassistant.core.usercenter.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ming on 2017/3/19.
 * 验证码操作类型
 */

public enum VerifyFunction {

    /**
     * 忘记密码
     */
    @SerializedName("1")
    ForgetPassword(1),

    /**
     * 修改手机号
     */
    @SerializedName("2")
    ChangePhone(2),

    /**
     * 绑定手机号
     */
    @SerializedName("3")
    BindPhone(3),
    /**
     * 重置支付密码
     */
    @SerializedName("6")
    ResetPayPassword(6);

    private int value;

    VerifyFunction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static VerifyFunction formatValue(int value) {
        switch (value) {
            case 1:
                return ForgetPassword;
            case 2:
                return ChangePhone;
            case 3:
                return BindPhone;
            case 6:
                return ResetPayPassword;
        }
        return null;
    }
}
