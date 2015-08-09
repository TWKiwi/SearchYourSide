package com.power.kiwi.searchyourside;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kiwi on 15/8/9.
 */
public class BarChartViewModel extends LazyChargeActivity.BarChartView {

    View mView = View.inflate(getActivity(),R.layout.bar_chart_view,null);
    LazyChargeActivity mLazyChargeActivity = new LazyChargeActivity();
    TextView ScaleNumM, ScaleNumD;
    Button ScaleBtn;

    Long mBudget = Option.optionSpr.getLong("mBudget", 0);
    Long mRglCost = Option.optionSpr.getLong("mRglCost",0);
    int mScaleTS = Option.optionSpr.getInt("mScaleTS",0);

    public View getVBarChartView(double percent){
        String[] titles = new String[] { "預算額", "已花費" };
        List< double []> values = new ArrayList<>();
        values.add( new  double [] {100});
        values.add( new  double [] {percent});
        int [] colors = new  int [] { Color.parseColor("#46A3FF"), Color.parseColor("#2828FF")};
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);//長條圖顏色設置
        renderer.setOrientation(XYMultipleSeriesRenderer.Orientation.VERTICAL);
        /**設置圖形renderer,標題,橫軸,縱軸,橫軸最小值,橫軸最大值,縱軸最大值,縱軸最小值,設定軸寬,設定軸色,標籤顏色*/
        setChartSettings(renderer, "", "", "", 0.9, 1.1, 0, 100 , 96f , Color.GRAY, Color.LTGRAY);
        renderer.getSeriesRendererAt(0).setDisplayChartValues(false);//在第1條圖形上顯示數據
        renderer.getSeriesRendererAt(1).setDisplayChartValues(false);//在第2條圖形上顯示數據
        renderer.setXLabels(0);//設置x軸標籤數  0為不顯示文字 程式設定文字
        renderer.setYLabels(5);//設置y軸標籤數
        renderer.setXLabelsAlign(Paint.Align.CENTER);//設置x軸標籤置中
        renderer.setYLabelsAlign(Paint.Align.RIGHT);//設置y軸標籤置中
        renderer.setYLabelsColor(0, Color.WHITE);//設置y軸標籤顏色
        renderer.setPanEnabled(false, false);//圖表移動  If you want to lock both axis, then use renderer.setPanEnabled(false, false);
        renderer.setZoomEnabled(false, false);//圖表縮放(x軸,y軸)
        renderer.setZoomRate(1.1f);//放大倍率
        renderer.setBarSpacing(0.5f);//長條圖的間隔
        renderer.setChartValuesTextSize(32);//設置長條圖上面字大小
        renderer.setMarginsColor(Color.argb(0, 0xff, 0, 0));//這句很重要，不能用transparent代替。
        renderer.setBackgroundColor(Color.TRANSPARENT);//設置透明色
        renderer.setApplyBackgroundColor(true);//使背景色生效
        renderer.setMargins(new int[]{25, 0, 25, 0});//右上左下
        renderer.setShowGrid(true);
        renderer.setGridColor(Color.GRAY);
        mView = ChartFactory.getBarChartView(getActivity(), buildBarDataset(titles, values), renderer, BarChart.Type.STACKED); // Type.STACKED
        return mView;
    }

    public void getMBarChart(){
        ScaleNumM = (TextView) mView.findViewById(R.id.showScaleNumM);
        ScaleNumD = (TextView) mView.findViewById(R.id.showScaleNumD);
        ScaleBtn = (Button) mView.findViewById(R.id.MD_ScaleBtn);

        FrameLayout scale_MView = (FrameLayout) mView.findViewById(R.id.scaleMView);
        scale_MView.removeAllViews();
        long percentMonth = scaleComputeOfMonth();


//        if(ScaleBtn.getText().equals("切換開銷類別瀏覽"))
        scale_MView.addView(getVBarChartView(percentMonth));
//        else if(ScaleBtn.getText().equals("切換月/日總開銷瀏覽")) {
//
//            Cursor cursor = mLazyChargeActivity.getCursor();
//            String select_month = new SimpleDateFormat("yyyyMMdd").format(new Date());
//            int sum = 0;
//            while (cursor.moveToNext()){
//                if(cursor.getString(1).substring(0,6).equals(select_month.substring(0,6))){
//
//                    sum += Integer.parseInt(cursor.getString(4));
//
//                }
//            }
//
//            Long percent = ((sum + mRglCost))*100 / mBudget;//算百分比條小數點弄成百分比整數
//            if(percent <= 100) {
//                if(mScaleTS == 0) {
//                    ScaleNumM.setText(Html.fromHtml("累計花費(月)<br>" + ((sum + mRglCost) + "<font color = '#FFFFFF'><big>/</font>" + mBudget + "元")));
//                }else if(mScaleTS == 1){
//                    ScaleNumM.setText(Html.fromHtml("剩餘預算(月)<br>" + ((mBudget - (sum + mRglCost)) + "<font color = '#FFFFFF'><big>/</font>" + mBudget + "元")));
//                }else{
//                    ScaleNumM.setText(mScaleTS);
//                }
//            }else if(percent > 100){
//                ScaleNumM.setText(Html.fromHtml("本月" + "<font color = '#FFFFFF'><big>超支<br></font>" + ((sum + mRglCost) - mBudget)+ "元"));
//                percent = 100L;
//            }
//            scale_MView.addView(getHBarChartView(sum,"Month"));
//
//        }

    }

    private long scaleComputeOfMonth(){
        Cursor cursor = mLazyChargeActivity.getCursor();
        String select_month = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int sum = 0;

        while (cursor.moveToNext()){
            if(cursor.getString(1).substring(0,6).equals(select_month.substring(0,6))){
                sum += Double.parseDouble(cursor.getString(4));
            }
        }
        //        SharedPreferences option = getPreferences(MODE_PRIVATE);
        //        mBudget = option.getInt("mBudget",20000);

        Long percent = ((sum + mRglCost) * 100)/ mBudget;//算百分比條小數點弄成百分比整數
        if(percent <= 100) {
            if(mScaleTS == 0) {
                ScaleNumM.setText(Html.fromHtml("累計花費(月)" + percent + "%<br>" + ((sum + mRglCost) + "<font color = '#FFFFFF'><big>/</font>" + mBudget + "元")));
            }else if(mScaleTS == 1){
                ScaleNumM.setText(Html.fromHtml("剩餘預算(月)" + percent + "%<br>" + ((mBudget - (sum + mRglCost)) + "<font color = '#FFFFFF'><big>/</font>" + mBudget + "元")));
            }else{
                ScaleNumM.setText(mScaleTS);
            }
        }else if(percent > 100){
            ScaleNumM.setText(Html.fromHtml("本月" + "<font color = '#FFFFFF'><big>超支<br></font>" + ((sum + mRglCost) - mBudget)+ "元"));
            percent = 100L;
        }

        return percent;
    }

    public void getDBarChart(){

        FrameLayout scale_DView = (FrameLayout) mView.findViewById(R.id.scaleDView);
        scale_DView.removeAllViews();
        long persentDay = scaleComputeOfDay();

//        if(ScaleBtn.getText().equals("切換開銷類別瀏覽"))
        scale_DView.addView(getVBarChartView(persentDay));
//        else if(ScaleBtn.getText().equals("切換月/日總開銷瀏覽")) {
//
//            Cursor cursor = mLazyChargeActivity.getCursor();
//            String select_month = new SimpleDateFormat("yyyyMMdd").format(new Date());
//            int sum = 0;
//            while (cursor.moveToNext()){
//                if(cursor.getString(1).substring(0,8).equals(select_month.substring(0,8))){
//                    sum += Integer.parseInt(cursor.getString(4));
//                }
//            }
//
//            Long percent = ((sum + mRglCost / 30)*100) / (mBudget / 30);//算百分比條小數點弄成百分比整數
//            if(percent <= 100) {
//                if(mScaleTS == 0) {
//                    ScaleNumD.setText(Html.fromHtml("累計花費(日)<br>" + (sum + mRglCost / 30) + "<font color = '#FFFFFF'><big>/</font>" + (mBudget / 30) + "元"));
//                }else if(mScaleTS == 1){
//                    ScaleNumD.setText(Html.fromHtml("剩餘預算(日)<br>" + ((mBudget / 30) - (sum + mRglCost / 30) + "<font color = '#FFFFFF'><big>/</font>" + (mBudget / 30) + "元")));
//                }else{
//                    ScaleNumM.setText(mScaleTS);
//                }
//            }else if(percent > 100){
//                ScaleNumD.setText(Html.fromHtml("本日" + "<font color = '#FFFFFF'><big>超支<br></font>" + ((sum + mRglCost / 30 - mBudget / 30)) + "元"));
//                percent = 100L;
//            }
//            scale_DView.addView(getHBarChartView(sum,"Day"));
//        }

    }

    private long scaleComputeOfDay(){
        Cursor cursor = mLazyChargeActivity.getCursor();
        String select_month = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int sum = 0;

        while (cursor.moveToNext()){
            if(cursor.getString(1).substring(0,8).equals(select_month.substring(0,8))){
                sum += Double.parseDouble(cursor.getString(4));
            }
        }
        Long percent = ((sum + mRglCost / 30) * 100) / (mBudget / 30);//算百分比條小數點弄成百分比整數
        if(percent <= 100) {
            if(mScaleTS == 0) {
                ScaleNumD.setText(Html.fromHtml("累計花費(日)" + percent + "%<br>" + (sum + mRglCost / 30) + "<font color = '#FFFFFF'><big>/</font>" + (mBudget / 30) + "元"));
            }else if(mScaleTS == 1){
                ScaleNumD.setText(Html.fromHtml("剩餘預算(日)" + percent + "%<br>" + ((mBudget / 30) - (sum + mRglCost / 30) + "<font color = '#FFFFFF'><big>/</font>" + (mBudget / 30) + "元")));
            }else{
                ScaleNumM.setText(mScaleTS);
            }
        }else if(percent > 100){
            ScaleNumD.setText(Html.fromHtml("本日" + "<font color = '#FFFFFF'><big>超支<br></font>" + ((sum + mRglCost / 30 - mBudget / 30)) + "元"));
            percent = 100L;
        }

        return percent;
    }

    private XYMultipleSeriesDataset buildBarDataset(String[] titles, List< double []> values) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for ( int i = 0; i < length; i++ ) {
            CategorySeries series = new CategorySeries(titles[i]);
            double [] v = values.get(i);
            int seriesLength = v.length;
            for ( int k = 0; k < seriesLength; k++ ) {
                series.add(v[k]);//加入每筆values資料
            }
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
    }

    private XYMultipleSeriesRenderer buildBarRenderer( int [] colors) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize( 50 );
        renderer.setChartTitleTextSize( 20 );
        renderer.setLabelsTextSize( 25 );
        renderer.setLegendTextSize( 32 );


        int length = colors.length;
        for ( int i = 0; i < length; i++ ) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[i]);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }
    /**設置圖形renderer,標題,橫軸,縱軸,最小伸縮刻度,最大伸縮刻度,縱軸最大值,縱軸最小值,設定軸寬,設定軸色,標籤顏色*/
    private void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle, String yTitle, double xMin, double xMax, double yMin, double yMax,float width, int axesColor, int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setBarWidth(width);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }
}
