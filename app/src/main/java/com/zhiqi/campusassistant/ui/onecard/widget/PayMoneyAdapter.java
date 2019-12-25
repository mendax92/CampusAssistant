package com.zhiqi.campusassistant.ui.onecard.widget;

import android.view.View;

import com.ming.base.util.NumberUtil;
import com.ming.base.widget.SelectMutilView;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.core.onecard.entity.TopUpAccount;

import java.util.List;

/**
 * Created by ming on 2016/10/12.
 * 显示支付金额列表
 */

public class PayMoneyAdapter extends BaseQuickAdapter<TopUpAccount> {

    private int checkedPosition = -1;

    private OnCheckListener onCheckListener;

    public PayMoneyAdapter() {
        super(R.layout.item_pay_money, null);
    }

    public void setData(List<TopUpAccount> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setChecked(int position) {
        if (checkedPosition == position) {
            return;
        }
        int oldPosition = checkedPosition;
        this.checkedPosition = position;
        if (oldPosition >= 0) {
            notifyItemChanged(oldPosition);
        }
        if (checkedPosition >= 0) {
            notifyItemChanged(checkedPosition);
        } else {
            notifyDataSetChanged();
        }
    }

    public int getCheckedPosition() {
        return checkedPosition;
    }

    @Override
    protected void convert(BaseViewHolder helper, TopUpAccount item, int position) {
        helper.setText(R.id.pay_money, mContext.getString(R.string.one_card_money, NumberUtil.format(item.amount, 2)));
        SelectMutilView rootView = (SelectMutilView) helper.getConvertView();
        rootView.setTag(position);
        if (checkedPosition == position) {
            rootView.setSelected(true);
        } else {
            rootView.setSelected(false);
        }
        View disableView = helper.getView(R.id.pay_disable);
        if (item.is_limited) {
            disableView.setVisibility(View.VISIBLE);
            rootView.setSelectEnable(false);
        } else {
            disableView.setVisibility(View.GONE);
            rootView.setSelectEnable(true);
        }
        rootView.setOnSelectedChangeListener(selectedChangeListener);
        rootView.setOnClickListener(itemClickListener);
    }

    private SelectMutilView.OnSelectedChangeListener selectedChangeListener = new SelectMutilView.OnSelectedChangeListener() {
        @Override
        public void onSelectedChanged(SelectMutilView buttonView, boolean isSelected) {
            if (isSelected) {
                int position = (int) buttonView.getTag();
                TopUpAccount item = getItem(position);
                if (item == null || item.is_limited) {
                    return;
                }
                if (checkedPosition != position) {
                    int oldPosition = checkedPosition;
                    checkedPosition = position;
                    if (oldPosition >= 0) {
                        notifyItemChanged(oldPosition);
                    }
                }
                if (onCheckListener != null) {
                    onCheckListener.onChecked(position);
                }
            }
        }
    };

    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            TopUpAccount item = getItem(position);
            if (item == null || !item.is_limited) {
                return;
            }
            if (onCheckListener != null) {
                onCheckListener.onClickLimited(position);
            }
        }
    };

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public interface OnCheckListener {
        void onChecked(int position);

        void onClickLimited(int position);
    }
}
