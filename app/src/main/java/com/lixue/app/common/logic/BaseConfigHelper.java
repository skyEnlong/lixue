package com.lixue.app.common.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by 0H7RXL on 2016/7/15.
 */
public abstract  class BaseConfigHelper {

    private String mXmlFile = "lixu_config";
    private Context context;

    public BaseConfigHelper(Context mContext) {
        this.context = mContext;
        String config = getXmlFile();
        this.mXmlFile = (TextUtils.isEmpty(config)) ? mXmlFile : config;
    }


    public  abstract String getXmlFile();

    /* 读取一个整数 */
    public int getIntValue(String key) {
        try {
            SharedPreferences store = context.getSharedPreferences(mXmlFile, Context.MODE_MULTI_PROCESS);// 建立storexml.xml
            return store.getInt(key, 0);// 从codoon_config_xml中读取上次进度，存到string1中
        } catch (Exception e) {
            return 0;
        }
    }

    /* 读取一个整数 */
    public int getIntValue(String key, int defaultValue) {
        try {
            SharedPreferences store = context.getSharedPreferences(mXmlFile, Context.MODE_MULTI_PROCESS);// 建立storexml.xml
            return store.getInt(key, defaultValue);// 从codoon_config_xml中读取上次进度，存到string1中
        } catch (Exception e) {
            return 0;
        }
    }

    /* 设置一个整数 */
    public void setIntValue(String key, int value) {
        SharedPreferences store = context.getSharedPreferences(mXmlFile, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = store.edit();
        editor.putInt(key, value);
        editor.commit();

    }


    /* 读取一个长整数 */
    public Long getLongValue(String key, long defalut) {
        SharedPreferences store = context.getSharedPreferences(mXmlFile, Context.MODE_MULTI_PROCESS);// 建立storexml.xml
        long value = store.getLong(key, defalut);

        return value;// 从codoon_config_xml中读取上次进度，存到string1中
    }

    /* 设置一个长整数 */
    public void setLongValue(String key, long value) {
        SharedPreferences store = context.getSharedPreferences(mXmlFile, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = store.edit();
        editor.putLong(key, value);
        editor.commit();

    }

    /* 获取一个浮点数 */
    public float getFloatValue(String key, float defalut) {
        SharedPreferences store = context.getSharedPreferences(mXmlFile, Context.MODE_MULTI_PROCESS);// 建立storexml.xml
        return store.getFloat(key, defalut);// 从codoon_config_xml中读取上次进度，存到string1中
    }

    /* 设置一个浮点数 */
    public void setFloatValue(String key, float value) {
        SharedPreferences store = context.getSharedPreferences(mXmlFile, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = store.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /* 获取一个字符 */
    public String getStringValue(String key) {
        SharedPreferences store = context.getSharedPreferences(mXmlFile, Context.MODE_MULTI_PROCESS);// 建立storexml.xml
        return store.getString(key, "");// 从codoon_config_xml中读取上次进度，存到string1中
    }

    public String getStringValue(String key, String defaultStr) {
        SharedPreferences store = context.getSharedPreferences(mXmlFile, Context.MODE_MULTI_PROCESS);// 建立storexml.xml
        return store.getString(key, defaultStr);// 从codoon_config_xml中读取上次进度，存到string1中
    }

    /* 设置一个字符 */
    public void setStringValue(String key, String value) {
        SharedPreferences store = context.getSharedPreferences(mXmlFile, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = store.edit();
        editor.putString(key, value);
        editor.commit();

    }

    /* 获取一个布尔值 */
    public boolean getBooleanValue(String key,
                                   boolean defalut) {
        SharedPreferences store = context.getSharedPreferences(mXmlFile, Context.MODE_MULTI_PROCESS);
        return store.getBoolean(key, defalut);
    }

    /* 设置一个布尔值 */
    public void setBooleanValue(String key,
                                boolean value) {
        SharedPreferences store = context.getSharedPreferences(mXmlFile, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = store.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

}
