package com.zhiqi.campusassistant.core.user.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edmin on 2016/11/5 0005.
 */

public enum Gender {

    /**
     * 女
     */
    @SerializedName("0")
    Female(0),

    /**
     * 男
     */
    @SerializedName("1")
    Male(1);

    private int value;

    Gender(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Gender formatValue(int value) {
        if (value == 1) {
            return Male;
        }
        return Female;
    }
}
