package com.lixue.app.common.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enlong on 2017/2/20.
 */

public class NetReceiver extends BroadcastReceiver {
    private List<OnNetChange> listeners = new ArrayList<>();

    public void registerNet(OnNetChange ls){
        if(null != ls && !listeners.contains(ls)){
            listeners.add(ls);
        }
    }

    public void unRegisterNet(OnNetChange ls){
        listeners.remove(ls);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            boolean wifi = false;
            boolean mobile = false;
            for (NetworkInfo info : infos) {
                if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI
                        && !info.isConnected()) {
                    wifi = true;
                }
                if (info != null
                        && info.getType() == ConnectivityManager.TYPE_MOBILE
                        && !info.isConnected()) {
                    mobile = true;
                }
            }

            if(listeners.size() > 0){
                for (int i = 0; i < listeners.size(); i++){
                    listeners.get(i).onNetChange(wifi, mobile);
                }
            }
        }

    }


    public interface OnNetChange{
        public void onNetChange(boolean isWifi, boolean isMobile);
    }


}
