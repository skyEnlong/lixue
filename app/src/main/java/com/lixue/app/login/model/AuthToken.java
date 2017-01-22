package com.lixue.app.login.model;

import java.io.Serializable;

/**
 * Created by enlong on 2017/1/22.
 */

public class AuthToken implements Serializable{
    public String token;
    public String user_id;
    public long expire;
    public String nick;
    public int user_type;
}
