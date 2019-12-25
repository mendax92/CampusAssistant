package com.zhiqi.campusassistant.core.leave.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ming on 2017/3/8.
 * 请假操作
 */

public enum LeaveAction {

    /**
     * 同意
     */
    @SerializedName("1")
    Agree(1),
    /**
     * 驳回
     */
    @SerializedName("2")
    Reject(2),
    /**
     * 取消
     */
    @SerializedName("3")
    Cancel(3),
    /**
     * 修改
     */
    @SerializedName("4")
    Edit(4),
    /**
     * 归档
     */
    @SerializedName("5")
    Archived(5),
    /**
     * 催办
     */
    @SerializedName("6")
    Urge(6);


    private int value;

    LeaveAction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static LeaveAction formatValue(int value) {
        switch (value) {
            case 1:
                return Agree;
            case 2:
                return Reject;
            case 3:
                return Cancel;
            case 4:
                return Edit;
            case 5:
                return Archived;
            case 6:
                return Urge;
        }
        return null;
    }
}
