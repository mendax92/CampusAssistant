package com.ming.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ming.base.R;


/**
 * 自带清除按钮的输入框，清除按钮的作用是清空输入框的输入内容；
 * 需要注意的是，清除按钮会占据drawableRight的位置，所以设置drawableRight会无效
 */
public class ClearEditText extends AppEditText {
    /**
     * 清楚按钮的图标
     */
    private Drawable drawableClear;

    private float dx;
    private float dy;
    private boolean clearCancel;

    public ClearEditText(Context context) {
        super(context);
        init(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int iconWidth = 0;
        int iconHeight = 0;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText);
            drawableClear = array.getDrawable(R.styleable.ClearEditText_edit_clear_icon);
            iconWidth = array.getDimensionPixelSize(R.styleable.ClearEditText_edit_clear_icon_width, 0);
            iconHeight = array.getDimensionPixelSize(R.styleable.ClearEditText_edit_clear_icon_height, 0);
            array.recycle();
        }
        if (drawableClear == null) {
            final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.abc_ic_clear_material);
            drawableClear = DrawableCompat.wrap(drawable); //Wrap the drawable so that it can be tinted pre Lollipop
            DrawableCompat.setTint(drawableClear, getCurrentHintTextColor());
        }
        drawableClear.setBounds(0, 0, iconWidth <= 0 ? drawableClear.getIntrinsicHeight() : iconWidth, iconHeight <= 0 ? drawableClear.getIntrinsicHeight() : iconHeight);
        updateIconClear();

        // 设置TextWatcher用于更新清除按钮显示状态
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateIconClear();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 点击是的 x 坐标
        // 清除按钮的起始区间大致为[getWidth() - getCompoundPaddingRight(), getWidth()]，
        // 点击的x坐标在此区间内则可判断为点击了清除按钮
        if (event.getX() >= (getWidth() - getCompoundPaddingRight()) && event.getX() < getWidth()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    clearCancel = false;
                    dx = event.getX();
                    dy = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    if (!clearCancel) {
                        // 清空文本
                        setText("");
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(event.getX() - dx) < 20 && Math.abs(event.getY() - dy) < 20) {
                        break;
                    }
                case MotionEvent.ACTION_CANCEL:
                    clearCancel = true;
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 更新清除按钮图标显示
     */
    public void updateIconClear() {
        // 获取设置好的drawableLeft、drawableTop、drawableRight、drawableBottom
        Drawable[] drawables = getCompoundDrawables();
        if (length() > 0) //输入框有输入内容才显示删除按钮
        {
            setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawableClear,
                    drawables[3]);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], null,
                    drawables[3]);
        }
    }

    /**
     * 设置清除按钮图标样式
     *
     * @param resId 图标资源id
     */
    public void setIconClear(@DrawableRes int resId) {
        drawableClear = getResources().getDrawable(resId);
        updateIconClear();
    }
}
