package com.zhiqi.campusassistant.common.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.ming.base.util.DeviceUtil;
import com.zhiqi.campusassistant.R;

public class CodeView extends View {

    public final static int SHOW_TYPE_WORD = 1;
    public final static int SHOW_TYPE_PASSWORD = 2;

    //密码长度，默认6位
    private int length;
    //描边颜色，默认#E1E1E1
    private int borderColor;
    //描边宽度，默认1px
    private float borderWidth;
    //分割线颜色，默认#E1E1E1
    private int dividerColor;
    //分割线宽度，默认1px
    private float dividerWidth;
    //默认文本，在XML设置后可预览效果
    private String code;
    //密码点颜色，默认#000000
    private int codeColor;
    //密码点半径，默认8dp
    private float pointRadius;
    //显示明文时的文字大小，默认unitWidth/2
    private float textSize;
    //显示类型，支持密码、明文，默认明文
    private int showType;
    // 文字间隔
    private float dividerPadding;
    // 是否有间隔
    private boolean hasDivider;
    // 每格的背景
    private Drawable gridDrawable;

    private float unitWidth;
    private Paint paint;
    private ValueChangeListener listener;

    public CodeView(Context context) {
        super(context);
        init(null);
    }

    public CodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT
                && params.height != ViewGroup.LayoutParams.MATCH_PARENT) {
            float height = params.height;
            float realHeight = height - getPaddingTop() - getPaddingBottom();
            unitWidth = realHeight - 2 * borderWidth;
            float width = getPaddingLeft() + getPaddingRight() + 2 * borderWidth + (length - 1) * getDividerWidth() + unitWidth * length;
            setMeasuredDimension((int) width, (int) height);
        } else {
            //根据宽度来计算单元格大小（高度）
            float width = getMeasuredWidth();
            //宽度-左右边宽-中间分割线宽度
            float realWidth = width - getPaddingLeft() - getPaddingRight();
            unitWidth = (realWidth - (2 * borderWidth) - ((length - 1) * getDividerWidth())) / length;

            setMeasuredDimension((int) width, (int) (unitWidth + (2 * borderWidth)) + getPaddingTop() + getPaddingBottom());
        }
        if (textSize == 0) {
            textSize = unitWidth / 2;
        }
        if (pointRadius == 0) {
            pointRadius = unitWidth / 4;
            pointRadius = pointRadius <= 0 ? 2 : pointRadius;
        }
    }

    private void init(AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        if (attrs == null) {
            length = 6;
            borderColor = Color.parseColor("#E1E1E1");
            borderWidth = 1;
            dividerColor = Color.parseColor("#E1E1E1");
            dividerWidth = 1;
            code = "";
            codeColor = Color.parseColor("#000000");
            pointRadius = DeviceUtil.dp2px(getContext(), 8);
            showType = SHOW_TYPE_WORD;
            textSize = 0;
        } else {
            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.CodeView);
            length = typedArray.getInt(R.styleable.CodeView_length, 6);
            borderColor = typedArray.getColor(R.styleable.CodeView_borderColor, Color.parseColor("#E1E1E1"));
            borderWidth = typedArray.getDimensionPixelSize(R.styleable.CodeView_borderWidth, 1);
            dividerPadding = typedArray.getDimensionPixelSize(R.styleable.CodeView_dividerPadding, 0);
            gridDrawable = typedArray.getDrawable(R.styleable.CodeView_gridDrawable);
            hasDivider = dividerPadding > 0;
            if (!hasDivider) {
                dividerColor = typedArray.getColor(R.styleable.CodeView_dividerColor, Color.parseColor("#E1E1E1"));
                dividerWidth = typedArray.getDimensionPixelSize(R.styleable.CodeView_dividerWidth, 1);
            }
            code = typedArray.getString(R.styleable.CodeView_code);
            if (code == null) {
                code = "";
            }
            codeColor = typedArray.getColor(R.styleable.CodeView_codeColor, Color.parseColor("#000000"));
            pointRadius = typedArray.getDimensionPixelSize(R.styleable.CodeView_pointRadius, 0);
            showType = typedArray.getInt(R.styleable.CodeView_showType, SHOW_TYPE_PASSWORD);
            textSize = typedArray.getDimensionPixelSize(R.styleable.CodeView_textSize, 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (gridDrawable != null) {
            drawGridBackground(canvas);
        } else {
            if (!hasDivider) {
                drawDivider(canvas);
                drawBorder(canvas);
            } else {
                drawGrid(canvas);
            }
        }
        switch (showType) {
            case SHOW_TYPE_PASSWORD:
                drawPoint(canvas);
                break;
            default:
                drawValue(canvas);
                break;
        }
    }

    private void drawGridBackground(Canvas canvas) {
        for (int i = 0; i < length; i++) {
            int left = (int) (getPaddingLeft() + i * getDividerWidth() + i * unitWidth + borderWidth / 2);
            int top = (int) (getPaddingTop() + borderWidth / 2);
            int right = (int) (left + unitWidth - borderWidth / 2);
            int bottom = (int) (getHeight() - getPaddingBottom() - borderWidth / 2);
            gridDrawable.setBounds(left, top, right, bottom);
            gridDrawable.draw(canvas);

        }
    }

    private void drawGrid(Canvas canvas) {
        if (borderWidth > 0) {
            paint.setColor(borderColor);
            float width = paint.getStrokeWidth();
            Paint.Style style = paint.getStyle();
            paint.setStrokeWidth(borderWidth);
            paint.setStyle(Paint.Style.STROKE);
            for (int i = 0; i < length; i++) {
                final float left = getPaddingLeft() + i * getDividerWidth() + i * unitWidth + borderWidth / 2;
                float top = getPaddingTop() + borderWidth / 2;
                float right = left + unitWidth - borderWidth / 2;
                float bottom = getHeight() - getPaddingBottom() - borderWidth / 2;
                Path path = new Path();
                path.moveTo(left, top);
                path.lineTo(right, top);
                path.lineTo(right, bottom);
                path.lineTo(left, bottom);
                path.close();
                canvas.drawPath(path, paint);
            }
            paint.setStyle(style);
            paint.setStrokeWidth(width);
        }
    }

    /**
     * 描边
     */
    private void drawBorder(Canvas canvas) {
        if (borderWidth > 0) {
            paint.setColor(borderColor);
            int left = getPaddingLeft();
            int right = getWidth() - getPaddingRight();
            int top = getPaddingTop();
            int bottom = getHeight() - getPaddingBottom();
            canvas.drawRect(left, top, right, left + borderWidth, paint);
            canvas.drawRect(left, bottom - borderWidth, right, bottom, paint);
            canvas.drawRect(left, top, left + borderWidth, bottom, paint);
            canvas.drawRect(right - borderWidth, top, right, bottom, paint);
        }
    }

    /**
     * 画分割线
     */
    private void drawDivider(Canvas canvas) {
        if (dividerWidth > 0) {
            paint.setColor(dividerColor);
            for (int i = 0; i < length - 1; i++) {
                final float left = unitWidth * (i + 1) + dividerWidth * i + borderWidth + getPaddingLeft();
                canvas.drawRect(left, getPaddingTop(), left + dividerWidth, getHeight() - getPaddingBottom(), paint);
            }
        }
    }

    /**
     * 画输入文字
     */
    private void drawValue(Canvas canvas) {
        paint.setColor(codeColor);
        paint.setTextSize(textSize);
        for (int i = 0; i < code.length(); i++) {
            final float left = unitWidth * i + getDividerWidth() * i + borderWidth + getPaddingLeft();
            canvas.drawText(code.charAt(i) + "",
                    left + unitWidth / 2,
                    getTextBaseLine(0, getHeight(), paint),
                    paint);
        }
    }

    public float getTextBaseLine(float backgroundTop, float backgroundBottom, Paint paint) {
        final Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (backgroundTop + backgroundBottom - fontMetrics.bottom - fontMetrics.top) / 2;
    }

    /**
     * 画密码点
     */
    private void drawPoint(Canvas canvas) {
        if (pointRadius > 0) {
            paint.setColor(codeColor);
            for (int i = 0; i < code.length(); i++) {
                final float left = unitWidth * i + getDividerWidth() * i + borderWidth + getPaddingLeft();
                canvas.drawCircle(left + unitWidth / 2, getHeight() / 2, pointRadius, paint);
            }
        }
    }

    public void append(String number) {
        if (code.length() < length) {
            code += number;
            if (listener != null) {
                listener.onValueChanged(code);
                if (code.length() == length) {
                    listener.onComplete(code);
                }
            }
            invalidate();
        }
    }

    public void delete() {
        if (code.length() > 0) {
            code = code.substring(0, code.length() - 1);
            if (listener != null) {
                listener.onValueChanged(code);
            }
            invalidate();
        }
    }

    public void clear() {
        if (code.length() > 0) {
            code = "";
            invalidate();
        }
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
        invalidate();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        invalidate();
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public float getDividerWidth() {
        return hasDivider ? borderWidth * 2 + dividerPadding : dividerWidth;
    }

    public void setDividerWidth(float dividerWidth) {
        this.dividerWidth = dividerWidth;
        invalidate();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        invalidate();
    }

    public int getCodeColor() {
        return codeColor;
    }

    public void setCodeColor(int codeColor) {
        this.codeColor = codeColor;
        invalidate();
    }

    public float getPointRadius() {
        return pointRadius;
    }

    public void setPointRadius(float pointRadius) {
        this.pointRadius = pointRadius;
        invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
        invalidate();
    }

    public void setValueChangeListener(ValueChangeListener listener) {
        this.listener = listener;
    }

    public interface ValueChangeListener {

        public void onValueChanged(String value);

        public void onComplete(String value);

    }

}

