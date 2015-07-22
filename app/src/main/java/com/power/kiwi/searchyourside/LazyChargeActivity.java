package com.power.kiwi.searchyourside;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import static android.provider.BaseColumns._ID;
import static com.power.kiwi.searchyourside.DbConstants.NAME;
import static com.power.kiwi.searchyourside.DbConstants.PICNAME;
import static com.power.kiwi.searchyourside.DbConstants.PRICE;
import static com.power.kiwi.searchyourside.DbConstants.TABLE_NAME;
import static com.power.kiwi.searchyourside.DbConstants.TYPE;


public class LazyChargeActivity extends FragmentActivity implements View.OnClickListener{

    private DBHelper mSQLiteDB = null;//資料庫物件



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lazy_charge);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//螢幕保持直立顯示
        openDatabase(this);
        initView();

    }

    private void initView(){



        FragmentTabHost TabHost = (FragmentTabHost)findViewById(R.id.tabHost);
        TabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        TabHost.addTab(TabHost.newTabSpec("懶人記帳")
                                            .setIndicator("懶人記帳"),
                                            LazyChargeModel.class,
                                            null);
        TabHost.addTab(TabHost.newTabSpec("月曆查詢")
                                            .setIndicator("月曆查詢"),
                                            CaleadarSearchModel.class,
                                            null);
        TabHost.addTab(TabHost.newTabSpec("圖表查詢")
                                            .setIndicator("圖表查詢"),
                                            BarChartViewModel.class,
                                            null);

    }



    /**
     * 建立資料庫
     * @param context 整個系統環境
     * */
    protected void openDatabase(Context context){

        mSQLiteDB = new DBHelper(context);

    }

    /**
     * 關閉資料庫
     * */
    protected void closeDatabase(){

        mSQLiteDB.close();

    }

    /**
     * 加入資料庫
     * @param picName 圖片名稱，命名格式為年月日時分秒，也是辨別用名稱
     * @param chargeRecord 入帳名稱，可不填
     * @param chargeType 類別名稱，食衣住行育樂醫療
     * @param chargePrice 金額
     * */
    protected void addDb(String picName,String chargeRecord,String chargeType, String chargePrice){

        SQLiteDatabase db = mSQLiteDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PICNAME, picName.trim());
        values.put(NAME,chargeRecord.trim());
        values.put(TYPE,chargeType.trim());
        values.put(PRICE,chargePrice.trim());
        db.insert(TABLE_NAME,null,values);



    }

    /**
     * Cursor在資料庫中的游標的意思
     * @param activity 呼叫用
     * */
    public Cursor getCursor(Activity activity){

        SQLiteDatabase db = mSQLiteDB.getReadableDatabase();
        String[] colums = {_ID,PICNAME,NAME,TYPE,PRICE};

        Cursor cursor = db.query(TABLE_NAME,colums,null,null,null,null,null);
        activity.startManagingCursor(cursor);

        return cursor;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lazy_charge, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        closeDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.takePicBtn :
                Toast.makeText(this,"拍照",Toast.LENGTH_LONG).show();
                break;
            case R.id.addDataBtn :
                Toast.makeText(this,"儲存",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
