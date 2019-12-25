package com.zhiqi.campusassistant.ui.leave.widget;

import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ming.base.util.NumberUtil;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.leave.entity.LeaveInfo;
import com.zhiqi.campusassistant.core.leave.entity.LeaveStatus;
import com.zhiqi.campusassistant.ui.leave.activity.LeaveDetailActivity;

/**
 * Created by ming on 2016/11/14.
 * 请假申请adapter
 */

public class LeaveApplyAdapter extends BaseQuickAdapter<LeaveInfo> {

    public LeaveApplyAdapter() {
        super(R.layout.item_leave_apply, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, LeaveInfo item, int position) {
        helper.setText(R.id.leave_name, mContext.getString(R.string.leave_describe, item.type_name, NumberUtil.format(item.total_days, 1)));
        helper.setText(R.id.leave_time, item.apply_time);
        helper.setText(R.id.leave_date, mContext.getString(R.string.leave_data_content, item.start_time, item.end_time));
        TextView statusView = helper.getView(R.id.leave_status);
        ViewCompat.setBackground(statusView, LeaveHelper.getLeaveStatusBackground(mContext, item.status));
        statusView.setText(LeaveStatus.Processing == item.status ? mContext.getString(R.string.leave_wait_status, item.approver) : item.status_name);
        helper.getConvertView().setTag(R.id.leave_item, position);
        helper.getConvertView().setOnClickListener(itemClick);
    }

    private View.OnClickListener itemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag(R.id.leave_item);
            if (tag != null) {
                int position = (int) tag;
                LeaveInfo info = getItem(position);
                if (info != null && mContext instanceof AppCompatActivity) {
                    Intent intent = new Intent(mContext, LeaveDetailActivity.class);
                    intent.putExtra(AppConstant.EXTRA_LEAVE_ID, info.id);
                    ((AppCompatActivity) mContext).startActivityForResult(intent, AppConstant.REQUEST_CODE_FOR_UPDATE);
                }
            }
        }
    };
}
