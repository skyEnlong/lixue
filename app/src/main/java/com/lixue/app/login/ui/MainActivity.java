package com.lixue.app.login.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.lixue.app.MyActivity;
import com.lixue.app.R;
import com.lixue.app.library.view.SlidingTabLayout;

/**
 * Created by enlong on 2017/2/13.
 * app main page
 */

public class MainActivity extends MyActivity{
    SlidingTabLayout tabLayout;
    ViewPager contentPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }



    private void initView() {

    }

    private void initData() {

    }
}
