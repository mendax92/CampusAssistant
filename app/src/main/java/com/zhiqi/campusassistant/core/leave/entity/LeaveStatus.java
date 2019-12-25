package com.zhiqi.campusassistant.core.leave.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edmin on 2016/11/13 0013.
 * 请假操作流状态
 */

public enum LeaveStatus {
    /**
     * 提交申请（流程开始节点）
     */
    @SerializedName("0")
    Start(0),
    /**
     * 处理（审核）中
     */
    @SerializedName("1")
    Processing(1),
    /**
     * 同意（已审核）
     */
    @SerializedName("2")
    Agree(2),
    /**
     * 拒绝
     */
    @SerializedName("3")
    Reject(3),
    /**
     * 撤销
     */
    @SerializedName("4")
    Cancel(4),
    /**
     * 完成
     */
    @SerializedName("5")
    End(5);

    private int value;

    LeaveStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static LeaveStatus formatValue(int value) {
        switch (value) {
            case 0:
                return Start;
            case 1:
                return Processing;
            case 2:
                return Agree;
            case 3:
                return Reject;
            case 4:
                return Cancel;
            case 5:
                return End;
        }
        return null;
    }
}
