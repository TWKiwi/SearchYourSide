package com.power.kiwi.searchyourside;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * 懶人記帳與智能管家選擇畫面
 * */
public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    /**
     * 懶人記帳按鈕與智能管家按鈕
     * */
    Button mLazyChargeBtn, mSmartButlerBtn, mOptionsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//螢幕保持直立顯示
        initView();
        setListener();

    }
    /**
     * 載入View物件
     * */
    private void initView(){
        mLazyChargeBtn = (Button)findViewById(R.id.lazyChargeBtn);
        mSmartButlerBtn = (Button)findViewById(R.id.smartButlerBtn);
        mOptionsBtn = (Button)findViewById(R.id.OptionBtn);
    }
    /**
     * 設置View物件監聽
     * */
    private void setListener(){
        mLazyChargeBtn.setOnClickListener(this);
        mSmartButlerBtn.setOnClickListener(this);
        mOptionsBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        Intent intent;

        switch(v.getId()){
            case R.id.lazyChargeBtn :

                intent = new Intent(this,LazyChargeActivity.class);
                startActivity(intent);
                break;

            case R.id.smartButlerBtn :

                intent = new Intent(this,SmartButlerActivity.class);
                startActivity(intent);
                break;

            case R.id.OptionBtn :

                intent = new Intent(this, OptionsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
