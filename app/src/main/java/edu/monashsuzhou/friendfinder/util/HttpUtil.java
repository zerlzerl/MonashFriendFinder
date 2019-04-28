package edu.monashsuzhou.friendfinder.util;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import edu.monashsuzhou.friendfinder.Constant;
import edu.monashsuzhou.friendfinder.activity.Map;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    private static OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(chain -> {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(request);
            })
            .build();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String service_path = "http://" + Constant.SEVER_HOST + "/" + Constant.SEVER_NAME + "/webresources/";
    private final static java.util.Map<String, String> TABLE2SERVICE = new HashMap<String, String>(){{
        put("Profile","entity.studentprofile");
        put("Location","entity.studentlocation");
        put("Friendship","entity.studentfriendship");
    }};

    public static String get(String table, String uri){

        return "";
    }

    public static String post(String table, String uri, Object obj) throws IOException {
        String url = service_path + TABLE2SERVICE.get(table) + "/" + uri;
        RequestBody body = RequestBody.create(JSON, com.alibaba.fastjson.JSON.toJSONString(obj));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

}
