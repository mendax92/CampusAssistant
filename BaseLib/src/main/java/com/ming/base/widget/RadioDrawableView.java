package com.ming.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

import com.ming.base.R;

/**
 * Created by ming on 2016/9/14.
 * 单选drawable
 */
public class RadioDrawableView extends AppCompatRadioButton {

    private int leftDrawableWidth = 0;

    private int leftDrawableHeight = 0;

    private int topDrawableWidth = 0;

    private int topDrawableHeight = 0;

    private int rightDrawableWidth = 0;

    private int rightDrawableHeight = 0;

    private int bottomDrawableWidth = 0;

    private int bottomDrawableHeight = 0;

    private int bodyWidth = 0;

    public RadioDrawableView(Context context) {
        super(context);
    }

    public RadioDrawableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RadioDrawableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.minor_drawable);
        if (typedArray != null) {
            leftDrawableWidth = typedArray.getDimensionPixelSize(R.styleable.minor_drawable_left_drawable_width, 0);
            leftDrawableHeight = typedArray.getDimensionPixelSize(R.styleable.minor_drawable_left_drawable_height, 0);
            topDrawableWidth = typedArray.getDimensionPixelSize(R.styleable.minor_drawable_top_drawable_width, 0);
            topDrawableHeight = typedArray.getDimensionPixelSize(R.styleable.minor_drawable_top_drawable_height, 0);
            rightDrawableWidth = typedArray.getDimensionPixelSize(R.styleable.minor_drawable_right_drawable_width, 0);
            rightDrawableHeight = typedArray.getDimensionPixelSize(R.styleable.minor_drawable_right_drawable_height, 0);
            bottomDrawableWidth = typedArray.getDimensionPixelSize(R.styleable.minor_drawable_bottom_drawable_width, 0);
            bottomDrawableHeight = typedArray.getDimensionPixelSize(R.styleable.minor_drawable_bottom_drawable_height, 0);
            typedArray.recycle();
        }
        initDrawables();
    }

    private void initDrawables() {
        Drawable[] drawables = getCompoundDrawables();
        initDrawableSize(drawables);
    }


    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        initDrawableSize(new Drawable[]{left, top, right, bottom});
    }

    private void initDrawableSize(Drawable[] drawables) {
        if (drawables.length > 0) {
            Drawable left = drawables[0];
            if (left != null) {
                Rect rect = left.getBounds();
                if (leftDrawableWidth <= 0) {
                    leftDrawableWidth = left.getIntrinsicWidth();
                }
                if (leftDrawableHeight <= 0) {
                    leftDrawableHeight = left.getIntrinsicHeight();
                }
                left.setBounds(rect.left, rect.top, rect.left + leftDrawableWidth, rect.top + leftDrawableHeight);
            }
            Drawable top = drawables[1];
            if (top != null) {
                Rect rect = top.getBounds();
                if (topDrawableWidth <= 0) {
                    topDrawableWidth = top.getIntrinsicWidth();
                }
                if (topDrawableHeight <= 0) {
                    topDrawableHeight = top.getIntrinsicHeight();
                }
                top.setBounds(rect.left, rect.top, rect.left + topDrawableWidth, rect.top + topDrawableHeight);
            }
            Drawable right = drawables[2];
            if (right != null) {
                Rect rect = right.getBounds();
                if (rightDrawableWidth <= 0) {
                    rightDrawableWidth = right.getIntrinsicWidth();
                }
                if (rightDrawableHeight <= 0) {
                    rightDrawableHeight = right.getIntrinsicHeight();
                }
                right.setBounds(rect.left, rect.top, rect.left + rightDrawableWidth, rect.top + rightDrawableHeight);
            }
            Drawable bottom = drawables[3];
            if (bottom != null) {
                Rect rect = bottom.getBounds();
                if (bottomDrawableWidth <= 0) {
                    bottomDrawableWidth = bottom.getIntrinsicWidth();
                }
                if (bottomDrawableHeight <= 0) {
                    bottomDrawableHeight = bottom.getIntrinsicHeight();
                }
                bottom.setBounds(rect.left, rect.top, rect.left + bottomDrawableWidth, rect.top + bottomDrawableHeight);
            }
            super.setCompoundDrawables(left, top, right, bottom);
        }
    }
}
