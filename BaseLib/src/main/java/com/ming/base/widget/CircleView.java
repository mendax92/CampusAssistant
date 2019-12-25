package com.ming.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.ming.base.R;

/**
 * Created by ming on 2016/12/21.
 */

public class CircleView extends View {

    private Paint mTextPain;

    private int color;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        configPaint(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        configPaint(attrs);
    }

    private void configPaint(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typeArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircleView);
            color = typeArray.getColor(R.styleable.CircleView_circle_color, Color.TRANSPARENT);
            typeArray.recycle();
        }
        mTextPain = new Paint();
        mTextPain.setAntiAlias(true);           //开启抗锯齿，平滑文字和圆弧的边缘
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth() / 2;
        int height = getHeight() / 2;
        int radius = Math.min(width, height);
        //paint bg
        if (color == 0) {
            Drawable bg = getBackground();
            if (bg != null && bg instanceof ColorDrawable) {
                color = ((ColorDrawable) bg).getColor();
            }
        }
        if (color == 0) {
            color = Color.TRANSPARENT;
        }
        mTextPain.setColor(color);     //设置画笔颜色为随机颜色
        canvas.drawCircle(width, height, radius, mTextPain);        //利用canvas画一个圆
    }

    public void setCircleColor(int color) {
        this.color = color;
        invalidate();
    }

    public void setCircleColorResource(int colorRes) {
        setCircleColor(ContextCompat.getColor(getContext(), colorRes));
    }
}
