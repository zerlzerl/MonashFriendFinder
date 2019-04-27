package edu.monashsuzhou.friendfinder.util;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RestClient {
    private static final String BASE_URI =
            "http://192.168.6.17:8080/5183_A1/webresources";

    public static String findByAttribute(String attr, String val) {
        final String methodPath = attr + val;
//initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
//Making HTTP request
        try {
            url = new URL(BASE_URI + methodPath);
//open the connection
            conn = (HttpURLConnection) url.openConnection();
//set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
//set the connection method to GET
            conn.setRequestMethod("GET");
//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
//Read the response
            Scanner inStream = new Scanner(conn.getInputStream());
//read the input steream and store it as string
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            Log.e("RestClient", e.getMessage());
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return textResult;
    }

}


