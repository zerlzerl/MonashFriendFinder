package edu.monashsuzhou.friendfinder.util;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.monashsuzhou.friendfinder.Constant;
import edu.monashsuzhou.friendfinder.R;

public class FetchAddressIntentService extends IntentService {
    private static String TAG = FetchAddressIntentService.class.getName();

    protected ResultReceiver receiver;



    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public FetchAddressIntentService(String name) {
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

        Location location = intent.getParcelableExtra(Constant.LOCATION_DATA_EXTRA);
        System.out.println("fads in");
        List<Address> addresses = null;

        try{
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),maxResult);
        } catch (IOException ioException){
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException){
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        if(addresses == null || addresses.size() == 0){
            if(errorMessage.isEmpty()){
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG,errorMessage);
            }
            deliverResultToReceiver(Constant.FAILURE_RESULT, errorMessage);
        } else {
            for(int cnt = 0; cnt < maxResult; cnt++){
                Address address = addresses.get(cnt);
                ArrayList<String> addressFragments = new ArrayList<String>();

                for(int i = 0; i <= address.getMaxAddressLineIndex(); i ++){
                    addressFragments.add(address.getAddressLine(i));
                }
                Log.i(TAG, getString(R.string.address_found));
                String msg = TextUtils.join(System.getProperty("line.separator"),addressFragments);
                Log.i(TAG,msg);
                JSONObject address_json = new JSONObject();
                String[] msg_list = msg.split(",");
                if(msg_list.length < 5){
                    continue;
                }
                String county = msg_list[1].trim().split(" ")[0];
                address_json.put("county", county);
                String city = msg_list[2].trim().split(" ")[0];
                address_json.put("city",city);
                String province = msg_list[3].trim().split(" ")[0];
                address_json.put("province",province);
                String country = msg_list[4].trim().split(" ")[0];
                address_json.put("country",country);

                address_json.put("latitude",location.getLatitude());

                address_json.put("longitude",location.getLongitude());
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
