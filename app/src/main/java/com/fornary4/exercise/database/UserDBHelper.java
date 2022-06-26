package com.fornary4.exercise.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.fornary4.exercise.entity.ErrorInfo;

import java.util.ArrayList;
import java.util.List;


public class UserDBHelper extends SQLiteOpenHelper {
    public static final int TYPE_SINGLE = 0;
    public static final int TYPE_MULTIPLE = 1;
    public static final int TYPE_JUDGE = 2;

    private static final String DB_NAME = "user.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "ERROR_INFO";
    private static UserDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private UserDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static UserDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new UserDBHelper(context);
        }
        return mHelper;
    }

    public SQLiteDatabase openReadLink() {
        if (mRDB == null || !mRDB.isOpen())
            mRDB = mHelper.getReadableDatabase();
        return mRDB;
    }

    public SQLiteDatabase openWriteLink() {
        if (mWDB == null || !mWDB.isOpen())
            mWDB = mHelper.getWritableDatabase();
        return mWDB;
    }

    public void closeLink() {
        if (mRDB != null && mRDB.isOpen()) {
            mRDB.close();
            mRDB = null;
        }

        if (mWDB != null && mWDB.isOpen()) {
            mWDB.close();
            mWDB = null;
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists ERROR_INFO (_id integer primary key autoincrement not null, type integer not null, position integer not null, count integer not null);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "alter table " + TABLE_NAME + " add column phone varchar;";
        db.execSQL(sql);
    }

    public void deleteAll() {
        String sql = "delete from ERROR_INFO";
        mWDB.execSQL(sql);
    }


    public long insert(ErrorInfo info) {
        if (!queryExistence(info.type, info.position)) {
            return firstInsert(info);
        } else {
            return updateCount(info);
        }
    }

    public List<ErrorInfo> queryByType(int type) {
        List<ErrorInfo> res = new ArrayList<>();
        Cursor cursor = mRDB.query(TABLE_NAME, null, "type=?", new String[]{String.valueOf(type)}, null, null,"count desc");
        while (cursor.moveToNext()) {
            ErrorInfo info = new ErrorInfo();
            info.id = cursor.getInt(0);
            info.type = cursor.getInt(1);
            info.position = cursor.getInt(2);
            info.count = cursor.getInt(3);
            res.add(info);
        }
        return res;
    }

    public List<ErrorInfo> queryAll() {
        List<ErrorInfo> res = new ArrayList<>();
        Cursor cursor = mRDB.rawQuery("select * from ERROR_INFO order by type asc, count desc", null);
        while (cursor.moveToNext()) {
            ErrorInfo info = new ErrorInfo();
            info.id = cursor.getInt(0);
            info.type = cursor.getInt(1);
            info.position = cursor.getInt(2);
            info.count = cursor.getInt(3);
            res.add(info);
        }
        return res;
    }

    public boolean queryExistence(int type, int position) {
        Cursor cursor = mRDB.query(TABLE_NAME, null, "type=? and position=?", new String[]{String.valueOf(type), String.valueOf(position)}, null, null, null);
        return cursor.moveToNext();
    }

    private long firstInsert(ErrorInfo info) {
        ContentValues values = new ContentValues();
        values.put("type", info.type);
        values.put("position", info.position);
        values.put("count", 1);
        return mWDB.insert(TABLE_NAME, null, values);
    }

    private int updateCount(ErrorInfo info) {
        ContentValues values = new ContentValues();
        values.put("count", queryCount(info) + 1);
        return mWDB.update(TABLE_NAME, values, "type=? and position=?", new String[]{String.valueOf(info.type), String.valueOf(info.position)});
    }

    private int queryCount(ErrorInfo info) {
        Cursor cursor = mRDB.query(TABLE_NAME, null, "type=? and position=?", new String[]{String.valueOf(info.type), String.valueOf(info.position)}, null, null, null);
        if (cursor.moveToNext()) {
            return cursor.getInt(3);
        }
        return 0;
    }

}
