package com.zhiqi.campusassistant.ui.onecard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseLoadActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.onecard.dagger.component.DaggerCardPayComponent;
import com.zhiqi.campusassistant.core.onecard.dagger.module.CardPayModule;
import com.zhiqi.campusassistant.core.onecard.entity.CardOrderDetail;
import com.zhiqi.campusassistant.core.onecard.presenter.CardPayPresenter;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ming on 2018/1/28.
 * 一卡通支付详情页
 */

public class CardOrderDetailActivity extends BaseLoadActivity<CardOrderDetail> implements ILoadView<CardOrderDetail> {

    @BindView(R.id.time)
    AppCompatTextView time;
    @BindView(R.id.pay_amount)
    AppCompatTextView payAmount;
    @BindView(R.id.type_name)
    AppCompatTextView typeName;
    @BindView(R.id.order_no)
    AppCompatTextView orderNoTxt;
    @BindView(R.id.balance)
    AppCompatTextView accountBalance;
    @BindView(R.id.remark)
    AppCompatTextView remark;

    @Inject
    CardPayPresenter mPresenter;

    private long orderId;
    private String orderNo;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_one_card_pay_detail;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        init();
        refresh();
    }

    private void initDagger() {
        DaggerCardPayComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .cardPayModule(new CardPayModule())
                .build()
                .inject(this);
    }

    private void init() {
        Intent intent = getIntent();
        if (intent != null) {
            orderId = intent.getLongExtra(AppConstant.EXTRA_PAY_ORDER_ID, 0);
            orderNo = intent.getStringExtra(AppConstant.EXTRA_PAY_ORDER_NO);
        }
        if (orderId <= 0 && TextUtils.isEmpty(orderNo)) {
            finish();
        }
    }

    @Override
    protected void onRefresh() {
        mPresenter.getOrderDetail(orderId, orderNo, this);
    }

    @Override
    public void onLoadData(CardOrderDetail data) {
        super.onLoadData(data);
        if (data != null) {
            time.setText(data.payment_time);
            payAmount.setText(data.topup_amount);
            typeName.setText(data.type_name);
            orderNoTxt.setText(data.order_no);
            accountBalance.setText(data.balance);
            remark.setText(data.comment);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.release();
        }
    }
}
