package com.power.kiwi.searchyourside;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;



public class EditStoreDataActivity extends ActionBarActivity implements View.OnClickListener {

    EditText mNewStoreName,mNewStoreAddress,mNewStoreDescription,mNewStoreRemarkNote;
    Button mStoreEditBtn,mEditCancelBtn;
    Spinner mOpenHouseSpn,mOpenMinuteSpn,mCloseHouseSpn,mCloseMinuteSpn;

    String mStoreName,mgX,mgY,mOpenTime,mCloseTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store_data);

        initView();


    }

    private void initView(){
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

                mOpenTime = mOpenHouseSpn.getSelectedItem().toString() + mOpenMinuteSpn.getSelectedItem().toString();
                mCloseTime = mCloseHouseSpn.getSelectedItem().toString() + mCloseMinuteSpn.getSelectedItem().toString();

                String index_sel = "INSERT INTO `user_extra`.`changelist` (`Name`, `NewName`, `gX`, `gY`, `gOpen`, `gClose`, `Description`, `Remarknote`) VALUES ('" +
                        mStoreName + "', '" + mNewStoreName + "', '" + mgX + "', '" + mgY + "', '" + mOpenTime + "', '" + mCloseTime + "', '" +
                        mNewStoreDescription.getText().toString() + "', '" + mNewStoreRemarkNote.getText().toString() + "');";



                MySQLConnector.executeQuery(index_sel);
                break;
            case R.id.EditCancelBtn :
                this.finish();
                break;
        }
    }
}
