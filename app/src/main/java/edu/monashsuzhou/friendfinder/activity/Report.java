package edu.monashsuzhou.friendfinder.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.util.DisplayMetrics;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import edu.monashsuzhou.friendfinder.R;

import edu.monashsuzhou.friendfinder.MainActivity;
import edu.monashsuzhou.friendfinder.util.HttpUtil;

public class Report extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private PieChart mChart;
    private PieData pieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        Toolbar toolbar = findViewById(R.id.report_toolbar);
        setSupportActionBar(toolbar);


        // initialize the utilities
        Utils.init(this);

        ArrayList<ContentItem> objects = new ArrayList<>();

        ////

        objects.add(0, new ContentItem("Common attributes Report", "a pie chart"));

        objects.add(1, new ContentItem("Location Report", "a line chart."));

        MyAdapter adapter = new MyAdapter(this, objects);

        ListView lv = findViewById(R.id.listView1);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long arg3) {

        Intent i = null;

        switch (pos) {
            case 0:
                i = new Intent(this, CommonAttributesReport.class);
                break;
            case 1:
                i = new Intent(this, LocationReport.class);
                break;
        }
        if (i != null) startActivity(i);
    }


    private class ContentItem {

        final String name;
        final String desc;
        boolean isSection = false;

        ContentItem(String n) {
            name = n;
            desc = "";
            isSection = true;
        }

        ContentItem(String n, String d) {
            name = n;
            desc = d;
        }
    }
    private class MyAdapter extends ArrayAdapter<ContentItem> {


        MyAdapter(Context context, List<ContentItem> objects) {
            super(context, 0, objects);

         }

        @SuppressLint("InflateParams")
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            ContentItem c = getItem(position);

            ViewHolder holder;

            holder = new ViewHolder();

            if (c != null && c.isSection) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_section, null);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
            }

            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvDesc = convertView.findViewById(R.id.tvDesc);

            convertView.setTag(holder);


            holder.tvName.setText(c != null ? c.name : null);
            holder.tvDesc.setText(c != null ? c.desc : null);

            return convertView;
        }

        private class ViewHolder {

            TextView tvName, tvDesc;
        }
    }

}
