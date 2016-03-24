package com.nju.util;

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
        return  null;
    }

    public static int year(Calendar calendar) {
        return calendar!=null?calendar.get(Calendar.YEAR):0;
    }

    public static int month(Calendar calendar) {
        return calendar!=null?calendar.get(Calendar.MONTH)+1:0;
    }

    public static int day(Calendar calendar) {
        return calendar!=null?calendar.get(Calendar.DAY_OF_MONTH):0;
    }

    public static long getTime(String strDate)  {
        try {
            Date date = format.parse(strDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
