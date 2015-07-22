package com.power.kiwi.searchyourside;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kiwi on 15/7/2.
 * 作為LazyChargeActivity背後的支援者，盡可能扮演好MVC架構中的Model角色
 */
public class LazyChargeModel extends Fragment implements View.OnClickListener {

    private LazyChargeActivity mLazyChargeActivity;//透過他存取LazyChargeActivity資源
    private Button mTakePicBtn, mAddBtn;//拍照按鈕與入帳按鈕

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

        setListener();
    }
    /**
     * 設置監聽器
     * */
    private void setListener() {

        mTakePicBtn.setOnClickListener(this);
        mAddBtn.setOnClickListener(this);
    }

    private String mPicName = "null";//作為當下image圖檔名稱
    private File mDirFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/" + "RecordPic");//指定儲存路徑
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.takePicBtn:
                Toast.makeText(this.getView().getContext(), "拍照", Toast.LENGTH_LONG).show();
                mPicName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
                Uri imgUri = Uri.parse("file://" + mDirFile + mPicName);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                startActivityForResult(cameraIntent,0);
                break;
            case R.id.addDataBtn:
                Toast.makeText(this.getView().getContext(), "儲存", Toast.LENGTH_LONG).show();
                break;
        }
    }
}

