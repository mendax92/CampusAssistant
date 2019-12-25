package com.zhiqi.campusassistant.ui.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.core.usercenter.entity.VerifyFunction;
import com.zhiqi.campusassistant.ui.login.activity.BindPhoneActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ming on 2017/3/19.
 * 更换手机号码
 */

public class ChangePhoneActivity extends BaseToolbarActivity {

    @BindView(R.id.phone_number)
    TextView phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        initView();
    }

    private void initView() {
        LoginUser user = LoginManager.getInstance().getLoginUser();
        if (user != null) {
            phoneNumber.setText(getString(R.string.user_change_phone_info, user.getPhone()));
        }
    }

    @OnClick(R.id.commit)
    void onClick(View view) {
        Intent intent = new Intent(this, BindPhoneActivity.class);
        intent.putExtra(AppConstant.EXTRA_BIND_PHONE_OPT, VerifyFunction.ChangePhone.getValue());
        startActivityForResult(intent, AppConstant.ACTIVITY_REQUEST_VERIFY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.ACTIVITY_REQUEST_VERIFY_CODE == requestCode && RESULT_OK == resultCode) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
