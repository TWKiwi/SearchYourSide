package com.power.kiwi.searchyourside;

import static com.power.kiwi.searchyourside.DbConstants.TABLE_NAME;
import static com.power.kiwi.searchyourside.DbConstants.PHNAME;
import static com.power.kiwi.searchyourside.DbConstants.NAME;
import static com.power.kiwi.searchyourside.DbConstants.TYPE;
import static com.power.kiwi.searchyourside.DbConstants.PRICE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

/**
 * Created by kiwi on 15/7/2.
 */
public class DBHelper extends SQLiteOpenHelper  {

    /**
     * @param DATABASE_NAME 資料庫名稱
     * */
    private final static String DATABASE_NAME = "ChargeRecord.db";
    /**
     * @param DATABASE_VERSION 資料庫版本
     * */
    private final static int DATABASE_VERSION = 0;//資料庫版本每次有修改都要+1

    /**
     * */
    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String INIT_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" +_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PHNAME + " CHAR, " +
                NAME + " CHAR, " +
                TYPE + " CHAR, " +
                PRICE + " CHAR);";
        db.execSQL(INIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);

    }
}
