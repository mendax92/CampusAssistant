package com.zhiqi.campusassistant.ui.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ming.base.util.RxUtil;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.utils.ProgressDialogUtil;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.core.usercenter.dagger.component.DaggerSecurityComponent;
import com.zhiqi.campusassistant.core.usercenter.dagger.module.SecurityModule;
import com.zhiqi.campusassistant.core.usercenter.entity.AreaInfo;
import com.zhiqi.campusassistant.core.usercenter.entity.VerifyFunction;
import com.zhiqi.campusassistant.core.usercenter.entity.VerifyInfo;
import com.zhiqi.campusassistant.core.usercenter.entity.VerifyResult;
import com.zhiqi.campusassistant.core.usercenter.presenter.SecurityPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ming on 2016/10/8.
 * 填写验证码
 */

public class VerifyCodeActivity extends BaseToolbarActivity implements ILoadView<VerifyResult> {

    @Inject
    SecurityPresenter mPresenter;

    @BindView(R.id.verify_next)
    Button nextBtn;
    @BindView(R.id.verify_number)
    EditText verifyNum;
    @BindView(R.id.verify_resend)
    TextView resendView;
    @BindView(R.id.phone_number)
    TextView phoneNumber;

    private AreaInfo areaInfo;
    private String phone;

    private VerifyInfo verifyInfo;

    private Disposable mDisposable;

    private VerifyFunction function;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
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
        Intent intent = getIntent();
        phone = intent.getStringExtra(AppConstant.EXTRA_PHONE);
        if (TextUtils.isEmpty(phone)) {
            finish();
            return;
        }
        areaInfo = intent.getParcelableExtra(AppConstant.EXTRA_AREA_INFO);
        if (areaInfo == null) {
            String[] areaList = getResources().getStringArray(R.array.area_list);
            String[] area = areaList[0].split(",");
            areaInfo = new AreaInfo(area[0], area[1]);
        }
        verifyInfo = intent.getParcelableExtra(AppConstant.EXTRA_VERIFY_INFO);
        phoneNumber.setText(getString(R.string.login_phone_whole, areaInfo.areaState, areaInfo.areaCode, phone));
        if (verifyInfo != null) {
            function = verifyInfo.function;
            countDownResend();
        } else {
            int opt = intent.getIntExtra(AppConstant.EXTRA_BIND_PHONE_OPT, 0);
            function = VerifyFunction.formatValue(opt);
            if (function == null) {
                finish();
                return;
            }
            mPresenter.getVerifyCode(areaInfo.areaCode, phone, function, verifyCallback);
            countDownResend();
        }
    }

    private void countDownResend() {
        RxUtil.countdown(60).subscribe(new Observer<Integer>() {

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                resendView.setText(getString(R.string.login_verify_resend));
                resendView.setEnabled(true);

            }

            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(Integer integer) {
                resendView.setText(getString(R.string.login_verify_resend_count, integer));
            }
        });
    }

    @OnTextChanged(R.id.verify_number)
    void onPhoneChanged() {
        if (verifyNum.getText().length() < AppConstant.LOGIN_ACCOUNT_MIN_LENGTH) {
            nextBtn.setEnabled(false);
        } else {
            nextBtn.setEnabled(true);
        }
    }

    @OnClick({R.id.verify_next, R.id.verify_resend})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.verify_next:
                ProgressDialogUtil.show(this, null);
                mPresenter.checkVerifyCode(verifyNum.getText().toString(), verifyInfo.request_id, this);
                break;
            case R.id.verify_resend:
                resendView.setEnabled(false);
                countDownResend();
                mPresenter.getVerifyCode(areaInfo.areaCode, phone, function, verifyCallback);
                break;
        }
    }

    private ILoadView<VerifyInfo> verifyCallback = new ILoadView<VerifyInfo>() {
        @Override
        public void onLoadData(VerifyInfo data) {
            if (data != null) {
                data.function = function;
                verifyInfo = data;
                if (!TextUtils.isEmpty(data.code)) {
                    verifyNum.setText(data.code);
                }
            }
        }

        @Override
        public void onFailed(int errorCode, String message) {
            ToastUtil.show(VerifyCodeActivity.this, message);
        }
    };

    @Override
    protected void onDestroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        mPresenter.release();
        ProgressDialogUtil.dismiss();
        super.onDestroy();
    }

    @Override
    public void onLoadData(VerifyResult data) {
        ProgressDialogUtil.dismiss();
        switch (function) {
            case ForgetPassword:
                if (data != null) {
                    Intent intent = new Intent(this, ResetPwdActivity.class);
                    intent.putExtra(AppConstant.EXTRA_BIND_PHONE_OPT, function.getValue());
                    intent.putExtra(AppConstant.EXTRA_RESET_TOKEN, data.reset_token);
                    startActivity(intent);
                }
                break;
            default:
                LoginManager.getInstance().changePhone(phone);
                Intent intent = new Intent();
                intent.putExtra(AppConstant.EXTRA_RESET_TOKEN, data.reset_token);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onFailed(int errorCode, String message) {
        ProgressDialogUtil.error(message);
    }
}
