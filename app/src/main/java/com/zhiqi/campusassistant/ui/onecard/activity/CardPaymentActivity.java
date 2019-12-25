package com.zhiqi.campusassistant.ui.onecard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ming.base.util.NumberUtil;
import com.ming.base.widget.recyclerView.animation.NoAlphaItemAnimator;
import com.ming.base.widget.recyclerView.decoration.GridOffsetsItemDecoration;
import com.ming.pay.PayServiceFactory;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseLoadActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.widget.PayCheckGroupView;
import com.zhiqi.campusassistant.common.utils.NumberFormatUtil;
import com.zhiqi.campusassistant.common.utils.ProgressDialogUtil;
import com.zhiqi.campusassistant.common.utils.PromptDialogUtil;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.onecard.dagger.component.DaggerCardPayComponent;
import com.zhiqi.campusassistant.core.onecard.dagger.module.CardPayModule;
import com.zhiqi.campusassistant.core.onecard.entity.CardPayRequest;
import com.zhiqi.campusassistant.core.onecard.entity.CardTopUpInfo;
import com.zhiqi.campusassistant.core.onecard.entity.TopUpAccount;
import com.zhiqi.campusassistant.core.onecard.presenter.CardPayPresenter;
import com.zhiqi.campusassistant.core.payment.entity.OrderResult;
import com.zhiqi.campusassistant.core.payment.entity.PayType;
import com.zhiqi.campusassistant.core.payment.entity.PayTypeInfo;
import com.zhiqi.campusassistant.ui.onecard.widget.PayMoneyAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ming on 2016/10/11.
 * 支付
 */

public class CardPaymentActivity extends BaseLoadActivity<CardTopUpInfo> implements ILoadView<CardTopUpInfo> {

    @BindView(R.id.balance)
    TextView balanceText;
    @BindView(R.id.pay_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.pay_confirm)
    Button payConfirm;
    @BindView(R.id.pay_check_group)
    PayCheckGroupView payGroup;
    @BindView(R.id.pay_rate_tip)
    TextView rateTip;
    @BindView(R.id.pay_rate_rmb)
    TextView rateMoney;

    @Inject
    CardPayPresenter mPresenter;

    private PayMoneyAdapter mAdapter;

    private CardTopUpInfo topUpInfo;

