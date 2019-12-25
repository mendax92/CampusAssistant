package com.zhiqi.campusassistant.core.upload.entity;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ming on 2017/2/26.
 */

public enum UploadType {

    /**
     * 用户信息
     */
    @SerializedName("USER_PROFILE")
    UserProfile("USER_PROFILE"),

    /**
     * 维修
     */
    @SerializedName("MAINTENANCE")
    Maintenance("MAINTENANCE"),

    /**
     * 学校
     */
    @SerializedName("SCHOOL")
    School("SCHOOL"),

    /**
     * 请假
     */
    @SerializedName("VACATION")
    Vacation("VACATION"),

    /**
     * 失物招领
     */
    @SerializedName("LOST")
    LOST("LOST");


    private String value;

    UploadType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UploadType formatValue(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        if ("USER_PROFILE".equals(value)) {
            return UserProfile;
        } else if ("MAINTENANCE".equals(value)) {
            return Maintenance;
        } else if ("SCHOOL".equals(value)) {
            return School;
        } else if ("VACATION".equals(value)) {
            return Vacation;
        } else if ("LOST".equals(value)) {
            return LOST;
        }
        return null;
    }
}
