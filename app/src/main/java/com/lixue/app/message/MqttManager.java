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
                    subscriber.onError(e);
                }
                connection.connect(null, null);
                subscriber.onComplete();
            }
        }, BackpressureStrategy.LATEST)
                .observeOn(Schedulers.io())
                .subscribe();


    }

    public void disConnect(){
        connection.disconnect();
    }

    public void sendMsg(String topic, MessageBean bean){
        Flowable.create(new FlowableOnSubscribe<ResJsonString>() {
            @Override
            public void subscribe(final FlowableEmitter<ResJsonString> subscriber) throws Exception {

                connection.sendMsgToTopic(topic, bean);
                subscriber.onComplete();
            }
        }, BackpressureStrategy.LATEST)
                .observeOn(Schedulers.io())
                .subscribe();
    }
}
