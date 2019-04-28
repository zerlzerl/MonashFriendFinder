package edu.monashsuzhou.friendfinder;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import edu.monashsuzhou.friendfinder.R;

import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FlowingDrawer mDrawer;

    private TextView tv_date;
    private TextView tv_time;
    private TextView tv_temp;
    private TextView tv_city;
    private TextView tv_discrip;

    private Handler mHandler;
    //定位都要通过LocationManager这个类实现
    private LocationManager locationManager;
    private String provider;

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

        String location = "Suzhou";
        //通过location去查天气情况
        //显示地点和温度
        //第一步：检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
//        WeatherSearchQuery query = new WeatherSearchQuery("北京", WeatherSearchQuery.WEATHER_TYPE_LIVE);
//        WeatherSearch weathersearch=new WeatherSearch(this);
//        weathersearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
//            @Override
//            public void onWeatherLiveSearched(LocalWeatherLiveResult result, int i) {
//                LocalWeatherLive liveResult = result.getLiveResult();
////            liveTimeTxt.setText(liveResult.getReportTime()); //时间
//                tv_discrip.setText(liveResult.getWeather()); //气象
//                tv_temp.setText(liveResult.getTemperature() + "°C");     //温度
//            }
//
//            @Override
//            public void onWeatherForecastSearched(LocalWeatherForecastResult forecastResult, int i) {
//
//            }
//        });
//        weathersearch.setQuery(query);
//        weathersearch.searchWeatherAsyn(); //异步搜索

        /**
         * 实时天气查询回调
         */
//        public void onWeatherLiveSearched(LocalWeatherLiveResult result, int i) {
//            LocalWeatherLive liveResult = result.getLiveResult();
////            liveTimeTxt.setText(liveResult.getReportTime()); //时间
//            tv_discrip.setText(liveResult.getWeather()); //气象
//            tv_temp.setText(liveResult.getTemperature() + "°C");     //温度
////            liveWindDirectionTxt.setText(liveResult.getWindDirection()); //风向
////            liveWindPowerTxt.setText(liveResult.getWindPower() + "级"); //风力
////            liveHumidityTxt.setText(liveResult.getHumidity() + "%"); //湿度
//        }





    }

    private void initTextViews() {
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_discrip = (TextView) findViewById(R.id.tv_discrip);

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





}