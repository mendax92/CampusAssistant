package com.zhiqi.campusassistant.common.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.ming.base.widget.AppEditText;

/**
 * 监听在有软键盘的时候的按键事件
 */
public class KeyImeEditText extends AppEditText {

    private KeyImeChangeListener mKeyImeChangeListener;

    public KeyImeEditText(Context context) {
        super(context);
    }

    public KeyImeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyImeEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public interface KeyImeChangeListener {
        void onKeyIme(int keyCode, KeyEvent event);
    }

    public void setKeyImeChangeListener(KeyImeChangeListener listener) {
        this.mKeyImeChangeListener = listener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (mKeyImeChangeListener != null) {
            mKeyImeChangeListener.onKeyIme(keyCode, event);
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection connection = super.onCreateInputConnection(outAttrs);
        outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        return connection;
    }
}
