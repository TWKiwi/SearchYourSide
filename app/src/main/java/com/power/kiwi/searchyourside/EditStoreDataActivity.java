package com.power.kiwi.searchyourside;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class EditStoreDataActivity extends ActionBarActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    OptionsActivity mOptionsActivity = new OptionsActivity();

    EditText mNewStoreName,mNewStoreAddress,mNewStoreDescription,mNewStoreRemarkNote;
    Button mStoreEditBtn,mEditCancelBtn;
    Spinner mOpenHouseSpn,mOpenMinuteSpn,mCloseHouseSpn,mCloseMinuteSpn;

    String mStoreName,mgX,mgY,mOpenTime,mCloseTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store_data);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//螢幕保持直立顯示
        mOptionsActivity.optionSpr = getApplication().getSharedPreferences("Option", Context.MODE_PRIVATE);
        initEditView();


    }

    private void initEditView(){
        setTitle("提出編輯");

        mNewStoreName = (EditText) findViewById(R.id.newStoreName);
        mNewStoreDescription = (EditText) findViewById(R.id.newStoreDescription);
        mNewStoreAddress = (EditText) findViewById(R.id.newStoreAddress);
        mNewStoreRemarkNote = (EditText) findViewById(R.id.newStoreRemarkNote);
        mStoreEditBtn = (Button) findViewById(R.id.StoreEditBtn);
        mStoreEditBtn.setOnClickListener(this);
        mEditCancelBtn = (Button) findViewById(R.id.EditCancelBtn);
        mEditCancelBtn.setOnClickListener(this);
        mOpenHouseSpn = (Spinner) findViewById(R.id.OpenHours);
        mOpenMinuteSpn = (Spinner) findViewById(R.id.OpenMinute);
        mCloseHouseSpn = (Spinner) findViewById(R.id.CloseHours);
        mCloseMinuteSpn = (Spinner) findViewById(R.id.CloseMinute);
        mOpenHouseSpn.setOnItemSelectedListener(this);
        mOpenMinuteSpn.setOnItemSelectedListener(this);
        mCloseHouseSpn.setOnItemSelectedListener(this);
        mCloseMinuteSpn.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        mNewStoreName.setText(intent.getStringExtra("gName"));
        mStoreName = intent.getStringExtra("gName");
        mNewStoreAddress.setText(intent.getStringExtra("Address"));
        mNewStoreDescription.setText(intent.getStringExtra("Description"));
        mgX = intent.getStringExtra("gX");
        mgY = intent.getStringExtra("gY");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_store_data, menu);
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
        switch (v.getId()){
            case R.id.StoreEditBtn :

                String memberID = mOptionsActivity.getString("MemberID");

                mOpenTime = mOpenHouseSpn.getSelectedItem().toString() + mOpenMinuteSpn.getSelectedItem().toString();
                mCloseTime = mCloseHouseSpn.getSelectedItem().toString() + mCloseMinuteSpn.getSelectedItem().toString();

                String index_sel = "INSERT INTO `user_extra`.`changelist` (`Name`, `NewName`, `gX`, `gY`, `gOpen`, `gClose`, `Description`, `Remarknote`, `Account`) VALUES ('" +
                        mStoreName + "', '" + mNewStoreName + "', '" + mgX + "', '" + mgY + "', '" + mOpenTime + "', '" + mCloseTime + "', '" +
                        mNewStoreDescription.getText().toString() + "', '" + mNewStoreRemarkNote.getText().toString() + "', '" + memberID + "' );";

                MySQLConnector.executeQuery(index_sel);

                Toast.makeText(this,"申請已提交\n我們會盡快審核並更新\n感謝您",Toast.LENGTH_LONG).show();
                this.finish();
                break;
            case R.id.EditCancelBtn :
                this.finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView mSpinnerTextColor = (TextView)mOpenHouseSpn.getChildAt(0);
        mSpinnerTextColor.setTextColor(Color.WHITE);
        mSpinnerTextColor = (TextView)mOpenMinuteSpn.getChildAt(0);
        mSpinnerTextColor.setTextColor(Color.WHITE);
        mSpinnerTextColor = (TextView)mCloseHouseSpn.getChildAt(0);
        mSpinnerTextColor.setTextColor(Color.WHITE);
        mSpinnerTextColor = (TextView)mCloseMinuteSpn.getChildAt(0);
        mSpinnerTextColor.setTextColor(Color.WHITE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
