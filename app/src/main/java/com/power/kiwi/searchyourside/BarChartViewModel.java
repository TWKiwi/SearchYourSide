package com.power.kiwi.searchyourside;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kiwi on 15/7/22.
 */
public class BarChartViewModel extends Fragment {

    LazyChargeActivity mlazyChargeActivity;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        mlazyChargeActivity = (LazyChargeActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.bar_chart_view,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

}
