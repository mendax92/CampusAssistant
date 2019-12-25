package com.zhiqi.campusassistant.ui.repair.widget;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.repair.entity.RepairInfo;
import com.zhiqi.campusassistant.ui.repair.activity.RepairDetailActivity;

/**
 * Created by ming on 2016/12/31.
 * 维修受理adapter
 */

public class RepairApprovalAdapter extends BaseQuickAdapter<RepairInfo> {

    private Fragment mFragment;

    public RepairApprovalAdapter() {
        this(null);
    }

    public RepairApprovalAdapter(Fragment fragment) {
        super(R.layout.item_repair_approval, null);
        this.mFragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, final RepairInfo item, int position) {
        helper.setText(R.id.repair_type_name, item.type_name);
        helper.setText(R.id.apply_user_name, item.applicant_name);
        helper.setText(R.id.repair_apply_date, mContext.getString(R.string.apply_apply_date, item.apply_time));
        helper.setText(R.id.repair_area, item.location);
        helper.setText(R.id.repair_appointment_time, item.appointment);
        helper.setText(R.id.repair_detail, item.detail);
        helper.getConvertView().setTag(R.id.repair_detail, position);
        helper.getConvertView().setOnClickListener(itemClickListener);
    }

    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag(R.id.repair_detail);
            if (tag != null) {
                int position = (int) tag;
                RepairInfo info = getItem(position);
                if (info == null || !(mContext instanceof AppCompatActivity)) {
                    return;
                }
                Intent intent = new Intent(mContext, RepairDetailActivity.class);
                intent.putExtra(AppConstant.EXTRA_IS_SELF, false);
                intent.putExtra(AppConstant.EXTRA_REPAIR_ID, info.id);
                if (mFragment != null) {
                    mFragment.startActivityForResult(intent, AppConstant.REQUEST_CODE_FOR_UPDATE);
                } else {
                    ((AppCompatActivity) mContext).startActivityForResult(intent, AppConstant.REQUEST_CODE_FOR_UPDATE);
                }
            }
        }
    };
}
