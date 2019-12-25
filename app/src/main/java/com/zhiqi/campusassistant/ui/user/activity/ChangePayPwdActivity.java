package com.zhiqi.campusassistant.ui.user.activity;

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
 * Created by minh on 18-2-26.
 * 修改支付密码
 */

public class ChangePayPwdActivity extends BaseToolbarActivity implements IRequestView {

    @BindView(R.id.old_pwd_input)
    EditText oldPwdEdit;

    @BindView(R.id.login_pwd_input)
    EditText newPwdEdit;

    @BindView(R.id.login_confirm_input)
    EditText confirmEdit;

    @BindView(R.id.commit)
    Button commit;

    @Inject
    SecurityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_pay_pwd);
        initDagger();
    }

    private void initDagger() {
        DaggerSecurityComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .securityModule(new SecurityModule())
                .build()
                .inject(this);
    }

    @OnTextChanged(value = {R.id.old_pwd_input, R.id.login_pwd_input, R.id.login_confirm_input})
    void onTextChanged() {
        if (oldPwdEdit.getText().length() < AppConstant.PAY_PASSWORD_LENGTH || newPwdEdit.getText().length() < AppConstant.PAY_PASSWORD_LENGTH ||
                confirmEdit.getText().length() < AppConstant.PAY_PASSWORD_LENGTH) {
            commit.setEnabled(false);
        } else {
            commit.setEnabled(true);
        }
    }

    @OnClick(R.id.commit)
    void onNext() {
        if (!newPwdEdit.getText().toString().equals(confirmEdit.getText().toString())) {
            ToastUtil.show(this, R.string.login_tip_pwd_not_same);
        } else if (newPwdEdit.getText().toString().equals(oldPwdEdit.getText().toString())) {
            ToastUtil.show(this, R.string.login_pwd_old_new_same);
        } else {
            ProgressDialogUtil.show(this, R.string.common_commit_ing);
            mPresenter.changePayPassword(oldPwdEdit.getText().toString(), newPwdEdit.getText().toString(), this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.release();
        }
        ProgressDialogUtil.dismiss();
        super.onDestroy();
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
}
