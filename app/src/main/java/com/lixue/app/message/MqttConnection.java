package com.lixue.app.message;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lixue.app.common.logic.CLog;
import com.lixue.app.common.logic.UserInfoManager;
import com.lixue.app.library.util.NetUtil;
import com.lixue.app.library.util.StringUtil;
import com.lixue.app.library.util.ZipUtil;
import com.lixue.app.login.model.UserInfo;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by enlong on 2017/2/20.
 * 消息入口和出口管理类
 */

public class MqttConnection {

    private final String TAG = "msg";
    private static MqttConnection mqttConnection;
    private String userName = "userName";
    private String passWord = "password";
    private CallbackConnection client;
    private MQTT mqtt;
    private Context mContext;
    private final boolean retained = false;
    private HandleMessage handleMessage;
    private Listener listener;

    private List<MessageBean> bufferMessage;
    private MqttConnection(Context mContext) {
        this.mContext = mContext;
        handleMessage = new HandleMessage(mContext);
        bufferMessage =  Collections.synchronizedList(new  ArrayList<>());

    }

    public synchronized static MqttConnection getInstance(Context context) {
        if (mqttConnection == null) {
            mqttConnection = new MqttConnection(context);
        }
        return mqttConnection;
    }

    private void tellUpgradeResult(MessageBean msg, int result){
        if(null == msg) return;

        bufferMessage.remove(msg);

        msg.send_status = result;
        EventBus.getDefault().post(msg);
    }

    public synchronized void init() throws URISyntaxException {
        UserInfo info = UserInfoManager.getInstance().getCurrentUserInfo();
        if (info == null) return;

        //mqtt消息实例
        mqtt = new MQTT();
        //MQTT设置说明
        mqtt.setHost("message.codoon.com", 1883);
        mqtt.setCleanSession(true); //若设为false，MQTT服务器将持久化客户端会话的主体订阅和ACK位置，默认为true
        mqtt.setKeepAlive((short) 30);//定义客户端传来消息的最大时间间隔秒数，服务器可以据此判断与客户端的连接是否已经断开，从而避免TCP/IP超时的长时间等待
        mqtt.setUserName(userName);//服务器认证用户名
        mqtt.setPassword(info.token);//服务器认证密码
        mqtt.setClientId(info.user_id);

        mqtt.setWillTopic(MqttConstant.SERVER_WILL_TOPIC);//设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息
        mqtt.setWillMessage("will json");//设置“遗嘱”消息的内容，默认是长度为零的消息
        mqtt.setWillQos(QoS.AT_LEAST_ONCE);//设置“遗嘱”消息的QoS，默认为QoS.ATMOSTONCE
        mqtt.setWillRetain(retained);//若想要在发布“遗嘱”消息时拥有retain选项，则为true
        mqtt.setVersion("3.1.1");
        mqtt.setReconnectDelay(2000);//首次重连接间隔毫秒数1s
        mqtt.setReconnectDelayMax(5000);//重连接间隔毫秒数5s
        client = mqtt.callbackConnection();

        listener = new Listener() {
            @Override
            public void onConnected() {
                CLog.i(TAG, "mqtt onConnected");

            }

            @Override
            public void onDisconnected() {
                Log.e(MqttConstant.TAG, "====mqtt disconnected=====");

            }

            @Override
            public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
                try {
                    ack.run();

                    String msg = new String(ZipUtil.decompress(body.toByteArray()), "UTF-8");
                    if (null == handleMessage) {
                        handleMessage = new HandleMessage(mContext);
                    }

                    MessageBean messageBean = JSON.parseObject(msg, MessageBean.class);

                    handleMessage.handlMessage(messageBean);
                } catch (Exception e) {
                }


            }

            @Override
            public void onFailure(Throwable value) {
                Log.e(MqttConstant.TAG, "====mqtt onFailure=====" + value.toString());

                if ("Already connected".equals(value.getMessage())) {
                    Log.e(MqttConstant.TAG, "mqtt has connected!");
                    return;
                }
                Log.e(MqttConstant.TAG, "connect failure " + value.toString());

            }
        };
        //连接监听
        client.listener(listener);

    }


    /**
     * 退出、断开链接
     */
    public void disconnect() {
        if (client != null) {
            String topicName = MqttConstant.PRIVATE_TOPIC + "/#";
            UTF8Buffer[] topics = {new UTF8Buffer(topicName)};
            client.unsubscribe(topics, new Callback() {
                @Override
                public void onSuccess(Object aVoid) {
                    client.disconnect(null);
                    client.kill(null);
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });

            client = null;
            mqtt = null;
            listener = null;
        }


    }


    /**
     * 订阅主题
     *
     * @param topicName
     */
    public void subscribeToTopic(String topicName) {
        {
            try {
                if (client != null) {
                    Topic[] topics = {new Topic(topicName, QoS.AT_LEAST_ONCE)};
                    client.subscribe(topics, new Callback() {
                        @Override
                        public void onSuccess(Object bytes) {
                            Log.e(TAG, "subscribe successful");
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.e(TAG, "subscribe fail");
                        }
                    });
                }
            } catch (Exception e) {
//                    Log.e(MqttConstant.TAG, e.getMessage());
            }
        }
    }



    protected synchronized void sendMsgToTopic(final String topicName,
                                  final MessageBean messageBean){

        if(TextUtils.isEmpty(topicName) || null == messageBean){
            return;
        }

        if (client == null || mqtt == null) {

            bufferMessage.add(messageBean);
            connectAndSend(topicName, messageBean);

        }else {
            //发布消息
            byte[] message = null;
            String msgStr = JSON.toJSONString(messageBean);

            if (topicName.equals(MqttConstant.SERVER_WILL_TOPIC)) {
                message = msgStr.getBytes();
            } else {
                try {
                    message = ZipUtil.compress(msgStr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (StringUtil.isEmpty(topicName) || message == null) {
                tellUpgradeResult(messageBean, MqttConstant.STATUS_FAILED);
                return;
            }

            client.publish(topicName, message, QoS.AT_LEAST_ONCE, true, new Callback<Void>() {
                @Override
                public void onSuccess(Void value) {
                    CLog.i(TAG, "send msg success");
                    tellUpgradeResult(messageBean, MqttConstant.STATUS_SUCCESS);

                }

                @Override
                public void onFailure(Throwable value) {
                    CLog.i(TAG, "send msg failed");
                    tellUpgradeResult(messageBean, MqttConstant.STATUS_FAILED);

                }
            });

        }
    }


    private synchronized void connectAndSend(final String topicName,
                                             final MessageBean messageBean) {
        try {
            if (client == null || mqtt == null) {
                init();
                connect(topicName, messageBean);
             } else {

                connect(topicName, messageBean);

            }
        } catch (Exception e) {
            tellUpgradeResult(messageBean, MqttConstant.STATUS_FAILED);
            e.printStackTrace();
            disconnect();
        }
    }

    public synchronized void connect(final String topicName,final MessageBean messageBean) {

        if (!NetUtil.isNetEnable(mContext)) {
            tellUpgradeResult(messageBean, MqttConstant.STATUS_FAILED);

            return;
        }
        client.connect(new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
               sendMsgToTopic(topicName, messageBean);
            }

            @Override
            public void onFailure(Throwable value) {
                if ("Already connected".equals(value.getMessage())) {
                    Log.e(MqttConstant.TAG, "mqtt has connected!");
                    return;
                }
                tellUpgradeResult(messageBean, MqttConstant.STATUS_FAILED);
             }
        });

    }


}
