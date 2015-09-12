package com.power.kiwi.searchyourside;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kiwi on 15/7/1.
 * 這是一個設定類別，用在整體系統客製化方面的資料存取與處理。
 */
public class OptionsActivity extends ActionBarActivity implements View.OnClickListener {

    Button mSetOptionsBtn, mSetGPSBtn, mAboutUsBtn, mLogoutBtn;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//螢幕保持直立顯示
        optionSpr = getApplication().getSharedPreferences("Option", Context.MODE_PRIVATE);
        initView();
        initData();
        setListener();
    }

    private void initView(){

        mSetOptionsBtn = (Button) findViewById(R.id.setOptionsBtn);
        mSetGPSBtn = (Button) findViewById(R.id.setGpsBtn);
        mAboutUsBtn = (Button) findViewById(R.id.aboutUsBtn);
        mLogoutBtn = (Button) findViewById(R.id.logoutBtn);
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
        mLogoutBtn.setOnClickListener(this);
    }

    /**
     * 儲存個人化的整數設定
     * @param key 字串
     * @param value 儲存得值(Long)
     * */
    protected void setLong(String key,Long value){

        SharedPreferences.Editor editor = optionSpr.edit();

        editor.putLong(key, value).apply();
        //apply()與commit()差異在於後者會回傳boolean值做確認，所以前者執行效率上會比較快。
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

        editor.putString(key, value).apply();
        //apply()與commit()差異在於後者會回傳boolean值做確認，所以前者執行效率上會比較快。
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
            case "飯類" : return optionSpr.getLong("飯類", 0);
            case "粥類" : return optionSpr.getLong("粥類", 0);
            case "麵類" : return optionSpr.getLong("麵類", 0);
            case "油炸類" : return optionSpr.getLong("油炸類", 0);
            case "中式" : return optionSpr.getLong("中式", 0);
            case "西式" : return optionSpr.getLong("西式", 0);
            case "點心" : return optionSpr.getLong("點心", 0);
            case "冰飲" : return optionSpr.getLong("冰飲", 0);
            case "其他" : return optionSpr.getLong("其他", 0);

        }

        return 0;
    }

    /**
     * 使用者飲食記錄做類別排序
     * */
    public String hashMapSort(){

        HashMap<String,Long> hashMap = new HashMap<>();
        hashMap.put("飯類",getData("飯類"));
        hashMap.put("粥類",getData("粥類"));
        hashMap.put("麵類",getData("麵類"));
        hashMap.put("油炸類",getData("油炸類"));
        hashMap.put("中式",getData("中式"));
        hashMap.put("西式",getData("西式"));
        hashMap.put("點心",getData("點心"));
        hashMap.put("冰飲",getData("冰飲"));
        hashMap.put("其他",getData("其他"));

        List<Map.Entry<String,Long>> listData = new ArrayList<Map.Entry<String,Long>>(hashMap.entrySet());

        Collections.sort(listData, new Comparator<Map.Entry<String, Long>>() {
            public int compare(Map.Entry<String, Long> entry1,
                               Map.Entry<String, Long> entry2) {
                return (int) (entry2.getValue() - entry1.getValue());
            }
        });
        //取得首筆資料
        return listData.get(0).getKey();

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

                Toast.makeText(this,"更改成功",Toast.LENGTH_LONG).show();
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

            case R.id.logoutBtn :
                setString("MemberID","null");
                setString("MemberPW","null");
                Toast.makeText(this,"已登出",Toast.LENGTH_LONG).show();

                intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                break;
        }
    }
}
