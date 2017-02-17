package com.lixue.app;

import android.app.Application;
import android.content.Context;

import com.lixue.app.library.util.CrashHandler;

/**
 * Created by enlong on 2017/1/22.
 *
 */

public class MyApplicattion extends Application {
    public static MyApplicattion instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance= this;
        if (BuildConfig.DEBUG) {

            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
