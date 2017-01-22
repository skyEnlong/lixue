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

package antistatic.spinnerwheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Spinner wheel vertical view.
 *
 * @author Yuri Kanivets
 * @author Dimitri Fedorov
 */
public class WheelVerticalView extends AbstractWheelView {

    private static int itemID = -1;

    @SuppressWarnings("unused")
    private final String LOG_TAG = WheelVerticalView.class.getName() + " #"
            + (++itemID);

    /**
     * The height of the selection divider.
     */
    protected int mSelectionDividerHeight;

    // Cached item height
    private int mItemHeight = 0;

    // --------------------------------------------------------------------------
    //
    // Constructors
    //
    // --------------------------------------------------------------------------

    /**
     * Create a new wheel vertical view.
     *
     * @param context The application environment.
     */
    public WheelVerticalView(Context context) {
        this(context, null);
    }

    /**
     * Create a new wheel vertical view.
     *
     * @param context The application environment.
     * @param attrs   A collection of attributes.
     */
    public WheelVerticalView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.abstractWheelViewStyle);
    }

    /**
     * Create a new wheel vertical view.
     *
     * @param context  the application environment.
     * @param attrs    a collection of attributes.
     * @param defStyle The default style to apply to this view.
     */
    public WheelVerticalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // --------------------------------------------------------------------------
    //
    // Initiating assets and setter for selector paint
    //
    // --------------------------------------------------------------------------

    @Override
    protected void initAttributes(AttributeSet attrs, int defStyle) {
        super.initAttributes(attrs, defStyle);

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.WheelVerticalView, defStyle, 0);
        mSelectionDividerHeight = a.getDimensionPixelSize(
                R.styleable.WheelVerticalView_selectionDividerHeight,
                DEF_SELECTION_DIVIDER_SIZE);
        a.recycle();
    }

    @Override
    public void setSelectorPaintCoeff(float coeff) {
        LinearGradient shader;
        Log.d("wheel", String.valueOf(coeff));
        int h = getMeasuredHeight();
        int ih = getItemDimension();
        float p1 = (1 - ih / (float) h) / 2;
        float p2 = (1 + ih / (float) h) / 2;
        float z = mItemsDimmedAlpha * (1 - coeff);
        float c1f = z + 255 * coeff;

        if (mVisibleItems == 2) {
            int c1 = Math.round(c1f) << 24;
            int c2 = Math.round(z) << 24;
            int[] colors = {c2, c1, 0xff000000, 0xff000000, c1, c2};
            float[] positions = {0, p1, p1, p2, p2, 1};
            shader = new LinearGradient(0, 0, 0, h, colors, positions,
                    Shader.TileMode.CLAMP);
        } else {
            float p3 = (1 - ih * 7 / (float) h) / 2;
            float p4 = (1 + ih * 7 / (float) h) / 2;

            float s = 255 * p3 / p1;
            float c3f = s * coeff; // here goes some optimized stuff
            float c2f = z + c3f;

            int c1 = Math.round(c1f) << 24;
            int c2 = Math.round(c2f) << 24;
            int c3 = Math.round(c3f) << 24;

            int[] colors = {0, c3, c2, c1, 0xff000000, 0xff000000, c1, c2, c3,
                    0};
            // int[] colors = {0, c3, c2, c1, 0xFF0000, 0xFF0000, c1, c2, c3,
            // 0};

            float[] positions = {0, p3, p3, p1, p1, p2, p2, p4, p4, 1};
            shader = new LinearGradient(0, 0, 0, h, colors, positions,
                    Shader.TileMode.CLAMP);
        }
        mSelectorWheelPaint.setShader(shader);
        invalidate();
    }

    // --------------------------------------------------------------------------
    //
    // Scroller-specific methods
    //
    // --------------------------------------------------------------------------

    @Override
    protected WheelScroller createScroller(
            WheelScroller.ScrollingListener scrollingListener) {
        return new WheelVerticalScroller(getContext(), scrollingListener);
    }

    @Override
    protected float getMotionEventPosition(MotionEvent event) {
        return event.getY();
    }

    // --------------------------------------------------------------------------
    //
    // Base measurements
    //
    // --------------------------------------------------------------------------

    @Override
    protected int getBaseDimension() {
        return getHeight();
    }

    /**
     * Returns height of the spinnerwheel
     *
     * @return the item height
     */
    @Override
    protected int getItemDimension() {
        if (mItemHeight != 0) {
            return mItemHeight;
        }

        if (mItemsLayout != null && mItemsLayout.getChildAt(0) != null) {
            mItemHeight = mItemsLayout.getChildAt(0).getMeasuredHeight();
            return mItemHeight;
        }

        return getBaseDimension() / mVisibleItems;
    }

    // --------------------------------------------------------------------------
    //
    // Layout creation and measurement operations
    //
    // --------------------------------------------------------------------------

    /**
     * Creates item layout if necessary
     */
    @Override
    protected void createItemsLayout() {
        if (mItemsLayout == null) {
            mItemsLayout = new LinearLayout(getContext());
            mItemsLayout.setOrientation(LinearLayout.VERTICAL);
        }
    }

    @Override
    protected void doItemsLayout() {
        mItemsLayout.layout(0, 0, getMeasuredWidth() - 2 * mItemsPadding,
                getMeasuredHeight());
    }

    @Override
    protected void measureLayout() {
        mItemsLayout.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        mItemsLayout.measure(MeasureSpec.makeMeasureSpec(getWidth() - 2
                * mItemsPadding, MeasureSpec.EXACTLY), MeasureSpec
                .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        rebuildItems(); // rebuilding before measuring

        int width = calculateLayoutWidth(widthSize, widthMode);

        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = Math.max(getItemDimension()
                            * (mVisibleItems - mItemOffsetPercent / 100),
                    getSuggestedMinimumHeight());

            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    /**
     * Calculates control width
     *
     * @param widthSize the input layout width
     * @param mode      the layout mode
     * @return the calculated control width
     */
    private int calculateLayoutWidth(int widthSize, int mode) {
        mItemsLayout.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mItemsLayout
                .measure(MeasureSpec.makeMeasureSpec(widthSize,
                        MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(
                        0, MeasureSpec.UNSPECIFIED));
        int width = mItemsLayout.getMeasuredWidth();

        if (mode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width += 2 * mItemsPadding;

            // Check against our minimum width
            width = Math.max(width, getSuggestedMinimumWidth());

            if (mode == MeasureSpec.AT_MOST && widthSize < width) {
                width = widthSize;
            }
        }

        // forcing recalculating
        mItemsLayout.measure(MeasureSpec.makeMeasureSpec(width - 2
                * mItemsPadding, MeasureSpec.EXACTLY), MeasureSpec
                .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        return width;
    }

    // --------------------------------------------------------------------------
    //
    // Drawing items
    //
    // --------------------------------------------------------------------------

    @Override
    protected void drawItems(Canvas canvas) {
        canvas.save();
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        int ih = getItemDimension();

        // resetting intermediate bitmap and recreating canvases
        mSpinBitmap.eraseColor(0);
        Canvas c = new Canvas(mSpinBitmap);
        Canvas cSpin = new Canvas(mSpinBitmap);

        int top = (mCurrentItemIdx - mFirstItemIdx) * ih + (ih - getHeight())
                / 2;

        c.translate(mItemsPadding, -top + mScrollingOffset);
        Log.d("ScrollingOffset", String.valueOf(mScrollingOffset) + ";ih:"
                + String.valueOf(ih));
        Log.d("childcount", String.valueOf(mCurrentItemIdx - mFirstItemIdx));
        if (getItemScaleSytle()) {
            int selectionColor = getViewAdapter().getSelectionTextColor();
            int normalColor = getViewAdapter().getTextColor();

            float scale = 2.0f;
            int centerLine = getHeight() / 2;
            View itemView;
            int startIndex=mCurrentItemIdx-getVisibleItems()/2;
            startIndex=startIndex<0?0:startIndex;
            for (int i = startIndex; i < startIndex+getVisibleItems()+1; i++) {
                itemView = mItemsLayout.findViewWithTag(i);
                if (itemView != null) {
                    int adsDis = Math.abs(centerLine - (itemView.getTop() + ih / 2)+top);
                    if(i<=mCurrentItemIdx)
                    {
                        adsDis-=mScrollingOffset;
                    }else

                    {
                        adsDis+=mScrollingOffset;
                    }

                    if(i == mCurrentItemIdx){
                        ((TextView) itemView.findViewById(getViewAdapter()
								.getItemTextResource()))
								.setTextColor(selectionColor);
                    }else {
                        ((TextView) itemView.findViewById(getViewAdapter()
                                .getItemTextResource()))
                                .setTextColor(normalColor);
                    }
                    float itemScale = scale - (adsDis*1.0f / centerLine);
                    itemView.setScaleX(itemScale);
                    itemView.setScaleY(itemScale);
                }
            }
        }
        mItemsLayout.draw(c);

        mSeparatorsBitmap.eraseColor(0);
        Canvas cSeparators = new Canvas(mSeparatorsBitmap);

        if (mSelectionDivider != null) {
            // draw the top divider
            int topOfTopDivider = (getHeight() - ih - mSelectionDividerHeight) / 2;
            int bottomOfTopDivider = topOfTopDivider + mSelectionDividerHeight;
            mSelectionDivider.setBounds(0, topOfTopDivider, w,
                    bottomOfTopDivider);
            mSelectionDivider.draw(cSeparators);

            // draw the bottom divider
            int topOfBottomDivider = topOfTopDivider + ih;
            int bottomOfBottomDivider = bottomOfTopDivider + ih;
            mSelectionDivider.setBounds(0, topOfBottomDivider, w,
                    bottomOfBottomDivider);
            mSelectionDivider.draw(cSeparators);
        }
        if (mSelectionBackground != null) {
            int topOfItem = (getHeight() - ih) / 2;
            int bottomOfItem = topOfItem + ih;
            mSelectionBackground.setBounds(0, topOfItem, w,
                    bottomOfItem);
            mSelectionBackground.draw(cSeparators);
        }
        cSpin.drawRect(0, 0, w, h, mSelectorWheelPaint);
        cSeparators.drawRect(0, 0, w, h, mSeparatorsPaint);


        if (getShowSplitLine())
            canvas.drawBitmap(mSeparatorsBitmap, 0, 0, null);
        canvas.drawBitmap(mSpinBitmap, 0, 0, null);
        canvas.restore();
    }

    public int getCenterItemTop() {
        return (getHeight() - getItemDimension()) / 2;
    }

    private void setScaleTextColor(View view) {
        int selectionColor = getViewAdapter().getSelectionTextColor();
        int normalColor = getViewAdapter().getTextColor();

        float scale0 = view.getScaleX();
        int deltred = Color.red(selectionColor) - Color.red(normalColor);
        int deltgreen = Color.green(selectionColor) - Color.green(normalColor);
        int deltblue = Color.blue(selectionColor) - Color.blue(normalColor);
        float deltscale = scale0 - 1;
        int newred = (int) (Color.red(normalColor) + deltscale * deltred);
        int newgreen = (int) (Color.green(normalColor) + deltscale * deltgreen);
        int newblue = (int) (Color.blue(normalColor) + deltscale * deltblue);
        if (deltscale >= 0) {

            ((TextView) view.findViewById(getViewAdapter()
                    .getItemTextResource())).setTextColor(Color.rgb(newred,
                    newgreen, newblue));
        }
    }
}
