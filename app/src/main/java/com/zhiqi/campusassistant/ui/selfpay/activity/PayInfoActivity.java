package com.zhiqi.campusassistant.ui.selfpay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ming.base.util.NumberUtil;
import com.ming.pay.PayServiceFactory;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.common.ui.activity.BaseLoadActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.widget.PayCheckGroupView;
import com.zhiqi.campusassistant.common.utils.NumberFormatUtil;
import com.zhiqi.campusassistant.common.utils.ProgressDialogUtil;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;
import com.zhiqi.campusassistant.core.payment.dagger.component.DaggerSelfPayComponent;
import com.zhiqi.campusassistant.core.payment.dagger.module.SelfPayModule;
import com.zhiqi.campusassistant.core.payment.entity.ExpenseInfo;
import com.zhiqi.campusassistant.core.payment.entity.OrderResult;
import com.zhiqi.campusassistant.core.payment.entity.PayType;
import com.zhiqi.campusassistant.core.payment.entity.PayTypeInfo;
import com.zhiqi.campusassistant.core.payment.presenter.SelfPayPresenter;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ming on 17-8-2.
 * 支付页面Activity
 */

public class PayInfoActivity extends BaseLoadActivity<ExpenseInfo> implements ILoadView<ExpenseInfo> {

    @BindView(R.id.user_header)
    ImageView userHeader;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.class_name)
    TextView classNameTxt;
    @BindView(R.id.user_number_text)
    TextView userNumber;

    @BindView(R.id.pay_title)
    TextView payTitle;
    @BindView(R.id.pay_money)
    TextView payMoney;

    @BindView(R.id.pay_check_group)
    PayCheckGroupView payGroup;

    @BindView(R.id.pay_confirm)
    Button payConfirm;

    @Inject
    SelfPayPresenter mPresenter;

    private long expenseId;

    private ExpenseInfo expenseInfo;

    private PayType payType;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_pay_info;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        initView();
        initData();
    }

    private void initDagger() {
        DaggerSelfPayComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .selfPayModule(new SelfPayModule())
                .build()
                .inject(this);
    }

    private void initView() {
        LoginUser user = LoginManager.getInstance().getLoginUser();
        if (user != null) {
            GlideApp.with(this).load(user.getHead()).placeholder(R.drawable.ic_user_default_head).into(userHeader);
            userName.setText(user.getReal_name());
            userNumber.setText(user.getUser_no());
            classNameTxt.setText(user.getPosition());
        }
        payGroup.setOnCheckedChangeListener(checkedChangeListener);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            expenseId = intent.getLongExtra(AppConstant.EXTRA_PAY_EXPENSE_ID, 0);
        }
        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PayServiceFactory.checkResult();
    }

    @Override
    public void onLoadData(ExpenseInfo data) {
        super.onLoadData(data);
        this.expenseInfo = data;
        if (expenseInfo == null) {
            return;
        }
        payMoney.setText(NumberUtil.formatWhole(expenseInfo.amount, 2));
        payTitle.setText(expenseInfo.expense_name);
        payConfirm.setText(getString(R.string.pay_confirm_whole, NumberFormatUtil.formatRMB(expenseInfo.amount)));
        List<PayTypeInfo> payTypeInfos = expenseInfo.support_payment_type;

        for (int i = 0; i < payTypeInfos.size(); i++) {
            PayTypeInfo payInfo = payTypeInfos.get(i);
            int checkPos = -1;
            switch (payInfo.payment_type) {
                case WECHAT:
                    checkPos = PayCheckGroupView.CHECK_WECHAT;
                    break;
                case ALIPAY:
                    checkPos = PayCheckGroupView.CHECK_ALI_PAY;
                    break;
            }
            if (checkPos >= 0) {
                payGroup.setVisible(checkPos, true);
                if (i == 0) {
                    payGroup.setChecked(checkPos);
                }
            }
        }
        int checkPos = payGroup.getCheckedPosition();
        payConfirm.setEnabled(checkPos >= 0);
        checked(checkPos);
    }

    private PayCheckGroupView.OnCheckedChangeListener checkedChangeListener = new PayCheckGroupView.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(int index, boolean checked) {
            if (checked) {
                checked(index);
            }
        }
    };

    public void checked(int index) {
        if (expenseInfo == null) {
            return;
        }
        switch (index) {
            case PayCheckGroupView.CHECK_ALI_PAY:
                payType = PayType.ALIPAY;
                break;
            case PayCheckGroupView.CHECK_WECHAT:
                payType = PayType.WECHAT;
                break;
        }
    }

    @OnClick(R.id.pay_confirm)
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_confirm:
                if (mPresenter.pay(expenseInfo.expense_id, payType, payResult)) {
                    ProgressDialogUtil.show(this, R.string.pay_ing);
                } else {
                    ToastUtil.show(this, R.string.pay_do_not_support);
                }
                break;
        }
    }

    /**
     * 支付结果回调
     */
    private ILoadView<OrderResult> payResult = new ILoadView<OrderResult>() {
        @Override
        public void onLoadData(OrderResult data) {
            ProgressDialogUtil.success(data.description);
            setResult(RESULT_OK);
            Intent intent = new Intent(PayInfoActivity.this, PaidDetailActivity.class);
            intent.putExtra(AppConstant.EXTRA_PAY_ORDER_ID, data.order_id);
            startActivity(intent);
            finish();
        }

        @Override
        public void onFailed(int errorCode, String message) {
            ProgressDialogUtil.error(message);
        }
    };

    @Override
    protected void onRefresh() {
        mPresenter.getPayInfo(expenseId, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.release();
        }
        ProgressDialogUtil.dismiss();
    }
}
