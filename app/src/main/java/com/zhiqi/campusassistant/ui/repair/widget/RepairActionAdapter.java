package com.zhiqi.campusassistant.ui.repair.widget;

import android.content.Context;
import android.view.View;

import com.ming.base.widget.TextDrawableLayout;
import com.ming.base.widget.listView.BaseListAdapter;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.core.repair.entity.RepairAction;

/**
 * Created by ming on 2017/2/7.
 */

public class RepairActionAdapter extends BaseListAdapter<RepairAction> {

    public RepairActionAdapter(Context context) {
        super(context);
    }


    @Override
    public int getLayoutResource() {
        return R.layout.item_apply_action;
    }

    @Override
    public void onBindView(int position, View convertView, RepairAction item) {
        TextDrawableLayout textView = findView(convertView, R.id.action_opera);
        textView.setText(RepairHelper.getRepairActionText(mContext, item));
        textView.setCompoundDrawables(RepairHelper.getRepairActionDrawable(mContext, item), null, null, null);
        textView.setTag(R.id.action_opera, position);
    }
}
