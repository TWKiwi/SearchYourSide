package com.power.kiwi.searchyourside;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;


public class StoreListActivity extends ActionBarActivity implements LocationListener, AdapterView.OnItemClickListener {

    private String mInputType,mTime,mName,mType;
    private String[] mArrayType;
    private ListView mStoreListView,mFoodListView;
    private ImageView mStorePic;
    private Button mSearchFoodBtn,mHelpEditBtn,mGPSGoBtn;
    private TextView mStoreDataTxt,mStoreName;
    private int mNumber;
    private ArrayList<HashMap<String, Object>> mStoreList = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> mClickStoreData = new ArrayList<>();

    /**
     * 定位工程
     * */
    static final int mMIN_TIME = 5000;
    static final float mMIN_DIST = 5;
    private LocationManager mLocationManager;
    double mLatitude = 24.989206;
    double mLongitude = 121.313548;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        this.setTitle("等候衛星定位...");
        mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

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


    }

    private void initView(){
        this.setTitle("查詢資料結果");
        mStoreListView = (ListView)findViewById(R.id.StoreList);
        mStoreListView.setOnItemClickListener(this);

        getBundle();

        setListView();
        MyStoreAdapter adapter = new MyStoreAdapter(this);
        mStoreListView.setAdapter(adapter);

    }

    private void getBundle(){
        Intent intent = getIntent();

        mInputType = intent.getStringExtra("inputType");
        mTime = intent.getStringExtra("Time");
        mName = intent.getStringExtra("Name");
        mType = intent.getStringExtra("Type");
        mArrayType = intent.getStringArrayExtra("arrayType");
        mNumber = intent.getIntExtra("Number",2);

        Log.d("Test", mInputType + "\n" + mTime + "\n" + mName + "\n" + mType + "\n" +  mNumber);
    }

    private ArrayList<HashMap<String, Object>> setListView(){

//
//        if(whatBtn.equals("isProposal")){
//            Intent intent = getIntent();
//            latitude = intent.getDoubleExtra("latitude", 0);
//            longitude = intent.getDoubleExtra("longitude", 0);
//
//
//            String index_sum = "UPDATE `ai_pomo`.`health` SET `hUserX` = " + "119.57241" + ", `hUserY` = " + "23.57852" + ";";
//            MySQLConnector.executeQuery(index_sum,php);
//
//
//            try {
//                String indexG = "Create or Replace View HospitalDistanceView AS" +
//                        "SELECT *, round(6378.138*2*asin(sqrt(pow(sin( (`hY`*pi()/180-`hUserY`*pi()/180)/2),2)+cos(`hY`*pi()/180)*cos(`hUserY`*pi()/180)* pow(sin( (`hX`*pi()/180-`hUserX`*pi()/180)/2),2)))*1000) as HospitalDistance from `health`ORDER BY `HospitalDistance` ASC limit 20 ;";
//
//                String resultG  = MySQLConnector.executeQuery(indexG,php);
//                Log.d("ResultG",resultG);
//
//
//                String index_sel = "Select * from `ai_pomo`.`HospitalDistanceView` where `HospitalDistance` < 5000 ORDER BY `HospitalDistance` ASC ;";
//                String result_sumsel =  MySQLConnector.executeQuery(index_sel,php);
//                JSONArray jsonArray2 = new JSONArray(result_sumsel);
//
//                setTitle("查詢資料結果");
//
//                for (int i = 0; i < jsonArray2.length(); i++) {
//                    JSONObject jsonData = jsonArray2.getJSONObject(i);
//                    HashMap<String, Object> h2 = new HashMap<String, Object>();
//                    h2.put("hName", jsonData.getString("hName"));
//                    h2.put("HospitalDistance", jsonData.getString("HospitalDistance") + " 公尺");
//
//
//                    mStoreList.add(h2);
//
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return mStoreList;


//        if(whatBtn.equals("isProposal")){



        try {
            int count = 0;
            if(mNumber == 0){
                Log.d("test","mNumber == 0");
                String index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.GPSDistanceAndStoreTimeView AS " +
                         "SELECT gId, gName, gX, gY, gOpen, gClose, gFrequency,GPSDistance from `ai_pomo`.`gps` where `gName` like '%" + mName + "%';";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.StoreDistanceView AS " +
                         "SELECT gId, gName, gX, gY, gOpen, gClose, gFrequency, GPSDistance from `ai_pomo`.`store information` where `gName` like '%" + mName + "%';";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`GPSDistanceAndStoreTimeView` set `GPSDistance` = round(6378.138*2*asin(sqrt(pow(sin(((`gY`-" + mLatitude + ")*pi()/180)/2),2)+" +
                        "cos(`gY`*pi()/180)*cos(" + mLatitude + "*pi()/180)* pow(sin(((`gX`-" + mLongitude + ")*pi()/180)/2),2)))*1000);";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`GPSDistanceAndStoreTimeView` set `gFrequency` = `gFrequency` /`GPSDistance`;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`StoreDistanceView` set `GPSDistance` = round(6378.138*2*asin(sqrt(pow(sin(((`gY`-" + mLatitude + ")*pi()/180)/2),2)+" +
                        "cos(`gY`*pi()/180)*cos(" + mLatitude + "*pi()/180)* pow(sin(((`gX`-" + mLongitude + ")*pi()/180)/2),2)))*1000);";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`StoreDistanceView` set `gFrequency` = `gFrequency` /`GPSDistance`;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "SELECT gId,gName,gOpen, gClose, gFrequency, GPSDistance from `ai_pomo`.`GPSDistanceAndStoreTimeView` where `gOpen` < " + mTime + " OR `gClose`>" + mTime +
                           " union " +
                           "SELECT gId,gName,gOpen, gClose, gFrequency, GPSDistance from `ai_pomo`.`StoreDistanceView` where `gOpen` < " + mTime + " OR `gClose`> " + mTime + " ORDER BY `GPSDistance` ASC, `gFrequency` DESC;";
                String result_sumsel = MySQLConnector.executeQuery(index_sel);

                Log.d("test",result_sumsel);

                JSONArray jsonArray2 = new JSONArray(result_sumsel);

                setTitle("查詢資料結果");

                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject jsonData = jsonArray2.getJSONObject(i);
                    HashMap<String, Object> h2 = new HashMap<String, Object>();
                    h2.put("gId", jsonData.getString("gId"));
                    h2.put("gName", jsonData.getString("gName"));
                    h2.put("GPSDistance", jsonData.getString("GPSDistance"));

                    mStoreList.add(h2);
                    count++;
                }
            }else if (mNumber == 1){

                Log.d("test","mNumber == 1 " + mArrayType.length);

                String index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.GPSDistanceAndStoreTimeView AS " +
                        "SELECT gId, gName, gX, gY, gOpen, gClose, gFrequency, gRank, GPSDistance from `ai_pomo`.`gps` where round(" + mLatitude + ",1) = round(`gY`, 1) AND " +
                        "round(" + mLongitude + ",2) = round(`gX`, 2) order by `GPSDistance` ASC;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.StoreDistanceView AS " +
                        "SELECT gId, gName, gX, gY, gOpen, gClose, gFrequency, gRank, GPSDistance from `ai_pomo`.`store information` where round(" + mLatitude + ",1) = round(`gY`, 2) AND " +
                        "round(" + mLongitude + ",2) = round(`gX`, 2);";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`GPSDistanceAndStoreTimeView` set `GPSDistance` = " +
                        "round(6378.138*2*asin(sqrt(pow(sin(((`gY`-" + mLatitude + ")*pi()/180)/2),2)+cos(`gY`*pi()/180)*cos(" + mLatitude + "*pi()/180)* pow(sin(((`gX`-" + mLongitude + ")*pi()/180)/2),2)))*1000);";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`GPSDistanceAndStoreTimeView` set `gFrequency` = `gFrequency` /`GPSDistance`;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`StoreDistanceView` set `GPSDistance` = " +
                        "round(6378.138*2*asin(sqrt(pow(sin(((`gY`-" + mLatitude + ")*pi()/180)/2),2)+cos(`gY`*pi()/180)*cos(" + mLatitude + "*pi()/180)* pow(sin(((`gX`-" + mLongitude + ")*pi()/180)/2),2)))*1000);";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`StoreDistanceView` set `gFrequency` = `gFrequency` /`GPSDistance`;";
                MySQLConnector.executeQuery(index_sel);

                int i = 0;

                    do{

                        index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.StoreTypeView AS " +
                                "SELECT * from `ai_pomo`.`food` WHERE `fSort` = '" + mArrayType[i] + "';";
                        MySQLConnector.executeQuery(index_sel);

                        index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.StoreTypeView2 AS " +
                                "SELECT DISTINCT gName, gOpen, gClose,gX, gY, gFrequency, gRank, GPSDistance FROM `ai_pomo`.`GPSDistanceAndStoreTimeView`, `ai_pomo`.`StoreTypeView` where SUBSTRING_INDEX(`GPSDistanceAndStoreTimeView`.`gName`, ' ', 1) = " +
                                "`StoreTypeView`.`fStore` order by `GPSDistance` ASC;";
                        MySQLConnector.executeQuery(index_sel);

                        index_sel = "update `ai_pomo`.`gps`, `ai_pomo`.`StoreTypeView2` set `gps`.`gRank` =" +
                                " `gps`.`gRank` + 1 +round(`gps`.`gFrequency`/ 6378.138*2*asin(sqrt(pow(sin(((`gps`.`gY`-" + mLatitude + ")*pi()/180)/2),2)+" +
                                "cos(`gps`.`gY`*pi()/180)*cos(" + mLatitude + "*pi()/180)* pow(sin(((`gps`.`gX`-" + mLongitude + ")*pi()/180)/2),2)))*10000)where `gps`.`gName` = `StoreTypeView2`.`gName`;";
                        MySQLConnector.executeQuery(index_sel);

                        index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.GPSDistanceAndStoreTimeView AS " +
                                "SELECT gId, gName, gX, gY, gOpen, gClose, gFrequency, gRank, GPSDistance from `ai_pomo`.`gps` where round(" + mLatitude + ",1) = round(`gY`, 1) AND round(" + mLongitude + ",2) = round(`gX`, 2) order by `GPSDistance` ASC;";
                        MySQLConnector.executeQuery(index_sel);

                        index_sel = "UPDATE `ai_pomo`.`GPSDistanceAndStoreTimeView` set `GPSDistance` = " +
                                "round(6378.138*2*asin(sqrt(pow(sin(((`gY`-" + mLatitude + ")*pi()/180)/2),2)+cos(`gY`*pi()/180)*cos(" + mLatitude + "*pi()/180)* pow(sin(((`gX`-" + mLongitude + ")*pi()/180)/2),2)))*1000);";
                        MySQLConnector.executeQuery(index_sel);

                        index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.StoreTypeView3 AS " +
                                "SELECT * from `ai_pomo`.`store` WHERE `fSort` = '" + mArrayType[i] + "';";
                        MySQLConnector.executeQuery(index_sel);

                        index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.StoreTypeView4 AS " +
                                "SELECT DISTINCT gName, gOpen, gClose,gX, gY, gFrequency, gRank, GPSDistance FROM `ai_pomo`.`store information`, `ai_pomo`.`StoreTypeView` where `store information`.`gName` = `StoreTypeView`.`fStore` order by `GPSDistance` ASC;";
                        MySQLConnector.executeQuery(index_sel);

                        index_sel = "update `ai_pomo`.`store information`, `ai_pomo`.`StoreTypeView4` set `store information`.`gRank` =" +
                                " `store information`.`gRank` + 1 + round(`store information`.`gFrequency` /6378.138*2*asin(sqrt(pow(sin(((`store information`.`gY`-" + mLatitude + ")*pi()/180)/2),2)+" +
                                "cos(`store information`.`gY`*pi()/180)*cos(" + mLatitude + "*pi()/180)* pow(sin(((`store information`.`gX`-" + mLongitude + ")*pi()/180)/2),2)))*10000) where `store information`.`gName` = `StoreTypeView4`.`gName`;";
                        MySQLConnector.executeQuery(index_sel);

                        index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.StoreDistanceView AS " +
                                "SELECT gId, gName, gX, gY, gOpen, gClose, gFrequency, gRank, GPSDistance from `ai_pomo`.`store information` where round(" + mLatitude + ",1) = round(`gY`, 1) AND round(" + mLongitude + ",2) = round(`gX`, 2) order by `GPSDistance` ASC;";
                        MySQLConnector.executeQuery(index_sel);

                        index_sel = "UPDATE `ai_pomo`.`StoreDistanceView` set `GPSDistance` = round(6378.138*2*asin(sqrt(pow(sin(((`gY`-" + mLatitude + ")*pi()/180)/2),2)+" +
                                "cos(`gY`*pi()/180)*cos(" + mLatitude + "*pi()/180)* pow(sin(((`gX`-" + mLongitude + ")*pi()/180)/2),2)))*1000);";
                        MySQLConnector.executeQuery(index_sel);


                        i++;
                    }while (mArrayType[i] != null);

                index_sel = "UPDATE `ai_pomo`.`GPSDistanceAndStoreTimeView`, `ai_pomo`.`StoreTypeView2` set `GPSDistanceAndStoreTimeView`.`gFrequency` =" +
                        " 100 where `GPSDistanceAndStoreTimeView`.`gName` = `StoreTypeView2`.`gName`;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`StoreDistanceView`, `ai_pomo`.`StoreTypeView4` set `StoreDistanceView`.`gFrequency` =" +
                        " 100 where `StoreDistanceView`.`gName` = `StoreTypeView4`.`gName`;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "SELECT gId, gName, gOpen, gClose,gX, gY, gFrequency, gRank, GPSDistance from `ai_pomo`.`GPSDistanceAndStoreTimeView` where `gOpen` < " + mTime + " OR `gClose`>" + mTime +
                        " union " +
                        "SELECT gId, gName, gOpen, gClose,gX, gY, gFrequency, gRank, GPSDistance from `ai_pomo`.`StoreDistanceView` where `gOpen` < " + mTime + " OR `gClose`>" + mTime + " ORDER BY `gFrequency` DESC,`gRank` DESC, `GPSDistance` ASC;";
                String result_sumsel = MySQLConnector.executeQuery(index_sel);


                Log.d("test",result_sumsel);

                JSONArray jsonArray2 = new JSONArray(result_sumsel);

                setTitle("查詢資料結果");

                for (i = 0; i < jsonArray2.length(); i++) {
                    JSONObject jsonData = jsonArray2.getJSONObject(i);
                    HashMap<String, Object> h2 = new HashMap<String, Object>();
                    h2.put("gId", jsonData.getString("gId"));
                    h2.put("gName", jsonData.getString("gName"));
                    h2.put("GPSDistance", jsonData.getString("GPSDistance"));


                    mStoreList.add(h2);
                    count++;
                }

            }else if(mNumber == 2){

                Log.d("test","mNumber == 2");

                String index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.GPSDistanceAndStoreTimeView AS " +
                        "SELECT gId, gName, gX, gY, gOpen, gClose, gFrequency, gRank, GPSDistance from `ai_pomo`.`gps` where round(" + mLatitude + ",1) = round(`gY`, 1) AND " +
                        "round(" + mLongitude + ",2) = round(`gX`, 2) order by `GPSDistance` ASC;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.StoreDistanceView AS " +
                        "SELECT gId, gName, gX, gY, gOpen, gClose, gFrequency, gRank, GPSDistance from `ai_pomo`.`store information` where round(" + mLatitude + ",1) = round(`gY`, 2) AND " +
                        "round(" + mLongitude + ",2) = round(`gX`, 2);";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`GPSDistanceAndStoreTimeView` set `GPSDistance` = " +
                        "round(6378.138*2*asin(sqrt(pow(sin(((`gY`-" + mLatitude + ")*pi()/180)/2),2)+cos(`gY`*pi()/180)*cos(" + mLatitude + "*pi()/180)* pow(sin(((`gX`-" + mLongitude + ")*pi()/180)/2),2)))*1000);";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`GPSDistanceAndStoreTimeView` set `gFrequency` = `gFrequency` /`GPSDistance`;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`StoreDistanceView` set `GPSDistance` = " +
                        "round(6378.138*2*asin(sqrt(pow(sin(((`gY`-" + mLatitude + ")*pi()/180)/2),2)+cos(`gY`*pi()/180)*cos(" + mLatitude + "*pi()/180)* pow(sin(((`gX`-" + mLongitude + ")*pi()/180)/2),2)))*1000);";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`StoreDistanceView` set `gFrequency` = `gFrequency` /`GPSDistance`;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.StoreTypeView AS " +
                        "SELECT * from `ai_pomo`.`food` WHERE `fSort` = '" + mType + "';";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.StoreTypeView2 AS " +
                        "SELECT DISTINCT gName, gOpen, gClose,gX, gY, gFrequency, gRank, GPSDistance FROM `ai_pomo`.`GPSDistanceAndStoreTimeView`, `ai_pomo`.`StoreTypeView` where SUBSTRING_INDEX(`GPSDistanceAndStoreTimeView`.`gName`, ' ', 1) = " +
                        "`StoreTypeView`.`fStore` order by `GPSDistance` ASC;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "update `ai_pomo`.`gps`, `ai_pomo`.`StoreTypeView2` set `gps`.`gRank` =" +
                        " `gps`.`gRank` + 1 +round(`gps`.`gFrequency`/ 6378.138*2*asin(sqrt(pow(sin(((`gps`.`gY`-" + mLatitude + ")*pi()/180)/2),2)+" +
                        "cos(`gps`.`gY`*pi()/180)*cos(" + mLatitude + "*pi()/180)* pow(sin(((`gps`.`gX`-" + mLongitude + ")*pi()/180)/2),2)))*10000)where `gps`.`gName` = `StoreTypeView2`.`gName`;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.GPSDistanceAndStoreTimeView AS " +
                        "SELECT gId, gName, gX, gY, gOpen, gClose, gFrequency, gRank, GPSDistance from `ai_pomo`.`gps` where round(" + mLatitude + ",1) = round(`gY`, 1) AND round(" + mLongitude + ",2) = round(`gX`, 2) order by `GPSDistance` ASC;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`GPSDistanceAndStoreTimeView` set `GPSDistance` = " +
                        "round(6378.138*2*asin(sqrt(pow(sin(((`gY`-" + mLatitude + ")*pi()/180)/2),2)+cos(`gY`*pi()/180)*cos(" + mLatitude + "*pi()/180)* pow(sin(((`gX`-" + mLongitude + ")*pi()/180)/2),2)))*1000);";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.StoreTypeView3 AS " +
                        "SELECT * from `ai_pomo`.`store` WHERE `fSort` = '" + mType + "';";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.StoreTypeView4 AS " +
                        "SELECT DISTINCT gName, gOpen, gClose,gX, gY, gFrequency, gRank, GPSDistance FROM `ai_pomo`.`store information`, `ai_pomo`.`StoreTypeView` where `store information`.`gName` = `StoreTypeView`.`fStore` order by `GPSDistance` ASC;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "update `ai_pomo`.`store information`, `ai_pomo`.`StoreTypeView4` set `store information`.`gRank` =" +
                        " `store information`.`gRank` + 1 + round(`store information`.`gFrequency` /6378.138*2*asin(sqrt(pow(sin(((`store information`.`gY`-" + mLatitude + ")*pi()/180)/2),2)+" +
                        "cos(`store information`.`gY`*pi()/180)*cos(" + mLatitude + "*pi()/180)* pow(sin(((`store information`.`gX`-" + mLongitude + ")*pi()/180)/2),2)))*10000) where `store information`.`gName` = `StoreTypeView4`.`gName`;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "CREATE OR REPLACE VIEW `ai_pomo`.StoreDistanceView AS " +
                        "SELECT gId, gName, gX, gY, gOpen, gClose, gFrequency, gRank, GPSDistance from `ai_pomo`.`store information` where round(" + mLatitude + ",1) = round(`gY`, 1) AND round(" + mLongitude + ",2) = round(`gX`, 2) order by `GPSDistance` ASC;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`StoreDistanceView` set `GPSDistance` = round(6378.138*2*asin(sqrt(pow(sin(((`gY`-" + mLatitude + ")*pi()/180)/2),2)+" +
                        "cos(`gY`*pi()/180)*cos(" + mLatitude + "*pi()/180)* pow(sin(((`gX`-" + mLongitude + ")*pi()/180)/2),2)))*1000);";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`GPSDistanceAndStoreTimeView`, `ai_pomo`.`StoreTypeView2` set `GPSDistanceAndStoreTimeView`.`gFrequency` =" +
                        " 100 where `GPSDistanceAndStoreTimeView`.`gName` = `StoreTypeView2`.`gName`;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "UPDATE `ai_pomo`.`StoreDistanceView`, `ai_pomo`.`StoreTypeView4` set `StoreDistanceView`.`gFrequency` =" +
                        " 100 where `StoreDistanceView`.`gName` = `StoreTypeView4`.`gName`;";
                MySQLConnector.executeQuery(index_sel);

                index_sel = "SELECT gId, gName, gOpen, gClose,gX, gY, gFrequency, gRank, GPSDistance from `ai_pomo`.`GPSDistanceAndStoreTimeView` where `gOpen` < " + mTime + " OR `gClose`>" + mTime +
                        " union " +
                        "SELECT gId, gName, gOpen, gClose,gX, gY, gFrequency, gRank, GPSDistance from `ai_pomo`.`StoreDistanceView` where `gOpen` < " + mTime + " OR `gClose`>" + mTime + " ORDER BY `gFrequency` DESC,`gRank` DESC, `GPSDistance` ASC;";
                String result_sumsel = MySQLConnector.executeQuery(index_sel);

                Log.d("test",result_sumsel);

                JSONArray jsonArray2 = new JSONArray(result_sumsel);

                setTitle("查詢資料結果");

                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject jsonData = jsonArray2.getJSONObject(i);
                    HashMap<String, Object> h2 = new HashMap<String, Object>();
                    h2.put("gId", jsonData.getString("gId"));
                    h2.put("gName", jsonData.getString("gName"));
                    h2.put("GPSDistance", jsonData.getString("GPSDistance"));

                    mStoreList.add(h2);
                    count++;
                }
            }

            Log.d("test",""+count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//    }

        return mStoreList;
    }

    @Override
    public void onResume(){
        super.onResume();
        //取得最佳定位提供者
        String best = mLocationManager.getBestProvider(new Criteria(), true);//true 找出已啟用
        if(best != null){
            mLocationManager.requestLocationUpdates(best, mMIN_TIME, mMIN_DIST, this);//註冊監聽器
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        mLocationManager.removeUpdates(this);//取消註冊
    }


    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();

        initView();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_list, menu);
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

    private class MyStoreAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyStoreAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return mStoreList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)convertView = mInflater.inflate(R.layout.store_list_view_object,null);

//            final ImageView StoreListImage = (ImageView)convertView.findViewById(R.id.StoreListImage);
//            byte[] decodedString = Base64.decode(mStoreList.get(position).get("gPic").toString(), Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            StoreListImage.setImageBitmap(decodedByte);

//         itemImageView.setImageBitmap((Bitmap)mStoreList.get(position).get("gPic"));


            TextView StoreListStoreText = (TextView)convertView.findViewById(R.id.StoreListStoreText);
            StoreListStoreText.setText(mStoreList.get(position).get("gName").toString());
            TextView StoreListDistanceText = (TextView)convertView.findViewById(R.id.StoreListDistanceText);
            StoreListDistanceText.setText(mStoreList.get(position).get("GPSDistance").toString() + "公尺");


            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mClickStoreData.clear();

        AlertDialog.Builder StoreView = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.store_data_object, null);
        mStoreName = (TextView)view.findViewById(R.id.storeNameTxt);
//        mStorePic = (ImageView)view.findViewById(R.id.storeImage);
        mStoreDataTxt = (TextView)view.findViewById(R.id.storeDataTxt);
        mSearchFoodBtn = (Button)view.findViewById(R.id.SearchFoodBtn);
        mHelpEditBtn = (Button)view.findViewById(R.id.HelpEditBtn);
        mGPSGoBtn = (Button)view.findViewById(R.id.GPSGoBtn);


        try {

            String index_sel = "SELECT gName,Address, gOpen, gClose,gX, gY,Picture , Description FROM `ai_pomo`.`gps` WHERE `gName` = '" +
                    mStoreList.get(position).get("gName").toString() + "' AND `gid` = " + mStoreList.get(position).get("gId").toString() +
                    " union SELECT gName,Address, gOpen, gClose,gX, gY,Picture , Description FROM `ai_pomo`.`store information` WHERE `gName` = '" +
                    mStoreList.get(position).get("gName").toString() + "' AND `gid` = " + mStoreList.get(position).get("gId").toString() + ";";

            String result_sumsel = MySQLConnector.executeQuery(index_sel);

            Log.d("test",result_sumsel);

            JSONArray jsonArray2 = new JSONArray(result_sumsel);

            setTitle("查詢資料結果");

            int count = 0;
            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject jsonData = jsonArray2.getJSONObject(i);
                HashMap<String, Object> h2 = new HashMap<String, Object>();
                h2.put("gName", jsonData.getString("gName"));
                h2.put("Address", jsonData.getString("Address"));
                h2.put("gOpen", jsonData.getString("gOpen"));
                h2.put("gClose", jsonData.getString("gClose"));
                h2.put("gX", jsonData.getString("gX"));
                h2.put("gY", jsonData.getString("gY"));
                h2.put("Picture", jsonData.getString("Picture"));
                h2.put("Description", jsonData.getString("Description"));


                mClickStoreData.add(h2);
                count++;
            }
            Log.d("test",""+count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        if(mClickStoreData.get(0).get("Picture").toString().length() > 0) {
//            try {
//                URL url = new URL(mClickStoreData.get(0).get("Picture").toString());
//                URLConnection conn = url.openConnection();
//
//                HttpURLConnection httpConn = (HttpURLConnection) conn;
//                httpConn.setRequestMethod("GET");
//                httpConn.connect();
//
//                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    InputStream inputStream = httpConn.getInputStream();
//
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    inputStream.close();
//                    mStorePic.setImageBitmap(bitmap);
//                }
//            } catch (MalformedURLException e1) {
//                e1.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }else{
//            try {
//                URL url = new URL("http://203.68.252.55/AndroidConnectDB/DJZ.jpg");
//                URLConnection conn = url.openConnection();
//
//                HttpURLConnection httpConn = (HttpURLConnection) conn;
//                httpConn.setRequestMethod("GET");
//                httpConn.connect();
//
//                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    InputStream inputStream = httpConn.getInputStream();
//
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    inputStream.close();
//                    mStorePic.setImageBitmap(bitmap);
//                }
//            } catch (MalformedURLException e1) {
//                e1.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        String OpenTime = mClickStoreData.get(0).get("gOpen").toString();

        if(100 < Integer.parseInt(OpenTime) && Integer.parseInt(OpenTime) < 1000 ){
            OpenTime = OpenTime.substring(0, 1) + ":" + OpenTime.substring(1,3);
        }else if(Integer.parseInt(OpenTime) >= 1000){
            OpenTime = OpenTime.substring(0,2) + ":" + OpenTime.substring(2,4);
        }else {
            OpenTime = "00:00";
        }

        String CloseTime = mClickStoreData.get(0).get("gClose").toString();

        if(100 < Integer.parseInt(CloseTime) && Integer.parseInt(CloseTime) < 1000 ){
            CloseTime = CloseTime.substring(0,1) + ":" + CloseTime.substring(1,3);
        }else if(Integer.parseInt(CloseTime) >= 1000){
            CloseTime = CloseTime.substring(0,2) + ":" + CloseTime.substring(2,4);
        }else {
            CloseTime = "00:00";
        }


        mStoreName.setText(mClickStoreData.get(0).get("gName").toString().trim());
        mStoreDataTxt.setText(
                "店家地址：" +
                mClickStoreData.get(0).get("Address").toString().trim() + "\n" +
                "店家介紹：" +
                mClickStoreData.get(0).get("Description").toString().trim() + "\n" +
                "營業時間：" +
                OpenTime + " ~ " + CloseTime);

        mSearchFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreListActivity.this,FoodListActivity.class);
                intent.putExtra("gName",mClickStoreData.get(0).get("gName").toString().trim());
                intent.putExtra("gX",mClickStoreData.get(0).get("gX").toString().trim());
                intent.putExtra("gY",mClickStoreData.get(0).get("gY").toString().trim());
                startActivity(intent);
            }
        });

        mHelpEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreListActivity.this,EditStoreDataActivity.class);
                intent.putExtra("gName",mClickStoreData.get(0).get("gName").toString().trim());
                intent.putExtra("Address",mClickStoreData.get(0).get("Address").toString().trim());
                intent.putExtra("Description",mClickStoreData.get(0).get("Description").toString().trim());
                intent.putExtra("gX",mClickStoreData.get(0).get("gX").toString().trim());
                intent.putExtra("gY",mClickStoreData.get(0).get("gY").toString().trim());
                intent.putExtra("gOpen",mClickStoreData.get(0).get("gOpen").toString().trim());
                intent.putExtra("gClose",mClickStoreData.get(0).get("gClose").toString().trim());
                startActivity(intent);
            }
        });

        mGPSGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setData(Uri.parse("http://maps.google.com/maps?f=d&saddr=" + mClickStoreData.get(0).get("gY").toString() + "," + mClickStoreData.get(0).get("gX").toString() +
                        "&daddr=" + String.valueOf(mLatitude) + "," + String.valueOf(mLongitude) + "&hl=tw"));

                startActivity(it);
            }
        });

        StoreView.setView(view);
        StoreView.setPositiveButton("離開", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mClickStoreData.clear();
            }
        });
        StoreView.show();

    }
}
