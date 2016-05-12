package com.nju.util;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xiaojuzhang on 2015/12/24.
 */
public class DateUtil {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    public static Calendar getCalendar(String strDate) {
        try {
            Date date = format.parse(strDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int year(Calendar calendar) {
        return calendar != null ? calendar.get(Calendar.YEAR) : 0;
    }

    public static int month(Calendar calendar) {
        return calendar != null ? calendar.get(Calendar.MONTH) + 1 : 0;
    }

    public static int day(Calendar calendar) {
        return calendar != null ? calendar.get(Calendar.DAY_OF_MONTH) : 0;
    }

    public static int hour(Calendar calendar) {
        return calendar != null ? calendar.get(Calendar.HOUR_OF_DAY) : 0;
    }

    public static int minute(Calendar calendar) {
        return calendar != null ? calendar.get(Calendar.MINUTE) : 0;
    }
    public static int second(Calendar calendar) {
        return calendar != null ? calendar.get(Calendar.SECOND) : 0;
    }

    public static long getTime(String strDate) {
        try {
            Date date = format.parse(strDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getRelativeTimeSpanString(String date) {
        final long time = DateUtil.getTime(date);
        return DateUtils.getRelativeTimeSpanString(time, new Date().getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_NUMERIC_DATE).toString();
    }

    public static String getTimeString(String date){
        Calendar calendar = getCalendar(date);
        return month(calendar)+"-"+day(calendar)+" "+hour(calendar)+":"+minute(calendar)+":"+second(calendar);
    }

    public static String getNoZeroMonth(String month) {
        if (month.startsWith("0")) {
            return month.charAt(1) + Constant.MONTH;
        }
        return month + Constant.MONTH;
    }

}
