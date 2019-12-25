package com.zhiqi.campusassistant.common.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.zhiqi.campusassistant.common.ui.widget.MaterialSwipeRefreshLayout;

/**
 * Created by ming on 2017/8/23.
 * 刷新加载界面
 */

public abstract class BaseRefreshLoadActivity<T> extends BaseLoadActivity<T> {

    protected SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        mRefreshLayout = provideSwipeRefreshLayout();
        if (mRefreshLayout == null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            int childIndex = parent.indexOfChild(rootView);
            mRefreshLayout = new MaterialSwipeRefreshLayout(this);
            mRefreshLayout.setLayoutParams(rootView.getLayoutParams());
            parent.removeView(rootView);
            mRefreshLayout.addView(rootView);
            parent.addView(mRefreshLayout, childIndex);
        }
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(false);
            }
        });
    }

    @Override
    public void onLoadData(T data) {
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
