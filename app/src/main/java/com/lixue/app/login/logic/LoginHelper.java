package com.lixue.app.login.logic;

import com.lixue.app.common.constants.HttpConst;
import com.lixue.app.http.BaseRequest;
import com.lixue.app.library.http.HttpManager;

import org.reactivestreams.Subscriber;

/**
 * Created by enlong on 2017/2/17.
 */

public class LoginHelper {

    public void login(String name, String pass, Subscriber subscriber){
        BaseRequest request = new BaseRequest(HttpConst.HTTP_LOGIN);
        request.setNeedToken(false);
        request.addParams("name", name);
        request.addParams("pass", pass);
        HttpManager.getInstance().doTask(request, subscriber);
    }
}
