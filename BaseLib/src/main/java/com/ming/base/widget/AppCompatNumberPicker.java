package com.ming.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.widget.NumberPicker;

import com.ming.base.R;

import java.lang.reflect.Field;

/**
 * Created by ming on 2017/4/8.
 * 兼容NumberPicker
 */

public class AppCompatNumberPicker extends NumberPicker {

    private int dividerColor;
    private int dividerHeight;

    public AppCompatNumberPicker(Context context) {
        this(context, null);
    }

    public AppCompatNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AppCompatNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberPicker);
        if (typedArray != null) {
            int colorAccent = resolveColor(context, R.attr.colorAccent);
            dividerColor = typedArray.getColor(R.styleable.NumberPicker_picker_divider_color, colorAccent);
            dividerHeight = typedArray.getDimensionPixelSize(R.styleable.NumberPicker_picker_divider_height, 0);
            typedArray.recycle();
            setDividerColor(dividerColor);
        }
    }

    @ColorInt
    public int resolveColor(Context context, @AttrRes int attr) {
        return resolveColor(context, attr, 0);
    }

    @ColorInt
    public int resolveColor(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getColor(0, fallback);
        } finally {
            a.recycle();
        }
    }

    private void setDividerColor(int color) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        boolean breaking = false;
        for (Field pf : pickerFields) {
            try {
                boolean isBread = breaking;
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    //设置分割线的颜色值 透明
                    pf.set(this, new ColorDrawable(color));
                    breaking = true;
                } else if (pf.getName().equals("mSelectionDividerHeight")) {
                    pf.setAccessible(true);
                    pf.set(this, dividerHeight);
                    breaking = true;
                }
                if (isBread) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
