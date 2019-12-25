package com.zhiqi.campusassistant.ui.onecard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
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
 * 校园卡主界面
 */

public class CampusCardActivity extends BaseToolbarActivity implements ILoadView<CardBalanceInfo>{

    @BindView(R.id.account_balance)
    TextView balanceTxt;
    @Inject
    CardPayPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_card_campus);
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

    private void refresh() {
        mPresenter.getBalance(this);
    }

    @OnClick(value = {R.id.one_card_pay_qr, R.id.one_card_balance})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.one_card_pay_qr:
                startActivityForResult(new Intent(this, CardQrCodeActivity.class), AppConstant.ACTIVITY_REQUEST_ONE_CARD_TOP_UP);
                break;
            case R.id.one_card_balance:
                startActivityForResult(new Intent(this, CardBalanceActivity.class), AppConstant.ACTIVITY_REQUEST_ONE_CARD_BALANCE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.ACTIVITY_REQUEST_ONE_CARD_BALANCE == requestCode && RESULT_OK == resultCode) {
            String balance = data.getStringExtra(AppConstant.EXTRA_CARD_BALANCE);
            if (!TextUtils.isEmpty(balance)) {
                balanceTxt.setText(balance);
            } else {
                refresh();
            }
        }
    }

    @Override
    public void onLoadData(CardBalanceInfo data) {
        if (data != null) {
            balanceTxt.setText(data.balance);
        }
    }

    @Override
    public void onFailed(int errorCode, String message) {
    }
}
