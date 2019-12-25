package com.zhiqi.campusassistant.ui.leave.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.core.leave.entity.LeaveAction;
import com.zhiqi.campusassistant.core.leave.entity.LeaveDetails;
import com.zhiqi.campusassistant.core.leave.entity.LeaveRequest;
import com.zhiqi.campusassistant.core.leave.entity.LeaveStatus;

/**
 * Created by ming on 2017/1/14.
 */

public class LeaveHelper {


    /**
     * 获取请假申请drawable
     *
     * @param context
     * @param status
     * @return
     */
    public static Drawable getLeaveStatusBackground(Context context, LeaveStatus status) {
        if (status == null) {
            return null;
        }
        switch (status) {
            case Agree:
                return ContextCompat.getDrawable(context, R.drawable.common_bg_status_agree);
            case Cancel:
                return ContextCompat.getDrawable(context, R.drawable.common_bg_status_cancel);
            case Reject:
                return ContextCompat.getDrawable(context, R.drawable.common_bg_status_reject);
            default:
                return ContextCompat.getDrawable(context, R.drawable.common_bg_status_waiting);
        }
    }

    /**
     * 获取维修操作的文本
     *
     * @param context
     * @param userAction
     * @return
     */
    public static String getLeaveActionText(Context context, LeaveAction userAction) {
        if (userAction == null) {
            return null;
        }
        String[] actions = context.getResources().getStringArray(R.array.leave_action);
        if (userAction.getValue() <= actions.length) {
            return actions[userAction.getValue() - 1];
        }
        return null;
    }

    /**
     * 操作动作返回drawable
     *
     * @param context
     * @param userAction
     * @return
     */
    public static Drawable getLeaveActionDrawable(Context context, LeaveAction userAction) {
        if (userAction == null) {
            return null;
        }
        switch (userAction) {
            case Cancel:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_cancel);
            case Reject:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_reject);
            case Edit:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_resubmit);
            case Agree:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_agree);
            case Urge:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_urge);
            case Archived:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_archived);
            default:
                return null;
        }
    }

    /**
     * 根据维修状态获取图标
     *
     * @param context
     * @param status
     * @return
     */
    public static Drawable getLeaveStatusDrawable(Context context, LeaveStatus status) {
        if (status == null) {
            return null;
        }
        switch (status) {
            case Cancel:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_cancel);
            case Reject:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_reject);
            case Agree:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_agree);
            default:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_wait);
        }
    }

    /**
     * 详情转换请求
     *
     * @param details
     * @return
     */
    public static LeaveRequest convertDetails(LeaveDetails details) {
        LeaveRequest request = new LeaveRequest();
        request.form_id = details.id;
        request.type = details.type_id;
        request.imageDatas = details.images;
        request.start_time = details.start_time;
        request.end_time = details.end_time;
        request.total_days = details.total_days;
        request.reason = details.reason;
        return request;
    }
}
