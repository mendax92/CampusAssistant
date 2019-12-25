package com.zhiqi.campusassistant.ui.leave.widget;

import android.content.Context;
import android.view.View;

import com.ming.base.widget.TextDrawableLayout;
import com.ming.base.widget.listView.BaseListAdapter;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.core.leave.entity.LeaveAction;

/**
 * Created by ming on 2017/2/7.
 */

public class LeaveActionAdapter extends BaseListAdapter<LeaveAction> {

    public LeaveActionAdapter(Context context) {
        super(context);
    }


    @Override
    public int getLayoutResource() {
        return R.layout.item_apply_action;
    }

    @Override
    public void onBindView(int position, View convertView, LeaveAction item) {
        TextDrawableLayout textView = findView(convertView, R.id.action_opera);
        textView.setText(LeaveHelper.getLeaveActionText(mContext, item));
        textView.setCompoundDrawables(LeaveHelper.getLeaveActionDrawable(mContext, item), null, null, null);
        textView.setTag(R.id.action_opera, position);
    }
}
