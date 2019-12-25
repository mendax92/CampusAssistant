package com.zhiqi.campusassistant.common.ui.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.util.DialogUtils;
import com.zhiqi.campusassistant.R;

/**
 * Created by ming on 2016/11/30.
 * 刷新列表
 */

public class MaterialSwipeRefreshLayout extends SwipeRefreshLayout {
    public MaterialSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public MaterialSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        int widgetColor = DialogUtils.resolveColor(context, R.attr.colorAccent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            widgetColor = DialogUtils.resolveColor(context, android.R.attr.colorAccent, widgetColor);
        }
        setColorSchemeColors(widgetColor);
    }
}
