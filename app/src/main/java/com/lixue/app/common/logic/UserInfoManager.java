package com.lixue.app.common.logic;

import android.content.Context;

import com.lixue.app.MyApplicattion;
import com.lixue.app.db.UserTokenDB;
import com.lixue.app.login.model.UserInfo;

/**
 * Created by enlong on 2017/2/13.
 */

public class UserInfoManager {
    private static UserInfoManager instance;
    private Context mContext;
    private UserInfo currentUserInfo;
    private UserTokenDB userTokenDB;

    public static synchronized UserInfoManager getInstance(){
        if(null == instance){
            instance = new UserInfoManager();
        }

        return instance;
    }

    private UserInfoManager(){
        mContext = MyApplicattion.instance;
        userTokenDB = new UserTokenDB(mContext);
    }

    public synchronized  UserInfo getCurrentUserInfo(){
        if(null == currentUserInfo){
            currentUserInfo = userTokenDB.getCurrentUser();
        }
        return currentUserInfo;
    }

    /**
     * 设置当前用户
     * @param info
     */
    public synchronized void setCurrentUserInfo(UserInfo info){


        if(null != currentUserInfo){

            userTokenDB.updateInfoById(info);
            currentUserInfo = info;
        }else {
            currentUserInfo = info;
            userTokenDB.insert(currentUserInfo);
        }

    }

    public void logOut(){
        if(null != currentUserInfo){
            userTokenDB.delete(currentUserInfo);
            currentUserInfo = null;
        }

    }

    public void updateToken(String token){
        if(null != currentUserInfo){
            currentUserInfo.token = token;
            userTokenDB.updateInfoById(currentUserInfo);
        }

    }


}
