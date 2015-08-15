package com.power.kiwi.searchyourside;

import android.provider.BaseColumns;

/**
 * Created by kiwi on 15/7/2.
 */
public interface DbConstants extends BaseColumns {

    /**
     * 資料表名字
     * */
    public static final String TABLE_NAME = "ChargeRecord";

    /**
     * 圖片名字
     * */
    public static final String PICNAME = "PicName";

    /**
     * 消費名稱
     * */
    public static final String NAME = "Name";

    /**
     * 消費類型
     * */
    public static final String TYPE = "Type";

    /**
     * 消費金額
     * */
    public static final String PRICE = "Price";
}
