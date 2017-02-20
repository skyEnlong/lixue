package com.lixue.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.lixue.app.common.logic.NetReceiver;
import com.lixue.app.library.util.CrashHandler;

/**
 * Created by enlong on 2017/1/22.
 *
 */

public class MyApplicattion extends Application {
    public static MyApplicattion instance;
    private BroadcastReceiver netReceiver ;

    @Override
    public void onCreate() {
        super.onCreate();
        instance= this;
        if (BuildConfig.DEBUG) {

            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }


        initMessageConfig();
        addReceiver();
    }

    private void initMessageConfig() {

    }

    private void addReceiver() {

        netReceiver = new NetReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netReceiver, filter);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
