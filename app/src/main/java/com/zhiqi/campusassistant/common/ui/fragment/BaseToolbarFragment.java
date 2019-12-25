package com.zhiqi.campusassistant.common.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ming.base.util.StringUtil;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;

import butterknife.ButterKnife;

/**
 * Created by Edmin on 2016/9/28 0028.
 */

public abstract class BaseToolbarFragment extends BaseFilterFragment {

    private String actionbarTitle;

    private View actionbarLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(onCreateView(savedInstanceState), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 返回resource layout id
     *
     * @param savedInstanceState
     * @return
     */
    public abstract int onCreateView(Bundle savedInstanceState);

    public void setActionbarTitle(int titleRes) {
        setActionbarTitle(getString(titleRes));
    }

    public void setActionbarTitle(String title) {
        this.actionbarTitle = title;
        invalidateActionbar();
    }

    public void setActionbarLayout(View actionbarLayout) {
        this.actionbarLayout = actionbarLayout;
        invalidateActionbar();
    }

    /**
     * 修改actionbar样式
     */
    public void invalidateActionbar() {
        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (actionBar == null) {
                return;
            }
            if (activity instanceof BaseToolbarActivity) {
                Toolbar toolbar = ((BaseToolbarActivity) activity).getToolbar();
                toolbar.removeView(actionbarLayout);
                if (actionbarLayout != null) {
                    actionBar.setDisplayShowTitleEnabled(false);
                    toolbar.addView(actionbarLayout);
                    return;
                }
            }
            if (!StringUtil.isEmpty(actionbarTitle)) {
                actionBar.setTitle(actionbarTitle);
                actionBar.setDisplayShowTitleEnabled(true);
            }
        }
    }

    public Toolbar getToolbar() {
        Activity activity = getActivity();
        if (activity instanceof BaseToolbarActivity) {
            return ((BaseToolbarActivity) activity).getToolbar();
        }
        return null;
    }

    /**
     * 重置actionbar
     */
    public void resetActionbar() {
        Activity activity = getActivity();
        if (activity instanceof BaseToolbarActivity && actionbarLayout != null) {
            Toolbar toolbar = ((BaseToolbarActivity) activity).getToolbar();
            toolbar.removeView(actionbarLayout);
        }
    }
}
