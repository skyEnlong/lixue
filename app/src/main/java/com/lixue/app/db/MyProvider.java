package com.lixue.app.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by enlong on 2017/1/22.
 * provider 类
 */

public class MyProvider extends ContentProvider {
    private MyDBHelper myDBHelper;
    @Override
    public boolean onCreate() {
        myDBHelper = new MyDBHelper(getContext(), MyDBHelper.DB_COMMON_NAME, null,
                MyDBHelper.DB_COMMON_VERSION );

        return true;
    }

    private String getTableName(Uri uri){
        return uri.getPathSegments().get(0);
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = null;
        String table_name =  getTableName(uri);
        db = myDBHelper.getWritableDatabase();

        Cursor c = null;
        if (null != db) {
            c = db.query(table_name,
                    projection, selection, selectionArgs, null, null, sortOrder);
        }
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        SQLiteDatabase db = null;
        String table_name =  getTableName(uri);
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri noteUri = null;
        SQLiteDatabase db = null;
        String table_name =  getTableName(uri);
        db = myDBHelper.getWritableDatabase();

        if (null != db) {
            long id = db.insert(table_name, null, contentValues);

            noteUri = ContentUris.withAppendedId(uri, id);

            getContext().getContentResolver().notifyChange(noteUri, null);
        }
        return noteUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = null;
        String table_name =  getTableName(uri);
        db = myDBHelper.getWritableDatabase();
        int resut = 0;

        if (null != db) {
            resut = db.delete(table_name, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);

        }
        return resut;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase db = null;
        String table_name =  getTableName(uri);
        db = myDBHelper.getWritableDatabase();

        int resut = 0;
        if (null != db) {
            resut = db.update(table_name, contentValues, s, strings);

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return resut;
    }
}
