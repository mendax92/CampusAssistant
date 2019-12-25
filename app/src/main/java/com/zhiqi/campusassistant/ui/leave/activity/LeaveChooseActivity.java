package com.zhiqi.campusassistant.ui.leave.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.config.AppConstant;

import butterknife.OnClick;

/**
 * Created by ming on 17-8-31.
 * 请假审批选择入口
 */

public class LeaveChooseActivity extends BaseToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_choose);
    }

    @OnClick({R.id.leave_employee, R.id.leave_student})
    void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.leave_employee:
                intent = new Intent(this, LeaveApprovalActivity.class);
                intent.putExtra(AppConstant.EXTRA_LEAVE_USER_TYPE, AppConstant.VALUE_LEAVE_USER_TYPE_EMPLOYEE);
                break;
            case R.id.leave_student:
                intent = new Intent(this, LeaveApprovalActivity.class);
                intent.putExtra(AppConstant.EXTRA_LEAVE_USER_TYPE, AppConstant.VALUE_LEAVE_USER_TYPE_STUDENT);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }


}
