package com.zhiqi.campusassistant.ui.repair.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ming.base.util.GsonUtils;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.decoration.HorizontalDividerItemDecoration;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseRefreshPageActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.widget.BaseRadioButtonAdapter;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.repair.dagger.component.DaggerRepairComponent;
import com.zhiqi.campusassistant.core.repair.dagger.module.RepairModule;
import com.zhiqi.campusassistant.core.repair.entity.RepairApplicantInfo;
import com.zhiqi.campusassistant.core.repair.entity.RepairDictionary;
import com.zhiqi.campusassistant.core.repair.presenter.RepairPresenter;
import com.zhiqi.campusassistant.ui.repair.widget.RepairApplicantAdapter;

import javax.inject.Inject;

/**
 * Created by ming on 2017/3/30.
 * 常用维修申报人
 */

public class RepairApplicantListActivity extends BaseRefreshPageActivity<RepairApplicantInfo> {

    @Inject
    RepairPresenter mPresenter;

    private MenuItem confirmMenu;
    private int checkedPosition = -1;

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
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
        Intent intent = getIntent();
        if (intent != null) {
            String dataJson = intent.getStringExtra(AppConstant.EXTRA_CAMPUS_VERSION_DATA);
            RepairDictionary dictionary = GsonUtils.fromJson(dataJson, RepairDictionary.class);
            if (dictionary != null) {
                ((RepairApplicantAdapter) mAdapter).setRepairDictionary(dictionary);
            } else {
                mPresenter.loadVersionData(new ILoadView<RepairDictionary>() {
                    @Override
                    public void onLoadData(RepairDictionary data) {
                        if (data != null) {
                            ((RepairApplicantAdapter) mAdapter).setRepairDictionary(data);
                        }
                    }

                    @Override
                    public void onFailed(int errorCode, String message) {
                        ToastUtil.show(RepairApplicantListActivity.this, message);
                    }
                });
            }
        }
        refresh();
    }

    private BaseRadioButtonAdapter.OnCheckedChangeListener checkedChangeListener = new BaseRadioButtonAdapter.OnCheckedChangeListener() {
        @Override
        public void onChecked(int position) {
            confirmMenu.setEnabled(true);
            checkedPosition = position;
        }
    };

    @Override
    protected void onRefresh() {
        mPresenter.queryRepairUserInfo(page, this);
    }

    @Override
    protected void onLoadMore(int page) {
        mPresenter.queryRepairUserInfo(page, this);
    }

    @Override
    protected BaseQuickAdapter<RepairApplicantInfo> provideAdapter() {
        RepairApplicantAdapter adapter = new RepairApplicantAdapter();
        adapter.setOnCheckedChangeListener(checkedChangeListener);
        return adapter;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_confirm_menu, menu);
        confirmMenu = menu.findItem(R.id.confirm);
        confirmMenu.setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirm:
                RepairApplicantInfo info = mAdapter.getItem(checkedPosition);
                Intent intent = new Intent();
                intent.putExtra(AppConstant.EXTRA_APPLICANT_INFO, info);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.release();
    }
}
