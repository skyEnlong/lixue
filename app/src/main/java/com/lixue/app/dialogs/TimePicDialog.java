package com.lixue.app.dialogs;

import android.content.Context;

import com.lixue.app.common.logic.DateTimeHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import antistatic.spinnerwheel.adapters.ArrayWheelAdapter;
import antistatic.spinnerwheel.adapters.IntegerWheelAdapter;

/**
 * Created by workEnlong on 2015/5/15.
 * 时间选择器
 */
public class TimePicDialog extends CommonWheelDialog {

    public interface OnTimeChoose{
        public void onTimeChoose(String mString);
    }

    private OnTimeChoose mOnTimeChoose;

    private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");


    public TimePicDialog(Context mContext, String date, int hour, int min) {
        super(mContext);
        initView(date, hour, min);
    }



    private void initView(final String date, final int hour, int min) {
        String[] dates = new String[365];
        String curDay = getCurrentDay();

        int date_index = 0;


        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(sdf.parse(curDay));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        dates[0] = curDay;


        for (int i = 1; i < dates.length ; i++) {
            cal.add(Calendar.DATE, 1);
            dates[i] =  sdf.format(cal.getTime());

            if (date.contains(dates[i])) {
                date_index = i;
            }
        }

        final ArrayWheelAdapter<String> dateAdapter = new ArrayWheelAdapter<String>(mContext, dates);
        final IntegerWheelAdapter adapterHours = new IntegerWheelAdapter(mContext, 0, 23, "%02d");
        final IntegerWheelAdapter adapterMin = new IntegerWheelAdapter(mContext, 0, 59, "%02d");


        setAdapters(dateAdapter, adapterHours, adapterMin);

        initBaseAdapterView(mContext, dateAdapter);
        initBaseAdapterView(mContext, adapterHours);
        initBaseAdapterView(mContext, adapterMin);


        setCurDatas(date_index, hour, min, false);

        setOnWheelSelecetListener(new OnWheelSelecetInterface() {
            @Override
            public void onValuesSelect(int[] indexs, String[] values) {
                int date_index = indexs[0];
                String curDate = DateTimeHelper.getCurrentDay();

                String chooseDate = curDate;
                if (date_index > 0) {
                    chooseDate = DateTimeHelper.getNextDayByDay(curDate, date_index);
                }

                String hourString = values[1];
                String min = values[2];

                String time = chooseDate + " " + hourString + ":" + min;

                if(null != mOnTimeChoose){
                    mOnTimeChoose.onTimeChoose(time);
                }
            }
        });
    }

    public void setOnTimeChoose(OnTimeChoose mOnTimeChoose){
        this.mOnTimeChoose = mOnTimeChoose;
    }


    public String getCurrentDay() {
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTimeInMillis(System.currentTimeMillis());
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public String getNextDay(String date, int day) {
        Calendar cal = Calendar.getInstance();

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
