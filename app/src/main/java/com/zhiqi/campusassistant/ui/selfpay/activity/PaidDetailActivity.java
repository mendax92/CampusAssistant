package com.zhiqi.campusassistant.ui.selfpay.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ming.base.util.AppUtil;
import com.ming.base.util.NumberUtil;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseLoadActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.payment.dagger.component.DaggerSelfPayComponent;
import com.zhiqi.campusassistant.core.payment.dagger.module.SelfPayModule;
import com.zhiqi.campusassistant.core.payment.entity.OrderDetail;
import com.zhiqi.campusassistant.core.payment.entity.PayStatus;
import com.zhiqi.campusassistant.core.payment.presenter.SelfPayPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ming on 2017/8/13.
 * 支付详情页
 */

public class PaidDetailActivity extends BaseLoadActivity<OrderDetail> implements ILoadView<OrderDetail> {

    @Inject
    SelfPayPresenter mPresenter;
    @BindView(R.id.pay_trade)
    TextView payTrade;
    @BindView(R.id.pay_status)
    TextView payStatus;
    @BindView(R.id.pay_money)
    TextView payMoney;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.student_no)
    TextView studentNo;
    @BindView(R.id.user_grade_class)
    TextView userGradeClass;
    @BindView(R.id.pay_type)
    TextView payType;
    @BindView(R.id.order_no)
    TextView orderNo;
    @BindView(R.id.pay_time)
    TextView payTime;

    private long orderId;

    private OrderDetail detail;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_paid_detail;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        init();
    }

    private void initDagger() {
        DaggerSelfPayComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .selfPayModule(new SelfPayModule())
                .build()
                .inject(this);
    }

    private void init() {
        Intent intent = getIntent();
        orderId = intent.getLongExtra(AppConstant.EXTRA_PAY_ORDER_ID, -1);
        if (orderId <= 0) {
            finish();
            return;
        }
        refresh();
    }

    @Override
    protected void onRefresh() {
        mPresenter.getOrderDetail(orderId, this);
    }

    @Override
    public void onLoadData(OrderDetail data) {
        if (data != null) {
            this.detail = data;
            fillData();
        }
        super.onLoadData(data);
    }

    private void fillData() {
        payTrade.setText(detail.expense_name);
        if (detail.order_status != null) {
            payStatus.setText(detail.status_name);
            payMoney.setText(NumberUtil.formatWhole(detail.amount, 2));
            if (PayStatus.Success == detail.order_status) {
                ViewCompat.setBackground(payStatus, ContextCompat.getDrawable(this, R.drawable.pay_status_success_bg));
                payMoney.setTextColor(ContextCompat.getColor(this, R.color.green));
            } else {
                ViewCompat.setBackground(payStatus, ContextCompat.getDrawable(this, R.drawable.pay_status_fail_bg));
                payMoney.setTextColor(ContextCompat.getColor(this, R.color.red));
            }
        }
        userName.setText(detail.real_name);
        studentNo.setText(detail.student_no);
        userGradeClass.setText(detail.class_name);
        payType.setText(detail.payment_type_name);
        orderNo.setText(detail.order_no);
        payTime.setText(detail.payment_time);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.release();
        }
    }

    @OnClick(R.id.call)
    public void onClick() {
        if (detail != null && !TextUtils.isEmpty(detail.service_tell)) {
            requestPermissions(false, Manifest.permission.CALL_PHONE);
        }
    }

    private void call() {
        AppUtil.callTel(this, detail.service_tell);
    }

    @Override
    protected void onPermissionGranted(String[] permissions) {
        super.onPermissionGranted(permissions);
        call();
    }
}
