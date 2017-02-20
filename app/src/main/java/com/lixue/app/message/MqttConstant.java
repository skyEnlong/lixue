package com.lixue.app.message;

/**
 * Created by enlong on 2017/2/20.
 */

public class MqttConstant {

    public static final String TAG = "MQTT";

    public static final int STATUS_NONE = 1;
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_SENDING = 2;
    public static final int STATUS_FAILED = -1;

    //订阅私聊的topic
    public static final String PRIVATE_TOPIC = "private";
    //订阅群聊的topic
//    public static final String GROUP_TOPIC = "group";
    //订阅全局的topic
//    public static final String GLOBAL_TOPIC = "global";
    //发送在线状态topic
    public static final String SERVER_ONLINE_TOPIC = "server/online";
    //发送私聊消息的topic
    public static final String SERVER_PRIVATE_TOPIC = "server/private";
    //发送群聊的消息topic
    public static final String SERVER_GROUP_TOPIC = "server/group";
    //will topic
    public static final String SERVER_WILL_TOPIC = "server/will";
    //    public static final String MQTT_BROKER_LIST = "mqtt_broker_list";
    public static final String LAST_LOAD_MQTT_BROKER_LIST = "last_load_mqtt_broker_list";
    public static final String LAST_UPLOAD_ACK_STATUS = "last_upload_ack_status";
    public static final String RANGE_UPLOAD_ACK_STATUS = "range_upload_ack_status";
    public static final String SWITCH_UPLOAD_ACK_STATUS = "switch_upload_ack_status";
    //
    public static final String MQTT_GROUP_SIMPLE = "mqtt_group_simple";


}
