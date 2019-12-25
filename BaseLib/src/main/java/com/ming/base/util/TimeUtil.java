package com.ming.base.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * TimeUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtil {

    public static final SimpleDateFormat FORMAT_DATE_DEFAULT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static final SimpleDateFormat FORMAT_MINUTE = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    private static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000L;

    public static String getTime(long timeInMillis, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return getTime(timeInMillis, dateFormat);
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #FORMAT_DATE_DEFAULT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, FORMAT_DATE_DEFAULT);
    }


    /**
     * get current time in seconds
     *
     * @return
     */
    public static long getSimpleCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #FORMAT_DATE_DEFAULT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * 周格式化
     *
     * @param time
     * @param format
     * @return
     */
    public static String getWeekOfChinese(long time, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return getWeekOfChinese(dayOfWeek, format);
    }

    /**
     * 周格式化
     *
     * @param dayOfWeek 周几
     * @param format
     * @return
     */
    public static String getWeekOfChinese(int dayOfWeek, String format) {
        switch (dayOfWeek) {
            case 0:
            case 7:
                return String.format(format, "日");
            case 1:
                return String.format(format, "一");
            case 2:
                return String.format(format, "二");
            case 3:
                return String.format(format, "三");
            case 4:
                return String.format(format, "四");
            case 5:
                return String.format(format, "五");
            case 6:
                return String.format(format, "六");
        }
        return null;
    }

    /**
     * 获取当前时间，主要以field决定值
     *
     * @param field
     * @return
     */
    public static int getField(int field) {
        return getField(System.currentTimeMillis(), field);
    }

    /**
     * 根据时间戳获取时间内容，主要以field决定值
     *
     * @param time
     * @param field
     * @return
     */
    public static int getField(long time, int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int date = cal.get(field);
        if (Calendar.MONTH == field) {
            return date + 1;
        }
        return date;
    }

    /**
     * 是否为同一天
     *
     * @param time1
     * @param time2
     * @return
     */
    public static boolean isSameDay(long time1, final long time2) {
        final long interval = time1 - time2;
        return interval < ONE_DAY_MILLIS
                && interval > -1L * ONE_DAY_MILLIS
                && toDay(time1) == toDay(time2);
    }

    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / ONE_DAY_MILLIS;
    }

    /**
     * 两个时间相差天数，time2-tim1
     *
     * @param time1
     * @param time2
     * @return
     */
    public static int daysBetween(long time1, long time2) {
        return (int) ((time1 - time2) / ONE_DAY_MILLIS);
    }

    /**
     * 两个时间相差周数，time1-tim2
     *
     * @param time1
     * @param time2
     * @return
     */
    public static int weeksBetween(long time1, long time2) {
        return (int) ((time1 - time2) / (7 * ONE_DAY_MILLIS));
    }

    /**
     * 获取周一
     *
     * @param time
     * @return
     */
    public static long getFirstDayOfWeek(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTimeInMillis();
    }


    /**
     * 日期所在周的结束日期
     */
    public static long getLastDayOfWeek(long time) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTimeInMillis(time);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTimeInMillis();
    }

    /**
     * 获取几周后的时间
     *
     * @param time
     * @param week
     * @return
     */
    public static long getAfterWeekTime(long time, int week) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.add(Calendar.DATE, week * 7);
        return cal.getTimeInMillis();
    }

    /**
     * 获取几天后的时间
     *
     * @param time
     * @param day
     * @return
     */
    public static long getAfterDayTime(long time, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.add(Calendar.DATE, day);
        return cal.getTimeInMillis();
    }
}
