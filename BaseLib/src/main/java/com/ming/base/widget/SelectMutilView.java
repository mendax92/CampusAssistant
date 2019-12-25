package com.ming.base.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by ming on 2016/10/12.
 * 多试图选中view
 */

public class SelectMutilView extends FrameLayout {

    private boolean mSelected;

    private OnSelectedChangeListener mOnCheckedChangeListener;

    private OnClickListener onClickListener;

    private boolean isSelectEnable;

    public SelectMutilView(Context context) {
        super(context);
        init();
    }

    public SelectMutilView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectMutilView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SelectMutilView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setClickable(true);
        setChildDuplicateParentState(this);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectEnable()) {
                    setSelected(true);
                } else if (onClickListener != null) {
                    onClickListener.onClick(SelectMutilView.this);
                }
            }
        });
    }

    public void setSelectEnable(boolean selectEnable) {
        isSelectEnable = selectEnable;
    }

    public boolean isSelectEnable() {
        return isSelectEnable;
    }

    private void setChildDuplicateParentState(ViewGroup viewGroup) {
        if (viewGroup != this) {
            viewGroup.setClickable(false);
        }
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            view.setDuplicateParentStateEnabled(true);
            view.setClickable(false);
            if (view instanceof ViewGroup) {
                setChildDuplicateParentState((ViewGroup) view);
            }
        }
    }

    private void setChildSelected(ViewGroup viewGroup, boolean selected) {
        if (viewGroup != this) {
            viewGroup.setSelected(selected);
        }
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            view.setSelected(selected);
            if (view instanceof ViewGroup) {
                setChildSelected((ViewGroup) view, selected);
            }
        }
    }

    @Override
    public void setSelected(boolean selected) {
        if (mSelected != selected) {
            if (!isSelectEnable()) {
                return;
            }
            mSelected = selected;
            super.setSelected(selected);
            //setChildSelected(this, selected);
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onSelectedChanged(this, selected);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public void setOnSelectedChangeListener(OnSelectedChangeListener onCheckedChangeListener) {
        this.mOnCheckedChangeListener = onCheckedChangeListener;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.onClickListener = l;
    }

    public interface OnSelectedChangeListener {

        void onSelectedChanged(SelectMutilView buttonView, boolean isSelected);
    }
}
