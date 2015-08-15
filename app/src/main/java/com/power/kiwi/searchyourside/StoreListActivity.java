package com.power.kiwi.searchyourside;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class StoreListActivity extends ActionBarActivity implements LocationListener, AdapterView.OnItemClickListener {

    private String mInputType,mTime,mName,mType;
    private ListView StoreListView,FoodListView;
    private ImageView StorePic;
    private TextView StoreTxt,StoreName;
    private int mNumber,StorePosition;
    private ArrayList<HashMap<String, Object>> StoreList = new ArrayList<HashMap<String, Object>>();
    private ArrayList<HashMap<String, Object>> FoodList;

    /**
     * 定位工程
     * */
    static final int MIN_TIME = 5000;
    static final float MIN_DIST = 5;
    private LocationManager mLocationManager;
    double mLatitude,mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        initView();

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
        mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        StoreListView = (ListView)findViewById(R.id.StoreList);
        StoreListView.setOnItemClickListener(this);

        getBundle();
    }

    private void getBundle(){
        Intent intent = getIntent();

        mInputType = intent.getStringExtra("inputType");
        mTime = intent.getStringExtra("Time");
        mName = intent.getStringExtra("Name");
        mType = intent.getStringExtra("Type");
        mNumber = intent.getIntExtra("Number",2);

        Log.d("Test", mInputType + "\n" + mTime + "\n" + mName + "\n" + mType + "\n" + mNumber);
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
//                    StoreList.add(h2);
//
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return StoreList;


//        if(whatBtn.equals("isProposal")){



        String index_sum = "UPDATE `ai_pomo`.`gps` SET `gUserX` = " + mLongitude + ", `gUserY` = " + mLatitude + ";";
        MySQLConnector.executeQuery(index_sum);


        try {
            String indexG = "SELECT *, \n" +"round(6378.138*2*asin(sqrt(pow(sin( (`gY`*pi()/180-`gUserY`*pi()/180)/2),2)+cos(`gY`*pi()/180)*cos(`gUserY`*pi()/180)* pow(sin( (`gX`*pi()/180-`gUserX`*pi()/180)/2),2)))*1000)  'Distance'  from `gps`;";
            String resultG  = MySQLConnector.executeQuery(indexG);
            Log.d("ResultG",resultG);
            JSONArray jsonArray = new JSONArray(resultG);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                int selGps = Integer.parseInt(jsonData.getString("Distance"));
                String index_long =  "UPDATE `gps` SET `long` = "+ selGps +" where `gId` = '" + (i+1) + ";'";
                MySQLConnector.executeQuery(index_long);
            }
            String index_rank = "UPDATE `gps` SET `gRank`=`gFrequency`/`long`";
            MySQLConnector.executeQuery(index_rank);


            if(hashMapSort().equals("rice")) {
                //特殊加成
                String select = "SELECT DISTINCT `fStore` FROM `food` WHERE `fSort` like '%rice%' ORDER BY `fRank` DESC";
                JSONArray jsonArray1 = new JSONArray(MySQLConnector.executeQuery(select));

                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject = jsonArray1.getJSONObject(i);
                    MySQLConnector.executeQuery("UPDATE `gps` SET `gRank`=`gFrequency`*10/`long`+50 where `gName` = '" + jsonObject.getString("fStore") + "'", php);

                }
            }else if(hashMapSort().equals("noodles")) {
                //特殊加成
                String select = "SELECT DISTINCT `fStore` FROM `food` WHERE `fSort` like '%noodles%' ORDER BY `fRank` DESC";
                JSONArray jsonArray1 = new JSONArray(MySQLConnector.executeQuery(select));

                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject = jsonArray1.getJSONObject(i);
                    MySQLConnector.executeQuery("UPDATE `gps` SET `gRank`=`gFrequency`*10/`long`+50 where `gName` = '" + jsonObject.getString("fStore") + "'", php);

                }
            }
            String index_sel = "SELECT * from `gps` where `long` < 5000 and `gStoreClass` LIKE '%" + SpinnerClass + "%'order by `gRank` desc;";
            String result_sumsel =  MySQLConnector.executeQuery(index_sel);
            JSONArray jsonArray2 = new JSONArray(result_sumsel);

            setTitle("查詢資料結果");

            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject jsonData = jsonArray2.getJSONObject(i);
                HashMap<String, Object> h2 = new HashMap<String, Object>();
                h2.put("gName", jsonData.getString("gName"));
                h2.put("long", jsonData.getString("long") + " 公尺");
                h2.put("gX", jsonData.getString("gX"));
                h2.put("gY", jsonData.getString("gY"));
                h2.put("gUserX", jsonData.getString("gUserX"));
                h2.put("gUserY", jsonData.getString("gUserY"));
                h2.put("gPic", jsonData.getString("gPic"));
                h2.put("Description", jsonData.getString("Description"));

                StoreList.add(h2);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
//    }

        return StoreList;
    }

    @Override
    public void onResume(){
        super.onResume();
        //取得最佳定位提供者
        String best = mLocationManager.getBestProvider(new Criteria(), true);//true 找出已啟用
        if(best != null){
            mLocationManager.requestLocationUpdates(best, MIN_TIME, MIN_DIST, this);//註冊監聽器
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        mLocationManager.removeUpdates(this);//取消註冊
    }


    @Override
    public void onLocationChanged(Location location) {
        String str = "定位提供者：" + location.getProvider();
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        str += String.format("\n緯度:%.5f\n經度:%.5f",
                mLatitude,
                mLongitude);

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
            return StoreList.size();
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

            final ImageView StoreListImage = (ImageView)convertView.findViewById(R.id.StoreListImage);
            byte[] decodedString = Base64.decode(StoreList.get(position).get("gPic").toString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            StoreListImage.setImageBitmap(decodedByte);

//         itemImageView.setImageBitmap((Bitmap)StoreList.get(position).get("gPic"));
            TextView StoreListStoreText = (TextView)convertView.findViewById(R.id.StoreListStoreText);
            StoreListStoreText.setText(StoreList.get(position).get("gName").toString());
            TextView StoreListDistanceText = (TextView)convertView.findViewById(R.id.StoreListDistanceText);
            StoreListDistanceText.setText(StoreList.get(position).get("long").toString());


            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
