package edu.monashsuzhou.friendfinder.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.monashsuzhou.friendfinder.Constant;
import edu.monashsuzhou.friendfinder.MainActivity;
import edu.monashsuzhou.friendfinder.R;
import edu.monashsuzhou.friendfinder.activity.Login;
import edu.monashsuzhou.friendfinder.activity.MyFriends;
import edu.monashsuzhou.friendfinder.activity.Searching;
import edu.monashsuzhou.friendfinder.activity.Subscription;
import edu.monashsuzhou.friendfinder.litepalbean.DatabaseHelper;
import edu.monashsuzhou.friendfinder.litepalbean.MiniStudent;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.MyViewHolder> {
    List<MyFriends.Student> students;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cv_student;
        TextView tv_fName;
        TextView tv_fSuburb;
        TextView tv_fMovie;
        ImageView iv_fGender;
        ImageButton iv_detail;
        ImageButton iv_add;


        public MyViewHolder(View v) {
            super(v);
            cv_student = (CardView) v.findViewById(R.id.cv_student);
            tv_fName = (TextView) v.findViewById(R.id.tv_fName);
            tv_fSuburb = (TextView) v.findViewById(R.id.tv_fSuburb);
            tv_fMovie = (TextView) v.findViewById(R.id.tv_fMovie);
            iv_fGender = (ImageView) v.findViewById(R.id.iv_fGender);
            iv_detail = (ImageButton) v.findViewById(R.id.iv_detail);
            iv_add = (ImageButton) v.findViewById(R.id.iv_add);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public StudentsAdapter(List<MyFriends.Student> students) {
        this.students = students;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StudentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_card, parent, false);
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.student_card, parent, false);
//        ...
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tv_fName.setText(students.get(i).fName);
        holder.iv_fGender.setImageResource(students.get(i).fGender);
        holder.tv_fSuburb.setText("  Suburb: " + students.get(i).fSuburb);
        holder.tv_fMovie.setText("  Movie: " + students.get(i).fMovie);

        holder.iv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("======","解析info格式化字符串，查电影，弹窗");
                SearchMovieTask searchMovieTask = new SearchMovieTask();
                searchMovieTask.execute(students.get(i).info, view);

            }
        });

        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setTitle("Add a new friend").setMessage("Are you sure you want to add " + students.get(i).fName + " as your friend");

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        // do nothing
                    }
                });
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("==========","服务器通信");
                        String friendInfo = students.get(i).info;
                        AddFriendTask addFriendTask = new AddFriendTask();
                        addFriendTask.execute(friendInfo, view);
                        // User clicked OK button
                    }
                });
                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class SearchMovieTask extends AsyncTask<Object, Integer, Object>{
        JSONObject stuInfo;
        JSONObject movie;
        Bitmap poster;
        @Override
        protected Object doInBackground(Object... objects) {
            String info = (String) objects[0];
            stuInfo = JSON.parseObject(info);

            try {
                String movieInfo = HttpUtil.get(Constant.MOVIE_HOST + stuInfo.getString("favouriteMovie"));
                if (movieInfo != null){
                    movie = JSON.parseObject(movieInfo);
                }
                String posterUrl = movie.getString("Poster");
                if (posterUrl != null){
                    poster = getBitmap(posterUrl);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            View view = (View) objects[1];
             return view;
        }

        @Override
        protected void onPostExecute(Object o) {
            View view = (View) o;
            // 1. Instantiate an AlertDialog.Builder with its constructor


            // 2. Chain together various setter methods to set the dialog characteristics


//                builder.setMessage(students.get(i).info);
            StringBuilder sb = new StringBuilder();

            String name = stuInfo.getString("firstName") + " " + stuInfo.getString("surname");
            String location = stuInfo.getString("suburb") + " " + stuInfo.getString("address");
            String email = stuInfo.getString("email");
            String gender =  stuInfo.getString("gender");
            String birthday = stuInfo.getString("birthday");
            String nation = stuInfo.getString("nationality");
            String lang = stuInfo.getString("nativeLanguage");
            String fSport = stuInfo.getString("favouriteSport");
            String fUnit = stuInfo.getString("favouriteUnit");
            String fMovie = stuInfo.getString("favouriteMovie");
            String currentJob = stuInfo.getString("currentJob");
            String studyMode = stuInfo.getString("studyMode");
            String course = stuInfo.getString("course");
            String movieDesp = "Director: " + movie.getString("Director") + "\nWriter: " +
                    movie.getString("Writer") + "\nPlot: " + movie.getString("Plot");
            //封装属性
            LayoutInflater inflater = LayoutInflater.from(view.getContext());
            View imgEntryView = inflater.inflate(R.layout.detail_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());



            ((TextView) imgEntryView.findViewById(R.id.detail_name)).setText(name);
            ((TextView) imgEntryView.findViewById(R.id.detail_location)).setText(location);
            ((TextView) imgEntryView.findViewById(R.id.detail_email)).setText(email);
            ((TextView) imgEntryView.findViewById(R.id.detail_gender)).setText(gender);
            ((TextView) imgEntryView.findViewById(R.id.detail_birthday)).setText(birthday);
            ((TextView) imgEntryView.findViewById(R.id.detail_nation)).setText(nation);
            ((TextView) imgEntryView.findViewById(R.id.detail_lang)).setText(lang);
            ((TextView) imgEntryView.findViewById(R.id.detail_fSport)).setText(fSport);
            ((TextView) imgEntryView.findViewById(R.id.detail_fUnit)).setText(fUnit);
            ((TextView) imgEntryView.findViewById(R.id.detail_fMovie)).setText(fMovie);
            ((TextView) imgEntryView.findViewById(R.id.detail_fMovie_desp)).setText(movieDesp);
            ImageView img = (ImageView) imgEntryView.findViewById(R.id.detail_movie_poster);
            img.setImageBitmap(poster);
            ((TextView) imgEntryView.findViewById(R.id.detail_currentJob)).setText(currentJob);
            ((TextView) imgEntryView.findViewById(R.id.detail_studyMode)).setText(studyMode);
            ((TextView) imgEntryView.findViewById(R.id.detail_course)).setText(course);



//
//            sb.append(name).append(location).append(email).append(gender).append(birthday).append(nation).append(lang)
//                    .append(fSport).append(fUnit).append(fMovie).append(currenJob).append(studyMode).append(course).append(moviePoster);
//
//            builder.setMessage(sb.toString());

            //setButton
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    // do nothing
                }
            });
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.setView(imgEntryView);
            dialog.show();
            super.onPostExecute(o);
        }

        private Bitmap getBitmap(String posterUrl) {
            URL imgUrl = null;
            Bitmap bitmap = null;
            try {
                imgUrl = new URL(posterUrl);
                HttpURLConnection conn = (HttpURLConnection) imgUrl
                        .openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

    public static class AddFriendTask extends AsyncTask<Object, Integer, Object>{
        String friendInfo;
        String myInfo;
        int friendId;
        String friendName;
        boolean state;
        @Override
        protected Object doInBackground(Object... objs) {
            friendInfo = (String) objs[0];
            Integer currentLoginUserId = Login.getCurrentId();
            myInfo = null;

            try {
                myInfo = HttpUtil.get("Profile","" + currentLoginUserId);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (myInfo != null){
                //没有出现网络错误
                JSONObject newFriedShip = new JSONObject();
                JSONObject friend = JSON.parseObject(friendInfo);
                JSONObject me = JSON.parseObject(myInfo);
                friendId = friend.getInteger("studentId");
                friendName = friend.getString("firstName");
                if(me.getInteger("studentId") < friendId){
                    newFriedShip.put("friendId",JSON.parse(friendInfo));
                    newFriedShip.put("studentId",JSON.parse(myInfo));
                } else {
                    newFriedShip.put("studentId", JSON.parse(friendInfo));
                    newFriedShip.put("friendId", JSON.parse(myInfo));
                }
                String currentTime = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss").format(new Date()) + "+08:00";
                currentTime = currentTime.replace("/","T");
                newFriedShip.put("startingDate", currentTime);
                Log.i("",newFriedShip.toJSONString());
                try {
                    HttpUtil.post("Friendship", "", newFriedShip);
                    state = true;
                } catch (IOException e) {
                    state = false;
                    e.printStackTrace();
                }
            }
            return objs[1];
        }

        @Override
        protected void onPostExecute(Object obj) {
            super.onPostExecute(obj);
            View view = (View) obj;
            if (state){
                //执行成功
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Add Success!");

                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //修改数据库
//                        DatabaseHelper dh = new DatabaseHelper();
//                        MiniStudent ms = new MiniStudent();
//                        ms.setFriendMarker(1);
//                        ms.setMatchingMarker(0);
//                        ms.setFirstname(friendName);
//                        ms.setStudentid(friendId);
//                        dh.insertMiniStudent(ms);
                        //重绘页面
                        Searching.addFriendId(friendId);
                        Searching.SearchAndRenderTask searchAndRenderTask = new Searching.SearchAndRenderTask();
                        searchAndRenderTask.execute(Searching.getSearchCriteria());
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 1000);



            } else {
                //执行出错
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Add Failure!");
                builder.show();

            }
        }


    }
}
