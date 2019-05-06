package edu.monashsuzhou.friendfinder;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.monashsuzhou.friendfinder.R;
import edu.monashsuzhou.friendfinder.activity.Login;
import edu.monashsuzhou.friendfinder.util.HttpUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FlowingDrawer mDrawer;
    private String  temp;
    private String city = "Suzhou"; //默认的城市
    private String uName = "User"; //默认的用户名
    private String descrip;
    private String icon;
    private Integer weatherR; 

    private TextView tv_date;
    private TextView tv_time;
    private TextView tv_temp;
    private TextView tv_city;
    private TextView tv_uName;
    private TextView tv_descrip;
    private ImageView iv_weather;

    private Handler mHandler;


    @SuppressWarnings("static-access")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        setupToolbar();
        setupMenu();

        initTextViews();
/*
        //显示地点和温度
        SharedPreferences setting = getSharedPreferences("quickresume", 0);
        Boolean user_first = setting.getBoolean("FIRST",true);
        if(user_first){ //第一次onCreate
            WeatherDisplayTask weatherDisplayTask = new WeatherDisplayTask();
            weatherDisplayTask.execute("" + Login.getCurrentId());
        }else{ //不是第一次onCreate，可以直接显示
            setting.edit().putBoolean("FIRST", false).commit();
            tv_city.setText(city);
            tv_uName.setText("Welcome, " + uName + "!");
            weatherR = getImageIcon(icon);
            tv_temp.setText(temp);
            tv_descrip.setText(descrip);
            iv_weather.setImageResource(weatherR);
        }

*/
        //显示日期和时间
        mHandler = new Handler();
        if (true) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// HH:mm:ss
                    Date now = new Date(System.currentTimeMillis());
                    String now_format = "" + simpleDateFormat.format(now);
                    String[] dateTime = now_format.split(" ");
                    String date = dateTime[0];
                    String time = dateTime[1];
                    tv_date.setText(date);
                    tv_time.setText(time);
                    mHandler.postDelayed(this, 1000); //一秒钟刷新一次
                }
            });
        }


    }


    @Override
    protected void onRestart() {
        super.onRestart();
        tv_city.setText(city);
        tv_uName.setText("Welcome, " + uName + "!");
        weatherR = getImageIcon(icon);
        tv_temp.setText(temp);
        tv_descrip.setText(descrip);
        iv_weather.setImageResource(weatherR);
    }

    private void initTextViews() {
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_descrip = (TextView) findViewById(R.id.tv_descrip);
        tv_uName = (TextView) findViewById(R.id.tv_uName);
        iv_weather = (ImageView) findViewById(R.id.iv_weather);

    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.toggleMenu();
            }
        });
    }


    private void setupMenu() {
        FragmentManager fm = getSupportFragmentManager();
        MenuListFragment mMenuFragment = (MenuListFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            mMenuFragment = new MenuListFragment();
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment).commit();
        }

//        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
//            @Override
//            public void onDrawerStateChange(int oldState, int newState) {
//                if (newState == ElasticDrawer.STATE_CLOSED) {
//                    Log.i("MainActivity", "Drawer STATE_CLOSED");
//                }
//            }
//
//            @Override
//            public void onDrawerSlide(float openRatio, int offsetPixels) {
//                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isMenuVisible()) {
            mDrawer.closeMenu();
        } else {
            super.onBackPressed();
        }
    }


    public static int getImageIcon(String weatherDescription){
        int weatherIcon ;
        switch(weatherDescription) {
            case "Thunderstorm":
                weatherIcon = R.mipmap.ic_atmosphere;
                break;
            case "Drizzle":
                weatherIcon = R.mipmap.ic_drizzle;
                break;
            case "Rain":
                weatherIcon = R.mipmap.ic_rain;
                break;
            case "Snow":
                weatherIcon = R.mipmap.ic_snow;
                break;
            case "Atmosphere":
                weatherIcon = R.mipmap.ic_atmosphere;
                break;
            case "Clear":
                weatherIcon = R.mipmap.ic_clear;
                break;
            case "Clouds":
                weatherIcon = R.mipmap.ic_cloudy;
                break;
            case "Extreme":
                weatherIcon = R.mipmap.ic_extreme;
                break;
            default:
                weatherIcon = R.mipmap.ic_launcher;
        }
        return weatherIcon;

    }

    private class WeatherDisplayTask extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings) {
            String currentId = strings[0];
            String studentInfo = null;
            try {
                studentInfo = HttpUtil.get("Profile", currentId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject studentObj = JSON.parseObject(studentInfo);
            city = studentObj.getString("suburb");
            uName = studentObj.getString("firstName");
            tv_city.setText(city);
            tv_uName.setText("Welcome, " + uName + "!");
            try {
                String weatherInfo = HttpUtil.get(Constant.WEATHER_HOST + "?q=" + city + "&APPID=" + Constant.APPID
                        + "&units=" + Constant.UNITS + "&cnt=" + Constant.N_DAYS);
                return weatherInfo;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String weatherInfo) {
            super.onPostExecute(weatherInfo);
//            Log.i("Weather", weatherInfo);
            JSONObject weatherJson = JSON.parseObject(weatherInfo);
            
            temp = "" + weatherJson.getJSONArray("list").getJSONObject(0)
                    .getJSONObject("temp").getInteger("day") + "°";
            descrip = weatherJson.getJSONArray("list").getJSONObject(0)
                    .getJSONArray("weather").getJSONObject(0).getString("description");
            icon = weatherJson.getJSONArray("list").getJSONObject(0)
                    .getJSONArray("weather").getJSONObject(0).getString("main");
            weatherR = getImageIcon(icon);
            tv_temp.setText(temp);
            tv_descrip.setText(descrip);
            iv_weather.setImageResource(weatherR);


        }
    }


}