package com.zhiqi.campusassistant.ui.selfpay.widget;

import com.ming.base.util.NumberUtil;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.utils.NumberFormatUtil;
import com.zhiqi.campusassistant.core.payment.entity.StudentExpense;

/**
 * Created by ming on 17-8-1.
 * 学生支付列表adapter
 */

public class StudentExpenseAdapter extends BaseQuickAdapter<StudentExpense> {

    public StudentExpenseAdapter() {
        super(R.layout.item_pay_student_expense, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, StudentExpense item, int position) {
        helper.setText(R.id.pay_title, item.expense_name);
        helper.setText(R.id.pay_money, NumberUtil.formatWhole(item.amount, 2));
    }
}
