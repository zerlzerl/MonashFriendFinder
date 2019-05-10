package edu.monashsuzhou.friendfinder.util;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.monashsuzhou.friendfinder.Constant;
import edu.monashsuzhou.friendfinder.R;

public class FethLatLongIntentservice extends IntentService {
    private static String TAG = FethLatLongIntentservice.class.getName();

    protected ResultReceiver receiver;



    public FethLatLongIntentservice() {
        super("FethLatLongIntentservice");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public FethLatLongIntentservice(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent == null){
            Log.d(TAG,"intent is null");
            return;
        }

        String errorMessage = "";
        int maxResult = 5;
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        Bundle parameters = intent.getExtras();

        receiver = parameters.getParcelable(Constant.RECEIVER);

        String city_name = intent.getStringExtra(Constant.LOCATION_NAME_EXTRA);
        System.out.println(city_name);
        List<Address> addresses = null;

        try{
            addresses = geocoder.getFromLocationName(city_name,maxResult);
        } catch (IOException ioException){
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException){
            errorMessage = getString(R.string.invalid_lat_long_used);
        }

        if(addresses == null || addresses.size() == 0){
            if(errorMessage.isEmpty()){
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG,errorMessage);
            }
            deliverResultToReceiver(Constant.FAILURE_RESULT, errorMessage);
        } else {
            for(int cnt = 0; cnt < addresses.size(); cnt++){
                Address address = addresses.get(cnt);
                Log.i(TAG,"latitude : " + address.getLongitude() + "longitude : " + address.getLatitude() );
                JSONObject address_json = new JSONObject();
                address_json.put("latitude",address.getLatitude());
                address_json.put("longitude",address.getLongitude());
                address_json.put("city",city_name);
                address_json.toJSONString();
                deliverResultToReceiver(Constant.SUCCESS_RESULT,address_json.toJSONString());
            }
        }
    }

    private void deliverResultToReceiver(int resultCode, String message){
        Bundle bundle = new Bundle();
        bundle.putString(Constant.RESULT_DATA_KEY, message);
        receiver.send(resultCode, bundle);
    }
}
