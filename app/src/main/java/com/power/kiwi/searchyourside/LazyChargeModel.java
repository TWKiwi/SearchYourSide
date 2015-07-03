package com.power.kiwi.searchyourside;

import static com.power.kiwi.searchyourside.DbConstants.TABLE_NAME;
import static com.power.kiwi.searchyourside.DbConstants.PICNAME;
import static com.power.kiwi.searchyourside.DbConstants.NAME;
import static com.power.kiwi.searchyourside.DbConstants.TYPE;
import static com.power.kiwi.searchyourside.DbConstants.PRICE;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Observable;

import static android.provider.BaseColumns._ID;

/**
 * Created by kiwi on 15/7/2.
 */
public class LazyChargeModel extends Observable {

    private DBHelper mLazyChargeDB = null;//資料庫物件

    /**
     * 建立資料庫
     * @param context 整個系統環境
     * */
    protected void openDatabase(Context context){

        mLazyChargeDB = new DBHelper(context);

    }
    /**
     * 關閉資料庫
     * */
    protected void closeDatabase(){

        mLazyChargeDB.close();

    }



    /**
     * 加入資料庫
     * @param picName 圖片名稱，命名格式為年月日時分秒，也是辨別用名稱
     * @param chargeRecord 入帳名稱，可不填
     * @param chargeType 類別名稱，食衣住行育樂醫療
     * @param chargePrice 金額
     * */
    protected void addDb(String picName,String chargeRecord,String chargeType, String chargePrice){

        SQLiteDatabase db = mLazyChargeDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PICNAME,picName.trim());
        values.put(NAME,chargeRecord.trim());
        values.put(TYPE,chargeType.trim());
        values.put(PRICE,chargePrice.trim());
        db.insert(TABLE_NAME,null,values);

        notifyObservers();

    }

    /**
     * Cursor在資料庫中的游標的意思
     * @param activity 呼叫用
     * */
    public Cursor getCursor(Activity activity){

        SQLiteDatabase db = mLazyChargeDB.getReadableDatabase();
        String[] colums = {_ID,NAME,TYPE,PRICE,PICNAME};

        Cursor cursor = db.query(TABLE_NAME,colums,null,null,null,null,null);
        activity.startManagingCursor(cursor);

        return cursor;
    }

}
