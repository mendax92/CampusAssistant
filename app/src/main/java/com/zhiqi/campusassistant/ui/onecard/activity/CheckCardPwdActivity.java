package com.zhiqi.campusassistant.ui.onecard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.common.ui.widget.AppToast;
import com.zhiqi.campusassistant.common.ui.widget.CodeView;
import com.zhiqi.campusassistant.common.ui.widget.NumberKeyboardView;
import com.zhiqi.campusassistant.common.utils.ProgressDialogUtil;
import com.zhiqi.campusassistant.common.utils.PromptDialogUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.onecard.dagger.component.DaggerCardPayComponent;
import com.zhiqi.campusassistant.core.onecard.dagger.module.CardPayModule;
import com.zhiqi.campusassistant.core.onecard.presenter.CardPayPresenter;
import com.zhiqi.campusassistant.ui.user.activity.AccountSafeActivity;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by minh on 18-2-5.
 * 校园卡支付密码验证
 */

public class CheckCardPwdActivity extends BaseToolbarActivity {

    @BindView(R.id.pwd_tip)
    TextView pwdTipTxt;

    @BindView(R.id.keyboard)
    NumberKeyboardView keyboardView;

    @BindView(R.id.pwd_code)
    CodeView pwdCodeView;

    @Inject
    CardPayPresenter mPresenter;

    private boolean hasPayPassword = false;

    private String password;

    private Toast mToast;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_card_check_pwd);
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        initData();
        initView();
    }

    private void initDagger() {
        DaggerCardPayComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .cardPayModule(new CardPayModule())
                .build()
                .inject(this);
    }

    private void initView() {
        keyboardView.setOnKeyboardListener(new NumberKeyboardView.OnKeyboardListener() {
            @Override
            public void onInsertKeyEvent(String text) {
                pwdCodeView.append(text);
            }

            @Override
            public void onDeleteKeyEvent() {
                pwdCodeView.delete();
            }
        });
        pwdCodeView.setValueChangeListener(valueChangeListener);
        pwdTipTxt.setText(hasPayPassword ? R.string.one_card_verify_pwd_tip : R.string.one_card_open_pwd_tip);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            hasPayPassword = intent.getBooleanExtra(AppConstant.EXTRA_CARD_HAS_PAY_PASSWORD, false);
        }
    }

    private CodeView.ValueChangeListener valueChangeListener = new CodeView.ValueChangeListener() {
        @Override
        public void onValueChanged(String value) {
        }

        @Override
        public void onComplete(String value) {
            if (hasPayPassword) {
                checkPassword(value);
            } else {
                confirmPassword(value);
            }
        }
    };

    private void checkPassword(String newPwd) {
        ProgressDialogUtil.show(this, R.string.one_card_verify_pwd_ing);
        password = newPwd;
        mPresenter.openPayment(true, password, requestView);
    }

    private void confirmPassword(String newPwd) {
        if (password == null) {
            password = newPwd;
            pwdTipTxt.setText(R.string.one_card_open_pwd_confirm_tip);
            pwdCodeView.clear();
        } else {
            if (!password.equals(newPwd)) {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = AppToast.showCenterToast(CheckCardPwdActivity.this, R.drawable.ic_tip_error, R.string.one_card_confirm_pwd_error, Toast.LENGTH_SHORT);
                pwdCodeView.clear();
            } else {
                checkPassword(newPwd);
            }
        }
    }

    private IRequestView requestView = new IRequestView() {
        @Override
        public void onQuest(int errorCode, String message) {
            if (HttpErrorCode.SUCCESS == errorCode) {
                ProgressDialogUtil.success(message);
                setResult(RESULT_OK);
                finish();
            } else {
                if (HttpErrorCode.ERROR_PAYMENT_PASSWORD == errorCode) {
                    PromptDialogUtil.warn(CheckCardPwdActivity.this, message, getString(R.string.login_forget_pwd), new MaterialDialog.SingleButtonCallback() {

                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent intent = new Intent(CheckCardPwdActivity.this, AccountSafeActivity.class);
                            startActivity(intent);
                        }
                    });
                    ProgressDialogUtil.dismiss();
                } else {
                    ProgressDialogUtil.error(message);
                }
                pwdCodeView.clear();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.release();
        }
        ProgressDialogUtil.dismiss();
    }
}
