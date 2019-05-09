package edu.monashsuzhou.friendfinder.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.monashsuzhou.friendfinder.R;

import edu.monashsuzhou.friendfinder.MainActivity;
import edu.monashsuzhou.friendfinder.litepalbean.DatabaseHelper;
import edu.monashsuzhou.friendfinder.litepalbean.MiniStudent;
import edu.monashsuzhou.friendfinder.util.FriendsAdapter;
import edu.monashsuzhou.friendfinder.util.HttpUtil;
import edu.monashsuzhou.friendfinder.util.StringUtils;
import edu.monashsuzhou.friendfinder.util.StudentsAdapter;

public class MyFriends extends AppCompatActivity {
    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fab_map;
    private static List<Friends> students;

    private static JSONArray currentShownStudnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_friends);
        Toolbar toolbar = findViewById(R.id.friends_toolbar);
        setSupportActionBar(toolbar);
        //currentShownStudnt = new JSONArray();
        recyclerView = (RecyclerView) findViewById(R.id.rv_studentsCards);
        fab_map = (FloatingActionButton) findViewById(R.id.fab_map);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //执行根据ID查找朋友的方法
        FriendsShownTask friendsShownTask = new FriendsShownTask();
        friendsShownTask.execute(Login.getCurrentId());


//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean b) {
//
//            }
//        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy >0) {
                    // Scroll Down
                    if (fab_map.isShown()) {
                        fab_map.hide();
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!fab_map.isShown()) {
                        fab_map.show();
                    }
                }
            }
        });
        fab_map.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext() , GoogleMaps.class);
            intent.putExtra("map_type","friend");
            MyFriends.insertMiniStu();
            startActivity(intent);
        });

    }

    public static void insertMiniStu(){
        DatabaseHelper dh = new DatabaseHelper();
        for(int i = 0 ; i < currentShownStudnt.size(); i ++){
            JSONObject obj = currentShownStudnt.getJSONObject(i);
            MiniStudent ms = new MiniStudent();
            ms.setFriendMarker(1);
            ms.setFirstname(obj.getString("firstName"));
            ms.setStudentid(obj.getInteger("studentId"));
            dh.insertMiniStudent(ms);
            SearchLocationTask st = new SearchLocationTask();
            st.execute(new String[]{obj.getString("studentId")});
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class Student {
        public String fName;
        public int fGender;
        public String fSuburb;
        public String fMovie;
        public String info;
        Student(String fName, int fGender, String fSuburb, String fMovie, String info) {
            this.fName = fName;
            this.fGender = fGender;
            this.fSuburb = fSuburb;
            this.fMovie = fMovie;
            this.info = info;
        }
    }

    public static class Friends {
        public String fName;
        public int fGender;
        public String fSuburb;
        public String fMovie;
        public String info;
        public String friendshipInfo;
        Friends(String fName, int fGender, String fSuburb, String fMovie, String info, String friendshipInfo) {
            this.fName = fName;
            this.fGender = fGender;
            this.fSuburb = fSuburb;
            this.fMovie = fMovie;
            this.info = info;
            this.friendshipInfo = friendshipInfo;
        }
    }
    public static class FriendsShownTask extends AsyncTask<Integer, Integer, String>{
        String friendsStr_1;
        String friendsStr_2;
        @Override
        protected String doInBackground(Integer... integers) {
            Integer currentId = integers[0];

            try {
                friendsStr_1 = HttpUtil.get("Friendship","findByStuId/" + currentId);
                Log.i(MyFriends.class.getName(), friendsStr_1);
                friendsStr_2 = HttpUtil.get("Friendship","findByfriendId/" + currentId);
                Log.i(MyFriends.class.getName(), friendsStr_2);
                return "success";
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            // specify an adapter (see also next example)
            if (s.equals("success")){
                //没有网络错误
                students = new ArrayList<>();
                JSONArray friendList_1 = JSON.parseArray(friendsStr_1);
                JSONArray friendList_2 = JSON.parseArray(friendsStr_2);
                currentShownStudnt = new JSONArray();
                if (friendList_1.size() == 0){
                    //没有朋友
                } else {
                    //渲染朋友
                    for (Object i : friendList_1) {
                        String endDate = ((JSONObject) i).getString("endDate");
                        if (StringUtils.isBlank(endDate)) {
                            JSONObject friendship = (JSONObject) i;
                            Integer fsid = friendship.getInteger("friendshipId");
                            JSONObject friend = friendship.getJSONObject("friendId");
                            currentShownStudnt.add(friend);
                            String fname = friend.getString("firstName");
                            String gender = friend.getString("gender");
                            String suburb = friend.getString("suburb");
                            String fMovie = friend.getString("favouriteMovie");
                            Integer sid = friend.getInteger("studentId");
                            int genderPicture = R.drawable.male;
                            if (gender != null)
                                genderPicture = gender.equals("female") ? R.drawable.female : R.drawable.male;
                            students.add(new MyFriends.Friends(fname, genderPicture, suburb, fMovie,
                                    JSON.toJSONString(friend), JSON.toJSONString(friendship)));
                        }
                    }
                }

                if (friendList_2.size() == 0){
                    //没有朋友
                } else {
                    //渲染朋友
                    for (Object i : friendList_2) {
                        String endDate = ((JSONObject) i).getString("endDate");
                        if (StringUtils.isBlank(endDate)) {
                            JSONObject friendship = (JSONObject) i;
                            Integer fsid = friendship.getInteger("friendshipId");
                            JSONObject friend = friendship.getJSONObject("studentId");
                            currentShownStudnt.add(friend);
                            String fname = friend.getString("firstName");
                            String gender = friend.getString("gender");
                            String suburb = friend.getString("suburb");
                            String fMovie = friend.getString("favouriteMovie");
                            Integer sid = friend.getInteger("studentId");
                            int genderPicture = R.drawable.male;
                            if (gender != null)
                                genderPicture = gender.equals("female") ? R.drawable.female : R.drawable.male;
                            students.add(new MyFriends.Friends(fname, genderPicture, suburb, fMovie,
                                    JSON.toJSONString(friend), JSON.toJSONString(friendship)));
                        }
                    }
                }
            }
            mAdapter = new FriendsAdapter(students);
            recyclerView.setAdapter(mAdapter);
            super.onPostExecute(s);
        }
    }


    public static class SearchLocationTask extends AsyncTask<String, Void, String[]>{
        @Override
        public String[] doInBackground(String... params){
            int student_id = Integer.parseInt(params[0]);
            String uri = "findByStuId" + "/" + String.valueOf(student_id);
            String info = "";
            try {
                info = HttpUtil.get("Location", uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("firends", info);
            String [] str_list = new String[2];
            str_list[0] = info;
            str_list[1] = String.valueOf(student_id);
            return str_list;
        }

        @Override
        public void onPostExecute(String[] str_list){
            try{
                String info = str_list[0];
                String stu_id = str_list[1];
                JSONArray jsonArray = JSON.parseArray(info);
                if( jsonArray.size() > 0){
                    JSONObject obj = jsonArray.getJSONObject(0);
                    MiniStudent ms = new MiniStudent();
                    ms.setLatitude(obj.getDouble("latitude"));
                    ms.setLongtude(obj.getDouble("longitude"));
                    ms.setLocname(obj.getString("locName"));
                    ms.updateAll("studentid = ?",stu_id);
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
