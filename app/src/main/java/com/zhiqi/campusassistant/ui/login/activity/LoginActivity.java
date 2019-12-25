package com.zhiqi.campusassistant.ui.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ming.base.util.StatusBarUtil;
import com.ming.base.util.StringUtil;
import com.ming.base.util.URLEncodedUtils;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.app.dagger.component.ApplicationComponent;
import com.zhiqi.campusassistant.common.ui.activity.BaseFilterActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.utils.AppPreference;
import com.zhiqi.campusassistant.common.utils.ProgressDialogUtil;
import com.zhiqi.campusassistant.common.utils.SchemeUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.login.dagger.component.DaggerLoginComponent;
import com.zhiqi.campusassistant.core.login.dagger.module.LoginPresenterModule;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;
import com.zhiqi.campusassistant.core.login.presenter.LoginPresenter;
import com.zhiqi.campusassistant.core.usercenter.entity.VerifyFunction;
import com.zhiqi.campusassistant.ui.main.activity.HomeActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by ming on 2016/11/22.
 * 登录界面
 */

public class LoginActivity extends BaseFilterActivity implements ILoadView<LoginUser> {

    @BindView(R.id.login_rule_tip)
    TextView ruleTip;

    @BindView(R.id.login_account)
    EditText accountEdit;

    @BindView(R.id.login_pwd_input)
    EditText pwdEdit;

    @BindView(R.id.login_btn)
    Button loginBtn;

    @Inject
    LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 1);
        initDagger();
        init();
    }

    private void initDagger() {
        ApplicationComponent appComponent = AssistantApplication.getInstance().getApplicationComponent();
        DaggerLoginComponent.builder()
                .applicationComponent(appComponent)
                .loginPresenterModule(new LoginPresenterModule())
                .build().inject(this);
    }

    private void init() {
        String schemeUrl = SchemeUtil.getSchemeUrl(this, AppConstant.HOST_BROWSER);
        String privacyPolicy = schemeUrl + "?" + AppConstant.EXTRA_URL + "=" + URLEncodedUtils.encode(HttpUrlConstant.BROWSER_PRIVACY_POLICY);
        String termsOfService = schemeUrl + "?" + AppConstant.EXTRA_URL + "=" + URLEncodedUtils.encode(HttpUrlConstant.BROWSER_TERMS_OF_SERVICE);
        Spanned html = StringUtil.fromHtml(getResources().getString(R.string.login_rule_tip, privacyPolicy, termsOfService));
        ruleTip.setText(html);
        ruleTip.setMovementMethod(LinkMovementMethod.getInstance());
        accountEdit.setText(AppPreference.getInstance(this).getLoginAccount());
    }

    @OnTextChanged(value = {R.id.login_account, R.id.login_pwd_input})
    void onTextChanged() {
        if (accountEdit.getText().length() < AppConstant.LOGIN_ACCOUNT_MIN_LENGTH ||
                pwdEdit.getText().length() < AppConstant.LOGIN_PASSWORD_MIN_LENGTH) {
            loginBtn.setEnabled(false);
        } else {
            loginBtn.setEnabled(true);
        }
    }

    @OnClick({R.id.login_btn, R.id.login_forget_pwd, R.id.login_close})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_forget_pwd:
                Intent intent = new Intent(this, BindPhoneActivity.class);
                intent.putExtra(AppConstant.EXTRA_BIND_PHONE_OPT, VerifyFunction.ForgetPassword.getValue());
                startActivity(intent);
                break;
            case R.id.login_close:
                finish();
                break;
            case R.id.login_btn:
                ProgressDialogUtil.show(this, getString(R.string.login_ing));
                mPresenter.login(accountEdit.getText().toString(), pwdEdit.getText().toString(), this);
                break;
        }
    }

    @Override
    public void onLoadData(LoginUser data) {
        ProgressDialogUtil.success(getString(R.string.login_success));
        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    public void onFailed(int errorCode, String message) {
        ProgressDialogUtil.error(message);
    }

    @Override
    protected void onDestroy() {
        ProgressDialogUtil.dismiss();
        mPresenter.release();
        super.onDestroy();
    }
}
