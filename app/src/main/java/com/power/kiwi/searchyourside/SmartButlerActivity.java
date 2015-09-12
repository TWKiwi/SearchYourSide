package com.power.kiwi.searchyourside;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SmartButlerActivity extends ActionBarActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private OptionsActivity mOptionsActivity = new OptionsActivity();

    private ListView mListView;
    private EditText mEditText;
    private MyAdapter mAdapter;
    private ArrayList<String> mItemList;
    private Button mSelectAllBtn;
    private Button mCancelBtn;
    private Button mDeSelectAllBtn;
    private Button mStartSearchBtn;
    private int mCheckNum; // 記錄選中條目數量
    private TextView mShowTxt;// 用於顯示選中條目數量



    /** Called when the activity is first created. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_butler);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//螢幕保持直立顯示
        mOptionsActivity.optionSpr = getApplication().getSharedPreferences("Option", Context.MODE_PRIVATE);
        /* 實體化View */
        mEditText = (EditText) findViewById(R.id.SearchFoodEdit);
        mListView = (ListView) findViewById(R.id.lv);
        mSelectAllBtn = (Button) findViewById(R.id.SelectAllBtn);
        mCancelBtn = (Button) findViewById(R.id.CancelSelectAllBtn);
        mDeSelectAllBtn = (Button) findViewById(R.id.DeSelectAllBtn);
        mStartSearchBtn = (Button) findViewById(R.id.StartSearchBtn);
        mShowTxt = (TextView) findViewById(R.id.tv);
        mItemList = new ArrayList<String>();
        // 為Adapter準備數據
        initDate();
        // 實體化自定義的MyAdapter
        mAdapter = new MyAdapter(mItemList, this);
        // 設置Adapter給ListView
        mListView.setAdapter(mAdapter);
        setListener();
    }

    // 初始化数据
    private void initDate() {

        mEditText.setText("");
        mItemList.add("飯類");
        mItemList.add("粥類");
        mItemList.add("麵類");
        mItemList.add("油炸類");
        mItemList.add("中式");
        mItemList.add("西式");
        mItemList.add("點心");
        mItemList.add("冰飲");
        mItemList.add("其他");

    }


    // 刷新ListView和TextView的顯示
    private void dataChanged() {
        // 通知ListView的刷新
        mAdapter.notifyDataSetChanged();
        // TextView顯示最新顯示項目
//        mShowTxt.setText("已選擇" + mCheckNum + "項");
    }

    private void setListener(){
        mSelectAllBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        mDeSelectAllBtn.setOnClickListener(this);
        mStartSearchBtn.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.SelectAllBtn :
                // 取得List的長度，將MyAdapter中的map值全設為true
                for (int i = 0; i < mItemList.size(); i++) {
                    MyAdapter.getIsSelected().put(i, true);
                }
                // 數量設為List長度
                mCheckNum = mItemList.size();
                // 刷新ListView和TextView的顯示
                dataChanged();
                break;
            case R.id.CancelSelectAllBtn :
                // 取得List的長度，將已選的設為未選，未選的設為已選
                for (int i = 0; i < mItemList.size(); i++) {
                    if (MyAdapter.getIsSelected().get(i)) {
                        MyAdapter.getIsSelected().put(i, false);
                        mCheckNum--;
                    } else {
                        MyAdapter.getIsSelected().put(i, true);
                        mCheckNum++;
                    }
                }
                // 刷新ListView和TextView的顯示
                dataChanged();
                break;
            case R.id.DeSelectAllBtn :
                // 取得List的長度，將已選的設為未選
                for (int i = 0; i < mItemList.size(); i++) {
                    if (MyAdapter.getIsSelected().get(i)) {
                        MyAdapter.getIsSelected().put(i, false);
                        mCheckNum--;// 數量減一
                    }
                }
                // 刷新ListView和TextView的顯示
                dataChanged();
                break;

            case R.id.StartSearchBtn :
                String[] arrayType = new String[8];
                String time = new SimpleDateFormat("HHmm").format(new Date());
                if(mEditText.length() != 0){

                    Intent intent = new Intent(this,StoreListActivity.class);
                    intent.putExtra("inputType","Search");
                    intent.putExtra("Name",mEditText.getText().toString().trim());
                    intent.putExtra("Time",time);
                    intent.putExtra("Number",0);
                    startActivity(intent);
                    mShowTxt.setText(mEditText.getText());

                }else if(mCheckNum > 0){
                    int num = 0;
                    for(int i = 0; i < mItemList.size(); i++){
                        if(MyAdapter.getIsSelected().get(i)){
                            arrayType[num] = mItemList.get(i);
                            num++;
                        }
                    }

                    Intent intent = new Intent(this,StoreListActivity.class);
                    intent.putExtra("inputType","TypeChoice");
                    intent.putExtra("arrayType",arrayType);
                    intent.putExtra("Time",time);
                    intent.putExtra("Number",1);
                    startActivity(intent);


                }else {

                    Intent intent = new Intent(this,StoreListActivity.class);
                    intent.putExtra("inputType","SystemChoice");
                    intent.putExtra("Type",mOptionsActivity.hashMapSort());
                    intent.putExtra("Time",time);
                    intent.putExtra("Number",2);
                    startActivity(intent);

                }
                dataChanged();
                break;

        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 取得ViewHolder對象，這樣就省去了通過層層的findViewById去實體化我們需要的CheckBox步驟
        MyAdapter.ViewHolder holder = (MyAdapter.ViewHolder) view.getTag();
        // 改變CheckBox狀態
        holder.checkBox.toggle();
        // 將CheckBox的選中狀態記錄下來
        MyAdapter.getIsSelected().put(position, holder.checkBox.isChecked());
        // 調整選定項目
        if (holder.checkBox.isChecked()) {
            mCheckNum++;
        } else {
            mCheckNum--;
        }
        // 用TextView顯示
        mShowTxt.setText("已選擇" + mCheckNum + "項");
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smart_butler, menu);
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



class MyAdapter extends BaseAdapter {
    // 填充數據的list
    private ArrayList<String> list;
    // 用來控制CheckBox的選中狀況
    private static HashMap<Integer, Boolean> isSelected;
    // 整體環境
    private Context context;
    // 用來導入佈局
    private LayoutInflater inflater = null;

    // 建構子
    public MyAdapter(ArrayList<String> list, Context context) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化數據
        initDate();
    }

    // 初始化isSelected的數據
    private void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            // 獲得ViewHolder對象
            holder = new ViewHolder();
            // 導入佈局並賦予值convertView
            convertView = inflater.inflate(R.layout.search_item_list_object, null);
            holder.textView = (TextView) convertView.findViewById(R.id.item_tv);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.item_cb);
            // 為view設置標籤
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }
        // 設置List中TextView的顯示
        holder.textView.setText(list.get(position));
        // 根據isSelected來設置checkbox的選中狀況
        holder.checkBox.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        MyAdapter.isSelected = isSelected;
    }

    public static class ViewHolder {
        TextView textView;
        CheckBox checkBox;
    }
}
