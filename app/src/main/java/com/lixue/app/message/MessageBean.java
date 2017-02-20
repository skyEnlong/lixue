package com.lixue.app.message;

import com.google.gson.annotations.Expose;

/**
 * Created by enlong on 2017/2/20.
 */

public class MessageBean {

    @Expose(serialize = false)
    public int send_status;//发送的状态
    @Expose(serialize = false)
    public int handle_status = 0;//操作的状态 0：未操作，1操作成功，-1：操作失败,2他人同意，-2他人拒绝
    @Expose(serialize = false)
    public int read_status = 0;//阅读状态
    @Expose(serialize = false)
    public String from_user_id;//user_id来自
    @Expose(serialize = false)
    public String to_user_id;// user_id去往
    @Expose(serialize = false)
    public int show_time;//是否显示时间
    @Expose(serialize = false)
    public int progress;//图片发送的进度
    @Expose(serialize = false)
    public boolean isLoadingUrl;//是否正在加载url（图片？）
    @Expose(serialize = false)
    public String localUrl;//临时本地地址
    @Expose(serialize = false)
    public String moreMessage;//分享时说的话
}
