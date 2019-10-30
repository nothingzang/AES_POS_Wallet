package com.example.aes_pos_wallet.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    static SimpleDateFormat sfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

    public static String getDateToHMS(long time) {
        Date d = new Date(time);
        return sfDate.format(d);
    }

    public static String getDateToYMD(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }

    public static long fromDateToHMS(String time) throws ParseException {
        long timeMillon = sfDate.parse(time).getTime();
        return timeMillon;
    }

    public static long fromDateToYMD(String time) throws ParseException {
        long timeMillon = sf.parse(time).getTime();
        return timeMillon;
    }

    public static boolean isSameDayHMS(String day1, String day2) throws ParseException {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(fromDateToHMS(day1));
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(fromDateToHMS(day2));
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDayYMD(String day1, String day2) throws ParseException {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(fromDateToYMD(day1));
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(fromDateToYMD(day2));
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

}
