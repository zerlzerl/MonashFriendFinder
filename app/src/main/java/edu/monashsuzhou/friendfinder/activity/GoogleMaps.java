package edu.monashsuzhou.friendfinder.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.tasks.OnSuccessListener;


import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.monashsuzhou.friendfinder.Constant;
import edu.monashsuzhou.friendfinder.R;
import edu.monashsuzhou.friendfinder.entity.StudentLocation;
import edu.monashsuzhou.friendfinder.entity.StudentProfile;
import edu.monashsuzhou.friendfinder.litepalbean.DatabaseHelper;
import edu.monashsuzhou.friendfinder.litepalbean.MiniStudent;
import edu.monashsuzhou.friendfinder.util.FetchAddressIntentService;
import edu.monashsuzhou.friendfinder.util.HttpUtil;
import edu.monashsuzhou.friendfinder.util.SharedPreferencesUtils;

public class GoogleMaps extends FragmentActivity implements OnMapReadyCallback,  GoogleMap.OnMarkerClickListener {
    private static String TAG = GoogleMaps.class.getName();
    private static GoogleMap mMap;
    private  DatabaseHelper dh;
    private Spinner spinner;
    private TextView tv;
    private LinearLayout ll;
    private Button btn;
    private String map_type;
    private int stu_id;
    private MiniStudent myStu;
//    private AlertDialog.Builder builder;
    private LocationManager lm;
    private Location mLastKnownLocation;
    private AddressResultReceiver mResultReceiver;
    private Marker mMarker;
    private static Context context;
    protected static int fromActivity;//Myfriends - 1, Searching result - 2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        context = GoogleMaps.this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        spinner = (Spinner) findViewById(R.id.spinner_map);
        // set default selection
        spinner.setSelection(4);

        tv = (TextView) findViewById(R.id.tv_map);
        btn = (Button) findViewById(R.id.btn_map);

        Intent intent = getIntent();
        map_type = intent.getStringExtra("map_type");
        if(map_type == null){
            map_type = "friend";
        }

        SharedPreferences userSettings= getSharedPreferences("login", 0);
        stu_id = userSettings.getInt("loginId",-1);
        dh = new DatabaseHelper();
        myStu = dh.getMiniStudent(String.valueOf(stu_id));
        if (map_type.equals("friend")){

        } else if( map_type.equals("match")){
            ll = (LinearLayout) findViewById(R.id.ll1_map);
            ll.setVisibility(View.INVISIBLE);
        } else{
            map_type = "friend";
        }

//        initDialogBuild();
        initBtnListener();
        //mLastKnownLocation = getLocation();
        //startIntentService();
        mapFragment.getMapAsync(this);

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
       //get current location

//        if(mLastKnownLocation == null){
//            Log.d(TAG,"Location is null");
//            return;
//        }
//        // get my info
//        double latitude = mLastKnownLocation.getLatitude();
//        double longtitude = mLastKnownLocation.getLongitude();
//        myStu = dh.getMiniStudent(String.valueOf(stu_id));

//        ms.setLatitude(latitude);
//        ms.setLongtude(longtitude);
        //dh.insertMiniStudent(ms);
//        MiniStudent ms = myStu;
//        double latitude = ms.getLatitude();
//        double longtitude = ms.getLongtude();
//        LatLng my_loc = new LatLng(ms.getLatitude(), ms.getLongtude());
//        Marker my_loc_marker = mMap.addMarker(new MarkerOptions().position(my_loc).title("My location"));
//        my_loc_marker.setSnippet("name :" + ms.getFirstname());
//        my_loc_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        my_loc_marker.setTag(stu_id);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(my_loc));

//        List<MiniStudent> ms_list =  new ArrayList<>();
//        if (map_type.equals("friend")){
//            ms_list = dh.getFriend();
//        }
//        if( map_type.equals("match")){
//            ms_list = dh.getMatchingStudent();
//        }
//        for(int i = 0 ; i < ms_list.size(); i++){
//            ms = ms_list.get(i);
//            int ms_stu_id = ms.getStudentid();
//            if(ms_stu_id == stu_id){
//                continue;
//            }
//            String name = ms.getFirstname();
//            latitude = ms.getLatitude();
//            longtitude = ms.getLongtude();
//            LatLng friend_loc = new LatLng(latitude, longtitude);
//            Marker friend_loc_marker =  mMap.addMarker(new MarkerOptions().position(friend_loc).title("Student : " + name));
//            friend_loc_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED ));
//            friend_loc_marker.setSnippet("name : " + name);
//            friend_loc_marker.setTag(ms_stu_id);
//        }

