package com.power.kiwi.searchyourside;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kiwi on 15/7/2.
 * 作為LazyChargeActivity背後的支援者，盡可能扮演好MVC架構中的Model角色
 */
public class LazyChargeModel extends Fragment implements View.OnClickListener {

    private LazyChargeActivity mLazyChargeActivity;//透過他存取LazyChargeActivity資源
    private Button mTakePicBtn, mAddBtn;//拍照按鈕與入帳按鈕
    private ImageView mImageView;
    private Uri imgUri;//圖檔路徑
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mLazyChargeActivity = (LazyChargeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.camera_view, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTakePicBtn = (Button) this.getView().findViewById(R.id.takePicBtn);
        mAddBtn = (Button) this.getView().findViewById(R.id.addDataBtn);
        mImageView = (ImageView)this.getView().findViewById(R.id.PicImageView);

        setListener();
    }

    private String mPicName = "null";//作為當下image圖檔名稱
    private File mDirFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/" + "RecordPic");//指定儲存路徑
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
                break;
        }
    }

    /**
     * 設置監聽器
     * */
    private void setListener() {

        mTakePicBtn.setOnClickListener(this);
        mAddBtn.setOnClickListener(this);
    }

    /**
     * 顯示圖片，換算照片與圖片畫面的大小比例做調整，預防一次載入過大資料流造成系統崩潰
     * */
    public void showImage(){

        int PicW,PicH,ImgViewW,ImgViewH;


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgUri.getPath(),options);

        PicW = options.outHeight;
        PicH = options.outWidth;
        ImgViewW = mImageView.getWidth();
        ImgViewH = mImageView.getHeight();

        int scaleFactor = Math.min(PicW/ImgViewW , PicH/ImgViewH);
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        options.inPurgeable = true;
        /**讀取圖檔內容轉換為Bitmap物件*/
        Bitmap bmp = BitmapFactory.decodeFile(imgUri.getPath(),options);
        /**顯示*/
        mImageView.setImageBitmap(bmp);

        compressImageByQuality(bmp, imgUri.getPath());

    }

    /**
     * 圖片壓縮方法，每次壓縮成原本大小的90%，直到小於100kb
     * @param bmp 點陣圖資料流
     * @param imagePath 圖檔路徑
     * */
    private void compressImageByQuality(final Bitmap bmp, final String imagePath){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int options = 100;
                bmp.compress(Bitmap.CompressFormat.JPEG, options, byteArrayOutputStream);//壓縮方法，把壓縮後的數據存到byteArrayOutputStream中(100表示不壓縮，0表示壓縮到最小)
                while (byteArrayOutputStream.toByteArray().length / 1024 > 100) {//判斷壓縮後圖片是否大於100kb，大於就繼續壓縮
                    byteArrayOutputStream.reset();//重置，即複寫之前bmp的內容
                    options -= 10;//每次調整壓縮量，減至0則壓至最小
                    if(options < 10)options = 0;//如果成立，則將圖片質量壓縮到最小值
                    bmp.compress(Bitmap.CompressFormat.JPEG, options, byteArrayOutputStream);//將壓縮後的圖片保存至byteArrayOutputStream中
                    if(options == 0)break;//如果成立，不再進行壓縮
                }
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(imagePath));//將壓縮後的圖片保存至指定路徑中
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

