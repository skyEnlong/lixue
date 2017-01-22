package antistatic.spinnerwheel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by workEnlong on 2015/4/16.
 */
public class WheelVerticalWrapView extends WheelVerticalView{
    public WheelVerticalWrapView(Context context) {
        super(context);
    }

    public WheelVerticalWrapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WheelVerticalWrapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * Adds view for item to items layout
     *
     * @param index
     *            the item index
     * @param first
     *            the flag indicates if view should be first
     * @return true if corresponding item exists and is added
     */
    @Override
    public boolean addItemView(int index, boolean first) {
        View view = getItemView(index);
        if (view != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            view.setTag(index);
            if (first) {
                mItemsLayout.addView(view, 0, params);
            } else {
                mItemsLayout.addView(view, params);
            }
            return true;
        }
        return false;
    }
}
