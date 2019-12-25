package com.zhiqi.campusassistant.ui.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ming.base.util.Log;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.ming.base.widget.AppEditText;
import com.zhiqi.campusassistant.common.utils.ProgressDialogUtil;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.usercenter.dagger.component.DaggerUserCenterComponent;
import com.zhiqi.campusassistant.core.usercenter.dagger.module.UserCenterModule;
import com.zhiqi.campusassistant.core.usercenter.entity.FeedbackRequest;
import com.zhiqi.campusassistant.core.usercenter.presenter.UserCenterPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by ming on 2017/3/16.
 * 用户反馈
 */

public class UserFeedbackActivity extends BaseToolbarActivity implements IRequestView {

    private static final String TAG = "UserFeedbackActivity";

    @BindView(R.id.feedback_edit_area)
    AppEditText feedbackArea;

    @BindView(R.id.feedback_type)
    RadioGroup typeGroup;

    @BindView(R.id.contact)
    EditText contact;

    @BindView(R.id.feedback_edit_tip)
    TextView feedbackTip;

    @BindView(R.id.commit)
    Button commit;

    @Inject
    UserCenterPresenter mPresenter;

    private FeedbackRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        initDagger();
        initView();
    }

    private void initDagger() {
        DaggerUserCenterComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .userCenterModule(new UserCenterModule())
                .build()
                .inject(this);
    }

    private void initView() {
        request = new FeedbackRequest();
        feedbackTip.setText(getString(R.string.user_feedback_area_tip, 0));
    }

    @OnTextChanged(R.id.feedback_edit_area)
    void onTextChanged() {
        int textLen = feedbackArea.getValidText().toString().length();
        commit.setEnabled(typeGroup.getCheckedRadioButtonId() > 0 && textLen > 0);
        feedbackTip.setText(getString(R.string.user_feedback_area_tip, textLen));
    }

    @OnCheckedChanged({R.id.feedback_suggest, R.id.feedback_app_error, R.id.feedback_others})
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.i(TAG, "isChecked:" + isChecked);
        if (isChecked) {
            typeGroup.check(buttonView.getId());
            commit.setEnabled(feedbackArea.getValidText().toString().length() > 0);
            request.type = FeedbackRequest.TYPE_OTHERS;
            switch (buttonView.getId()) {
                case R.id.feedback_suggest:
                    request.type = FeedbackRequest.TYPE_SUGGEST;
                    break;
                case R.id.feedback_app_error:
                    request.type = FeedbackRequest.TYPE_APP_ERROR;
                    break;
            }
        }
    }

    @OnClick(R.id.commit)
    void onClick(View view) {
        request.content = feedbackArea.getValidText().toString();
        request.contact_info = contact.getText().toString();
        ProgressDialogUtil.show(this, R.string.common_commit_ing);
        mPresenter.requestFeedback(request, this);
    }

    @Override
    public void onQuest(int errorCode, String message) {
        if (HttpErrorCode.SUCCESS == errorCode) {
            clear();
            ProgressDialogUtil.success(message);
        } else {
            ProgressDialogUtil.error(message);
        }
    }

    private void clear() {
        request = new FeedbackRequest();
        feedbackArea.setText("");
        contact.setText("");
        typeGroup.clearCheck();
        feedbackTip.setText(getString(R.string.user_feedback_area_tip, 0));
    }

    @Override
    protected void onDestroy() {
        mPresenter.release();
        ProgressDialogUtil.dismiss();
        super.onDestroy();
    }
}
