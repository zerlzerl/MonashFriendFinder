package edu.monashsuzhou.friendfinder.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import edu.monashsuzhou.friendfinder.R;
import edu.monashsuzhou.friendfinder.entity.StudentProfile;
import edu.monashsuzhou.friendfinder.litepalbean.DatabaseHelper;
import edu.monashsuzhou.friendfinder.litepalbean.MiniStudent;
import edu.monashsuzhou.friendfinder.util.HttpUtil;

public class GoogleMaps extends FragmentActivity implements OnMapReadyCallback,  GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private  DatabaseHelper dh;
    private Spinner spinner;
    private TextView tv;
    private LinearLayout ll;
    private Button btn;
    private String map_type;
    private int stu_id;
    private MiniStudent myStu;
    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        spinner = (Spinner) findViewById(R.id.spinner_map);
        //spinner.setVisibility(View.INVISIBLE);
        tv = (TextView) findViewById(R.id.tv_map);
        btn = (Button) findViewById(R.id.btn_map);

        Intent intent = getIntent();
        map_type = intent.getStringExtra("map_type");
        map_type = "friend";

        stu_id = Login.getCurrentId();
        dh = new DatabaseHelper();
        myStu = dh.getMiniStudent(String.valueOf(stu_id));
        if (map_type == "friend"){

        }
        if( map_type == "match"){
            ll = (LinearLayout) findViewById(R.id.ll1_map);
            ll.setVisibility(View.INVISIBLE);
        }

        initDialogBuild();
        initBtnListener();
        mapFragment.getMapAsync(this);

    }
    private double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    public void initDialogBuild(){
        builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

            }
        });
    }
    public void initBtnListener(){
        this.btn.setOnClickListener(new Button.OnClickListener(){
            public void  onClick(View v){
                mMap.clear();
                LatLng my_loc = new LatLng(myStu.getLatitude(),myStu.getLongtude());
                Marker my_loc_marker = mMap.addMarker(new MarkerOptions().position(my_loc).title("My location"));
                my_loc_marker.setSnippet("name :" + myStu.getFirstname());
                my_loc_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                my_loc_marker.setTag(stu_id);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(my_loc));

                String spin = spinner.getSelectedItem().toString();
                boolean all;
                if(spin.equals("ALL")){
                    all = true;
                } else {
                    all = false;
                }
                double distance = 0;
                Log.i("map_circle","draw");
                if(!all){
                    distance = Double.parseDouble(spin);
                    Log.i("map_circle",String.valueOf(distance));

                    mMap.addCircle(new CircleOptions()
                            .center(my_loc)
                            .radius(distance * 1000.0)
                            .strokeColor(Color.argb(1000,255,193,193))
                            .fillColor(Color.argb(400,255,193,193))
                            );
                }
                List<MiniStudent> ms_list =  new ArrayList<>();
                ms_list = dh.getFriend();
                for(int i = 0 ; i < ms_list.size(); i++){
                    MiniStudent ms = ms_list.get(i);
                    int ms_stu_id = ms.getStudentid();
                    if(ms_stu_id == stu_id){
                        continue;
                    }
                    String name = ms.getFirstname();
                    double longtitude = ms.getLongtude();
                    double latitude = ms.getLatitude();
                    LatLng friend_loc = new LatLng(latitude,longtitude);
                    double dis_between = CalculationByDistance(my_loc, friend_loc);
                    if(dis_between > distance && !all){
                        continue;
                    }
                    Marker friend_loc_marker =  mMap.addMarker(new MarkerOptions().position(friend_loc).title("Student : " + name));
                    friend_loc_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED ));
                    friend_loc_marker.setSnippet("name : " + name);
                    Log.i("stu_id",String.valueOf(stu_id));
                    Log.i("friend_id",String.valueOf(ms_stu_id));
                    friend_loc_marker.setTag(ms_stu_id);
                }
            }
        });
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        MiniStudent ms = dh.getMiniStudent(String.valueOf(stu_id));
        // get my info
        double longtitude = ms.getLongtude();
        double latitude = ms.getLatitude();
        LatLng my_loc = new LatLng(latitude,longtitude);
        Marker my_loc_marker = mMap.addMarker(new MarkerOptions().position(my_loc).title("My location"));
        my_loc_marker.setSnippet("name :" + ms.getFirstname());
        my_loc_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        my_loc_marker.setTag(stu_id);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(my_loc));

        List<MiniStudent> ms_list =  new ArrayList<>();
        if (map_type.equals("friend")){
            ms_list = dh.getFriend();
        }

        if( map_type.equals("match")){
            ms_list = dh.getMatchingStudent();
        }

        for(int i = 0 ; i < ms_list.size(); i++){
            ms = ms_list.get(i);
            int ms_stu_id = ms.getStudentid();
            if(ms_stu_id == stu_id){
                continue;
            }
            String name = ms.getFirstname();
            longtitude = ms.getLongtude();
            latitude = ms.getLatitude();
            LatLng friend_loc = new LatLng(latitude,longtitude);
            Marker friend_loc_marker =  mMap.addMarker(new MarkerOptions().position(friend_loc).title("Student : " + name));
            friend_loc_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED ));
            friend_loc_marker.setSnippet("name : " + name);
            friend_loc_marker.setTag(ms_stu_id);
        }

        mMap.setOnMarkerClickListener(this);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer student_id = (Integer) marker.getTag();
        HttpConnector hc = new HttpConnector();
        hc.execute(new String[] {String.valueOf(student_id)});
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }


    private class HttpConnector extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String stu_id = params[0];
            String uri = stu_id;
            String info = "";

            try {
                info = HttpUtil.get("Profile", uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return  info;
        }

        @Override
        protected void onPostExecute(String info) {
            if (info == null){
                Log.d("map_asyT","info null");
                return;
            }
            try {
                JSONObject obj = JSONObject.parseObject(info);
                java.util.Map<String,Object> map = (java.util.Map<String,Object>)obj;
                builder.setTitle("Student info");
                String detail = "";
                for(String key : map.keySet()){
                    detail = detail + key + " : " + map.get(key).toString() + "\n";
                }
                builder.setMessage(detail);
                Dialog dialog = builder.create();
                dialog.show();
            } catch (com.alibaba.fastjson.JSONException e){
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }



}
