package com.zhiqi.campusassistant.core.news.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ming on 2016/12/22.
 */

public enum NewsType {

    /**
     * 标题
     */
    @SerializedName("0")
    Title(0),

    /**
     * 图片
     */
    @SerializedName("1")
    Picture(1),

    /**
     * 图文混合
     */
    @SerializedName("2")
    Fix(2);

    private int value;

    NewsType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static NewsType formatValue(int value) {
        switch (value) {
            case 0:
                return Title;
            case 1:
                return Picture;
            case 2:
                return Fix;
            default:
                return null;
        }
    }
}
