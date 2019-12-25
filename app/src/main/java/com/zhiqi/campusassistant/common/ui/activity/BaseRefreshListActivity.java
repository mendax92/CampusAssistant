package com.zhiqi.campusassistant.common.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.zhiqi.campusassistant.R;

import java.util.List;

import butterknife.BindView;

/**
 * Created by ming on 2017/2/18.
 * 基础刷新activity
 */

public abstract class BaseRefreshListActivity<T> extends BaseLoadListActivity<T> {

    @BindView(R.id.refresh_layout)
    protected SwipeRefreshLayout mRefreshLayout;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.view_common_refresh_recycler;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        SwipeRefreshLayout refreshLayout = provideSwipeRefreshLayout();
        if (refreshLayout != null) {
            mRefreshLayout = refreshLayout;
        }
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(false);
            }
        });
    }

    @Override
    public void onLoadData(List<T> data) {
        mRefreshLayout.setRefreshing(false);
        super.onLoadData(data);
    }

    @Override
    public void onFailed(int errorCode, String message) {
        super.onFailed(errorCode, message);
        mRefreshLayout.setRefreshing(false);
    }

    protected SwipeRefreshLayout provideSwipeRefreshLayout() {
        return null;
    }
}
