package com.lixue.app.login.ui;

import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSONArray;
import com.lixue.app.MyActivity;
import com.lixue.app.common.logic.UserInfoManager;
import com.lixue.app.library.http.ResJsonString;
import com.lixue.app.login.logic.LoginHelper;
import com.lixue.app.login.model.UserInfo;

import java.util.ArrayList;

/**
 * Created by enlong on 2017/1/22.
 */

public class LoginActivity extends MyActivity  {

    private LoginHelper loginHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginHelper = new LoginHelper();
    }

    public void doLogin(String name, String pass){
        loginHelper.login(name, pass, this);
    }

    @Override
    public void onNext(ResJsonString o) {
//        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<ArrayList<UserInfo>>() {
//        }.getType();

        ArrayList<UserInfo>  info = (ArrayList<UserInfo>) JSONArray.parse(o.resultString);

        if(null != info){
            UserInfoManager.getInstance().setCurrentUserInfo(info.get(0));
        }

        startActivity(new Intent(this, MainActivity.class));

    }


}
