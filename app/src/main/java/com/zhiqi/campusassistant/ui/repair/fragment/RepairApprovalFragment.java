package com.zhiqi.campusassistant.ui.repair.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.fragment.BaseRefreshPageFragment;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.repair.dagger.component.DaggerRepairComponent;
import com.zhiqi.campusassistant.core.repair.dagger.module.RepairModule;
import com.zhiqi.campusassistant.core.repair.entity.RepairInfo;
import com.zhiqi.campusassistant.core.repair.presenter.RepairPresenter;
import com.zhiqi.campusassistant.ui.repair.widget.RepairApprovalAdapter;

import javax.inject.Inject;

/**
 * Created by ming on 2017/2/12.
 * 维修审批fragment
 */

public class RepairApprovalFragment extends BaseRefreshPageFragment<RepairInfo> {

    private static final String EXTRA_QUERY_STATUS = "query_status";

    private int status;

    @Inject
    RepairPresenter mPresenter;

    public static RepairApprovalFragment newInstance(int queryStatus) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_QUERY_STATUS, queryStatus);
        RepairApprovalFragment fragment = new RepairApprovalFragment();
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
        DaggerRepairComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .repairModule(new RepairModule())
                .build()
                .inject(this);
    }

    private void initData() {
        Bundle args = getArguments();
        if (args != null) {
            status = args.getInt(EXTRA_QUERY_STATUS);
        }
        refresh();
    }

    @Override
    protected void onRefresh() {
        mPresenter.queryRepairApprovalList(status, page, this);
    }

    @Override
    protected void onLoadMore(int page) {
        mPresenter.queryRepairApprovalList(status, page, this);
    }

    @Override
    protected BaseQuickAdapter<RepairInfo> provideAdapter() {
        return new RepairApprovalAdapter(this);
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
