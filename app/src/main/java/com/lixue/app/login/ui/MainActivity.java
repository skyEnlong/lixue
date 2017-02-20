package com.lixue.app.login.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.lixue.app.MyActivity;
import com.lixue.app.R;
import com.lixue.app.library.view.SlidingTabLayout;
import com.lixue.app.library.view.TabDataHolder;

import java.util.ArrayList;
import java.util.List;

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
        tabLayout = (SlidingTabLayout) findViewById(R.id.tab_layout);
        contentPager = (ViewPager) findViewById(R.id.tab_content);
    }

    private void initData() {

        List<TabDataHolder> dataHolders = new ArrayList<>();


    }
}
