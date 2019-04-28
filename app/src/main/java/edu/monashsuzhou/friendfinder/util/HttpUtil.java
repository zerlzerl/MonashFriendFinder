package edu.monashsuzhou.friendfinder.util;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import edu.monashsuzhou.friendfinder.Constant;
import edu.monashsuzhou.friendfinder.activity.Map;
import edu.monashsuzhou.friendfinder.entity.StudentProfile;
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
    private static String service_path = "http://" + Constant.SEVER_HOST + ":" +Constant.SEVER_PORT
            + "/" + Constant.SEVER_NAME + "/webresources/";
    private final static java.util.Map<String, String> TABLE2SERVICE = new HashMap<String, String>(){{
        put("Profile","entity.studentprofile");
        put("Location","entity.studentlocation");
        put("Friendship","entity.studentfriendship");
    }};

    public static String get(String table, String uri) throws IOException {
        String url = service_path + TABLE2SERVICE.get(table) + "/" + uri;
//        Log.i(HttpUtil.class.getName() + "GET:", url);
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public static String post(String table, String uri, Object obj) throws IOException {
        String url = service_path + TABLE2SERVICE.get(table) + "/" + uri;
//        Log.i(HttpUtil.class.getName() + "POST:", url);
        System.out.println(url);
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

    public static String put(String table, String uri, Object obj) throws IOException {
        String url = service_path + TABLE2SERVICE.get(table) + "/" + uri;
//        Log.i(HttpUtil.class.getName() + "POST:", url);
        System.out.println(url);
        RequestBody body = RequestBody.create(JSON, com.alibaba.fastjson.JSON.toJSONString(obj));
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public static String deleteById(String table, String uri) throws IOException {
        String url = service_path + TABLE2SERVICE.get(table) + "/" + uri;
//        Log.i(HttpUtil.class.getName() + "POST:", url);
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public static JSONObject getJson(String table, String uri) throws IOException {
        return JSONObject.parseObject(get(table, uri));
    }
    public static JSONObject postJson(String table, String uri, Object obj) throws IOException {
        return JSONObject.parseObject(post(table, uri, obj));
    }
    public static void main(String[] args) throws ParseException {
        try {
            //Examples
            //1.通过ID找学生
            String msg = get("Profile","1");
            System.out.println(msg);

            //2.通过单个attr找学生
            msg = get("Profile", "findByDateOfBirth/1995-11-20");
            System.out.println(msg);

            //3.访问频率
            msg = get("Profile", "visitingFrequency/2010-01-01/2019-01-01/1/z/zhp");
            System.out.println(msg);

            //4.通过任何叠加条件查找：
            JSONObject params = new JSONObject();
            params.put("password","123");
            params.put("firstName","yq");
            msg = post("Profile", "findByAnyAttr", params);
            System.out.println(msg);
            //4.1 通过创建一个对象来进行查找
            StudentProfile student = new StudentProfile().setPassword("123").setFirstName("yq").setDateOfBirth("1995-11-11");
            msg = post("Profile", "findByAnyAttr", student);
            System.out.println(msg);

            //5 新增一个新对象
            student = new StudentProfile().setEmail("abcdefg@hijklmn").setFirstName("opqrst").setPassword("1234567890");
            msg = post("Profile", "", student);
            System.out.println(msg); // 无返回值

            //6 修改一个对象后更新
            JSONObject profile = getJson("Profile", "1");
            profile.put("email", "abcdefg@hijklmn");
            msg = put("Profile", "1", profile);
            System.out.println(msg); // 无返回值

            msg = get("Location", "1");

            //7 删除一个对象，此处修改了A1部分的StudentProfile.java文件，需要将CascadeType.ALL 改为 CascadeType.REMOVE
            StudentProfile deleteStu1 = new StudentProfile().setStudentId(1);
            msg = deleteById("Profile", "1");
            System.out.println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
