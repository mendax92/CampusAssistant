package com.ming.base.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by ming on 2016/10/13.
 */

public class NumberUtil {

    /**
     * 数字转字符串，不足时以0补足
     *
     * @param num            数字
     * @param fractionDigits 保留小数位
     * @return
     */
    public static String formatWhole(double num, int fractionDigits) {
        StringBuilder sb = new StringBuilder("0.");
        for (int i = 0; i < fractionDigits; i++) {
            sb.append("0");
        }
        DecimalFormat df = new DecimalFormat(sb.toString());
        return df.format(num);
    }

    /**
     * 数字转字符串
     *
     * @param num
     * @param fractionDigits 保留小数位
     * @return
     */
    public static String format(double num, int fractionDigits) {
        StringBuilder sb = new StringBuilder(".");
        for (int i = 0; i < fractionDigits; i++) {
            sb.append("#");
        }
        NumberFormat nf = new DecimalFormat("######" + sb.toString());
        return nf.format(num);
    }

    /**
     * 数字转字符串，不保留小数位
     *
     * @param num 数据
     * @return
     */
    public static String format(double num) {
        NumberFormat nf = new DecimalFormat("######0");
        return nf.format(num);
    }
}
