package com.zhiqi.campusassistant.ui.contacts.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ming.base.util.AppUtil;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.core.user.dagger.component.DaggerUserInfoComponent;
import com.zhiqi.campusassistant.core.user.dagger.module.UserInfoModule;
import com.zhiqi.campusassistant.core.user.entity.UserInfo;
import com.zhiqi.campusassistant.core.user.entity.UserRole;
import com.zhiqi.campusassistant.core.user.presenter.UserInfoPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ming on 2016/12/1.
 */

public class UserInfoActivity extends BaseToolbarActivity implements ILoadView<UserInfo> {

    @BindView(R.id.user_name)
    TextView userName;

    @BindView(R.id.user_phone)
    TextView userPhone;

    @BindView(R.id.user_tel)
    TextView userTel;

    @BindView(R.id.user_email)
    TextView userEmail;

    @BindView(R.id.user_department_label)
    TextView departmentLabel;

    @BindView(R.id.user_department)
    TextView departmentName;

    @BindView(R.id.user_grade_class_label)
    TextView classLabel;

    @BindView(R.id.user_grade_class)
    TextView classNameText;

    @BindView(R.id.user_header)
    ImageView userHeader;

    @BindView(R.id.user_tel_layout)
    View telLayout;

    @BindView(R.id.user_tel_line)
    View telLine;

    @BindView(R.id.short_number)
    TextView shortNumberTxt;

    @Inject
    UserInfoPresenter mPresenter;

    private UserInfo userInfo;

    private long userId;

    private String clickTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        if (!initData()) {
            finish();
            return;
        }
        initDagger();
        if (userId != 0) {
            LoginUser user = LoginManager.getInstance().getLoginUser();
            mPresenter.queryUserDetails(userId, user != null ? user.getRole_type() : null, this);
        }
    }

    private void initDagger() {
        DaggerUserInfoComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .userInfoModule(new UserInfoModule())
                .build().inject(this);
    }

    private boolean initData() {
        Intent intent = getIntent();
        userInfo = intent.getParcelableExtra(AppConstant.EXTRA_USER_INFO);
        if (userInfo == null) {
            userId = intent.getLongExtra(AppConstant.EXTRA_USER_ID, 0);
            if (userId == 0) {
                return false;
            }
        } else {
            fillData();
        }
        return true;
    }

    private void fillData() {
        userName.setText(userInfo.real_name);
        userPhone.setText(userInfo.phone);
        userTel.setText(userInfo.tel);
        userEmail.setText(userInfo.email);
        if (UserRole.Student == userInfo.role_type) {
            departmentLabel.setText(R.string.user_department);
            classLabel.setText(R.string.user_grade_class);
            telLayout.setVisibility(View.GONE);
            telLine.setVisibility(View.GONE);
        } else {
            departmentLabel.setText(R.string.user_staff_department);
            classLabel.setText(R.string.user_position);
        }
        if (!TextUtils.isEmpty(userInfo.short_number)) {
            shortNumberTxt.setText(userInfo.short_number);
        }
        departmentName.setText(userInfo.department);
        classNameText.setText(userInfo.position);
        GlideApp.with(this)
                .load(userInfo.head)
                .placeholder(R.drawable.img_user_default_square_head)
                .into(userHeader);
    }

    @OnClick({R.id.phone_item, R.id.short_number_item})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phone_item:
                clickTel = userPhone.getText().toString();
                break;
            case R.id.short_number_item:
                clickTel = shortNumberTxt.getText().toString();
                break;
        }
        requestPermissions(false, Manifest.permission.CALL_PHONE);
    }

    @Override
    protected void onPermissionGranted(String[] permissions) {
        super.onPermissionGranted(permissions);
        AppUtil.callTel(this, clickTel);
    }

    @Override
    public void onLoadData(UserInfo data) {
        userInfo = data;
        fillData();
    }

    @Override
    public void onFailed(int errorCode, String message) {
        ToastUtil.show(this, message);
    }

    @Override
    protected void onDestroy() {
        mPresenter.release();
        super.onDestroy();
    }
}
