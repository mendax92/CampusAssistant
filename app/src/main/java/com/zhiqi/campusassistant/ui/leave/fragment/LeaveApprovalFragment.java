package com.zhiqi.campusassistant.ui.leave.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.fragment.BaseRefreshPageFragment;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.leave.dagger.component.DaggerLeaveComponent;
import com.zhiqi.campusassistant.core.leave.dagger.module.LeavePresenterModule;
import com.zhiqi.campusassistant.core.leave.entity.LeaveInfo;
import com.zhiqi.campusassistant.core.leave.presenter.LeavePresenter;
import com.zhiqi.campusassistant.ui.leave.widget.LeaveApprovalAdapter;

import javax.inject.Inject;

/**
 * Created by ming on 2017/3/10.
 * 请假审批
 */

public class LeaveApprovalFragment extends BaseRefreshPageFragment<LeaveInfo> {

    private static final String EXTRA_QUERY_STATUS = "query_status";
    private static final String EXTRA_USER_TYPE = "user_type";

    private int status;

    // 查询的审批列表用户类型
    private int userType;

    @Inject
    LeavePresenter mPresenter;

    public static LeaveApprovalFragment newInstance(int queryStatus, int userType) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_QUERY_STATUS, queryStatus);
        args.putInt(EXTRA_USER_TYPE, userType);
        LeaveApprovalFragment fragment = new LeaveApprovalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDagger();
        initData();
    }

    private void initDagger() {
        DaggerLeaveComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .leavePresenterModule(new LeavePresenterModule())
                .build()
                .inject(this);
    }

    private void initData() {
        Bundle args = getArguments();
        if (args != null) {
            status = args.getInt(EXTRA_QUERY_STATUS);
            userType = args.getInt(EXTRA_USER_TYPE);
        }
        refresh();
    }

    @Override
    protected BaseQuickAdapter<LeaveInfo> provideAdapter() {
        return new LeaveApprovalAdapter(this);
    }

    @Override
    protected void onLoadMore(int page) {
        mPresenter.queryLeaveApprovalList(userType, status, page, this);
    }

    @Override
    protected void onRefresh() {
        mPresenter.queryLeaveApprovalList(userType, status, page, this);
    }

    @Override
    public void onDestroy() {
        mPresenter.release();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.REQUEST_CODE_FOR_UPDATE == requestCode && Activity.RESULT_OK == resultCode) {
            refresh();
        }
    }
}
