package com.zhiqi.campusassistant.ui.main.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.core.message.entity.ModuleType;

/**
 * Created by ming on 2017/3/11.
 * app帮助类
 */

public class AppHelper {

    public static Drawable getAppIcon(Context context, ModuleType type) {
        switch (type) {
            case LeaveApply:
                return ContextCompat.getDrawable(context, R.drawable.ic_app_leave_apply);
            case LeaveApproval:
                return ContextCompat.getDrawable(context, R.drawable.ic_app_leave_approval);
            case RepairApply:
                return ContextCompat.getDrawable(context, R.drawable.ic_app_repair_apply);
            case RepairApproval:
                return ContextCompat.getDrawable(context, R.drawable.ic_app_repair_approval);
            case News:
                return ContextCompat.getDrawable(context, R.drawable.ic_app_information);
            case CampusCard:
                return ContextCompat.getDrawable(context, R.drawable.ic_app_card);
            case CourseSchedule:
                return ContextCompat.getDrawable(context, R.drawable.ic_app_course_schedule);
            case StaffContacts:
            case StudentContacts:
                return ContextCompat.getDrawable(context, R.drawable.ic_app_contacts);
        }
        return null;
    }

    /**
     * 获取app名称
     *
     * @param context
     * @param type
     * @return
     */
    public static String getAppName(Context context, ModuleType type) {
        if (type == null) {
            return null;
        }
        int index = type.getValue();
        String[] arrays = context.getResources().getStringArray(R.array.module_name);
        if (index > arrays.length) {
            return null;
        }
        return arrays[index - 1];
    }
}
