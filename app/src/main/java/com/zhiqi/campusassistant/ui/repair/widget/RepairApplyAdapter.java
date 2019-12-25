package com.zhiqi.campusassistant.ui.repair.widget;

import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.repair.entity.RepairInfo;
import com.zhiqi.campusassistant.ui.repair.activity.RepairDetailActivity;

/**
 * Created by ming on 2016/12/30.
 */

public class RepairApplyAdapter extends BaseQuickAdapter<RepairInfo> {

    public RepairApplyAdapter() {
        super(R.layout.item_repair_apply, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, RepairInfo item, int position) {
        helper.setText(R.id.repair_name, item.type_name);
        helper.setText(R.id.repair_detail, item.detail);
        helper.setText(R.id.repair_time, item.apply_time);
        TextView statusView = helper.getView(R.id.repair_status);
        ViewCompat.setBackground(statusView, RepairHelper.getRepairStatusBackground(mContext, item.status));
        statusView.setText(item.status_name);
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
                if (info != null && mContext instanceof AppCompatActivity) {
                    Intent intent = new Intent(mContext, RepairDetailActivity.class);
                    intent.putExtra(AppConstant.EXTRA_IS_SELF, true);
                    intent.putExtra(AppConstant.EXTRA_REPAIR_ID, info.id);
                    ((AppCompatActivity) mContext).startActivityForResult(intent, AppConstant.REQUEST_CODE_FOR_UPDATE);
                }
            }
        }
    };
}
