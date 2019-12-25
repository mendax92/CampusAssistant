package com.zhiqi.campusassistant.ui.repair.widget;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ming.base.widget.CircleView;
import com.ming.base.widget.TextDrawableView;
import com.ming.base.widget.recyclerView.BaseMultiItemQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.core.repair.entity.RepairStatus;
import com.zhiqi.campusassistant.core.repair.entity.RepairStep;

/**
 * Created by ming on 2017/2/9.
 * 维修步骤adapter
 */

public class RepairStepAdapter extends BaseMultiItemQuickAdapter<RepairStep> {

    public RepairStepAdapter() {
        super(null);
        addItemType(1, R.layout.item_flow_empty_step);
        addItemType(2, R.layout.item_flow_step);
    }

    @Override
    protected int getItemViewType(RepairStep item) {
        if (item != null && item.status != null
                && RepairStatus.Start != item.status
                && RepairStatus.End != item.status) {
            return 2;
        }
        return 1;
    }

    @Override
    protected void convert(BaseViewHolder helper, RepairStep item, int position) {
        if (item == null || item.status == null) {
            return;
        }
        if (position == 0) {
            helper.getView(R.id.step_line_top).setVisibility(View.INVISIBLE);
            helper.setVisible(R.id.step_line_bottom, true);
        } else if (RepairStatus.Start == item.status) {
            helper.getView(R.id.step_line_bottom).setVisibility(View.INVISIBLE);
            helper.setVisible(R.id.step_line_top, true);
        } else {
            helper.setVisible(R.id.step_line_bottom, true);
            helper.setVisible(R.id.step_line_top, true);
        }
        CircleView circleView = helper.getView(R.id.flow_circle);
        if (position == 0 && RepairStatus.End != item.status) {
            circleView.setCircleColorResource(R.color.common_orange);
        } else {
            circleView.setCircleColorResource(R.color.common_line_color);
        }
        switch (getItemViewType(item)) {
            case 1:
                helper.setText(R.id.flow_step_name, item.comment);
                break;
            case 2:
                helper.setText(R.id.flow_position, item.position);
                helper.setText(R.id.flow_step_name, item.comment);
                ImageView headerView = helper.getView(R.id.user_header);
                GlideApp.with(mContext).load(item.operator_head).placeholder(R.drawable.img_user_default_square_head).into(headerView);
                helper.setText(R.id.flow_time, item.set_time);
                TextView commentTxt = helper.getView(R.id.comment);
                if (!TextUtils.isEmpty(item.comment)) {
                    commentTxt.setText(item.comment);
                    commentTxt.setVisibility(View.VISIBLE);
                } else {
                    commentTxt.setVisibility(View.GONE);
                }
                TextDrawableView flowStep = helper.getView(R.id.flow_step_name);
                flowStep.setText(item.operator_name);
                Drawable left = RepairHelper.getRepairStatusDrawable(mContext, item.status);
                flowStep.setCompoundDrawables(left, null, null, null);
                break;
        }
    }
}
