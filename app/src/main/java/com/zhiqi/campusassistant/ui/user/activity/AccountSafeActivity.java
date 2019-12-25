package com.zhiqi.campusassistant.ui.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.ui.login.activity.ResetPwdActivity;
import com.zhiqi.campusassistant.ui.login.util.LoginHelper;
import com.zhiqi.campusassistant.ui.onecard.activity.CampusCardActivity;

import butterknife.OnClick;

/**
 * Created by ming on 2017/3/19.
 * 账号安全
 */

public class AccountSafeActivity extends BaseToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safe);
    }

    @OnClick({R.id.change_pwd, R.id.change_pay_pwd, R.id.reset_pay_pwd})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_pwd:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
            case R.id.change_pay_pwd:
                startActivity(new Intent(this, ChangePayPwdActivity.class));
                break;
            case R.id.reset_pay_pwd:
                LoginHelper.checkBindPhone(this, R.string.one_card_bind_phone_tip, new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(AccountSafeActivity.this, ResetPayPwdActivity.class));
                    }
                });
                break;
        }
    }
}
