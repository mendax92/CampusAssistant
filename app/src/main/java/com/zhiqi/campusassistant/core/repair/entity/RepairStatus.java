package com.zhiqi.campusassistant.core.repair.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edmin on 2016/11/13 0013.
 * 维修工作流
 */

public enum RepairStatus {

    /**
     * 流程开始
     */
    @SerializedName("0")
    Start(0),
    /**
     * 已提交（流程开始节点）
     */
    @SerializedName("1")
    SUBMIT(1),
    /**
     * 已分配
     */
    @SerializedName("2")
    Allocated(2),
    /**
     * 等待维修
     */
    @SerializedName("3")
    Waiting(3),
    /**
     * 确认维修
     */
    @SerializedName("4")
    Confirm(4),
    /**
     * 已完成
     */
    @SerializedName("5")
    Completed(5),
    /**
     * 已撤销
     */
    @SerializedName("6")
    Cancel(6),
    /**
     * 被取消(驳回)
     */
    @SerializedName("7")
    Reject(7),

    /**
     * 结束
     */
    @SerializedName("8")
    End(8);

    private int value;

    RepairStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RepairStatus formatValue(int value) {
        switch (value) {
            case 0:
                return Start;
            case 1:
                return SUBMIT;
            case 2:
                return Allocated;
            case 3:
                return Waiting;
            case 4:
                return Confirm;
            case 5:
                return Completed;
            case 6:
                return Cancel;
            case 7:
                return Reject;
            case 8:
                return End;
        }
        return null;
    }
}
