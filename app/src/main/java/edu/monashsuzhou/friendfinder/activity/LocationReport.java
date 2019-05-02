package edu.monashsuzhou.friendfinder.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.monashsuzhou.friendfinder.MainActivity;
import edu.monashsuzhou.friendfinder.R;
import edu.monashsuzhou.friendfinder.util.HttpUtil;

public class LocationReport extends AppCompatActivity {

    private BarChart barChart;
    private Button btn;
    private EditText et_start_date;
    private EditText et_end_date;
    private String[] stored_lab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_report);
        Toolbar toolbar = findViewById(R.id.report_toolbar);
        setSupportActionBar(toolbar);
        barChart = (BarChart) findViewById(R.id.barChart1);
        btn = (Button) findViewById(R.id.btn_input_date);
        et_start_date = (EditText) findViewById(R.id.et_start_date);
        et_end_date = (EditText) findViewById(R.id.et_end_date);
        initAddUnitListeners();

       // barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(stored_lab));
        //设置显示动画效果

    }

    protected void initAddUnitListeners(){
        this.btn.setOnClickListener(new Button.OnClickListener(){
            public void  onClick(View v){
                String start_date = et_start_date.getText().toString();
                String end_date = et_end_date.getText().toString();
                if (start_date == "" || end_date == ""){
                    sendToast("ENTER TEXT FIRST");
                    return;
                }
                if( !isValidDate(start_date) || !isValidDate(end_date)){
                    sendToast("INCORRECT DATE FORMAT");
                    return;
                }
                HttpConnector hc = new HttpConnector();
                hc.execute(new String[]{start_date,end_date,"44"});

            }
        });
    }
    protected void sendToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
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

    private class HttpConnector extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String startingDate = params[0];
            String endingDate = params[1];
            int student_id = Integer.parseInt(params[2]);
            String uri = "visitingFrequency";
            uri = uri + "/" + startingDate + "/" + endingDate + "/" + String.valueOf(student_id);
            System.out.println(uri);
            String info = "";
            try {
                info = HttpUtil.get("Profile", uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("visitiFrequency", info);

            return info;
        }

        @Override
        protected void onPostExecute(String info) {
            JSONArray json_data = JSON.parseArray(info);

            ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
            ArrayList<String> labels = new ArrayList<>();
            for(int i = 0 ; i < json_data.size(); i++ ){
                JSONObject obj = json_data.getJSONObject(i);
                Float frequency = obj.getFloat("frequency");
                String loc_name = obj.getString("loc_name");
                entries.add(new BarEntry(i, frequency));
                labels.add(loc_name);
            }

            BarDataSet dataset = new BarDataSet(entries, "visiting frequency");
            dataset.setColors(ColorTemplate.COLORFUL_COLORS);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataset);
            BarData barData = new BarData(dataset);
            barChart.setData(barData);
            barChart.animateY(2000);
        }
    }

    public static boolean isValidDate(String str) {
        boolean convertSuccess=true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            convertSuccess=false;
        }
        return convertSuccess;
    }

}
