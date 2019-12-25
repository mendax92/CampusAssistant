package com.zhiqi.campusassistant.core.repair.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ming on 2017/1/14.
 * 维修操作
 */

public enum RepairAction {

    /**
     * 撤销(用户自己)
     */
    @SerializedName("1")
    Cancel(1),

    /**
     * 驳回(管理人)
     */
    @SerializedName("2")
    Reject(2),

    /**
     * 修改
     */
    @SerializedName("3")
    Edit(3),

    /**
     * 去维修
     */
    @SerializedName("4")
    Service(4),

    /**
     * 确认维修
     */
    @SerializedName("5")
    Confirm(5),

    /**
     * 催办
     */
    @SerializedName("6")
    Urge(6),

    /**
     * 联系报修人
     */
    @SerializedName("7")
    Contact(7),

    /**
     * 归档
     */
    @SerializedName("8")
    Archived(8);

    private int value;

    RepairAction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RepairAction formatValue(int value) {
        switch (value) {
            case 1:
                return Cancel;
            case 2:
                return Reject;
            case 3:
                return Edit;
            case 4:
                return Service;
            case 5:
                return Confirm;
            case 6:
                return Urge;
            case 7:
                return Contact;
            case 8:
                return Archived;
        }
        return null;
    }
}
