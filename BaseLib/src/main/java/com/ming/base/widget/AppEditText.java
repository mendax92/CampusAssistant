package com.ming.base.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ming on 2017/4/9.
 * 验证editText
 */

public class AppEditText extends AppCompatEditText {

    public AppEditText(Context context) {
        super(context);
    }

    public AppEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CharSequence getValidText() {
        CharSequence text = getText();
        if (!TextUtils.isEmpty(text)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(text);
            String strNoBlank = m.replaceAll("");
            if (TextUtils.isEmpty(strNoBlank)) {
                return "";
            }
        }
        return text;
    }
}
