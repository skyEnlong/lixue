package com.lixue.app.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.lixue.app.MyActivity;
import com.lixue.app.R;
import com.lixue.app.common.constants.HttpConst;
import com.lixue.app.common.logic.UserInfoManager;
import com.lixue.app.http.BaseRequest;
import com.lixue.app.library.http.HttpManager;
import com.lixue.app.library.util.NetUtil;
import com.lixue.app.login.model.UserInfo;

/**
 * Created by enlong on 2017/1/22.
 */

public class WelcomeActivity extends MyActivity implements Handler.Callback {
    UserInfo info;
    private Handler mHandler;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        initView();

        loadData();


    }

    private void initView() {
        setContentView(R.layout.activity_welcome);
    }

    private void loadData(){
        info = UserInfoManager.getInstance().getCurrentUserInfo();
        if (null != info && NetUtil.isNetEnable(this)) {
            //change token
            BaseRequest request = new BaseRequest(HttpConst.HTTP_UPDATE_TOKEN);
            request.setNeedToken(false);
            request.addParams("token_old", info.token);
            HttpManager.getInstance().doTask(request, this);
        }

        mHandler = new Handler(this);
        mHandler.sendEmptyMessageDelayed(1, 4000);
    }



    @Override
    public void onNext(String o) {
        UserInfo info = new Gson().fromJson(o, UserInfo.class);
        UserInfoManager.getInstance().setCurrentUserInfo(info);
        this.info = info;
    }

    @Override
    public void onError(Throwable t) {
        this.info = null;
        UserInfoManager.getInstance().logOut();
    }

    @Override
    public boolean handleMessage(Message message) {
        Intent intent = null;
        if(null == this.info){
             intent = new Intent(this, LoginActivity.class);
        }else {
            intent = new Intent(this, MainActivity.class);

        }
        startActivity(intent);
        finish();

        return true;
    }
}
