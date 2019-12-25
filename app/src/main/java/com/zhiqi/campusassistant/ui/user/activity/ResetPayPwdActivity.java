package com.zhiqi.campusassistant.ui.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.common.utils.ProgressDialogUtil;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.core.usercenter.dagger.component.DaggerSecurityComponent;
import com.zhiqi.campusassistant.core.usercenter.dagger.module.SecurityModule;
import com.zhiqi.campusassistant.core.usercenter.entity.VerifyFunction;
import com.zhiqi.campusassistant.core.usercenter.presenter.SecurityPresenter;
import com.zhiqi.campusassistant.ui.login.activity.VerifyCodeActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by ming on 2016/10/8.
 * 重置支付密码
 */

public class ResetPayPwdActivity extends BaseToolbarActivity implements IRequestView {

    @BindView(R.id.login_pwd_input)
    EditText newPwdEdit;

    @BindView(R.id.login_confirm_input)
    EditText confirmEdit;

    @BindView(R.id.commit)
    Button commitBtn;

    @Inject
    SecurityPresenter mPresenter;

    private String resetToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reset_pay_pwd);
        initDagger();
        initData();
    }

    private void initDagger() {
        DaggerSecurityComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .securityModule(new SecurityModule())
                .build()
                .inject(this);
    }

    private void initData() {
        Intent intent = new Intent(this, VerifyCodeActivity.class);
        intent.putExtra(AppConstant.EXTRA_PHONE, LoginManager.getInstance().getLoginUser().getPhone());
        intent.putExtra(AppConstant.EXTRA_BIND_PHONE_OPT, VerifyFunction.ResetPayPassword.getValue());
        startActivityForResult(intent, AppConstant.ACTIVITY_REQUEST_VERIFY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.ACTIVITY_REQUEST_VERIFY_CODE == requestCode) {
            if (RESULT_OK == resultCode && data != null) {
                resetToken = data.getStringExtra(AppConstant.EXTRA_RESET_TOKEN);
                if (!TextUtils.isEmpty(resetToken)) {
                    return;
                }
            }
            finish();
        }
    }

    @OnTextChanged(value = {R.id.login_pwd_input, R.id.login_confirm_input})
    void onTextChanged() {
        if (newPwdEdit.getText().length() < AppConstant.LOGIN_PASSWORD_MIN_LENGTH ||
                confirmEdit.getText().length() < AppConstant.LOGIN_PASSWORD_MIN_LENGTH) {
            commitBtn.setEnabled(false);
        } else {
            commitBtn.setEnabled(true);
        }
    }

    @OnClick(R.id.commit)
    void onNext() {
        if (!newPwdEdit.getText().toString().equals(confirmEdit.getText().toString())) {
            ToastUtil.show(this, R.string.login_tip_pwd_not_same);
        } else {
            ProgressDialogUtil.show(this, null);
            mPresenter.resetPayPassword(resetToken, newPwdEdit.getText().toString(), this);
        }
    }

    @Override
    public void onQuest(int errorCode, String message) {
        if (HttpErrorCode.SUCCESS == errorCode) {
            ProgressDialogUtil.success(message);
            finish();
        } else {
            ProgressDialogUtil.error(message);
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.release();
        ProgressDialogUtil.dismiss();
        super.onDestroy();
    }
}
