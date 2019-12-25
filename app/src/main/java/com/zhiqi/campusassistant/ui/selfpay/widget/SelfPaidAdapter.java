package com.zhiqi.campusassistant.ui.selfpay.widget;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.ming.base.util.NumberUtil;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.core.payment.entity.PayStatus;
import com.zhiqi.campusassistant.core.payment.entity.SelfPaidInfo;

/**
 * Created by ming on 2017/8/6.
 * 自助支付
 */

public class SelfPaidAdapter extends BaseQuickAdapter<SelfPaidInfo> {

    public SelfPaidAdapter() {
        super(R.layout.item_self_paid, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelfPaidInfo item, int position) {
        if (item != null) {
            helper.setText(R.id.pay_time, item.payment_time);
            helper.setText(R.id.pay_trade, item.expense_name);
            helper.setText(R.id.pay_money, NumberUtil.formatWhole(item.amount, 2));
            helper.setText(R.id.pay_status, item.status_name);
            TextView operaView = helper.getView(R.id.pay_opera);
            if (PayStatus.Unpaid == item.order_status) {
                operaView.setText(R.string.pay_to_pay);
                operaView.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            } else {
                operaView.setText(R.string.pay_detail);
                operaView.setTextColor(ContextCompat.getColor(mContext, R.color.text_link_color));
            }
        }
    }
}
