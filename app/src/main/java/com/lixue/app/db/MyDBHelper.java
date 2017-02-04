package com.lixue.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lixue.app.library.db.BaseDBHelper;

/**
 * Created by enlong on 2017/1/22.
 */

public class MyDBHelper extends BaseDBHelper {
    public static final String AUTHORITY = "com.lixue.app.ContentProvider";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.lixue.con_db";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.lixue.con_db";

    public static final String DB_COMMON_NAME = "con";
    public static final int DB_COMMON_VERSION =1;

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserTokenDB.creat_sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
