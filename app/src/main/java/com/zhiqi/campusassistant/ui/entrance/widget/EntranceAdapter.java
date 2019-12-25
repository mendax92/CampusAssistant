package com.zhiqi.campusassistant.ui.entrance.widget;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.core.entrance.entity.EntranceInfo;

/**
 * Created by ming on 17-8-17.
 * 报到指南列表
 */

public class EntranceAdapter extends BaseQuickAdapter<EntranceInfo> {

    public EntranceAdapter() {
        super(R.layout.item_entrance, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntranceInfo item, int position) {
        if (item != null) {
            helper.setText(R.id.title, item.title);
            helper.setText(R.id.description, item.description);
        }
    }
}
