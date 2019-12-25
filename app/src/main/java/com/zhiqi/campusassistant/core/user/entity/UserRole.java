package com.zhiqi.campusassistant.core.user.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edmin on 2016/11/5 0005.
 */

public enum UserRole {

    /**
     * 超级管理员
     */
    @SerializedName("1")
    SuperAdmin(1),

    /**
     * 管理员
     */
    @SerializedName("2")
    Admin(2),

    /**
     * 职工
     */
    @SerializedName("3")
    Staff(3),

    /**
     * 教师
     */
    @SerializedName("4")
    Teacher(4),

    /**
     * 学生
     */
    @SerializedName("5")
    Student(5);

    private int value;

    UserRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UserRole formatValue(int value) {
        switch (value) {
            case 1:
                return SuperAdmin;
            case 2:
                return Admin;
            case 3:
                return Staff;
            case 4:
                return Teacher;
            case 5:
                return Student;
        }
        return null;
    }
}
