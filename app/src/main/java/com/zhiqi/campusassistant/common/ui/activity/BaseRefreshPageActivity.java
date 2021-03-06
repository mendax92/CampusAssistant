package com.zhiqi.campusassistant.common.ui.activity;

import android.view.View;

import com.ming.base.util.Log;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.zhiqi.campusassistant.common.entity.BasePageData;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;

/**
 * Created by ming on 2017/2/18.
 * 基础分页activity
 */

public abstract class BaseRefreshPageActivity<T> extends BaseRefreshListActivity<T> implements ILoadView<BasePageData<T>> {

    private static final String TAG = "BaseRefreshPageFragment";

    protected int page = 1;

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
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
