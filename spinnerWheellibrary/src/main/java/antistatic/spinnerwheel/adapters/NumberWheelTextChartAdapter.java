package antistatic.spinnerwheel.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Iterator;
import java.util.Map;

import antistatic.spinnerwheel.R;
import antistatic.spinnerwheel.WheelChartView;

/**
 * Created by workEnlong on 2015/4/1.
 */
public class NumberWheelTextChartAdapter extends AbstractWheelMutilAdapter{

    /** The default min value */
    public static final int DEFAULT_MAX_VALUE = 9;

    /** The default max value */
    private static final int DEFAULT_MIN_VALUE = 0;

    // Values
    private int minValue;
    private int maxValue;
    // format
    private String format;
    private int[] pointSourceIds;
    private int itemValueResource;

    /**
     * Constructor
     *
     * @param context
     *            the current context
     */
    public NumberWheelTextChartAdapter(Context context) {
        this(context, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
    }

    /**
     * Constructor
     *
     * @param context
     *            the current context
     * @param minValue
     *            the spinnerwheel min value
     * @param maxValue
     *            the spinnerwheel max value
     */
    public NumberWheelTextChartAdapter(Context context, int minValue, int maxValue) {
        this(context, minValue, maxValue, null);
    }

    /**
     * Constructor
     *
     * @param context
     *            the current context
     * @param minValue
     *            the spinnerwheel min value
     * @param maxValue
     *            the spinnerwheel max value
     * @param format
     *            the format string
     */
    public NumberWheelTextChartAdapter(Context context, int minValue, int maxValue,
                                   String format) {
        super(context);

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = format;
    }

    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < getItemsCount()) {
            // int value = minValue + index;
            // return (format != null ? String.format(format, value) : Integer
            // .toString(value));
            Iterator iter = getTitleSource().get(index).entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                return val.toString();
            }

        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return maxValue - minValue + 1;
    }

    @Override
    public int getSelectionTextColor() {
        // TODO Auto-generated method stub
        return Color.GREEN;
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        if (index >= 0 && index < getItemsCount()) {
            if (convertView == null) {
                convertView = getView(itemResourceId, parent);
            }
            TextView textViewTitle = getTextView(convertView,
                    itemTitleResourceId);
            TextView textView = getTextView(convertView, itemTextResourceId);

            TextView value_txt = getTextView(convertView, itemValueResource);;

            if(null != itemchartResourceId){

                float curValue = 0;
                for(int i =0; i < itemchartResourceId.length; i++){
                    WheelChartView chartView = getChartView(convertView,
                            itemchartResourceId[i]);
                    chartView.setCurvePointIds(pointSourceIds);

                    chartView.setChartType(getChartShowType());
                    if (-1 < index && index < getTitleSource().size()) {
                        float prevalue = -1, nextvalue = -1;

                        String key = getTitleKey(index).trim();
                        boolean isToday = isToday(key);
                        chartView.setIsTodayCircle(isToday);


                        if (getDataSource().containsKey(key)) {
                            curValue = getDataSource().get(key)[i];
                        }

                        String pre_day = getDayByOff(key, -1);

                        if (getDataSource().containsKey(pre_day)) {
                            prevalue = getDataSource().get(pre_day)[i];
                        }

                        String next_day = getDayByOff(key, 1);

                        if (getDataSource().containsKey(next_day)) {
                            nextvalue = getDataSource().get(next_day)[i];
                        }

                        if(null != mLimitValues && mLimitValues.containsKey(key)){
                            if(mLimitValues.get(key)[i] != 0){

                                chartView.setIsRealData(true);
                            }else{
                                chartView.setIsRealData(false);
                            }
                        }else{
                            chartView.setIsRealData(false);
                        }

                        chartView.setMaxValue(mMaxValue[i]);
                        chartView.setChartValues(curValue, prevalue, nextvalue);

                    }
                    if (index == mAbstractWheel.getCurrentItem()) {
                        chartView.setIsCurrentItem(true);
                        textViewTitle.setAlpha(1.0f);
                        textView.setAlpha(1.0f);
                    } else {
                        chartView.setIsCurrentItem(false);
                        textViewTitle.setAlpha(0.6f);
                        textView.setAlpha(0.6f);

                    }
                }
            }



            // if (textView != null) {
            CharSequence text = getItemText(index);
            if (text == null) {
                text = "";
            }

            if(null != textView){

                textView.setText(text);
            }

            text = getTitleKey(index);

            if (text == null) {
                text = "";
            }

            if(null != textView)
            {
                textViewTitle.setText(text.subSequence(3, text.length()));
//                textViewTitle.setTag(index);
            }

            return convertView;
        }
        return null;
    }


    /**
     * setting the drawable resource id for dot
     * @param pointSourceIds
     */
    public void setPointSourceId(int[] pointSourceIds){
        this.pointSourceIds = pointSourceIds;
    }

    public void setItemValueResource(int itemValueResource) {
        this.itemValueResource = itemValueResource;
    }

    public void setBackground(View mView, Drawable drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mView.setBackground( drawable);
        } else {
            mView.setBackgroundDrawable(drawable);
        }
    }
}
