package com.zhiqi.campusassistant.ui.contacts.widget;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ming.base.widget.recyclerView.BaseMultiItemQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.core.user.entity.DepartmentInfo;
import com.zhiqi.campusassistant.core.user.entity.UserInfo;

/**
 * Created by ming on 2016/10/26.
 * 通讯录列表
 */

public class UserInfoListAdapter extends BaseMultiItemQuickAdapter<Object> {

    public UserInfoListAdapter() {
        super(null);
        addItemType(1, R.layout.item_addressbook_user);
        addItemType(2, R.layout.item_addressbook_depart);
    }

    @Override
    protected int getItemViewType(Object item) {
        if (item instanceof DepartmentInfo) {
            return 2;
        }
        return 1;
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item, int position) {
        if (item == null) {
            return;
        }
        int type = getItemViewType(item);
        switch (type) {
            case 1:
                ImageView header = helper.getView(R.id.user_header);
                UserInfo user = (UserInfo) item;
                helper.setText(R.id.user_name, user.real_name);
                TextView positionView = helper.getView(R.id.user_position);
                if (!TextUtils.isEmpty(user.position)) {
                    positionView.setVisibility(View.VISIBLE);
                    positionView.setText(user.position);
                } else {
                    positionView.setVisibility(View.GONE);
                }
                GlideApp.with(mContext)
                        .load(user.head)
                        .dontAnimate()
                        .placeholder(R.drawable.ic_user_default_head)
                        .into(header);
                break;
            case 2:
                header = helper.getView(R.id.department_logo);
                DepartmentInfo department = (DepartmentInfo) item;
                helper.setText(R.id.department_name, department.org_name);
                GlideApp.with(mContext)
                        .load(department.org_logo)
                        .dontAnimate()
                        .placeholder(R.drawable.ic_school_logo)
                        .into(header);
                break;
        }
    }
}
