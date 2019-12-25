package com.zhiqi.campusassistant.ui.leave.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.decoration.HorizontalDividerItemDecoration;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseRefreshPageActivity;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.leave.dagger.component.DaggerLeaveComponent;
import com.zhiqi.campusassistant.core.leave.dagger.module.LeavePresenterModule;
import com.zhiqi.campusassistant.core.leave.entity.LeaveInfo;
import com.zhiqi.campusassistant.core.leave.presenter.LeavePresenter;
import com.zhiqi.campusassistant.core.message.entity.ModuleType;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.ui.leave.widget.LeaveApplyAdapter;
import com.zhiqi.campusassistant.ui.user.activity.IntroduceActivity;

import javax.inject.Inject;

import butterknife.OnClick;

/**
 * Created by ming on 2016/10/31.
 * 请假历史
 */

public class LeaveRecordActivity extends BaseRefreshPageActivity<LeaveInfo> {


    @Inject
    LeavePresenter mPresenter;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_leave_record;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        refresh();
    }

    private void initDagger() {
        DaggerLeaveComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .leavePresenterModule(new LeavePresenterModule())
                .build()
                .inject(this);
    }


    @OnClick(R.id.leave_apply_me)
    void onClick() {
        startActivityForResult(new Intent(this, LeaveApplyActivity.class), AppConstant.REQUEST_CODE_FOR_ADD);
    }

    @Override
    protected void onRefresh() {
        mPresenter.queryRecordList(LoginManager.getInstance().getUserId(), page, this);
    }

    @Override
    protected void onLoadMore(int page) {
        mPresenter.queryRecordList(LoginManager.getInstance().getUserId(), page, this);
    }

    @Override
    protected BaseQuickAdapter<LeaveInfo> provideAdapter() {
        return new LeaveApplyAdapter();
    }

    @Override
    protected RecyclerView.ItemDecoration provideItemDecoration() {
        return new HorizontalDividerItemDecoration.Builder(this)
                .backgroundResource(R.color.white)
                .margin(getResources().getDimensionPixelSize(R.dimen.common_margin_s), 0)
                .showLastDivider()
                .build();
    }

    @Override
    protected void onDestroy() {
        mPresenter.release();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.REQUEST_CODE_FOR_ADD == requestCode && RESULT_OK == resultCode) {
            refresh();
        } else if (AppConstant.REQUEST_CODE_FOR_UPDATE == requestCode && RESULT_OK == resultCode) {
            refresh();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_module_menu, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.introduce:
                Intent intent = new Intent(this, IntroduceActivity.class);
                intent.putExtra(AppConstant.EXTRA_APP_MODULE, ModuleType.LeaveApply.getValue());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
