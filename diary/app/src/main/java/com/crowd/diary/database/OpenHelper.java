package com.crowd.diary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {

    private static final String name = "diary.db";
    private static final int version = 1;

    public OpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                "diary (diary_id INTEGER primary key autoincrement," +
                "userID int(6), bookName varchar(64)," +
                "uri varchar(64),content varchar(1024), note varchar(1024))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            //修改表.暂时可以不用
        }
    }
}