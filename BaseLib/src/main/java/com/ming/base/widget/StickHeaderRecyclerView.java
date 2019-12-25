/*
package com.ming.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

*/
/**
 * Created by ming on 2016/11/1.
 *//*


public class StickHeaderRecyclerView extends ObservableRecyclerView implements ObservableScrollViewCallbacks {

    private BaseQuickAdapter mAdapter;
    private View mHeaderView;
    private View mHideView;
    private int mBaseTranslationY;
    private boolean untilScroolTop = false;

    public StickHeaderRecyclerView(Context context) {
        super(context);
    }

    public StickHeaderRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickHeaderRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        this.mAdapter = adapter;
        super.setAdapter(adapter);
    }

    public void setStickView(View stickView) {
        setStickView(stickView, -1, false);
    }

    public void setStickView(View stickView, boolean untilScroolTop) {
        setStickView(stickView, -1, untilScroolTop);
    }

    public void setStickView(View stickView, int hideHeaderViewId) {
        setStickView(stickView, hideHeaderViewId, false);
    }

    */
/**
     * 设置粘附view
     *
     * @param stickView        整个粘附view的头部
     * @param hideHeaderViewId 其中需要隐藏的view id
     * @param untilScroolTop   是否滚动到顶部才展示整个头部
     *//*

    public void setStickView(View stickView, int hideHeaderViewId, boolean untilScroolTop) {
        if (stickView == null) {
            return;
        }
        this.mHeaderView = stickView;
        this.untilScroolTop = untilScroolTop;
        if (hideHeaderViewId > 0) {
            mHideView = mHeaderView.findViewById(hideHeaderViewId);
        }
        if (mHeaderView.getHeight() == 0) {
            ScrollUtils.addOnGlobalLayoutListener(mHeaderView, new Runnable() {
                @Override
                public void run() {
                    initHeaderView();
                }
            });
        } else {
            initHeaderView();
        }
        setScrollViewCallbacks(this);
    }

    private void initHeaderView() {
        int headerHeight = mHeaderView.getHeight();
        View headerView = new View(getContext());
        headerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, headerHeight));
        headerView.setMinimumHeight(headerHeight);
        mAdapter.addHeaderView(headerView);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int scrollViewHeight = getScrollHeight();
        if (scrollY == 0) {
            mBaseTranslationY = scrollY;
        }
        int offset = scrollY - mBaseTranslationY;
        if (scrollY <= scrollViewHeight) {
            if (offset < 0 && stickViewIsShown()) {
                return;
            }
            float headerTranslationY = ScrollUtils.getFloat(-scrollY, -scrollViewHeight, 0);
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewHelper.setTranslationY(mHeaderView, headerTranslationY);
        } else if (offset > 0 && !stickViewIsShown()) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewHelper.setTranslationY(mHeaderView, -scrollViewHeight);
        }
    }

    @Override
    public void onDownMotionEvent() {
        mBaseTranslationY = getCurrentScrollY();
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (untilScroolTop) {
            return;
        }
        int scrollHeight = getScrollHeight();
        if (getCurrentScrollY() > scrollHeight) {
            if (scrollState == ScrollState.DOWN) {
                showStickView();
            } else if (scrollState == ScrollState.UP) {
                hideStickView();
            }

        }
    }

    private int getScrollHeight() {
        return mHideView != null ? mHideView.getHeight() : mHeaderView.getHeight();
    }

    private boolean stickViewIsShown() {
        return ViewHelper.getTranslationY(mHeaderView) == 0;
    }

    private boolean stickViewIsHidden() {
        return ViewHelper.getTranslationY(mHeaderView) == -getScrollHeight();
    }

    private void showStickView() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(0).setDuration(200).start();
        }
    }

    private void hideStickView() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        int scrollHeight = getScrollHeight();
        if (headerTranslationY != -scrollHeight) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(-scrollHeight).setDuration(200).start();
        }
    }
}
*/
