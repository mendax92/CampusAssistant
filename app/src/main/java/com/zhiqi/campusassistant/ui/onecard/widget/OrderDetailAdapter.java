package com.zhiqi.campusassistant.ui.onecard.widget;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.core.onecard.entity.CardOrderDetail;

/**
 * Created by minh on 18-2-2.
 * 校园卡订单列表adapter
 */

public class OrderDetailAdapter extends BaseQuickAdapter<CardOrderDetail> {

    public OrderDetailAdapter() {
        super(R.layout.item_one_card_order_detail, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, CardOrderDetail item, int position) {
        helper.setText(R.id.time, item.payment_time);
        helper.setText(R.id.remark, item.comment);
        helper.setText(R.id.balance, mContext.getString(R.string.one_card_balance_rmb, item.balance));
        helper.setText(R.id.pay_amount, item.topup_amount);
    }
}
