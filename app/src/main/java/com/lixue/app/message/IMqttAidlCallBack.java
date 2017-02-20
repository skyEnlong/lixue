package com.lixue.app.message;

/**
 * Created by enlong on 2017/2/20.
 */

public interface IMqttAidlCallBack {
    void onMqttSendSuccessful();

    void onMqttSendFail();
}
