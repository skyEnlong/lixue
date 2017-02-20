package com.lixue.app.message;

import android.content.Context;

import com.lixue.app.library.http.ResJsonString;

import java.net.URISyntaxException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by enlong on 2017/2/20.
 */

public class MqttManager {
    private Context mContext;
    private MqttConnection connection;
    public MqttManager(Context mContext){
        this.mContext = mContext;
        connection = MqttConnection.getInstance(mContext);
    }


    public void startMqtt(){
        Flowable.create(new FlowableOnSubscribe<ResJsonString>() {
            @Override
            public void subscribe(final FlowableEmitter<ResJsonString> subscriber) throws Exception {
                try {
                    connection.init();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                connection.connect(null, null);
            }
        }, BackpressureStrategy.LATEST)
                .observeOn(Schedulers.io())
                .subscribe();


    }
}