        // Mark Points
        SetMarkersTask setMarkersTask = new SetMarkersTask();
        setMarkersTask.execute();
//        mMap.setOnMarkerClickListener(this);
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


    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        mResultReceiver = new AddressResultReceiver(new Handler());
        intent.putExtra(Constant.RECEIVER, mResultReceiver);
        intent.putExtra(Constant.LOCATION_DATA_EXTRA, mLastKnownLocation);
        startService(intent);
    }

    private Location getLocation() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider;
        List<String> providerList = lm.getProviders(true);

        if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (providerList.contains((LocationManager.GPS_PROVIDER))) {
            provider = LocationManager.GPS_PROVIDER;
        } else {
            Log.d(TAG,"no gps");
            Toast.makeText(this, "Check GPS", Toast.LENGTH_LONG).show();
            return null;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i(TAG, "no Permission ");
            return null;
        }
        Location location = lm.getLastKnownLocation(provider);
        if (location != null) {
            Log.i(TAG, "latitude : " + location.getLatitude() +
                    " longitude : " + location.getLongitude());
        } else {
            Log.i(TAG, "location is null");
        }
        return location;
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
//
//    public void initDialogBuild(){
//        builder = new AlertDialog.Builder(this);
//        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialog, int which){
//
//            }
//        });
//    }

    public void initBtnListener(){
        this.btn.setOnClickListener(new Button.OnClickListener(){
            public void  onClick(View v){
                mMap.clear();
                LatLng my_loc = new LatLng(myStu.getLatitude(), myStu.getLongtude());
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
                if(!all){
                    distance = Double.parseDouble(spin);
                    mMap.addCircle(new CircleOptions()
                            .center(my_loc)
                            .radius(distance * 1000.0)
                            .strokeColor(Color.argb(1000,255,193,193))
                            .fillColor(Color.argb(400,255,193,193))
                    );
                }
                List<MiniStudent> ms_list =  new ArrayList<>();
                ms_list = dh.getFriend();
                Log.i(TAG,"ms_list " + ms_list.size());
                for(int i = 0 ; i < ms_list.size(); i++){
                    MiniStudent ms = ms_list.get(i);
                    int ms_stu_id = ms.getStudentid();
                    if(ms_stu_id == stu_id){
                        continue;
                    }
                    String name = ms.getFirstname();
                    double longtitude = ms.getLongtude();
                    double latitude = ms.getLatitude();
                    Log.i(TAG + " friend location","long : " + longtitude + "lat : " + latitude);
                    LatLng friend_loc = new LatLng(latitude, longtitude);
                    double dis_between = CalculationByDistance(my_loc, friend_loc);
                    if(dis_between > distance && !all){
                        continue;
                    }
                    Marker friend_loc_marker =  mMap.addMarker(new MarkerOptions().position(friend_loc).title("Student : " + name));
                    friend_loc_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED ));
                    friend_loc_marker.setSnippet("name : " + name);
                }
            }
        });
    }

    private static class HttpConnector extends AsyncTask<String, Void, String> {

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
                Log.d(TAG," asyTask info null");
                return;
            }
            try {
                JSONObject obj = JSONObject.parseObject(info);
                java.util.Map<String,Object> map = (java.util.Map<String,Object>)obj;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){

                    }
                });
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

    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler){
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData){
            if(resultData == null){
                Log.i(TAG,"result data is null");
                return;
            }
            String addressOutput = resultData.getString(Constant.RESULT_DATA_KEY);
            if(addressOutput == null){
                addressOutput = "";
            }
            JSONObject address_json = JSON.parseObject(addressOutput);
            Log.i(TAG,address_json.toJSONString());
            StudentLocation sl = new StudentLocation();
            String currentTime = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss").format(new Date()) + "+08:00";
            currentTime = currentTime.replace("/","T");
            BigDecimal longitude_bd = new BigDecimal(address_json.getFloat("longitude"));
            longitude_bd.setScale(6, BigDecimal.ROUND_HALF_DOWN);
            BigDecimal latitude_bd = new BigDecimal(address_json.getFloat("latitude"));
            longitude_bd.setScale(6, BigDecimal.ROUND_HALF_DOWN);
            sl.setLongitude(longitude_bd).
                    setLatitude(latitude_bd).
                    setLocName(address_json.getString("city")).
                    setLocDate(currentTime).setLocTime(currentTime);
            GoogleMapLocationAsy gma = new GoogleMapLocationAsy();
            gma.execute(new Object[]{sl});
        }
    }

    private class GoogleMapLocationAsy extends AsyncTask<Object, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(Object... objs) {
            Log.i("New Location Subscription:", JSON.toJSONString(objs[0]));
            String json_obj = JSON.toJSONString(objs[0]);

            String myInfo = null;
            boolean state = false;
            try {
                myInfo = HttpUtil.get("Profile","" + stu_id);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (myInfo != null){
                //没有出现网络错误
                JSONObject student_location_obj = JSON.parseObject(JSON.toJSONString(objs[0]));
                student_location_obj.put("studentId",JSON.parse(myInfo));

                Log.i(TAG,student_location_obj.toJSONString());
                try {
                    HttpUtil.post("Location", "", student_location_obj);
                    state = true;
                } catch (IOException e) {
                    state = false;
                    e.printStackTrace();
                }
            }

           return state;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {

        }
    }

    public static class SetMarkersTask extends AsyncTask<Object, Integer, Object>{
        JSONObject myLocationJson = null;
        @Override
        protected Object doInBackground(Object... objects) {

            String myLocationJsonStr = null;
            try {
                myLocationJsonStr = HttpUtil.get("Location","findByStuId/" + Login.getCurrentId());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (myLocationJsonStr != null) {
                JSONArray myLoctionArray = JSON.parseArray(myLocationJsonStr);
                if (myLoctionArray.size() > 0) {
                    myLocationJson = myLoctionArray.getJSONObject(0);
                }
            }

            JSONArray studentsJson = null;
            if (fromActivity == 1){
                studentsJson = MyFriends.currentShownStudnt;
            } else if (fromActivity == 2){
                studentsJson = Searching.currentShownStudnt;
            } else {
                studentsJson = null;
            }
            JSONArray latestLocations = new JSONArray();
            if (studentsJson != null){
                //有内容
                for (Object s : studentsJson){
                    JSONObject student = (JSONObject) s;
                    String name = student.getString("firstName");
                    Integer id = student.getInteger("studentId");

                    String locationInfo = null;
                    try {
                        locationInfo = HttpUtil.get("Location","findByStuId/" + id);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(SetMarkersTask.class.getName(), locationInfo);
                    if (locationInfo != null){
                        //无网络错误
                        JSONArray locationArray = JSON.parseArray(locationInfo);
                        if (locationArray.size() > 0){
                            JSONObject latestLocation = locationArray.getJSONObject(0);
                            latestLocations.add(latestLocation);
                        }
                    }

                }
            }
//            mMap.setOnMarkerClickListener(this);

            return latestLocations;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            // my location
            //            MiniStudent ms = myStu;
//        double latitude = ms.getLatitude();
//        double longtitude = ms.getLongtude();
//        LatLng my_loc = new LatLng(ms.getLatitude(), ms.getLongtude());
//        Marker my_loc_marker = mMap.addMarker(new MarkerOptions().position(my_loc).title("My location"));
//        my_loc_marker.setSnippet("name :" + ms.getFirstname());
//        my_loc_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        my_loc_marker.setTag(stu_id);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(my_loc));
            if (myLocationJson != null) {
                Double latitude = myLocationJson.getDouble("latitude");
                Double longitude = myLocationJson.getDouble("longitude");
                LatLng my_loc = new LatLng(latitude, longitude);
                Marker my_loc_marker = mMap.addMarker(new MarkerOptions().position(my_loc).title("My location"));
                my_loc_marker.setSnippet("name :" + myLocationJson.getJSONObject("studentId").getString("firstName"));
                my_loc_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                my_loc_marker.setTag(myLocationJson.getJSONObject("studentId").getInteger("studentId"));
            }


            JSONArray latestLocations = (JSONArray) o;
            for (Object ll : latestLocations){
                JSONObject latestLocation = (JSONObject) ll;
                Double latitude = latestLocation.getDouble("latitude");
                Double longitude = latestLocation.getDouble("longitude");
                LatLng friend_loc = new LatLng(latitude, longitude);
                Marker friend_loc_marker =  mMap.addMarker(new MarkerOptions().position(friend_loc).title("Student : " + latestLocation.getJSONObject("studentId").getString("firstName")));
                friend_loc_marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED ));
                friend_loc_marker.setSnippet("name : " + latestLocation.getJSONObject("studentId").getString("firstName"));
                friend_loc_marker.setTag(latestLocation.getJSONObject("studentId").getInteger("studentId"));
            }
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Integer student_id = (Integer) marker.getTag();
                    HttpConnector hc = new HttpConnector();
                    hc.execute(new String[] {String.valueOf(student_id)});
                    return false;
                }
            });
        }
    }

}
