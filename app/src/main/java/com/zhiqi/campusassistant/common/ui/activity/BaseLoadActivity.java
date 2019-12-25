package com.zhiqi.campusassistant.common.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zhiqi.campusassistant.common.ui.widget.BaseEmptyView;
import com.zhiqi.campusassistant.common.ui.widget.SimpleEmptyView;

/**
 * Created by ming on 2017/4/9.
 * 基础刷新view，实现{@link #onCreateView(Bundle)}即可实现空白view的加载效果
 */

public abstract class BaseLoadActivity<T> extends BaseToolbarActivity {

    private BaseEmptyView mEmptyView;

    private View container;

    protected View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutRes = onCreateView(savedInstanceState);
        if (layoutRes > 0) {
            mEmptyView = provideEmptyView();

            if (mEmptyView != null) {
                FrameLayout layout = new FrameLayout(this);
                layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                container = LayoutInflater.from(this).inflate(layoutRes, layout, false);
                layout.addView(container);
                layout.addView(mEmptyView);
                showEmptyView(true);
                rootView = layout;
            } else {
                container = LayoutInflater.from(this).inflate(layoutRes, null);
                rootView = container;
            }
            setContentView(rootView);
        }
    }

    public void refresh() {
        refresh(true);
    }

    public void refresh(boolean showLoading) {
        if (mEmptyView == null) {
            return;
        }
        if (showLoading) {
            mEmptyView.showLoadingView();
        }
        onRefresh();
    }

    public void onLoadData(T data) {
        if (mEmptyView == null) {
            return;
        }
        // 根据数据是否为空显示缺省view
        if (data == null) {
            mEmptyView.showEmptyView();
        }
        showEmptyView(data == null);
    }

    public void showEmptyView(boolean show) {
        if (show) {
            mEmptyView.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        }
    }

    public void onFailed(int errorCode, String message) {
        if (mEmptyView == null) {
            return;
        }
        if (mEmptyView instanceof SimpleEmptyView) {
            ((SimpleEmptyView) mEmptyView).setTipMessage(message);
        }
        mEmptyView.showFailView();
        showEmptyView(true);
    }

    /**
     * 如果覆盖了方法，则即可自定义空白view，默认是{@link SimpleEmptyView}
     *
     * @return 空白view
     */
    protected BaseEmptyView provideEmptyView() {
        return new SimpleEmptyView(this);
    }

    /**
     * 主要内容布局，不包括空白view
     *
     * @return 内容布局id
     */
    protected abstract int onCreateView(Bundle savedInstanceState);

    protected abstract void onRefresh();
}
