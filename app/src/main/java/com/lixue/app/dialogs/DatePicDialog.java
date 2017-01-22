package com.lixue.app.dialogs;

import android.content.Context;

import java.util.Calendar;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.IntegerWheelAdapter;

/**
 * Created by workEnlong on 2015/2/10.
 * 日期选择器
 */
public class DatePicDialog extends CommonWheelDialog{
    private int MIN_YEAR = 1920;
    private int MAX_YEAR = 2030;

    public DatePicDialog(Context mContext, int year, int month, int day){
        super(mContext);
        initDate(year, month, day);
    }

    public void initDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        MAX_YEAR = calendar.get(Calendar.YEAR);
        MIN_YEAR = MAX_YEAR - 100;

       final IntegerWheelAdapter adapterYear = getYearAdapter();

        final IntegerWheelAdapter adapterMonth = getMonthAdapter();

        calendar.set(year,month -1, day);
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        final IntegerWheelAdapter adapterDay = getDayAdapter(maxDays);
        setAdapters(adapterYear, adapterMonth, adapterDay);

        initBaseAdapterView(mContext, adapterYear);
        initBaseAdapterView(mContext, adapterMonth);
        initBaseAdapterView(mContext, adapterDay);

        adapterYear.setTextSize(14);
        adapterDay.setSelectionTextSize(16);  // 有些手机显示不完整，取小点点

        setCurDatas(year - MIN_YEAR, month - 1, day -1, false );

        addOnWheelScrollListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(AbstractWheel wheel) {

            }

            @Override
            public void onScrollingFinished(AbstractWheel wheel) {
                if(wheel.equals(wheelLeft)){
                    updateDays(wheelLeft, wheelCenter, wheelRight);
                }else if(wheel.equals(wheelCenter)){
                    updateDays(wheelLeft, wheelCenter, wheelRight);
                }
            }
        });
    }

    public IntegerWheelAdapter getYearAdapter(){
        return new IntegerWheelAdapter(mContext, MIN_YEAR, MAX_YEAR, "%02d" );
    }


    public IntegerWheelAdapter getMonthAdapter(){
        return new IntegerWheelAdapter(mContext, 1, 12, "%02d");
    }


    public IntegerWheelAdapter getDayAdapter(int maxDays){
        return new IntegerWheelAdapter(mContext, 1, maxDays, "%02d");
    }

    private void updateDays(AbstractWheel year, AbstractWheel month, AbstractWheel day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,  MIN_YEAR + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());


        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //0, 60, 1, "%02d"
        IntegerWheelAdapter dayAdapter = new IntegerWheelAdapter(mContext,1, maxDays, "%02d");
        initBaseAdapterView(mContext, dayAdapter);
        day.setViewAdapter(dayAdapter);
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }
}
