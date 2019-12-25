package com.zhiqi.campusassistant.ui.repair.activity;

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
import com.zhiqi.campusassistant.core.message.entity.ModuleType;
import com.zhiqi.campusassistant.core.repair.dagger.component.DaggerRepairComponent;
import com.zhiqi.campusassistant.core.repair.dagger.module.RepairModule;
import com.zhiqi.campusassistant.core.repair.entity.RepairInfo;
import com.zhiqi.campusassistant.core.repair.presenter.RepairPresenter;
import com.zhiqi.campusassistant.ui.repair.widget.RepairApplyAdapter;
import com.zhiqi.campusassistant.ui.user.activity.IntroduceActivity;

import javax.inject.Inject;

import butterknife.OnClick;

/**
 * Created by ming on 2016/10/31.
 * 报修历史
 */

public class RepairRecordActivity extends BaseRefreshPageActivity<RepairInfo> {


    @Inject
    RepairPresenter mPresenter;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_repair_record;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        refresh();
    }

    private void initDagger() {
        DaggerRepairComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .repairModule(new RepairModule())
                .build()
                .inject(this);
    }

    @Override
    protected void onRefresh() {
        mPresenter.queryRecordList(page, this);
    }

    @Override
    protected void onLoadMore(int page) {
        mPresenter.queryRecordList(page, this);
    }


    @Override
    protected BaseQuickAdapter<RepairInfo> provideAdapter() {
        return new RepairApplyAdapter();
    }

    @Override
    protected RecyclerView.ItemDecoration provideItemDecoration() {
        return new HorizontalDividerItemDecoration.Builder(this)
                .showLastDivider()
                .backgroundResource(R.color.white)
                .marginProvider(new HorizontalDividerItemDecoration.MarginProvider() {
                    @Override
                    public int dividerLeftMargin(int position, RecyclerView parent) {
                        if (position == mAdapter.getItemCount() - 1) {
                            return 0;
                        }
                        return getResources().getDimensionPixelSize(R.dimen.common_margin_s);
                    }

                    @Override
                    public int dividerRightMargin(int position, RecyclerView parent) {
                        return 0;
                    }
                }).build();
    }

    @OnClick(R.id.repair_apply_me)
    void onClick() {
        startActivityForResult(new Intent(this, RepairApplyActivity.class), AppConstant.REQUEST_CODE_FOR_ADD);
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
                intent.putExtra(AppConstant.EXTRA_APP_MODULE, ModuleType.RepairApply.getValue());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
