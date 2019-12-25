package com.ming.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ming.base.R;

/**
 * Created by ming on 2017/8/26.
 * 虚线
 */

public class DashLineView extends View {

    public static final int DEFAULT_DASH_WIDTH = 100;
    public static final int DEFAULT_LINE_WIDTH = 100;
    public static final int DEFAULT_LINE_HEIGHT = 10;
    public static final int DEFAULT_LINE_COLOR = 0x9E9E9E;

    /**
     * 虚线的方向
     */
    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;
    /**
     * 默认为水平方向
     */
    public static final int DEFAULT_DASH_ORIENTATION = ORIENTATION_HORIZONTAL;
    /**
     * 间距宽度
     */
    private float dashGap;
    /**
     * 线段高度
     */
    private float dashHeight;
    /**
     * 线段宽度
     */
    private float dashWidth;
    /**
     * 线段颜色
     */
    private int dashColor;
    private int orientation;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int widthSize;
    private int heightSize;

    public DashLineView(Context context) {
        this(context, null);
    }

    public DashLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DashLineView);
        dashGap = typedArray.getDimension(R.styleable.DashLineView_dashGap, DEFAULT_DASH_WIDTH);
        dashHeight = typedArray.getDimension(R.styleable.DashLineView_dashHeight, DEFAULT_LINE_HEIGHT);
        dashWidth = typedArray.getDimension(R.styleable.DashLineView_dashWidth, DEFAULT_LINE_WIDTH);
        dashColor = typedArray.getColor(R.styleable.DashLineView_dashColor, DEFAULT_LINE_COLOR);
        orientation = typedArray.getInteger(R.styleable.DashLineView_dashOrientation, DEFAULT_DASH_ORIENTATION);
        mPaint.setColor(dashColor);
        mPaint.setStrokeWidth(dashHeight);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        heightSize = MeasureSpec.getSize(heightMeasureSpec - getPaddingTop() - getPaddingBottom());
        if (orientation == ORIENTATION_HORIZONTAL) {
            //不管在布局文件中虚线高度设置为多少，虚线的高度统一设置为实体线段的高度
            setMeasuredDimension(widthSize, (int) dashHeight);
        } else {
            setMeasuredDimension((int) dashHeight, heightSize);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (orientation) {
            case ORIENTATION_VERTICAL:
                drawVerticalLine(canvas);
                break;
            default:
                drawHorizontalLine(canvas);
        }
    }

    /**
     * 画水平方向虚线
     *
     * @param canvas
     */
    public void drawHorizontalLine(Canvas canvas) {
        float totalWidth = 0;
        canvas.save();
        float[] pts = {0, 0, dashWidth, 0};
        //在画线之前需要先把画布向下平移办个线段高度的位置，目的就是为了防止线段只画出一半的高度
        //因为画线段的起点位置在线段左下角
        canvas.translate(0, dashHeight / 2);
        while (totalWidth <= widthSize) {
            canvas.drawLines(pts, mPaint);
            canvas.translate(dashWidth + dashGap, 0);
            totalWidth += dashWidth + dashGap;
        }
        canvas.restore();
    }

    /**
     * 画竖直方向虚线
     *
     * @param canvas
     */
    public void drawVerticalLine(Canvas canvas) {
        float totalWidth = 0;
        canvas.save();
        float[] pts = {0, 0, 0, dashWidth};
        //在画线之前需要先把画布向右平移半个线段高度的位置，目的就是为了防止线段只画出一半的高度
        //因为画线段的起点位置在线段左下角
        canvas.translate(dashHeight / 2, 0);
        while (totalWidth <= heightSize) {
            canvas.drawLines(pts, mPaint);
            canvas.translate(0, dashWidth + dashGap);
            totalWidth += dashWidth + dashGap;
        }
        canvas.restore();
    }
}