package com.lixue.app.common.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lixue.app.R;
import com.lixue.app.library.util.StringUtil;

import java.util.List;

/**
 * Created by enlong on 2017/1/22.
 * 广告banner、滑动banner等
 */

public class BannerView extends RelativeLayout {
    private AutoScrollViewPager viewPager;
    private LinearLayout inicator;
    private RecyclingPagerAdapter adBannerAdapter;
    private View baseView;


    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.common_banner_layout, null);
        baseView = v;
        inicator = (LinearLayout) v.findViewById(R.id.baner_dot_layout);
        viewPager = (AutoScrollViewPager) v.findViewById(R.id.banner_pager);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.d140));
        this.addView(v, params);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                updateIndicatore(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    /**
     * you must call this. init adapter ,add set adapter value
     * @param adapter
     */
    public void setAdapter(RecyclingPagerAdapter adapter) {
        this.adBannerAdapter = adapter;
    }

    public void setInterval(long interval) {
        if (viewPager != null) viewPager.setInterval(interval);
    }

    private void updateIndicatore(int index) {
        int childCount = inicator.getChildCount();
        if (childCount != 0) {
            index = index % childCount;
        }
        if (childCount > index) {
            for (int i = 0; i < childCount; i++) {
                View view = inicator.getChildAt(i);

                if (index == i) {
                    view.setSelected(true);
                } else {
                    view.setSelected(false);

                }
            }
        }
    }


    public void setdatas(List<? extends  Object> ads) {
        if (StringUtil.isListEmpty(ads)) {
            this.setVisibility(GONE);
            return;
        } else {
            this.setVisibility(VISIBLE);
        }


        if (!StringUtil.isListEmpty(ads)) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = (int) (5 * getResources().getDisplayMetrics().density);
            int size = ads.size();
            inicator.removeAllViews();
            for (int i = 0; i < size && size != 1; i++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_banner_selector));
                inicator.addView(imageView, params);
                if (i == 0) {
                    imageView.setSelected(true);
                }
            }
            if (size == 1) {
                viewPager.setScrollble(false);
            } else {
                viewPager.setScrollble(true);
                viewPager.startAutoScroll();
            }

        } else {
            this.setVisibility(GONE);
        }
    }
}
