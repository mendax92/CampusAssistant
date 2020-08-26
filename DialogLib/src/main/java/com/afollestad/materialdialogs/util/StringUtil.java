package com.afollestad.materialdialogs.util;

import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Meiji on 2017/7/6.
 */

public class StringUtil {

    public static String getStringNum(String s) {
        String regex = "[^0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        return matcher.replaceAll("").trim();
    }

    public static boolean isNull(String str) {
        return str == null || str.isEmpty() || str.equals("null");
    }

    public static String notNull(String str) {
        return isNull(str) ? "" : str;
    }

    public static String notNull(String str, String defaultValue) {
        return isNull(str) ? defaultValue : str;
    }

    public static boolean compareValue(String theMoney, String price) {
        if (isNull(theMoney)) {
            return false;
        }
        if (isNull(price)) {
            return false;
        }
        return !(getDouble(price) > getDouble(theMoney));
    }

    public static double getDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static Integer getInteger(Object obj) {
        try {
            return (int) Double.parseDouble(String.valueOf(obj));
        } catch (Exception e) {
            return 0;
        }
    }

    public static Integer String2Int(String str) {
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {
            return -1;
        }
    }

    public static Integer optInteger(Integer i, int def) {
        return i == null ? def : i;
    }

    public static float getFloat(float value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return Float.parseFloat(decimalFormat.format(value));
    }

    public static String getStringWithComma(String... strs) {
        String str = "";
        for (String s : strs) {
            str = str.concat(",").concat(s);
        }
        if (!isNull(str)) {
            str = str.substring(1);
        }
        return str;
    }

    public static String getAstro(String data) {
        int years, monthOfYear, day;
        years = Integer.valueOf(data.substring(0, 4));
        monthOfYear = Integer.valueOf(data.substring(5, 7));
        day = Integer.valueOf(data.substring(8, 10));

        Calendar mycalendar = Calendar.getInstance();//获取现在时间
        String nowyear = String.valueOf(mycalendar.get(Calendar.YEAR));//获取年份
        // 用文本框输入年龄
        int age = Integer.parseInt(nowyear);
        int birth = age - years;
        String[] astro = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座",
                "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};
        int[] arr = new int[]{20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};// 两个星座分割日
        int index = monthOfYear;
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < arr[monthOfYear - 1]) {
            index = index - 1;
        }
        // 返回索引指向的星座string
        return astro[index].concat(String.valueOf(birth)).concat("岁");
    }

    public static String mapToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append("Key = ").append(entry.getKey()).append(", Value = ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    public static String hidePhone(String str) {
        if (StringUtil.isNull(str) || str.length() < 8) return str;
        return str.substring(0, 3).concat("****").concat(str.substring(7));
    }

    public static SpannableString getPriceSSB(String price) {
        if (StringUtil.isNull(price)) return new SpannableString("");
        price = "￥".concat(price);
        SpannableString ss = new SpannableString(price);
        ss.setSpan(new RelativeSizeSpan(0.7f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new PriceSpan(0.7f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //        ss.setSpan(new RelativeSizeSpan(0.4f),i,length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (price.contains(".")) {
            int i = price.indexOf(".");
            int length = price.length();
            ss.setSpan(new RelativeSizeSpan(0.6f), i, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

    public static SpannableString getPriceSSB2(String price) {
        if (StringUtil.isNull(price)) return new SpannableString("");
        price = "￥".concat(price);
        SpannableString ss = new SpannableString(price);
        if (price.contains(".")) {
            int i = price.indexOf(".");
            ss.setSpan(new RelativeSizeSpan(2f), 1, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

    public static String autoSplitText(final TextView tv, float tvWidth, final String indent) {
        final String rawText = tv.getText().toString(); //原始文本
        final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        //将缩进处理成空格
        String indentSpace = "";
        float indentWidth = 0;
        if (!TextUtils.isEmpty(indent)) {
            float rawIndentWidth = tvPaint.measureText(indent);
            if (rawIndentWidth < tvWidth) {
                while ((indentWidth = tvPaint.measureText(indentSpace)) < rawIndentWidth) {
                    indentSpace += " ";
                }
            }
        }
        //将原始文本按行拆分
        String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    //从手动换行的第二行开始，加上悬挂缩进
                    if (lineWidth < 0.1f && cnt != 0) {
                        sbNewText.append(indentSpace);
                        lineWidth += indentWidth;
                    }
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }
        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }
        return sbNewText.toString();
    }
}
