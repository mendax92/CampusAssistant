package com.zhiqi.campusassistant.ui.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.common.utils.ProgressDialogUtil;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.jpush.service.JPushManager;
import com.zhiqi.campusassistant.core.login.dagger.module.LoginPresenterModule;
import com.zhiqi.campusassistant.core.login.presenter.LoginPresenter;
import com.zhiqi.campusassistant.core.usercenter.dagger.component.DaggerUserCenterComponent;
import com.zhiqi.campusassistant.core.usercenter.dagger.module.UserCenterModule;
import com.zhiqi.campusassistant.core.usercenter.presenter.UserCenterPresenter;
import com.zhiqi.campusassistant.ui.launch.activity.NavigationActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by ming on 2017/3/18.
 * 设置界面
 */

public class SettingActivity extends BaseToolbarActivity implements IRequestView {

    @BindView(R.id.notice_toggle)
    CheckBox noticeToggle;

    @Inject
    UserCenterPresenter userCenterPresenter;

    @Inject
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initDagger();
        initView();
    }

    private void initDagger() {
        DaggerUserCenterComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .userCenterModule(new UserCenterModule())
                .loginPresenterModule(new LoginPresenterModule())
                .build()
                .inject(this);
    }

    private void initView() {
        noticeToggle.setChecked(JPushManager.getInstance().isEnabled());
    }

    @OnClick({R.id.logout, R.id.account_safe, R.id.clear_cache})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_safe:
                startActivity(new Intent(this, AccountSafeActivity.class));
                break;
            case R.id.logout:
                ProgressDialogUtil.show(this, R.string.logout_ing);
                loginPresenter.logout(logoutView);
                break;
            case R.id.clear_cache:
                ProgressDialogUtil.show(this, R.string.user_setting_clear_ing);
                userCenterPresenter.clearCache(this);
                break;
        }
    }

    @OnCheckedChanged(R.id.notice_toggle)
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        JPushManager.getInstance().enable(isChecked);
    }

    private IRequestView logoutView = new IRequestView() {
        @Override
        public void onQuest(int errorCode, String message) {
            if (HttpErrorCode.SUCCESS == errorCode) {
                ProgressDialogUtil.success(message);
                startActivity(new Intent(SettingActivity.this, NavigationActivity.class));
            } else {
                ProgressDialogUtil.error(message);
            }
        }
    };

    @Override
    public void onQuest(int errorCode, String message) {
        if (HttpErrorCode.SUCCESS == errorCode) {
            ProgressDialogUtil.success(message);
        } else {
            ProgressDialogUtil.error(message);
        }
    }

    @Override
    protected void onDestroy() {
        ProgressDialogUtil.dismiss();
        if (loginPresenter != null) {
            loginPresenter.release();
        }
        if (userCenterPresenter != null) {
            userCenterPresenter.release();
        }
        super.onDestroy();
    }
}
