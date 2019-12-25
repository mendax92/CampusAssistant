package com.zhiqi.campusassistant.common.ui.widget;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by ming on 2017/3/9.
 */

public class DecimalFilter implements InputFilter {

    private int length;

    public DecimalFilter(int decimalLength) {
        this.length = decimalLength;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (dest.length() == 0 && source.equals(".")) {
            return "0.";
        }
        String dValue = dest.toString();
        String[] splitArray = dValue.split("\\.");
        if (splitArray.length > 1) {
            String dotValue = splitArray[1];
            if (dotValue.length() == length) {
                return "";
            }
        }
        return null;
    }
}
