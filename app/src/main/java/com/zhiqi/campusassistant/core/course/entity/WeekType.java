package com.zhiqi.campusassistant.core.course.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ming on 2016/12/16.
 * 课程周类型
 */

public enum WeekType {
    /**
     * 单周
     */
    @SerializedName("1")
    OddWeek(1),

    /**
     * 双周
     */
    @SerializedName("2")
    EvenWeek(2),

    /**
     * 每周
     */
    @SerializedName("3")
    EveryWeek(3);

    private int value;

    WeekType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static WeekType formatValue(int value) {
        switch (value) {
            case 1:
                return OddWeek;
            case 2:
                return EveryWeek;
            case 3:
                return EveryWeek;
            default:
                return null;
        }
    }
}
