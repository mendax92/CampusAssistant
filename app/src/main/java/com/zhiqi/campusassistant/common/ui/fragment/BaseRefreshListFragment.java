package com.zhiqi.campusassistant.common.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.zhiqi.campusassistant.R;

import java.util.List;

import butterknife.BindView;

/**
 * Created by ming on 2017/4/5.
 * 可刷新fragment
 */

public abstract class BaseRefreshListFragment<T> extends BaseLoadListFragment<T> {

    @BindView(R.id.refresh_layout)
    protected SwipeRefreshLayout mRefreshLayout;

    @Override
    public int onCreateView(Bundle savedInstanceState) {
        return R.layout.view_common_refresh_recycler;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
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
        super.onLoadData(data);
        mRefreshLayout.setRefreshing(false);
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
