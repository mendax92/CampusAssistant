package com.zhiqi.campusassistant.ui.leave.widget;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ming.base.util.NumberUtil;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.leave.entity.LeaveInfo;
import com.zhiqi.campusassistant.ui.leave.activity.LeaveDetailActivity;

/**
 * Created by ming on 2016/12/29.
 */

public class LeaveApprovalAdapter extends BaseQuickAdapter<LeaveInfo> {

    private Fragment mFragment;

    public LeaveApprovalAdapter() {
        this(null);
    }

    public LeaveApprovalAdapter(Fragment fragment) {
        super(R.layout.item_leave_approval, null);
        this.mFragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, LeaveInfo item, int position) {
        helper.setText(R.id.leave_name, mContext.getString(R.string.leave_describe, item.type_name, NumberUtil.format(item.total_days, 1)));
        helper.setText(R.id.apply_user_name, item.applicant_name);
        helper.setText(R.id.leave_apply_date, mContext.getString(R.string.apply_apply_date, item.apply_time));
        helper.setText(R.id.leave_department, item.department);
        helper.setText(R.id.leave_date, mContext.getString(R.string.leave_data_content, item.start_time, item.end_time));
        helper.setText(R.id.leave_reason, item.reason);
        helper.getConvertView().setTag(R.id.leave_approval_item, position);
        helper.getConvertView().setOnClickListener(itemClick);
    }

    private View.OnClickListener itemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag(R.id.leave_approval_item);
            if (tag != null) {
                LeaveInfo info = getItem((int) tag);
                if (info != null) {
                    Intent intent = new Intent(mContext, LeaveDetailActivity.class);
                    intent.putExtra(AppConstant.EXTRA_LEAVE_ID, info.id);
                    if (mFragment != null) {
                        mFragment.startActivityForResult(intent, AppConstant.REQUEST_CODE_FOR_UPDATE);
                    } else if (mContext instanceof Activity) {
                        ((Activity) mContext).startActivityForResult(intent, AppConstant.REQUEST_CODE_FOR_UPDATE);
                    }
                }
            }
        }
    };
}
