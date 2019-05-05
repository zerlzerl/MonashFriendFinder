package edu.monashsuzhou.friendfinder.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;
import java.util.List;

import edu.monashsuzhou.friendfinder.R;
import edu.monashsuzhou.friendfinder.entity.StudentProfile;
import edu.monashsuzhou.friendfinder.litepalbean.DatabaseHelper;
import edu.monashsuzhou.friendfinder.litepalbean.MiniStudent;

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

        int stu_id = Login.getCurrentId();
        dh = new DatabaseHelper();
        myStu = dh.getMiniStudent(String.valueOf(stu_id));

        if (map_type == "friend"){

        }
        if( map_type == "match"){
            ll = (LinearLayout) findViewById(R.id.ll1_map);
            ll.setVisibility(View.INVISIBLE);
        }
        initBtnListener();
        mapFragment.getMapAsync(this);

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
                double distance = 0;
                if(!spin.equals("all")){
                    distance = Double.parseDouble(spin);
                }

                List<MiniStudent> ms_list =  new ArrayList<>();
                ms_list = dh.getFriend();
                for(int i = 0 ; i < ms_list.size(); i++){
                    MiniStudent ms = ms_list.get(i);
                    int stu_id = ms.getStudentid();
                    String name = ms.getFirstname();
                    double longtitude = ms.getLongtude();
                    double latitude = ms.getLatitude();
                    System.out.println(ms.getDistance());
                    if(ms.getDistance() > distance && !spin.equals("all")){
                        continue;
                    }
                    LatLng friend_loc = new LatLng(latitude,longtitude);
                    Marker friend_loc_marker =  mMap.addMarker(new MarkerOptions().position(friend_loc).title("Student : " + name));
                    friend_loc_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED ));
                    friend_loc_marker.setSnippet("name : " + name);
                    friend_loc_marker.setTag(stu_id);
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
        MiniStudent ms = myStu;
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
        if (map_type == "friend"){
            ms_list = dh.getFriend();
        }

        if( map_type == "match"){
            ms_list = dh.getMatchingStudent();
        }

        for(int i = 0 ; i < ms_list.size(); i++){
            ms = ms_list.get(i);
            stu_id = ms.getStudentid();
            String name = ms.getFirstname();
            longtitude = ms.getLongtude();
            latitude = ms.getLatitude();
            LatLng friend_loc = new LatLng(latitude,longtitude);
            Marker friend_loc_marker =  mMap.addMarker(new MarkerOptions().position(friend_loc).title("Student : " + name));
            friend_loc_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED ));
            friend_loc_marker.setSnippet("name : " + name);
            friend_loc_marker.setTag(stu_id);
        }

        mMap.setOnMarkerClickListener(this);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer student_id = (Integer) marker.getTag();
        System.out.println(student_id);
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private class HttpConnector extends AsyncTask<String, Void, List<MiniStudent>> {
        @Override
        protected List<MiniStudent> doInBackground(String... params) {
            String stu_id = params[0];
            String map_type = params[1];
            List<MiniStudent> ms_list =  new ArrayList<>();
            if( map_type == "match"){
                ms_list = dh.getMatchingStudent();
            }
            if (map_type == "friend"){
                ms_list = dh.getFriend();
            }
            return ms_list;
        }

        @Override
        protected void onPostExecute(List<MiniStudent> ms_list) {

            for(int i = 0 ; i < ms_list.size(); i++){
                MiniStudent ms = ms_list.get(i);
                int stu_id = ms.getStudentid();
                String name = ms.getFirstname();
                double longtitude = ms.getLongtude();
                double latitude = ms.getLatitude();
                LatLng friend_loc = new LatLng(latitude,longtitude);
                Marker friend_loc_marker =  mMap.addMarker(new MarkerOptions().position(friend_loc).title("Student" + name));
                friend_loc_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED ));
                friend_loc_marker.setSnippet("name : " + name);
                friend_loc_marker.setTag(stu_id);
            }
        }
    }

}
