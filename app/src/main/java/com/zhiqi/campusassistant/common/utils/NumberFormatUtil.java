package com.zhiqi.campusassistant.common.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by ming on 2016/10/11.
 */

public class NumberFormatUtil {

    /**
     * 数字转RMB
     */
    public static String formatRMB(double rmb) {
        NumberFormat nf = new DecimalFormat("¥ ######0.00");
        return nf.format(rmb);
    }

    /**
     * 数字转RMB
     */
    public static String formatRMB(int rmb) {
        NumberFormat nf = new DecimalFormat("¥ ######0");
        return nf.format(rmb);
    }
}
