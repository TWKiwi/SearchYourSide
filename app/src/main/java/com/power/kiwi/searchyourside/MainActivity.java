package com.power.kiwi.searchyourside;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 懶人記帳與智能管家選擇畫面
 * */
public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    OptionsActivity mOptionsActivity = new OptionsActivity();

    /**
     * 登入畫面、懶人記帳按鈕與智能管家按鈕
     * */
    Button mLoginBtn,mRegisterBtn,mForgotPWBtn,mLazyChargeBtn, mSmartButlerBtn, mOptionsBtn;
    /**
     * 登入畫面編輯物件
     * */
    EditText mMemberIDEdit,mMemberPWEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//螢幕保持直立顯示
        mOptionsActivity.optionSpr = getApplication().getSharedPreferences("Option", Context.MODE_PRIVATE);

        //連線
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        //連線

        initLoginView();

    }
    /**
     * 載入View物件
     * */
    private void initView(){
        setContentView(R.layout.activity_main);
        mLazyChargeBtn = (Button)findViewById(R.id.lazyChargeBtn);
        mSmartButlerBtn = (Button)findViewById(R.id.smartButlerBtn);
        mOptionsBtn = (Button)findViewById(R.id.OptionBtn);
        setListener();
    }

    private void initLoginView(){

        if(mOptionsActivity.getString("MemberID").equals("null") &&
                mOptionsActivity.getString("MemberPW").equals("null")){
            setContentView(R.layout.login_layout);
            setTitle("首次使用，請先登入。");
            mMemberIDEdit = (EditText) findViewById(R.id.MemberIDEdit);
            mMemberPWEdit = (EditText) findViewById(R.id.MemberPWEdit);
            mLoginBtn = (Button) findViewById(R.id.LoginBtn);
            mRegisterBtn = (Button) findViewById(R.id.MemberRegisterBtn);
//            mForgotPWBtn = (Button) findViewById(R.id.ForgotPWBtn);

            mLoginBtn.setOnClickListener(this);
            mRegisterBtn.setOnClickListener(this);
//            mForgotPWBtn.setOnClickListener(this);
        }else{
            initView();
            setListener();
        }

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

                intent = new Intent(this, LazyChargeActivity.class);
                startActivity(intent);
                break;

            case R.id.smartButlerBtn :

                intent = new Intent(this, SmartButlerActivity.class);
                startActivity(intent);
                break;

            case R.id.OptionBtn :

                intent = new Intent(this, OptionsActivity.class);
                startActivity(intent);
                break;

            case R.id.LoginBtn :

                if(mMemberIDEdit.getText().toString().length() == 0 ||
                        mMemberPWEdit.getText().toString().length() == 0){
                    Toast.makeText(this,"請於上方輸入您的信箱及密碼\n信箱即您的帳號",Toast.LENGTH_LONG).show();
                    break;
                }

                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(mMemberIDEdit.getText().toString()).matches()){
                    Toast.makeText(this,"不是有效的信箱！",Toast.LENGTH_LONG).show();
                    break;
                }

                if(mMemberPWEdit.getText().toString().length() < 5 ||
                        mMemberPWEdit.getText().toString().length() > 16){
                    Toast.makeText(this,"密碼只接受英文數字\n密碼長度請介於5~16字元之間",Toast.LENGTH_LONG).show();
                    break;
                }

                String index_sel = "CREATE OR REPLACE VIEW `member`.MemberPasswordView AS " +
                        "SELECT * from `member`.`memberpassword` WHERE `Account` = '" +
                        mMemberIDEdit.getText().toString().trim() + "';";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "SELECT if(`Password` = '" + mMemberPWEdit.getText().toString().trim() +
                        "' , 'true', 'false') AS CheckPassword FROM `member`.`MemberPasswordView`;";
                String result_sumsel = MySQLConnector.executeQuery(index_sel);

                String returnValue = "";
                try {

                    JSONArray jsonArray2 = new JSONArray(result_sumsel);
                    JSONObject jsonData = jsonArray2.getJSONObject(0);
                    returnValue = jsonData.getString("CheckPassword");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(returnValue.equals("true")){
                    mOptionsActivity.setString("MemberID", mMemberIDEdit.getText().toString().trim());
                    mOptionsActivity.setString("MemberPW", mMemberPWEdit.getText().toString().trim());
                    initView();
                }else{
                    mMemberPWEdit.setText("");
                    Toast.makeText(this,"帳號或密碼錯誤\n請確認是否有誤",Toast.LENGTH_LONG).show();
                }

                Toast.makeText(this, "歡迎回來", Toast.LENGTH_LONG).show();
                break;

            case R.id.MemberRegisterBtn :

                if(mMemberIDEdit.getText().toString().trim().length() == 0 ||
                        mMemberPWEdit.getText().toString().trim().length() == 0){
                    Toast.makeText(this,"請於上方設定您的信箱及密碼\n信箱將成為您的帳號\n並再度點選註冊按鈕",Toast.LENGTH_LONG).show();
                    break;
                }

                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(mMemberIDEdit.getText().toString().trim()).matches()){
                    Toast.makeText(this,"不是有效的信箱！",Toast.LENGTH_LONG).show();
                    break;
                }

                if(mMemberPWEdit.getText().toString().length() < 5 ||
                        mMemberPWEdit.getText().toString().length() > 16){
                    Toast.makeText(this,"密碼只接受英文數字\n密碼長度請介於5~16字元之間",Toast.LENGTH_LONG).show();
                    break;
                }

                index_sel = "SELECT distinct if(`Account` = '" + mMemberIDEdit.getText().toString().trim() +
                        "' , 'true', 'false') AS CheckAccount" +
                        " FROM `member`.`memberpassword` order by CheckAccount DESC;";
                result_sumsel = MySQLConnector.executeQuery(index_sel);

                returnValue = "";
                try {

                    JSONArray jsonArray2 = new JSONArray(result_sumsel);
                    JSONObject jsonData = jsonArray2.getJSONObject(0);
                    returnValue = jsonData.getString("CheckAccount");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(returnValue.equals("true")){
                    Toast.makeText(this,"帳號已被註冊過",Toast.LENGTH_LONG).show();
                    break;
                }

                index_sel = "INSERT INTO `member`.`memberpassword` (`Account`, `Password`) VALUES ('" +
                        mMemberIDEdit.getText().toString().trim() + "', '" + mMemberPWEdit.getText().toString().trim() + "');";
                MySQLConnector.executeQuery(index_sel);

                mOptionsActivity.setString("MemberID", mMemberIDEdit.getText().toString().trim());
                mOptionsActivity.setString("MemberPW", mMemberPWEdit.getText().toString().trim());

                initView();

                break;

//            case R.id.ForgotPWBtn :
//
//
//
//                break;
        }
    }
}
