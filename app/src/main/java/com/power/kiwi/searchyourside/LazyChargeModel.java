package com.power.kiwi.searchyourside;

import static com.power.kiwi.searchyourside.DbConstants.TABLE_NAME;
import static com.power.kiwi.searchyourside.DbConstants.PICNAME;
import static com.power.kiwi.searchyourside.DbConstants.NAME;
import static com.power.kiwi.searchyourside.DbConstants.TYPE;
import static com.power.kiwi.searchyourside.DbConstants.PRICE;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Observable;

import static android.provider.BaseColumns._ID;

/**
 * Created by kiwi on 15/7/2.
 */
public class LazyChargeModel extends Fragment implements View.OnClickListener {

    LazyChargeActivity mlazyChargeActivity;
    private Button mtakePicBtn, maddBtn;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mlazyChargeActivity = (LazyChargeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.camera_view, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mtakePicBtn = (Button) this.getView().findViewById(R.id.takePicBtn);
        maddBtn = (Button) this.getView().findViewById(R.id.addDataBtn);

        setListener();
    }

    private void setListener() {

        mtakePicBtn.setOnClickListener(this);
        maddBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.takePicBtn:
                Toast.makeText(this.getView().getContext(), "拍照", Toast.LENGTH_LONG).show();
                break;
            case R.id.addDataBtn:
                Toast.makeText(this.getView().getContext(), "儲存", Toast.LENGTH_LONG).show();
                break;
        }
    }
}

