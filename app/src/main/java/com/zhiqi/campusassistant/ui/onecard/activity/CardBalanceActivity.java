package com.zhiqi.campusassistant.ui.onecard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseLoadActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.onecard.dagger.component.DaggerCardPayComponent;
import com.zhiqi.campusassistant.core.onecard.dagger.module.CardPayModule;
import com.zhiqi.campusassistant.core.onecard.entity.CardBalanceInfo;
import com.zhiqi.campusassistant.core.onecard.presenter.CardPayPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ming on 2018/1/21.
 * 余额
 */

public class CardBalanceActivity extends BaseLoadActivity<CardBalanceInfo> implements ILoadView<CardBalanceInfo> {

    @BindView(R.id.balance)
    TextView balanceTxt;

    @Inject
    CardPayPresenter mPresenter;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_one_card_balance;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        refresh();
    }

    private void initDagger() {
        DaggerCardPayComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .cardPayModule(new CardPayModule())
                .build()
                .inject(this);
    }

    @Override
    protected void onRefresh() {
        mPresenter.getBalance(this);
    }

    @Override
    public void onLoadData(CardBalanceInfo data) {
        super.onLoadData(data);
        if (data != null) {
            Intent intent = new Intent();
            intent.putExtra(AppConstant.EXTRA_CARD_BALANCE, data.balance);
            setResult(RESULT_OK, intent);
            balanceTxt.setText(getString(R.string.one_card_money, data.balance));
        }
    }

    @OnClick({R.id.pay_top_up_layout, R.id.order_detail_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_top_up_layout:
                startActivityForResult(new Intent(this, CardPaymentActivity.class), AppConstant.ACTIVITY_REQUEST_ONE_CARD_TOP_UP);
                break;
            case R.id.order_detail_layout:
                startActivity(new Intent(this, CardOrderListActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.ACTIVITY_REQUEST_ONE_CARD_TOP_UP == requestCode && RESULT_OK == resultCode) {
            refresh();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.release();
    }
}
