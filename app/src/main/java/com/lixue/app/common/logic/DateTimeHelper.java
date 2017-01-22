package com.lixue.app.common.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by enlong on 2017/1/22.
 */

public class DateTimeHelper {
    /**
     * format yyyy-MM-dd
     * @return
     */
    public static String getCurrentDay() {
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal.setTimeInMillis(System.currentTimeMillis());
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    /**
     * format yyyy-MM-dd
     * @return
     */
    public static String getNextDayByDay(String date, int day) {
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        cal.add(Calendar.DATE, day);
        return sdf.format(cal.getTime());
    }
}
