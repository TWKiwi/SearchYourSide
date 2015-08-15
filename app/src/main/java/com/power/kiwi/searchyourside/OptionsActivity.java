package com.power.kiwi.searchyourside;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;

/**
 * Created by kiwi on 15/7/1.
 * 這是一個設定類別，用在整體系統客製化方面的資料存取與處理。
 */
public class OptionsActivity extends ActionBarActivity implements View.OnClickListener {

    Button mSetOptionsBtn, mSetGPSBtn, mAboutUsBtn;
    EditText mSetBudgetEdt, mSetRglCostEdt;

    /**
     * 取得一個 SharedPreferences 物件讓目前的 Activity 使用
     * 在 Android 平台上一個 SharedPreferences 物件會對應到一個檔案，
     * 這個檔案中儲存 key/value 的對應資料，
     * 而 SharedPreferences 物件提供了一些對應的方法來讀寫這些資料。*/
    public SharedPreferences optionSpr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        optionSpr = getApplication().getSharedPreferences("Option", Context.MODE_PRIVATE);
        initView();
        initData();
        setListener();
    }

    private void initView(){
        mSetOptionsBtn = (Button) findViewById(R.id.setOptionsBtn);
        mSetGPSBtn = (Button) findViewById(R.id.setGpsBtn);
        mAboutUsBtn = (Button) findViewById(R.id.aboutUsBtn);
        mSetBudgetEdt = (EditText) findViewById(R.id.setBudgetEdt);
        mSetRglCostEdt = (EditText) findViewById(R.id.setRglCostEdt);
    }

    private void initData(){
        mSetBudgetEdt.setText(String.valueOf(getData("mBudget")));
        mSetRglCostEdt.setText(String.valueOf(getData("mRglCost")));
    }

    private void setListener(){
        mSetOptionsBtn.setOnClickListener(this);
        mSetGPSBtn.setOnClickListener(this);
        mAboutUsBtn.setOnClickListener(this);
    }

    /**
     * 儲存個人化的整數設定
     * @param key 字串
     * @param value 儲存得值(Long)
     * */
    protected void setLong(String key,Long value){

        SharedPreferences.Editor editor = optionSpr.edit();

        editor.putLong(key, value).apply();
        //apply()與commit()差異在於後者會回傳booleen值做確認，所以前者執行效率上會比較快。
    }
    /**
     * 儲存個人化的整數設定
     * @param key 字串
     * @param value 儲存得值(Double轉String儲存)
     * */
    protected void setDouble(String key,double value){

        SharedPreferences.Editor editor = optionSpr.edit();

        editor.putString(key, String.valueOf(value)).apply();

    }
    /**
     * 儲存個人化的整數設定
     * @param key 字串
     * @param value 儲存得值(String)
     * */
    protected void setString(String key,String value){

        SharedPreferences.Editor editor = optionSpr.edit();

        editor.putString(key,value.trim()).apply();

    }

    /**
     * 取得個人化設定整數值
     * @param key 字串
     * */
    public long getLong(String key){

//        // NumberFormat 數字格式
//        // NumberFormat是一個抽象類別，我們必須用getInstance()來取得他裡面的方法，
//        // 因此第1行NumberFormat nf = NumberFormat.getInstance();宣告了一個NumberFormat物件。
//        // NumberFormat物件格式化的方式是固定的，都是以每三位數一個逗號的方式格式化數字，
//        // 浮點數欄位則是有的時候顯示，沒有就不顯示。
//        NumberFormat nf = NumberFormat.getInstance();
//        nf.setMaximumFractionDigits(2);

        return optionSpr.getLong(key,0);

    }

    /**
     * 取得個人化設定整數值，取至小數點第2位
     * @param key 字串
     * */
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
     * @param key 字串
     * */
    public String getString(String key){

        return optionSpr.getString(key, "null");

    }

    public long getData(String s){
//        optionSpr = getSharedPreferences("Option", 0);

        switch (s){

            case "mBudget" : return optionSpr.getLong("mBudget", 18000);
            case "mRglCost" : return optionSpr.getLong("mRglCost", 0);
            case "mScaleTS" : return optionSpr.getLong("mScaleTS", 0);
            case "飯" : return optionSpr.getLong("飯", 0);
            case "粥" : return optionSpr.getLong("粥", 0);
            case "麵" : return optionSpr.getLong("麵", 0);
            case "中式" : return optionSpr.getLong("中式", 0);
            case "西式" : return optionSpr.getLong("西式", 0);
            case "點心" : return optionSpr.getLong("點心", 0);
            case "冰飲" : return optionSpr.getLong("冰飲", 0);
            case "其他" : return optionSpr.getLong("其他", 0);

        }

        return 0;
    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_option, menu);
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
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.setOptionsBtn :
                if (mSetBudgetEdt.getText().toString().equals("") ||
                        mSetBudgetEdt.getText().toString().equals("0")){
                    mSetBudgetEdt.setText("18000");
                }
                setLong("mBudget", Long.parseLong(mSetBudgetEdt.getText().toString()));
                setLong("mRglCost", Long.parseLong(mSetRglCostEdt.getText().toString()));
                break;
            case R.id.setGpsBtn :
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                break;
            case R.id.aboutUsBtn :
                AlertDialog.Builder bdr = new AlertDialog.Builder(OptionsActivity.this);
                bdr.setMessage("打劫組是由一群對創作與行動裝置抱有熱忱的學生組織而成，目的希望能夠靠著自己的能力去證明，即使在離島就學，在教育程度及能力也能有傑出的表現，並透過參與各種公開比賽磨練自己心智，未來二三十餘年能在職場闖出一片天空．");
                bdr.setTitle("關於打劫組...");
                bdr.show();
                break;
        }
    }
}
