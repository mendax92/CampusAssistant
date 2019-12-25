package com.ming.base.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by ming on 2017/2/13.
 */

public class TextDrawableLayout extends LinearLayout {

    private TextDrawableView mTextView;

    public TextDrawableLayout(Context context) {
        this(context, null);
    }

    public TextDrawableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextDrawableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attr) {
        mTextView = new TextDrawableView(getContext(), attr);
        mTextView.setDuplicateParentStateEnabled(true);
        mTextView.setClickable(false);
        addView(mTextView);
    }

    public final void setText(CharSequence text) {
        mTextView.setText(text);
    }

    public final void setText(int resid) {
        mTextView.setText(resid);
    }

    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top,
                                     @Nullable Drawable right, @Nullable Drawable bottom) {
        mTextView.setCompoundDrawables(left, top, right, bottom);
    }

}
