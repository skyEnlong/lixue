package com.lixue.app.http;

import com.lixue.app.MyApplicattion;
import com.lixue.app.common.logic.UserInfoManager;
import com.lixue.app.library.http.HttpRequest;
import com.lixue.app.login.model.UserInfo;

import okhttp3.Request;

/**
 * Created by enlong on 2017/2/13.
 * 注意： 部分接口不需要token,
 */

public class BaseRequest extends HttpRequest{

    public BaseRequest(String url){
        super(MyApplicattion.instance, url);
    }

    public void setNeedToken(boolean isNeedToken){
        this.isNeedToken = isNeedToken;
    }

    @Override
    public void addHeader(Request.Builder request) {
        super.addHeader(request);
        if(isNeedToken){
            UserInfo cur = UserInfoManager.getInstance().getCurrentUserInfo();
            String token = (null != cur) ? cur.token : "";
            request.header("Authorization", "Bearer " + token);
        }
    }
}
