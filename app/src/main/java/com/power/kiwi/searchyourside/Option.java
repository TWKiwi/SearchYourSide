package com.power.kiwi.searchyourside;

import android.content.SharedPreferences;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by kiwi on 15/7/1.
 * 這是一個設定類別，用在整體系統客製化方面的資料存取與處理。
 */
public class Option {

    /**
     * 取得一個 SharedPreferences 物件讓目前的 Activity 使用
     * 在 Android 平台上一個 SharedPreferences 物件會對應到一個檔案，
     * 這個檔案中儲存 key/value 的對應資料，
     * 而 SharedPreferences 物件提供了一些對應的方法來讀寫這些資料。*/
    SharedPreferences optionSpr;

    /**
     * 儲存個人化的整數設定
     * key 字串
     * value 儲存得值(int)*/
    protected void setInt(String key,int value){

        SharedPreferences.Editor editor = optionSpr.edit();

        editor.putInt(key, value).apply();
        //apply()與commit()差異在於後者會回傳booleen值做確認，所以前者執行效率上會比較快。
    }
    /**
     * 儲存個人化的整數設定
     * key 字串
     * value 儲存得值(Double轉String儲存)*/
    protected void setDouble(String key,double value){

        SharedPreferences.Editor editor = optionSpr.edit();

        editor.putString(key, String.valueOf(value)).apply();

    }
    /**
     * 儲存個人化的整數設定
     * key 字串
     * value 儲存得值(String)*/
    protected void setString(String key,String value){

        SharedPreferences.Editor editor = optionSpr.edit();

        editor.putString(key,value).apply();

    }

    /**
     * 取得個人化設定整數值
     * key 字串*/
    public int getInt(String key){

//        // NumberFormat 數字格式
//        // NumberFormat是一個抽象類別，我們必須用getInstance()來取得他裡面的方法，
//        // 因此第1行NumberFormat nf = NumberFormat.getInstance();宣告了一個NumberFormat物件。
//        // NumberFormat物件格式化的方式是固定的，都是以每三位數一個逗號的方式格式化數字，
//        // 浮點數欄位則是有的時候顯示，沒有就不顯示。
//        NumberFormat nf = NumberFormat.getInstance();
//        nf.setMaximumFractionDigits(2);

        return optionSpr.getInt(key, 0);

    }

    /**
     * 取得個人化設定整數值，取至小數點第2位
     * key 字串*/
    public Double getDouble(String key){

//        // NumberFormat 數字格式
//        // NumberFormat是一個抽象類別，我們必須用getInstance()來取得他裡面的方法，
//        // 因此第1行NumberFormat nf = NumberFormat.getInstance();宣告了一個NumberFormat物件。
//        // NumberFormat物件格式化的方式是固定的，都是以每三位數一個逗號的方式格式化數字，
//        // 浮點數欄位則是有的時候顯示，沒有就不顯示。
//        NumberFormat nf = NumberFormat.getInstance();
//        nf.setMaximumFractionDigits(2);
//
//        return nf.format(Double.parseDouble(optionSpr.getString(key,"0.0")));

        DecimalFormat df = new DecimalFormat("0.00");

        return Double.parseDouble(df.format(Double.parseDouble(optionSpr.getString(key,"0.0"))));

    }

    /**
     * 取得個人化設定字串
     * key 字串*/
    public String getString(String key){

        return optionSpr.getString(key, "null");

    }

}
