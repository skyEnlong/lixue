

/*
 * android-spinnerwheel
 * https://github.com/ai212983/android-spinnerwheel
 *
 * based on
 *
 * Android Wheel Control.
 * https://code.google.com/p/android-wheel/
 *
 * Copyright 2011 Yuri Kanivets
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package antistatic.spinnerwheel.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import android.R.integer;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.WheelChartView;
import antistatic.spinnerwheel.WheelChartView.ChartType;

/**
 * Abstract spinnerwheel adapter provides common functionality for adapters.
 */
public abstract class AbstractWheelMutilAdapter extends
		AbstractWheelAdapter {


	/** Text view resource. Used as a default view for adapter. */
	public static final int TEXT_VIEW_ITEM_RESOURCE = -1;

	/** No resource constant. */
	protected static final int NO_RESOURCE = 0;

	/** Default text color */
	public static final int DEFAULT_TEXT_COLOR = 0xFF101010;

	/** Default text color */
	public static final int LABEL_COLOR = 0xFF700070;

	/** Default text size */
	public static final int DEFAULT_TEXT_SIZE = 24;

	private static final String TAG = "enlong";

	// / Custom text typeface
	private Typeface textTypeface;

	// Text settings
	private int textColor = DEFAULT_TEXT_COLOR;
	private int textSize = DEFAULT_TEXT_SIZE;
	private int titleNormalColor = Color.parseColor("#CACACA");
	private int titleSelectionColor = Color.parseColor("#71BB5A");
	// Current context
	protected Context context;
	// Layout inflater
	protected LayoutInflater inflater;

	// Items resources
	protected int itemResourceId;
	protected int itemTextResourceId;
	protected int itemTitleResourceId;
	protected int[] itemchartResourceId;
	// Empty items resources
	protected int emptyItemResourceId;

	protected ConcurrentHashMap<String, Float[]> mValues;
	protected ConcurrentHashMap<String, Float[]> mLimitValues;

	
	protected List<HashMap<String, String>> mTitles;



	/**
	 * Constructor
	 * 
	 * @param context
	 *            the current context
	 */
	protected AbstractWheelMutilAdapter(Context context) {
		this(context, TEXT_VIEW_ITEM_RESOURCE);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            the current context
	 * @param itemResource
	 *            the resource ID for a layout file containing a TextView to use
	 *            when instantiating items views
	 */
	protected AbstractWheelMutilAdapter(Context context, int itemResource) {
		this(context, itemResource, NO_RESOURCE);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            the current context
	 * @param itemResource
	 *            the resource ID for a layout file containing a TextView to use
	 *            when instantiating items views
	 * @param itemTextResource
	 *            the resource ID for a text view in the item layout
	 */
	protected AbstractWheelMutilAdapter(Context context, int itemResource,
			int itemTextResource) {
		this.context = context;
		itemResourceId = itemResource;
		itemTextResourceId = itemTextResource;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * Gets text color
	 * 
	 * @return the text color
	 */
	public int getTextColor() {
		return textColor;
	}

	/**
	 * Sets text color
	 * 
	 * @param textColor
	 *            the text color to set
	 */
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public void setTitleNormalColor(int titleNomalColor) {
		titleNormalColor = titleNomalColor;
	}

	public int getTitleNormalColor() {
		return titleNormalColor;
	}

	public void setTitleSelectionColor(int titleSelectColor) {
		titleSelectionColor = titleSelectColor;
	}

	public int getTitleSelectionColor() {
		return titleSelectionColor;
	}

	
	/**
	 * Sets text typeface
	 * 
	 * @param typeface
	 *            typeface to set
	 */
	public void setTextTypeface(Typeface typeface) {
		this.textTypeface = typeface;
	}

	/**
	 * Gets text size
	 * 
	 * @return the text size
	 */
	public int getTextSize() {
		return textSize;
	}

	/**
	 * Sets text size
	 * 
	 * @param textSize
	 *            the text size to set
	 */
	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	/**
	 * Gets resource Id for items views
	 * 
	 * @return the item resource Id
	 */
	public int getItemResource() {
		return itemResourceId;
	}

	/**
	 * Sets resource Id for items views
	 * 
	 * @param itemResourceId
	 *            the resource Id to set
	 */
	public void setItemResource(int itemResourceId) {
		this.itemResourceId = itemResourceId;
	}

	/**
	 * Gets resource Id for text view in item layout
	 * 
	 * @return the item text resource Id
	 */
	public int getItemTextResource() {
		return itemTextResourceId;
	}

	/**
	 * Gets resource Id for text view in item layout
	 * 
	 * @return the item text resource Id
	 */
	public int getItemTitleResource() {
		return itemTitleResourceId;
	}

	/**
	 * Sets resource Id for text view in item layout
	 * 
	 * @param itemTextResourceId
	 *            the item text resource Id to set
	 */
	public void setItemTextResource(int itemTextResourceId) {
		this.itemTextResourceId = itemTextResourceId;
	}

	/**
	 * Sets resource Id for text view in item layout
	 * 
	 *            the item text resource Id to set
	 */
	public void setItemTitleResource(int itemTitleResourceId) {
		this.itemTitleResourceId = itemTitleResourceId;
	}

	public void setItemChartResource(int[] itmeChartResourceId) {
		this.itemchartResourceId = itmeChartResourceId;
	}

	/**
	 * Gets resource Id for empty items views
	 * 
	 * @return the empty item resource Id
	 */
	public int getEmptyItemResource() {
		return emptyItemResourceId;
	}

	/**
	 * Sets resource Id for empty items views
	 * 
	 * @param emptyItemResourceId
	 *            the empty item resource Id to set
	 */
	public void setEmptyItemResource(int emptyItemResourceId) {
		this.emptyItemResourceId = emptyItemResourceId;
	}

	/**
	 * Returns text for specified item
	 * 
	 * @param index
	 *            the item index
	 * @return the text of specified items
	 */
	protected abstract CharSequence getItemText(int index);

	public float[] mMaxValue = {0,0,0,0,0,0};

	public void setDataSource(ConcurrentHashMap<String, Float[]> values) {
		mValues = values;
		Iterator<String> iter = values.keySet().iterator();
		while (iter.hasNext()) {
			Object key = iter.next();
			Float[] val = values.get(key);
			for(int i = 0; i < val.length; i++){
				mMaxValue[i] = (mMaxValue[i] > val[i]) ? mMaxValue[i] : val[i];
			}
		}
		
	}
	
	public void setLimitDataSource(ConcurrentHashMap<String, Float[]> values) {
		mLimitValues = values;
	}
	

	public ConcurrentHashMap<String, Float[]> getDataSource() {
		return mValues;
	}

	public void setTitleSource(List<HashMap<String, String>> titles) {
		mTitles = titles;
	}

	public List<HashMap<String, String>> getTitleSource() {
		return mTitles;
	}

	public void setCurrentItem(int currentItem) {
		mCurrentItem = currentItem;
	}

	public int getCurrtentItem() {
		return mCurrentItem;
	}

	@Override
	public View getItem(int index, View convertView, ViewGroup parent) {
		if (index >= 0 && index < getItemsCount()) {
			if (convertView == null) {
				convertView = getView(itemResourceId, parent);
			}
			TextView textViewTitle = getTextView(convertView,
					itemTitleResourceId);
			// TextView textView = getTextView(convertView, itemTextResourceId);
			if(null != itemchartResourceId){
				
				for(int i =0; i < itemchartResourceId.length; i++){
					WheelChartView chartView = getChartView(convertView,
							itemchartResourceId[i]);
					
					
					chartView.setChartType(getChartShowType());
					if (-1 < index && index < getTitleSource().size()) {
						float prevalue = -1, nextvalue = -1;
						
						String key = getTitleKey(index).trim();
						
						chartView.setIsTodayCircle(isToday(key));
						
						float curValue = 0;
						
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
						textViewTitle.setTextColor(getTitleSelectionColor());
					} else {
						chartView.setIsCurrentItem(false);
						
						textViewTitle.setTextColor(getTitleNormalColor());
					}
				}
			}

			// if (textView != null) {
			CharSequence text = getItemText(index);
			if (text == null) {
				text = "";
			}
			textViewTitle.setText(text);
			textViewTitle.setTag(index);

			return convertView;
		}
		return null;
	}

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public void setDateFormat(String partten){
        format =  new SimpleDateFormat(partten);
    }

	public String getDayByOff(String key, int off) {
		// TODO Auto-generated method stub
		Date date = null;
		try {
			date = format.parse(key);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, off);
		
		return format.format(calendar.getTime());
	}

	public boolean isToday(String day){
		Date date = new Date(System.currentTimeMillis());
		return format.format(date).equals(day);
	}
	
	@Override
	public View getEmptyItem(View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = getView(emptyItemResourceId, parent);
		}
		if (convertView instanceof TextView) {
			configureTextView((TextView) convertView);
		}

		return convertView;
	}

	/**
	 * Configures text view. Is called for the TEXT_VIEW_ITEM_RESOURCE views.
	 * 
	 * @param view
	 *            the text view to be configured
	 */
	protected void configureTextView(TextView view) {
		if (itemResourceId == TEXT_VIEW_ITEM_RESOURCE) {
			view.setTextColor(textColor);
			view.setGravity(Gravity.CENTER);
			view.setTextSize(textSize);
			view.setLines(1);
		}
		if (textTypeface != null) {
			view.setTypeface(textTypeface);
		} else {
			view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
		}
	}

	/**
	 * Loads a text view from view
	 * 
	 * @param view
	 *            the text view or layout containing it
	 * @param textResource
	 *            the text resource Id in layout
	 * @return the loaded text view
	 */
	public TextView getTextView(View view, int textResource) {
		TextView text = null;
		try {
			if (textResource == NO_RESOURCE && view instanceof TextView) {
				text = (TextView) view;
			} else if (textResource != NO_RESOURCE) {
				text = (TextView) view.findViewById(textResource);
			}
		} catch (ClassCastException e) {
			Log.e("AbstractWheelAdapter",
					"You must supply a resource ID for a TextView");
			throw new IllegalStateException(
					"AbstractWheelAdapter requires the resource ID to be a TextView",
					e);
		}

		return text;
	}

	public WheelChartView getChartView(View view, int chartResource) {
		WheelChartView chartView = null;
		try {

			chartView = (WheelChartView) view.findViewById(chartResource);

		} catch (ClassCastException e) {
			Log.e("AbstractWheelAdapter",
					"You must supply a resource ID for a TextView");
			throw new IllegalStateException(
					"AbstractWheelAdapter requires the resource ID to be a TextView",
					e);
		}

		return chartView;
	}

	/**
	 * Loads view from resources
	 * 
	 * @param resource
	 *            the resource Id
	 * @return the loaded view or null if resource is not set
	 */
	public View getView(int resource, ViewGroup parent) {
		switch (resource) {
		case NO_RESOURCE:
			return null;
		case TEXT_VIEW_ITEM_RESOURCE:
			return new TextView(context);
		default:
			return inflater.inflate(resource, parent, false);
		}
	}

	public String getTitleKey(int index) {
		Iterator iter = getTitleSource().get(index).entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			return key.toString();
		}
		return "";
	}


	public void setChartShowType(ChartType chartType) {
		mChartType = chartType;
	}

	public ChartType getChartShowType() {
		return mChartType;
	}

	public void setAbstractWheel(AbstractWheel wheel) {
		mAbstractWheel = wheel;
	}

	private ChartType mChartType = ChartType.Columnar;
	private int mCurrentItem = 0;
	public AbstractWheel mAbstractWheel;

}
