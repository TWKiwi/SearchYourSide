package com.power.kiwi.searchyourside;

import android.content.Context;

import java.util.Observable;

/**
 * Created by kiwi on 15/7/2.
 */
public class LazyChargeDB extends Observable {

    private DBHelper mLazyChargeDB = null;//資料庫物件

    /**
     * 開啟資料庫
     * @param context
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

}
