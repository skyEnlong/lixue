package com.lixue.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.lixue.app.library.base.BaseDB;
import com.lixue.app.login.model.AuthToken;

/**
 * Created by enlong on 2017/1/22.
 * the table save current user token and id
 */
public class UserTokenDB extends BaseDB {
    public static final String DATABASE_TABLE = "usertoken";

    public static final String Column_UserToken = "token";
    public static final String Column_User_Name = "nick";
    public static final String Column_User_Id = "user_id";
    public static final String Column_EXPIRE_IN = "expire";

    public static final String Column_User_type = "user_type"; // teacher student  parents

    public static final String creat_sql = "create table if not exists usertoken(" +
            "token NVARCHAR(200) not null,nick NVARCHAR(20)," +
            "user_id NVARCHAR(20),expire integer,user_type integer)";

    public UserTokenDB(Context mContext) {
        super(mContext);
    }


    @Override
    public Uri getUri() {
        return Uri.parse("content://" + MyDBHelper.AUTHORITY + "/usertoken");
    }

    public Uri insert(AuthToken auth) {
        ContentValues values = new ContentValues();
        values.put(Column_UserToken, auth.token);
        values.put(Column_User_Name, auth.nick);
        values.put(Column_User_Id, auth.user_id);
        values.put(Column_EXPIRE_IN, auth.expire);
        values.put(Column_User_type, auth.user_type);
        return resolver.insert(uri, values);
    }

    public int updateIdByToken(AuthToken auth) {
        ContentValues values = new ContentValues();
        values.put(Column_UserToken, auth.token);
        values.put(Column_User_Name, auth.nick);
        values.put(Column_User_Id, auth.user_id);
        values.put(Column_EXPIRE_IN, auth.expire);
        values.put(Column_User_type, auth.user_type);
        String where = Column_UserToken + "='" + auth.token + "'";
        return resolver.update(uri, values, where, null);
    }

    public int updateTokenById(AuthToken auth) {
        ContentValues values = new ContentValues();
        values.put(Column_UserToken, auth.token);
        values.put(Column_User_Name, auth.nick);
        values.put(Column_User_Id, auth.user_id);
        values.put(Column_EXPIRE_IN, auth.expire);
        values.put(Column_User_type, auth.user_type);
        String where = Column_User_Id + "='" + auth.user_id + "'";
        return resolver.update(uri, values, where, null);
    }


    /**
     * get current auth token, and userid
     *
     * @return
     */
    public AuthToken getCurrentUser() {
        Cursor c = resolver.query(uri, null, null, null, null);
        if (null == c) return null;

        AuthToken token = null;
        try {
            if (c.moveToFirst()) {
                token = new AuthToken();
                token.token = c.getString(c.getColumnIndex(Column_UserToken));
                token.expire = c.getLong(c.getColumnIndex(Column_EXPIRE_IN));
                token.user_id = c.getString(c.getColumnIndex(Column_User_Id));
                token.user_type = c.getInt(c.getColumnIndex(Column_User_type));
                token.nick = c.getString(c.getColumnIndex(Column_User_Name));
            }
        } catch (Exception e) {

        } finally {
            c.close();
        }
        return token;
    }


    /**
     * delete current token while exits app
     *
     * @param auth
     * @return
     */
    public int delete(AuthToken auth) {
        String where = Column_User_Id + "='" + auth.user_id + "'";
        return resolver.delete(uri, where, null);
    }
}
