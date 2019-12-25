package com.zhiqi.campusassistant.ui.repair.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.core.repair.entity.RepairAction;
import com.zhiqi.campusassistant.core.repair.entity.RepairApplyRequest;
import com.zhiqi.campusassistant.core.repair.entity.RepairDetails;
import com.zhiqi.campusassistant.core.repair.entity.RepairStatus;

/**
 * Created by ming on 2017/1/14.
 */

public class RepairHelper {


    /**
     * 获取维修申请drawable
     *
     * @param context
     * @param status
     * @return
     */
    public static Drawable getRepairStatusBackground(Context context, RepairStatus status) {
        if (status == null) {
            return null;
        }
        switch (status) {
            case Start:
            case SUBMIT:
            case Allocated:
            case Waiting:
            case Confirm:
                return ContextCompat.getDrawable(context, R.drawable.common_bg_status_waiting);
            case Completed:
            case End:
                return ContextCompat.getDrawable(context, R.drawable.common_bg_status_agree);
            case Cancel:
                return ContextCompat.getDrawable(context, R.drawable.common_bg_status_cancel);
            case Reject:
                return ContextCompat.getDrawable(context, R.drawable.common_bg_status_reject);
            default:
                return null;
        }
    }

    /**
     * 获取维修操作的文本
     *
     * @param context
     * @param userAction
     * @return
     */
    public static String getRepairActionText(Context context, RepairAction userAction) {
        if (userAction == null) {
            return null;
        }
        String[] actions = context.getResources().getStringArray(R.array.repair_action);
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
    public static Drawable getRepairActionDrawable(Context context, RepairAction userAction) {
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
            case Service:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_assign);
            case Confirm:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_agree);
            case Urge:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_urge);
            case Contact:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_contact);
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
    public static Drawable getRepairStatusDrawable(Context context, RepairStatus status) {
        if (status == null) {
            return null;
        }
        switch (status) {
            case Cancel:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_cancel);
            case Reject:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_reject);
            case Waiting:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_wait);
            default:
                return ContextCompat.getDrawable(context, R.drawable.ic_operate_agree);
        }
    }

    /**
     * 维修详情转换成维修请求
     *
     * @param details
     * @return
     */
    public static RepairApplyRequest convertDetails(RepairDetails details) {
        if (details == null) {
            return null;
        }
        RepairApplyRequest request = new RepairApplyRequest();
        request.form_id = details.id;
        request.type = details.repair_type;
        request.detail = details.detail;
        request.imageDatas = details.images;
        request.applicant_name = details.applicant_name;
        request.phone = details.phone;
        request.district_id = details.district_id;
        request.location_id = details.location_id;
        request.address = details.address;
        request.appointment = details.appointment;
        return request;
    }
}
