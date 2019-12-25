package com.zhiqi.campusassistant.core.lost.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ming on 2017/5/5.
 * 失物类型
 */

public enum LostApplyType {

    /**
     * 捡到
     */
    @SerializedName("1")
    Found(1),

    /**
     * 丢失
     */
    @SerializedName("2")
    Lost(2);

    private int value;

    LostApplyType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static LostApplyType formatValue(int value) {
        switch (value) {
            case 1:
                return Found;
            case 2:
                return Lost;
        }
        return null;
    }
}
