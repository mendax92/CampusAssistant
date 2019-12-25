package com.zhiqi.campusassistant.common.ui.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.zhiqi.campusassistant.R;

import me.zhanghai.android.materialprogressbar.IndeterminateCircularProgressDrawable;

/**
 * Created by ming on 2017/4/5.
 * 默认缺省view
 */

public class SimpleEmptyView extends BaseEmptyView {

    private ProgressBar mProgress;

    private TextView emptyTip;

    private CharSequence tipMessage;

    public SimpleEmptyView(Context context) {
        this(context, null);
    }

    public SimpleEmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mProgress = (ProgressBar) findViewById(R.id.progress);
        int color = ContextCompat.getColor(getContext(), R.color.colorAccent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            IndeterminateCircularProgressDrawable d = new IndeterminateCircularProgressDrawable(getContext());
            d.setTint(color);
            mProgress.setProgressDrawable(d);
            mProgress.setIndeterminateDrawable(d);
        } else {
            MDTintHelper.setTint(mProgress, color);
        }
        emptyTip = (TextView) findViewById(R.id.empty_tip);
        setOnStatusChangedListener(listener);
    }

    private OnStatusChangedListener listener = new OnStatusChangedListener() {
        @Override
        public void onStatusChanged(int form, int to) {
            if (TextUtils.isEmpty(tipMessage)) {
                return;
            }
            if (STATUS_EMPTY == to || STATUS_FAIL == to) {
                emptyTip.setText(tipMessage);
            }
        }
    };

    public void setTipMessage(CharSequence tipMessage) {
        this.tipMessage = tipMessage;
    }

    @Override
    int getLayoutId() {
        return R.layout.view_common_empty_simple;
    }

    @Override
    int getLoadingViewId() {
        return R.id.loading_view;
    }

    @Override
    int getEmptyViewId() {
        return R.id.empty_view;
    }
}
