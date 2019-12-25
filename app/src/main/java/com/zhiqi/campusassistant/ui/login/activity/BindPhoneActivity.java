package com.zhiqi.campusassistant.ui.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.utils.ProgressDialogUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.usercenter.dagger.component.DaggerSecurityComponent;
import com.zhiqi.campusassistant.core.usercenter.dagger.module.SecurityModule;
import com.zhiqi.campusassistant.core.usercenter.entity.AreaInfo;
import com.zhiqi.campusassistant.core.usercenter.entity.VerifyFunction;
import com.zhiqi.campusassistant.core.usercenter.entity.VerifyInfo;
import com.zhiqi.campusassistant.core.usercenter.presenter.SecurityPresenter;
import com.zhiqi.campusassistant.ui.web.activity.WebActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 忘记密码
 */
public class BindPhoneActivity extends BaseToolbarActivity implements ILoadView<VerifyInfo> {

    private static final int AREA_REQUEST = 1001;

    @BindView(R.id.forget_next)
    Button nextBtn;
    @BindView(R.id.forget_phone)
    EditText phoneEdit;
    @BindView(R.id.area_info)
    TextView areaText;

    @Inject
    SecurityPresenter mPresenter;

    private AreaInfo areaInfo;
    private VerifyFunction function = VerifyFunction.ForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
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
        ActionBar actionBar = this.getSupportActionBar();
        if (intent != null) {
            int opt = intent.getIntExtra(AppConstant.EXTRA_BIND_PHONE_OPT, 1);
            function = VerifyFunction.formatValue(opt);
        }
        if (actionBar != null) {
            if (VerifyFunction.ForgetPassword == function) {
                actionBar.setTitle(R.string.login_forget_pwd);
            } else {
                actionBar.setTitle(R.string.user_bind_phone);
            }
        }
        String[] areaList = getResources().getStringArray(R.array.area_list);
        String[] area = areaList[0].split(",");
        areaInfo = new AreaInfo(area[0], area[1]);
        areaText.setText(getString(R.string.login_area_info, areaInfo.areaState, areaInfo.areaCode));
    }

    @OnTextChanged(R.id.forget_phone)
    void onPhoneChanged() {
        if (phoneEdit.getText().length() < AppConstant.LOGIN_VERIFY_LENGTH) {
            nextBtn.setEnabled(false);
        } else {
            nextBtn.setEnabled(true);
        }
    }

    @OnClick({R.id.forget_next, R.id.forget_change_state, R.id.has_question})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_next:
                ProgressDialogUtil.show(this, null);
                mPresenter.getVerifyCode(areaInfo.areaCode, phoneEdit.getText().toString(), function, this);
                break;
            case R.id.forget_change_state:
                startActivityForResult(new Intent(this, AreaChoiceActvitity.class), AREA_REQUEST);
                break;
            case R.id.has_question:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra(AppConstant.EXTRA_URL, HttpUrlConstant.BROWSER_HELP_URL);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AREA_REQUEST == requestCode && RESULT_OK == resultCode && data != null) {
            areaInfo = data.getParcelableExtra(AppConstant.EXTRA_AREA_INFO);
            areaText.setText(getString(R.string.login_area_info, areaInfo.areaState, areaInfo.areaCode));
        } else if (AppConstant.ACTIVITY_REQUEST_VERIFY_CODE == requestCode && RESULT_OK == resultCode) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        ProgressDialogUtil.dismiss();
        mPresenter.release();
        super.onDestroy();
    }

    @Override
    public void onLoadData(VerifyInfo data) {
        ProgressDialogUtil.dismiss();
        data.function = function;
        Intent intent = new Intent(BindPhoneActivity.this, VerifyCodeActivity.class);
        intent.putExtra(AppConstant.EXTRA_VERIFY_INFO, data);
        intent.putExtra(AppConstant.EXTRA_AREA_INFO, areaInfo);
        intent.putExtra(AppConstant.EXTRA_PHONE, phoneEdit.getText().toString());
        startActivityForResult(intent, AppConstant.ACTIVITY_REQUEST_VERIFY_CODE);
    }

    @Override
    public void onFailed(int errorCode, String message) {
        ProgressDialogUtil.error(message);
    }
}
