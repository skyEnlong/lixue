package com.lixue.app.message;

import android.content.Context;

import com.lixue.app.common.logic.CLog;

/**
 * Created by enlong on 2017/2/20.
 */

public class HandleMessage {
    public HandleMessage(Context mContext) {

    }

    public void handlMessage(MessageBean messageBean) {
        CLog.i("msg", "receive msg: " + messageBean);
    }
}
