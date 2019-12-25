package com.zhiqi.campusassistant.common.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ming.base.util.Log;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.zhiqi.campusassistant.common.entity.BasePageData;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;

import java.util.List;

/**
 * Created by ming on 2017/2/18.
 * 基础分页fragment
 */

public abstract class BaseRefreshPageFragment<T> extends BaseRefreshListFragment<T> implements ILoadView<BasePageData<T>> {

    private static final String TAG = "BaseRefreshPageFragment";

    protected int page = 1;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
    }

    @Override
    public void refresh(boolean showLoading) {
        page = 1;
        super.refresh(showLoading);
    }

    public void loadMore() {
        page++;
        onLoadMore(page);
    }

    @Override
    public void onLoadData(BasePageData<T> data) {
        mRefreshLayout.setRefreshing(false);
        mAdapter.loadMoreComplete();
        if (data != null) {
            if (data.page_no == 1) {
                super.onLoadData(data.list);
                mRecyclerView.scrollToPosition(0);
            } else {
                mAdapter.addData(data.list);
            }
            if (data.lastpage) {
                mAdapter.setEnableLoadMore(false);
            } else {
                mAdapter.setEnableLoadMore(true);
                page = data.page_no;
            }
            Log.i(TAG, "page:" + page);
        } else {
            super.onLoadData(null);
        }
    }

    @Override
    public void onFailed(int errorCode, String message) {
        super.onFailed(errorCode, message);
        mAdapter.loadMoreFail();
        mAdapter.setEnableLoadMore(false);
    }

    public int getPage() {
        return page;
    }

    protected abstract void onLoadMore(int page);
}
