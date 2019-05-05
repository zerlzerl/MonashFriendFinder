package edu.monashsuzhou.friendfinder.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.monashsuzhou.friendfinder.MainActivity;
import edu.monashsuzhou.friendfinder.R;
import edu.monashsuzhou.friendfinder.util.HttpUtil;

public class CommonAttributesReport extends AppCompatActivity {

    private PieChart mChart;
    private PieData pieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_attributes_report);
        Toolbar toolbar = findViewById(R.id.report_toolbar);
        setSupportActionBar(toolbar);

        mChart = (PieChart) findViewById(R.id.pie_chart);
        getUnitFrequency();
        showChart(pieData);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showChart(PieData pieData) {
        mChart.setHoleColor(Color.TRANSPARENT);
        mChart.setHoleRadius(50f);  //半径
        mChart.setTransparentCircleRadius(54f); // 半透明圈
        //mChart.setHoleRadius(0)  //实心圆
        //mChart.setDescription("测试饼状图");
        // mChart.setDrawYValues(true);
        mChart.setDrawCenterText(true);  //饼状图中间可以添加文字
        mChart.setDrawHoleEnabled(true);
        mChart.setRotationAngle(90); // 初始旋转角度
        mChart.setCenterTextSize(15);
        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true); // 可以手动旋转
        // display percentage values
        mChart.setUsePercentValues(true);  //显示成百分比

        mChart.setCenterText("Unit Frequency");  //饼状图中间的文字
        //设置数据
        mChart.setData(pieData);
        mChart.setEntryLabelColor(Color.BLACK);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        mChart.animateXY(1000, 1000);  //设置动画
        Description desc = new Description();
        desc.setText("");
        mChart.setDescription(desc);
    }

    private PieData getPieData(JSONArray data) {

        int total_cnt = 0;
        java.util.Map<String, Float> reuslt_map =  new HashMap<String,Float>();
        for(int i = 0 ; i < data.size(); i++ ){
            JSONObject obj = data.getJSONObject(i);
            Float frequency = obj.getFloat("frequency");
            String unit_name = obj.getString("unit");
            if (!reuslt_map.containsKey(unit_name)){
                reuslt_map.put(unit_name,frequency);
            } else {
                reuslt_map.put(unit_name,reuslt_map.get(unit_name) + frequency);
            }
            total_cnt += frequency;
        }
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (String unit_name : reuslt_map.keySet()) {
            float f_num = reuslt_map.get(unit_name);

            entries.add(new PieEntry(f_num, unit_name));
        }

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(entries, "Favourite units pie graph"/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        pieDataSet.setColors(colors);

        pieDataSet.setValueTextColor(Color.BLACK);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度
        PieData pieData = new PieData(pieDataSet);
        return pieData;
    }

    public java.util.Map<String, Integer> getUnitFrequency() {
        java.util.Map<String, Integer> resultMap = new HashMap<String, Integer>();
        HttpConnector hc = new HttpConnector();
        hc.execute(new String[]{"1"});

        return resultMap;
    }

    private class HttpConnector extends AsyncTask<String, Void, PieData> {
        @Override
        protected PieData doInBackground(String... params) {
            int student_id = Integer.parseInt(params[0]);;
            String info = "";
            try {
                info = HttpUtil.get("Profile", "unitFrequency");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(info);
            Log.i("unitFrequency", info);

            JSONArray data = JSON.parseArray(info);
            PieData mPieData = getPieData(data);
            return mPieData;
        }

        @Override
        protected void onPostExecute(PieData result) {
            pieData = result;
            showChart(result);
        }
    }
}
