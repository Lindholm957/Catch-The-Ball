package com.example.catchtheball;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CatchTheBall";
    public static final String TABLE_SESSIONS = "Sessions";

    public static final String KEY_ID = "_id";
    public static final String KEY_SCORE = "score";
    public static final String KEY_TIME = "time";
    public static final String KEY_ORANGE = "orange";
    public static final String KEY_PINK = "pink";
    public static final String KEY_TOUCHES = "touches";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SESSIONS_TABLE = "CREATE TABLE " + TABLE_SESSIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SCORE + " INT,"
                + KEY_TIME + " INT,"
                + KEY_ORANGE + " INTEGER,"
                + KEY_PINK + " INTEGER,"
                + KEY_TOUCHES + " INTEGER" + ")";
        db.execSQL(CREATE_SESSIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_SESSIONS);

        onCreate(db);
    }
}
