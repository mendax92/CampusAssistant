package com.zhiqi.campusassistant.common.ui.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.zhiqi.campusassistant.R;

/**
 * Created by ming on 2017/2/16.
 * 基础空view
 */
public abstract class BaseEmptyView extends FrameLayout {

    public static final int STATUS_LOADING = 1;
    public static final int STATUS_EMPTY = 2;
    public static final int STATUS_FAIL = 3;

    private static final int DELAY_TIME = 1500;

    private View loadingView;

    private View emptyView;

    private View failView;

    //重试view为failView子view
    private View retryView;

    private int mEmptyStatus;

    private OnRetryClickListener onRetryClickListener;

    private long changeTime = 0;

    private Runnable delayRunnable = null;

    private Animation hideAnim;
    private Animation showAnim;

    private OnStatusChangedListener onStatusChangedListener;

    public BaseEmptyView(Context context) {
        this(context, null);
    }

    public BaseEmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        hideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.translucent_exit);
        showAnim = AnimationUtils.loadAnimation(getContext(), R.anim.translucent_enter);
        View rootView = LayoutInflater.from(getContext()).inflate(getLayoutId(), this, true);
        loadingView = rootView.findViewById(getLoadingViewId());
        emptyView = rootView.findViewById(getEmptyViewId());
        int failId = getFailViewId();
        if (failId > 0) {
            failView = rootView.findViewById(failId);
        }
        int retryId = getRetryViewId();
        if (retryId > 0) {
            retryView = rootView.findViewById(retryId);
        }
        setEmptyStatus(STATUS_LOADING);
    }

    public void setEmptyStatus(final int emptyStatus) {
        if (mEmptyStatus == emptyStatus) {
            return;
        }
        if (delayRunnable == null && changeTime > 0 && System.currentTimeMillis() - changeTime < DELAY_TIME && STATUS_LOADING == mEmptyStatus && STATUS_FAIL == emptyStatus) {
            delayRunnable = new DelayRunnable(emptyStatus);
            postDelayed(delayRunnable, DELAY_TIME);
            return;
        }
        if (delayRunnable != null) {
            removeCallbacks(delayRunnable);
        }
        changeTime = System.currentTimeMillis();
        int lastStatus = mEmptyStatus;
        this.mEmptyStatus = emptyStatus;
        View fadeView = null;
        View showView = null;
        switch (mEmptyStatus) {
            case STATUS_LOADING:
                loadingView.setVisibility(VISIBLE);
                emptyView.setVisibility(GONE);
                if (failView != null) {
                    failView.setVisibility(GONE);
                }
                fadeView = emptyView;
                if (failView != null && STATUS_FAIL == lastStatus) {
                    fadeView = failView;
                }
                showView = loadingView;
                break;
            case STATUS_FAIL:
                if (failView != null) {
                    loadingView.setVisibility(GONE);
                    emptyView.setVisibility(GONE);
                    failView.setVisibility(VISIBLE);
                    if (onRetryClickListener != null && retryView != null) {
                        retryView.setOnClickListener(onRetryClickListener);
                    }
                    fadeView = loadingView;
                    if (STATUS_EMPTY == lastStatus) {
                        fadeView = emptyView;
                    }
                    showView = failView;
                    break;
                }
                if (STATUS_EMPTY == lastStatus) {
                    return;
                }
                // 如果failView为空默认以空白页填充
            case STATUS_EMPTY:
                loadingView.setVisibility(GONE);
                emptyView.setVisibility(VISIBLE);
                if (failView != null) {
                    failView.setVisibility(GONE);
                }
                fadeView = loadingView;
                if (failView != null && STATUS_FAIL == lastStatus) {
                    fadeView = failView;
                }
                showView = emptyView;
                break;
        }
        if (showView != null) {
            showView.startAnimation(showAnim);
        }
        if (fadeView != null) {
            fadeView.startAnimation(hideAnim);
        }
        if (onStatusChangedListener != null) {
            onStatusChangedListener.onStatusChanged(lastStatus, mEmptyStatus);
        }
    }

    private class DelayRunnable implements Runnable {

        private int status;

        public DelayRunnable(int status) {
            this.status = status;
        }

        @Override
        public void run() {
            setEmptyStatus(status);
            delayRunnable = null;
        }
    }

    public void showLoadingView() {
        setEmptyStatus(STATUS_LOADING);
    }

    public void showEmptyView() {
        setEmptyStatus(STATUS_EMPTY);
    }

    public void showFailView() {
        setEmptyStatus(STATUS_FAIL);
    }


    /**
     * 空view layout
     */
    @LayoutRes
    abstract int getLayoutId();

    @IdRes
    abstract int getLoadingViewId();

    @IdRes
    abstract int getEmptyViewId();

    public int getFailViewId() {
        return 0;
    }

    public int getRetryViewId() {
        return 0;
    }

    public void setOnRetryClickListener(OnRetryClickListener onRetryClickListener) {
        this.onRetryClickListener = onRetryClickListener;
    }

    public void setOnStatusChangedListener(OnStatusChangedListener onStatusChangedListener) {
        this.onStatusChangedListener = onStatusChangedListener;
    }

    public interface OnRetryClickListener extends OnClickListener {

        void onClick(View view);
    }

    public interface OnStatusChangedListener {
        void onStatusChanged(int form, int to);
    }
}
