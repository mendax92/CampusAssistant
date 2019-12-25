package com.zhiqi.campusassistant.core.message.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ming on 2016/12/20.
 */

public enum ModuleType {

    /**
     * 请假申请
     */
    @SerializedName("1")
    LeaveApply(1),

    /**
     * 请假审批
     */
    @SerializedName("2")
    LeaveApproval(2),

    /**
     * 报修申请
     */
    @SerializedName("3")
    RepairApply(3),

    /**
     * 报修受理
     */
    @SerializedName("4")
    RepairApproval(4),

    /**
     * 校园资讯
     */
    @SerializedName("5")
    News(5),

    /**
     * 校园卡充值
     */
    @SerializedName("6")
    CampusCard(6),

    /**
     * 员工通讯录
     */
    @SerializedName("7")
    StaffContacts(7),

    /**
     * 员工通讯录
     */
    @SerializedName("8")
    StudentContacts(8),

    /**
     * 课程表
     */
    @SerializedName("9")
    CourseSchedule(9),

    /**
     * 成绩查询
     */
    @SerializedName("10")
    Score(10),

    /**
     * 失物招领
     */
    @SerializedName("11")
    Lost(11),

    /**
     * 公告通知
     */
    @SerializedName("12")
    Notice(12),

    /**
     * 考勤签到
     */
    @SerializedName("13")
    Attendance(13),

    /**
     * 自助缴费
     */
    @SerializedName("14")
    AutoPayment(14),

    /**
     * 选择床位
     */
    @SerializedName("15")
    FreshmanBed(15),

    /**
     * 报到指南
     */
    @SerializedName("16")
    EntranceGuide(16),

    /**
     * 校园指引
     */
    @SerializedName("17")
    FreshmanGuide(17),
    /**
     * 水表使用
     */
    @SerializedName("18")
    WaterMeterGuide(18),

    /**
     * 校园网
     */
    @SerializedName("20")
    CampusNetwork(20);


    private int value;

    ModuleType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ModuleType formatValue(int value) {
        switch (value) {
            case 1:
                return LeaveApply;
            case 2:
                return LeaveApproval;
            case 3:
                return RepairApply;
            case 4:
                return RepairApproval;
            case 5:
                return News;
            case 6:
                return CampusCard;
            case 7:
                return StaffContacts;
            case 8:
                return StudentContacts;
            case 9:
                return CourseSchedule;
            case 10:
                return Score;
            case 11:
                return Lost;
            case 12:
                return Notice;
            case 13:
                return Attendance;
            case 14:
                return AutoPayment;
            case 15:
                return FreshmanBed;
            case 16:
                return EntranceGuide;
            case 17:
                return FreshmanGuide;
            case 18:
                return WaterMeterGuide;
            case 20:
                return CampusNetwork;
        }
        return null;
    }
}
