package com.power.kiwi.searchyourside;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class FoodListActivity extends ActionBarActivity {
    private TextView mFoodName,mFoodPrice, mFoodAddScoreTxt;
    private ListView mFoodListView;
    private String mStoreName,gX,gY;
    private ArrayList<HashMap<String, Object>> mFoodList = new ArrayList<HashMap<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
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

        initView();

    }

    private void initView(){
        mFoodName = (TextView) findViewById(R.id.FoodNameTxt);
        mFoodPrice = (TextView) findViewById(R.id.FoodPriceTxt);
        mFoodAddScoreTxt = (TextView) findViewById(R.id.FoodAddScore);
        mFoodListView = (ListView) findViewById(R.id.FoodList);

        getBundle();

        setListView();
        MyFoodAdapter adapter = new MyFoodAdapter(this);
        mFoodListView.setAdapter(adapter);
    }

    private void getBundle(){
        Intent intent = getIntent();

        mStoreName = intent.getStringExtra("gName");
        gX = intent.getStringExtra("gX");
        gY = intent.getStringExtra("gY");
    }

    private ArrayList<HashMap<String, Object>> setListView(){

        Log.d("test", "setListView" + mStoreName + " " + gX + " " + gY);

        try {

            String index_sel = "SELECT fName, fPrice, frequency FROM `ai_pomo`.`food` WHERE `fStore` LIKE '%" + mStoreName.substring(0,3) + "%' " +
                    "union SELECT sName, sPrice, sFrequency FROM `ai_pomo`.`store` WHERE `sStore` LIKE '%" + mStoreName + "%' AND `sX` = " + gX +
                    " AND `sY` = " + gY + " ORDER BY `frequency` DESC;";


            String result_sumsel = MySQLConnector.executeQuery(index_sel);

            Log.d("test",result_sumsel);

            JSONArray jsonArray2 = new JSONArray(result_sumsel);

            setTitle("查詢資料結果");

            int count = 0;
            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject jsonData = jsonArray2.getJSONObject(i);
                HashMap<String, Object> h2 = new HashMap<String, Object>();
                h2.put("fName", jsonData.getString("fName"));
                h2.put("fPrice", jsonData.getString("fPrice"));
                h2.put("frequency", jsonData.getString("frequency"));

                mFoodList.add(h2);
                count++;
            }
            Log.d("test",""+count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//    }

        return mFoodList;
    }

    private class MyFoodAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyFoodAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return mFoodList.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null)convertView = mInflater.inflate(R.layout.activity_food_list,null);

//            final ImageView StoreListImage = (ImageView)convertView.findViewById(R.id.StoreListImage);
//            byte[] decodedString = Base64.decode(mStoreList.get(position).get("gPic").toString(), Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            StoreListImage.setImageBitmap(decodedByte);

//         itemImageView.setImageBitmap((Bitmap)mStoreList.get(position).get("gPic"));


            mFoodName = (TextView)convertView.findViewById(R.id.FoodNameTxt);
            mFoodName.setText(mFoodList.get(position).get("fName").toString());
            mFoodPrice = (TextView)convertView.findViewById(R.id.FoodPriceTxt);
            mFoodPrice.setText(mFoodList.get(position).get("fPrice").toString());
            mFoodAddScoreTxt = (TextView)convertView.findViewById(R.id.FoodAddScore);
            mFoodAddScoreTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String index_sel ="UPDATE `ai_pomo`.`food` SET `frequency` = `frequency`+ 1 WHERE `food`.`fName` = '" +
                            mFoodList.get(position).get("fName").toString() + "';";

                    MySQLConnector.executeQuery(index_sel);
                }
            });


            return convertView;
        }
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
}