    private boolean limit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_one_card_payment;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        initView();
        refresh();
    }


    @Override
    protected void onRefresh() {
        if (mPresenter != null) {
            mPresenter.loadTopUpInfo(this);
        }
    }

    private void initDagger() {
        DaggerCardPayComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .cardPayModule(new CardPayModule())
                .build()
                .inject(this);
    }

    private void initView() {
        mAdapter = new PayMoneyAdapter();
        mAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new NoAlphaItemAnimator());
        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);

        GridOffsetsItemDecoration offsetsItemDecoration = new GridOffsetsItemDecoration(this, R.dimen.common_margin_normal, R.dimen.common_margin_normal);

        mRecyclerView.addItemDecoration(offsetsItemDecoration);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter.setOnCheckListener(new PayMoneyAdapter.OnCheckListener() {
            @Override
            public void onChecked(int position) {
                TopUpAccount item = mAdapter.getItem(position);
                if (item == null || item.is_limited) {
                    return;
                }
                int payPosition = payGroup.getCheckedPosition();
                if (payPosition >= 0) {
                    PayTypeInfo payTypeInfo = getPayTypeInfo(getPayType(payPosition));
                    if (payTypeInfo != null) {
                        checkMoney(item.amount, payTypeInfo);
                    }
                }
            }

            @Override
            public void onClickLimited(int position) {
                TopUpAccount item = mAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                if (limit) {
                    PromptDialogUtil.warn(CardPaymentActivity.this, getString(R.string.one_card_pay_limit_tip, NumberUtil.format(topUpInfo.max_amount, 2)));
                }
            }
        });
        payGroup.setOnCheckedChangeListener(checkedChangeListener);
    }

    private PayCheckGroupView.OnCheckedChangeListener checkedChangeListener = new PayCheckGroupView.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(int index, boolean checked) {
            int checkedPosition = mAdapter.getCheckedPosition();
            PayTypeInfo payTypeInfo = null;
            if (getPayType(index) != null && checked) {
                payTypeInfo = getPayTypeInfo(PayType.WECHAT);
            }
            if (payTypeInfo == null) {
                return;
            }
            checkMoney(checkedPosition >= 0 ? mAdapter.getItem(checkedPosition).amount : 0, payTypeInfo);
        }
    };

    private void checkMoney(float checkMoney, PayTypeInfo payTypeInfo) {
        float money = 0;
        if (checkMoney > 0) {
            money = getPayMoney(checkMoney, payTypeInfo.service_rate);
            payConfirm.setText(getString(R.string.pay_confirm_whole, NumberFormatUtil.formatRMB(money)));
            payConfirm.setEnabled(true);
        } else {
            payConfirm.setText(R.string.pay_confirm_simple);
            payConfirm.setEnabled(false);
        }
        String moneyTxt = money == 0 ? "0" : NumberFormatUtil.formatRMB(money - checkMoney);
        rateMoney.setText(moneyTxt);
        rateTip.setText(getString(R.string.one_card_payment_rate_tip, NumberUtil.format(getRatePercent(payTypeInfo.service_rate))));
    }

    private PayType getPayType(int payPosition) {
        PayType payType = null;
        switch (payPosition) {
            case PayCheckGroupView.CHECK_ALI_PAY:
                payType = PayType.ALIPAY;
                break;
            case PayCheckGroupView.CHECK_WECHAT:
                payType = PayType.WECHAT;
                break;
        }
        return payType;
    }

    private float getRatePercent(float rate) {
        return rate * 100;
    }

    private PayTypeInfo getPayTypeInfo(PayType payType) {
        if (topUpInfo == null) {
            return null;
        }
        for (PayTypeInfo info : topUpInfo.support_payment_type) {
            if (payType == info.payment_type) {
                return info;
            }
        }
        return null;
    }

    private float getPayMoney(float money, float rate) {
        if (rate > 0) {
            return money + money * rate;
        }
        return money;
    }

    @Override
    public void onLoadData(CardTopUpInfo data) {
        if (data != null) {
            topUpInfo = data;
            balanceText.setText(getString(R.string.one_card_money, topUpInfo.balance));
            if (topUpInfo.topup_amounts != null) {
                limit = true;
                for (TopUpAccount account : topUpInfo.topup_amounts) {
                    limit = account.is_limited;
                    if (!limit) {
                        break;
                    }
                }
                mAdapter.setData(topUpInfo.topup_amounts);
            }
            rateMoney.setText("0");
            rateTip.setText(getString(R.string.one_card_payment_rate_tip, "0"));

            List<PayTypeInfo> payTypeInfos = topUpInfo.support_payment_type;
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
        }
        super.onLoadData(data);
    }

    @OnClick(R.id.pay_confirm)
    void onClick() {
        int checkPosition = mAdapter.getCheckedPosition();
        int payPosition = payGroup.getCheckedPosition();
        if (checkPosition >= 0 && payPosition >= 0) {
            TopUpAccount item = mAdapter.getItem(checkPosition);
            PayTypeInfo payTypeInfo = null;
            if (getPayType(payPosition) != null) {
                payTypeInfo = getPayTypeInfo(PayType.WECHAT);
            }
            if (payTypeInfo == null) {
                return;
            }
            float money = getPayMoney(item.amount, payTypeInfo.service_rate);
            CardPayRequest request = new CardPayRequest();
            request.service_fee = NumberUtil.formatWhole(money - item.amount, 2);
            request.topup_amount = NumberUtil.formatWhole(item.amount, 2);
            if (mPresenter.topUpCard(request, payTypeInfo.payment_type, payResult)) {
                ProgressDialogUtil.show(this, R.string.pay_ing);
            } else {
                ToastUtil.show(this, R.string.pay_do_not_support);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PayServiceFactory.checkResult();
    }

    /**
     * 支付结果回调
     */
    private ILoadView<OrderResult> payResult = new ILoadView<OrderResult>() {
        @Override
        public void onLoadData(OrderResult data) {
            ProgressDialogUtil.success(data.description);
            setResult(RESULT_OK);
            Intent intent = new Intent(CardPaymentActivity.this, CardOrderDetailActivity.class);
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
    protected void onDestroy() {
        super.onDestroy();
        ProgressDialogUtil.dismiss();
        if (mPresenter != null) {
            mPresenter.release();
        }
    }
}
