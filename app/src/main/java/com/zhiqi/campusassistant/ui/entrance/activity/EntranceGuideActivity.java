package com.zhiqi.campusassistant.ui.entrance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.listener.OnItemClickListener;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseRefreshListActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.entrance.dagger.component.DaggerEntranceComponent;
import com.zhiqi.campusassistant.core.entrance.dagger.module.EntranceModule;
import com.zhiqi.campusassistant.core.entrance.entity.EntranceInfo;
import com.zhiqi.campusassistant.core.entrance.presenter.EntrancePresenter;
import com.zhiqi.campusassistant.core.location.dagger.module.LocationModule;
import com.zhiqi.campusassistant.core.location.entity.CampusLocationInfo;
import com.zhiqi.campusassistant.core.location.presenter.CampusLocationPresenter;
import com.zhiqi.campusassistant.ui.bedroom.activity.BedRoomInfoActivity;
import com.zhiqi.campusassistant.ui.entrance.widget.EntranceAdapter;
import com.zhiqi.campusassistant.ui.location.activity.CampusNavActivity;
import com.zhiqi.campusassistant.ui.selfpay.activity.SelfPayActivity;
import com.zhiqi.campusassistant.ui.web.activity.WebActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.OnClick;

/**
 * Created by ming on 2017/8/16.
 * 报道指南activity
 */

public class EntranceGuideActivity extends BaseRefreshListActivity<EntranceInfo> implements ILoadView<List<EntranceInfo>> {


    @Inject
    EntrancePresenter mPresenter;

    @Inject
    CampusLocationPresenter locationPresenter;

    private ArrayList<CampusLocationInfo> locationInfos;

    private boolean gotoLocation = false;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_entrance_guide;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        initView();
    }

    private void initDagger() {
        DaggerEntranceComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .entranceModule(new EntranceModule())
                .locationModule(new LocationModule())
                .build()
                .inject(this);
    }

    private void initView() {
        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);
        mRecyclerView.addOnItemTouchListener(itemClickListener);
        refresh();
        locationPresenter.getCampusLocations(locationView);
    }

    /**
     * 获取定位列表，主要用于进入领取军服界面前，获取领取军服位置
     */
    private ILoadView<ArrayList<CampusLocationInfo>> locationView = new ILoadView<ArrayList<CampusLocationInfo>>() {
        @Override
        public void onLoadData(ArrayList<CampusLocationInfo> data) {
            locationInfos = data;
            if (gotoLocation) {
                gotoCampusLocation();
            }
        }

        @Override
        public void onFailed(int errorCode, String message) {
            ToastUtil.show(EntranceGuideActivity.this, message);
        }
    };

    private OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
            EntranceInfo info = mAdapter.getItem(position);
            if (info != null && !TextUtils.isEmpty(info.url)) {
                Intent intent = new Intent(EntranceGuideActivity.this, WebActivity.class);
                intent.putExtra(AppConstant.EXTRA_URL, info.url);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onRefresh() {
        mPresenter.getEntranceList(this);
    }

    @Override
    protected BaseQuickAdapter<EntranceInfo> provideAdapter() {
        return new EntranceAdapter();
    }

    @OnClick({R.id.step_1, R.id.step_2, R.id.step_3})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.step_1:
                intent = new Intent(this, SelfPayActivity.class);
                break;
            case R.id.step_2:
                intent = new Intent(this, BedRoomInfoActivity.class);
                break;
            case R.id.step_3:
                if (locationInfos == null) {
                    gotoLocation = true;
                    locationPresenter.getCampusLocations(locationView);
                } else {
                    gotoCampusLocation();
                }
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    private void gotoCampusLocation() {
        Intent intent = new Intent(this, CampusNavActivity.class);
        intent.putParcelableArrayListExtra(AppConstant.EXTRA_LOCATION_INFO, locationInfos);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.release();
        }
        if (locationPresenter != null) {
            locationPresenter.release();
        }
    }
}
