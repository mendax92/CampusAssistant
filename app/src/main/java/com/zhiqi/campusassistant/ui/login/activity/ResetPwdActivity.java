package com.zhiqi.campusassistant.ui.login.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.zhiqi.campusassistant.core.usercenter.dagger.component.DaggerSecurityComponent;
import com.zhiqi.campusassistant.core.usercenter.dagger.module.SecurityModule;
import com.zhiqi.campusassistant.core.usercenter.presenter.SecurityPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by ming on 2016/10/8.
 */

public class ResetPwdActivity extends BaseToolbarActivity implements IRequestView {

    @BindView(R.id.login_pwd_input)
    EditText newPwdEdit;

    @BindView(R.id.login_confirm_input)
    EditText confirmEdit;

    @BindView(R.id.reset_next)
    Button resetNext;

    @Inject
    SecurityPresenter mPresenter;

    private String resetToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
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
        resetToken = getIntent().getStringExtra(AppConstant.EXTRA_RESET_TOKEN);
    }

    @OnTextChanged(value = {R.id.login_pwd_input, R.id.login_confirm_input})
    void onTextChanged() {
        if (newPwdEdit.getText().length() < AppConstant.LOGIN_PASSWORD_MIN_LENGTH ||
                confirmEdit.getText().length() < AppConstant.LOGIN_PASSWORD_MIN_LENGTH) {
            resetNext.setEnabled(false);
        } else {
            resetNext.setEnabled(true);
        }
    }

    @OnClick(R.id.reset_next)
    void onNext() {
        if (!newPwdEdit.getText().toString().equals(confirmEdit.getText().toString())) {
            ToastUtil.show(this, R.string.login_tip_pwd_not_same);
        } else {
            ProgressDialogUtil.show(this, null);
            mPresenter.resetPassword(resetToken, newPwdEdit.getText().toString(), this);
        }
    }

    @Override
    public void onQuest(int errorCode, String message) {
        if (HttpErrorCode.SUCCESS == errorCode) {
            ProgressDialogUtil.success(message);
            startActivity(new Intent(this, LoginActivity.class));
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
