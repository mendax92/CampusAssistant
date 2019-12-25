package com.zhiqi.campusassistant.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.fragment.BaseToolbarFragment;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.core.usercenter.entity.VerifyFunction;
import com.zhiqi.campusassistant.ui.login.activity.BindPhoneActivity;
import com.zhiqi.campusassistant.ui.main.view.ITabView;
import com.zhiqi.campusassistant.ui.user.activity.AboutActivity;
import com.zhiqi.campusassistant.ui.user.activity.ChangePhoneActivity;
import com.zhiqi.campusassistant.ui.user.activity.SettingActivity;
import com.zhiqi.campusassistant.ui.user.activity.UserFeedbackActivity;
import com.zhiqi.campusassistant.ui.user.widget.UserInfoCardView;
import com.zhiqi.campusassistant.ui.web.activity.WebActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ming on 2016/10/10.
 * 用户中心
 */

public class UserCenterFragment extends BaseToolbarFragment implements ITabView {


    @BindView(R.id.user_card_view)
    UserInfoCardView userCard;
    @BindView(R.id.bind_phone_label)
    TextView bindLable;

    @Override
    public int onCreateView(Bundle savedInstanceState) {
        setActionbarTitle(R.string.common_me);
        return R.layout.frag_user_center;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        refreshBindPhone();
    }

    private void refreshBindPhone() {

        LoginUser user = LoginManager.getInstance().getLoginUser();
        if (user != null && !TextUtils.isEmpty(user.getPhone())) {
            bindLable.setText(user.getPhone());
        } else {
            bindLable.setText(R.string.user_nav_bind_phone);
        }
    }

    @OnClick({R.id.user_setting, R.id.user_bind_phone, R.id.user_help,
            R.id.user_feedback, R.id.user_about})
    void click(View view) {
        switch (view.getId()) {
            case R.id.user_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.user_bind_phone:
                gotoBindPhone();
                break;
            case R.id.user_help:
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra(AppConstant.EXTRA_URL, HttpUrlConstant.BROWSER_HELP_URL);
                startActivity(intent);
                break;
            case R.id.user_feedback:
                startActivity(new Intent(getActivity(), UserFeedbackActivity.class));
                break;
            case R.id.user_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
        }
    }

    private void initData() {
        userCard.setUser(LoginManager.getInstance().getLoginUser());
    }

    private void gotoBindPhone() {
        LoginUser user = LoginManager.getInstance().getLoginUser();
        if (user != null && !TextUtils.isEmpty(user.getPhone())) {
            startActivityForResult(new Intent(getActivity(), ChangePhoneActivity.class), AppConstant.ACTIVITY_REQUEST_VERIFY_CODE);
        } else {
            Intent intent = new Intent(getActivity(), BindPhoneActivity.class);
            intent.putExtra(AppConstant.EXTRA_BIND_PHONE_OPT, VerifyFunction.BindPhone.getValue());
            startActivityForResult(intent, AppConstant.ACTIVITY_REQUEST_VERIFY_CODE);
        }
    }

    @Override
    public void onChecked() {
        invalidateActionbar();
    }

    @Override
    public void onUnchecked() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.ACTIVITY_REQUEST_VERIFY_CODE == requestCode && RESULT_OK == resultCode) {
            refreshBindPhone();
        }
    }
}
