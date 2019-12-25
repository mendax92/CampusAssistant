package com.zhiqi.campusassistant.common.ui.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.decoration.HorizontalDividerItemDecoration;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.widget.BaseEmptyView;
import com.zhiqi.campusassistant.common.ui.widget.SimpleEmptyView;
import com.zhiqi.campusassistant.common.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by ming on 2017/4/5.
 * 基础加载list activity
 */

public abstract class BaseLoadListActivity<T> extends BaseToolbarActivity {

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    protected BaseEmptyView mEmptyView;

    protected BaseQuickAdapter<T> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutRes = onCreateView(savedInstanceState);
        if (layoutRes > 0) {
            setContentView(layoutRes);
        }
    }

    @LayoutRes
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.view_common_recycler;
    }

    @Override
    protected void onViewCreated(View contentView) {
        RecyclerView recyclerView = provideRecyclerView();
        if (recyclerView != null) {
            mRecyclerView = recyclerView;
        }
        RecyclerView.ItemDecoration itemDecoration = provideItemDecoration();
        if (itemDecoration != null) {
            mRecyclerView.addItemDecoration(provideItemDecoration());
        }
        mRecyclerView.setLayoutManager(provideLayoutManager());
        mAdapter = provideAdapter();
        mEmptyView = provideEmptyView();
        if (mAdapter != null) {
            if (mEmptyView != null) {
                mAdapter.setEmptyView(mEmptyView);
            }
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public void refresh() {
        refresh(mAdapter.getItemCount() == 0);
    }

    public void refresh(boolean showLoading) {
        if (mEmptyView != null && showLoading) {
            mEmptyView.showLoadingView();
        }
        onRefresh();
    }

    public void onLoadData(List<T> data) {
        if (mEmptyView != null) {
            mEmptyView.showEmptyView();
        }
        mAdapter.setNewData(data);
    }

    public void onFailed(int errorCode, String message) {
        if (mEmptyView != null) {
            mEmptyView.showFailView();
        }
        ToastUtil.show(this, message);
    }

    /**
     * 得到RecyclerView
     *
     * @return null为layout view_recycler_single中recycler_view
     */
    protected RecyclerView provideRecyclerView() {
        return null;
    }

    /**
     * 空缺页面
     *
     * @return
     */
    protected BaseEmptyView provideEmptyView() {
        return new SimpleEmptyView(this);
    }

    protected RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(this);
    }

    protected RecyclerView.ItemDecoration provideItemDecoration() {
        return new HorizontalDividerItemDecoration.Builder(this).showLastDivider().build();
    }

    protected abstract void onRefresh();

    protected abstract BaseQuickAdapter<T> provideAdapter();
}
