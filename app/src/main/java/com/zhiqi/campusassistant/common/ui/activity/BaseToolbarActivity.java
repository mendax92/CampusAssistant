package com.zhiqi.campusassistant.common.ui.activity;

import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ming.base.util.Log;
import com.ming.base.util.StatusBarUtil;
import com.ming.base.util.StringUtil;
import com.zhiqi.campusassistant.R;

import butterknife.BindView;

/**
 * Created by ming on 2016/9/14.
 * 基础toolbar activity
 */
public class BaseToolbarActivity extends BaseFilterActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initActionBar() {
        ActionBar actionBar = this.getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
    }

    public void hideHomeBack() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void setActionbarTitle(int titleRes) {
        setActionbarTitle(getString(titleRes));
    }

    /**
     * 设置样式
     *
     * @param title
     */
    public void setActionbarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Log.w("BaseToolbarActivity", "Your should use this method after onActionBarCreated");
            return;
        }
        if (!StringUtil.isEmpty(title)) {
            actionBar.setTitle(title);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowCustomEnabled(false);
        }
    }

    /**
     * 设置toolbar中的view
     *
     * @param layoutId
     * @return
     */
    public View setActionbarLayout(@LayoutRes int layoutId) {
        View view = LayoutInflater.from(this).inflate(layoutId, getToolbar(), false);
        setActionbarLayout(view);
        return view;
    }

    /**
     * 设置toolbar view
     *
     * @param actionbarLayout
     */
    public void setActionbarLayout(View actionbarLayout) {
        if (mToolbar == null) {
            return;
        }
        if (actionbarLayout != null) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar == null) {
                Log.w("BaseToolbarActivity", "Your should use this method after onActionBarCreated");
                return;
            }
            mToolbar.removeView(actionbarLayout);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            mToolbar.addView(actionbarLayout);
        }
    }

    @Override
    public void setContentView(View contentView) {
        ViewGroup rootView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_tool_bar, null);
        rootView.addView(contentView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        super.setContentView(rootView);
        this.setSupportActionBar(mToolbar);
        initActionBar();
        onViewCreated(rootView);
    }

    protected void onViewCreated(View contentView) {

    }
}
