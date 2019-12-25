package com.zhiqi.campusassistant.ui.main.widget;

import android.widget.ImageView;

import com.ming.base.widget.BadgeView;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.core.app.entity.AppInfo;

/**
 * Created by Edmin on 2016/10/4
 * 应用列表adapter
 */

public class AppAdapter extends BaseQuickAdapter<AppInfo> {

    public AppAdapter() {
        super(R.layout.item_app_info, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppInfo item, int position) {
        helper.setText(R.id.app_text, item.module_name);
        helper.setVisible(R.id.coming_soon, !item.is_release);
        GlideApp.with(mContext).load(item.module_icon).into((ImageView) (helper.getView(R.id.app_icon)));
        BadgeView tipView = helper.getView(R.id.app_msg_tip);
        if (item.badge <= 0) {
            tipView.hide();
        } else {
            tipView.setText(item.badge <= 99 ? String.valueOf(item.badge) : "99+");
            tipView.show();
        }
    }
}
