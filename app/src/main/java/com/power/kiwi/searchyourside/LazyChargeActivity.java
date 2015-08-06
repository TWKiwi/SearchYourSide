package com.power.kiwi.searchyourside;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.power.kiwi.searchyourside.DbConstants.NAME;
import static com.power.kiwi.searchyourside.DbConstants.PICNAME;
import static com.power.kiwi.searchyourside.DbConstants.PRICE;
import static com.power.kiwi.searchyourside.DbConstants.TABLE_NAME;
import static com.power.kiwi.searchyourside.DbConstants.TYPE;

/**
 * 懶人記帳功能的Activity，將以分頁呈現拍照記帳,月曆查詢,圖表畫面以上三分支功能，盡可能扮演好MVC架構中的Controller角色
 * */
public class LazyChargeActivity extends FragmentActivity implements ActionBar.TabListener {

    public static DBHelper mSQLiteDB = null;//資料庫物件

    private AppSectionsPagerAdapter mAppSectionsPagerAdapter;//畫面適配器

    private ViewPager mViewPager;//畫面

    public static List<String> mPageTittle;//畫面標題



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lazy_charge);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//螢幕保持直立顯示
        openDatabase(this);
        initView();

    }

    /**
     * 建立分頁畫面
     */
    private void initView() {

        //設定三個頁面標題
        mPageTittle = new ArrayList<>();
        mPageTittle.add("拍照記帳");
        mPageTittle.add("月曆查詢");
        mPageTittle.add("各類開銷");

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        // 建立一個適配器那將會回傳這個app的三個主要部分的每一個片段
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        // 取得動作列
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        // 指設定左上角圖標能否被點擊
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        // 指我們將要顯示tab在動作列上
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        // 取得ViewPager,附上適配器和當使用者在兩個部分之間滑動切換時設定監聽器
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    /**
     * 建立資料庫
     *
     * @param context 整個系統環境
     */
    protected void openDatabase(Context context) {

        mSQLiteDB = new DBHelper(context);

    }

    /**
     * 關閉資料庫
     */
    protected void closeDatabase() {

        mSQLiteDB.close();

    }

    /**
     * 加入資料庫
     *
     * @param picName      圖片名稱，命名格式為年月日時分秒，也是辨別用名稱
     * @param chargeRecord 入帳名稱，可不填
     * @param chargeType   類別名稱，食衣住行育樂醫療
     * @param chargePrice  金額
     */
    protected void addDb(String picName, String chargeRecord, String chargeType, String chargePrice) {

        SQLiteDatabase db = mSQLiteDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PICNAME, picName.trim());
        values.put(NAME, chargeRecord.trim());
        values.put(TYPE, chargeType.trim());
        values.put(PRICE, chargePrice.trim());
        db.insert(TABLE_NAME, null, values);

    }

    /**
     * Cursor在資料庫中的游標的意思
     */
    public Cursor getCursor() {

        SQLiteDatabase db = mSQLiteDB.getReadableDatabase();
        String[] colums = {_ID, PICNAME, NAME, TYPE, PRICE};

        Cursor cursor = db.query(TABLE_NAME, colums, null, null, null, null, null);
        startManagingCursor(cursor);

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
    public void onDestroy() {
        super.onDestroy();
        closeDatabase();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding
     * to one of the primary sections of the app.
     * 畫面適配器
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        FragmentManager fm;

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
            Log.d("進入App", "AppSectionsPagerAdapter");
        }

        @Override
        public Fragment getItem(int i) {
            Log.d("進入App", "getItem");
            switch (i) {
                case 0:
                    return new CameraView();
                case 1:
                    return new CalendarSearchView();
                default:
                    return new BarChartView();
            }
        }

        @Override
        public int getCount() {
            Log.d("進入App", "getCount()");
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.d("進入App", "getPageTitle");
            return mPageTittle.get(position);
        }
    }

    /**
     * 初始化拍照記帳畫面
     */
    public static class CameraView extends Fragment implements View.OnClickListener {
        private View mRootView;
        private Button mTakePicBtn, mAddBtn;//拍照按鈕與入帳按鈕
        private ImageView mImageView;
        private LazyChargeActivity mLazyChargeActivity = new LazyChargeActivity();
        private CalendarSearchView mCalendarSearchView = new CalendarSearchView();
        private EditText mItemName, mItemPrice;
        private Spinner mItemType;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mRootView = inflater.inflate(R.layout.camera_view, container, false);

            initView();

            //如果資料夾不存在
            if (!mDirFile.exists()) {
                //建立資料夾
                mDirFile.mkdirs();

            }

//            // Demonstration of a collection-browsing activity.
//            mRootView.findViewById(R.id.takePicBtn)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent intent = new Intent(getActivity(), CollectionDemoActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//
//            // Demonstration of navigating to external activities.
//            mRootView.findViewById(R.id.addDataBtn)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            // Create an intent that asks the user to pick a photo, but using
//                            // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
//                            // the application from the device home screen does not return
//                            // to the external activity.
//                            Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
//                            externalActivityIntent.setType("image/*");
//                            externalActivityIntent.addFlags(
//                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                            startActivity(externalActivityIntent);
//                        }
//                    });
            setListener();
            return mRootView;
        }

        /**
         * 初始化View物件
         */
        private void initView() {
            mTakePicBtn = (Button) mRootView.findViewById(R.id.takePicBtn);
            mAddBtn = (Button) mRootView.findViewById(R.id.addDataBtn);
            mImageView = (ImageView) mRootView.findViewById(R.id.PicImageView);
            mItemName = (EditText) mRootView.findViewById(R.id.ItemName);
            mItemType = (Spinner) mRootView.findViewById(R.id.ItemType);
            mItemPrice = (EditText) mRootView.findViewById(R.id.ItemPrice);
        }

        /**
         * 設置監聽器
         */
        private void setListener() {

            mTakePicBtn.setOnClickListener(this);
            mAddBtn.setOnClickListener(this);
        }

        /**
         * 作為當下image圖檔名稱
         */
        private String mPicName = "null";

        /**
         * 指定儲存路徑
         */
        private File mDirFile =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
                        "/" + "RecordPic");

        /**
         * 圖檔路徑
         */
        private Uri imgUri;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.takePicBtn:
                    Toast.makeText(this.getView().getContext(), "拍照", Toast.LENGTH_LONG).show();
                    mPicName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
                    imgUri = Uri.parse("file://" + mDirFile + "/" + mPicName);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    startActivityForResult(cameraIntent, 0);
                    break;
                case R.id.addDataBtn:
                    Toast.makeText(this.getView().getContext(), "儲存", Toast.LENGTH_LONG).show();
                    if (mPicName.equals("null")) mPicName =
                            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
                    mLazyChargeActivity.addDb(mPicName,
                            mItemName.getText().toString(),
                            mItemType.getSelectedItem().toString(),
                            mItemPrice.getText().toString());
                    mImageView.setImageDrawable(null);
                    mPicName = "null";
                    mItemName.setText("");
                    mItemPrice.setText("");
                    break;
            }
        }

        /**
         * @param requestCode 回傳參數
         * @param resultCode  判別按下是確定或取消
         * @param data        照片資料
         */
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            Log.d("進入onActivityResult", "onActivityResult");
            if (resultCode == RESULT_OK) {
                Log.d("進入onActivityResult", "resultCode == RESULT_OK");
                showImage();
            } else {
                Log.d("進入onActivityResult", "else");
            }

        }

        /**
         * 顯示圖片，換算照片與圖片畫面的大小比例做調整，預防一次載入過大資料流造成系統崩潰
         */
        public void showImage() {

            int PicW, PicH, ImgViewW, ImgViewH;


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imgUri.getPath(), options);

            PicW = options.outHeight;
            PicH = options.outWidth;
            ImgViewW = mImageView.getWidth();
            ImgViewH = mImageView.getHeight();

            int scaleFactor = Math.min(PicW / ImgViewW, PicH / ImgViewH);
            options.inJustDecodeBounds = false;
            options.inSampleSize = scaleFactor;
            options.inPurgeable = true;
            /**讀取圖檔內容轉換為Bitmap物件*/
            Bitmap bmp = BitmapFactory.decodeFile(imgUri.getPath(), options);
            /**顯示*/
            mImageView.setImageBitmap(bmp);

            compressImageByQuality(bmp, imgUri.getPath());

        }

        /**
         * 圖片壓縮方法，每次壓縮成原本大小的90%，直到小於100kb
         *
         * @param bmp       點陣圖資料流
         * @param imagePath 圖檔路徑
         */
        private void compressImageByQuality(final Bitmap bmp, final String imagePath) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    int options = 100;
                    //壓縮方法，把壓縮後的數據存到byteArrayOutputStream中(100表示不壓縮，0表示壓縮到最小)
                    bmp.compress(Bitmap.CompressFormat.JPEG, options, byteArrayOutputStream);
                    //判斷壓縮後圖片是否大於100kb，大於就繼續壓縮
                    while (byteArrayOutputStream.toByteArray().length / 1024 > 100) {
                        //重置，即複寫之前bmp的內容
                        byteArrayOutputStream.reset();
                        //每次調整壓縮量，減至0則壓至最小
                        options -= 10;
                        //如果成立，則將圖片質量壓縮到最小值
                        if (options < 10) options = 0;
                        //將壓縮後的圖片保存至byteArrayOutputStream中
                        bmp.compress(Bitmap.CompressFormat.JPEG, options, byteArrayOutputStream);
                        //如果成立，不再進行壓縮
                        if (options == 0) break;
                    }
                    try {
                        //將壓縮後的圖片保存至指定路徑中
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(imagePath));
                        fileOutputStream.write(byteArrayOutputStream.toByteArray());
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }
    }

    /**
     * 初始化日曆畫面
     */
    public static class CalendarSearchView extends ListFragment implements CalendarView.OnDateChangeListener {

        private LazyChargeActivity mLazyChargeActivity = new LazyChargeActivity();
        private MyAdapter mAdapter;

        private View rootView;
        private CalendarView mCalendarView;
        private ListView mListView;

        private Long mDate;
        private List<HashMap<String, Object>> mItemList;
        /**
         * 指定儲存路徑
         */
        private File mDirFile =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
                        "/" + "RecordPic");

        /**
         * 圖檔路徑
         */
        private Uri imgUri;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.calendar_view, container, false);

            initView();
            Log.d("Text","onCreateView");
            return rootView;
        }

        /**
         * 初始化View元件
         */
        private void initView() {
            mCalendarView = (CalendarView) rootView.findViewById(R.id.CalendarView);
            mDate = mCalendarView.getDate();
            mCalendarView.setOnDateChangeListener(this);

            mListView = (ListView) rootView.findViewById(R.id.listView);
            mItemList = getData(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            mAdapter = new MyAdapter(getActivity());
            mListView.setAdapter(mAdapter);
            Log.d("Text","initView");

        }

        @Override
        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

            String Year = String.valueOf(year);
            String Month = String.valueOf(month);
            String Day = String.valueOf(dayOfMonth);
            //避免翻月時誤觸監聽器
            if (mCalendarView.getDate() != mDate) {
                mDate = mCalendarView.getDate();
                //月份在這裡是從0算起故加1代表正確月份
                month += 1;
                if(year < 1000) Year = "0" + String.valueOf(year);

                if (month < 10) Month = "0" + String.valueOf(month);

                if (dayOfMonth < 10) Day = "0" + String.valueOf(dayOfMonth);

                String DATE = Year + Month + Day;

                mItemList = getData(String.valueOf(DATE));
                mAdapter = new MyAdapter(getActivity());
                mListView.setAdapter(mAdapter);
                Log.d("Text","onSelectDayChange");
            }
        }

        private List<HashMap<String, Object>> getData(String DATE) {
            //新建一個集合類，用於存放多條數據，Map的key是一個String類型，Map的value是Object類型
            ArrayList<HashMap<String, Object>> list = new ArrayList<>();


            Cursor cursor = mLazyChargeActivity.getCursor();

            while (cursor.moveToNext()) {

                if (DATE.equals(cursor.getString(1).substring(0, 8))) {

                    //依前面的路徑及檔案名建立Uri物件
                    imgUri = Uri.parse("file://" + mDirFile + "/" + cursor.getString(1));
                    //讀取圖檔內容轉換為Bitmap物件
                    Bitmap bmp = BitmapFactory.decodeFile(imgUri.getPath());
                    HashMap<String, Object> item = new HashMap<>();

                    int id = cursor.getInt(0);
                    String picname = cursor.getString(1);
                    String name = cursor.getString(2);
                    String type = cursor.getString(3);
                    String price = cursor.getString(4);

                    StringBuilder itemData = new StringBuilder();
                    itemData.append("品名：").append(name).append("\n");
                    itemData.append("類型：").append(type).append("\n");
                    itemData.append("價錢：").append(price).append("元\n");

                    item.put("itemImageView", bmp);
                    item.put("itemData", itemData);
                    item.put("id", id);
                    item.put("picName", picname);
                    list.add(item);

                    Log.d("Text","DATE.equals(cursor.getString(1).substring(0, 8))");
                }
                Log.d("Text","cursor.moveToNext()" + DATE);
            }
            Log.d("Text","getData");
            return list;
        }



        public class MyAdapter extends BaseAdapter {

            private LayoutInflater mInflater;

            public MyAdapter(Context context){
                this.mInflater = LayoutInflater.from(context);
            }

            @Override
            public int getCount() {
                return mItemList.size();
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

                if (convertView == null)
                    convertView = mInflater.inflate(R.layout.list_view_object, null);

                final ImageView itemImageView = (ImageView) convertView.findViewById(R.id.ChargeListImg);
                itemImageView.setImageBitmap((Bitmap) mItemList.get(position).get("itemImageView"));

                if(mItemList.get(position).get("itemImageView") == null) itemImageView.setImageDrawable(getResources().getDrawable(R.drawable.camerapic));

                TextView itemView = (TextView) convertView.findViewById(R.id.ChargeListTxt);
                itemView.setText(mItemList.get(position).get("itemData").toString());
                Button deleteBtn = (Button) convertView.findViewById(R.id.DeleteBtn);
                final String id = mItemList.get(position).get("id").toString();
                final String picName = mItemList.get(position).get("picName").toString();
                deleteBtn.setTag(position);
                deleteBtn.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                    try{
                        mItemList.remove(position);
                        notifyDataSetChanged();
                        SQLiteDatabase db = mSQLiteDB.getWritableDatabase();
                        db.delete(TABLE_NAME, _ID + "=" + id, null);
                        mListView.setAdapter(mAdapter);


//                  }catch (Exception e){
//                      Toast.makeText(DateListActivity.this,"當日沒有紀錄",Toast.LENGTH_LONG).show();
                        /**以下刪除功能在android 4.4以上版本不適用*/
                        File f = new File(String.valueOf(mDirFile + "/" + picName));
                        f.delete();
//                    }
                    }
                });
                return convertView;
            }

        }

    }

    /**
     * 初始化圖表畫面
     */
    public static class BarChartView extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.bar_chart_view, container, false);


            return rootView;
        }

    }

}
