package edu.monashsuzhou.friendfinder.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.monashsuzhou.friendfinder.Constant;
import edu.monashsuzhou.friendfinder.R;

import edu.monashsuzhou.friendfinder.MainActivity;
import edu.monashsuzhou.friendfinder.entity.StudentProfile;
import edu.monashsuzhou.friendfinder.util.HttpUtil;
import edu.monashsuzhou.friendfinder.util.MD5Util;
import edu.monashsuzhou.friendfinder.util.StringUtils;

public class Subscription extends AppCompatActivity {
    private static String TAG = Subscription.class.getName();

    private DatePickerDialog datePicker;
    private EditText dateOfBirth;

    private EditText courseSelector;
    private ArrayList<Integer> mSelectedItems;
    private boolean[] selectedItems;

    private EditText emailEditText;
    private Button SubcriptionButton;
    private boolean isEmailDuplicated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription);
        Toolbar toolbar = findViewById(R.id.subscription_toolbar);
        setSupportActionBar(toolbar);

        //set datepicker
        dateOfBirth = (EditText) findViewById(R.id.dateOfBirth);
        dateOfBirth.setInputType(InputType.TYPE_NULL);
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datePicker = new DatePickerDialog(Subscription.this,
                        (view1, year1, monthOfYear, dayOfMonth) -> dateOfBirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
                datePicker.show();
            }
        });

        //set course selector
        mSelectedItems = new ArrayList();  // Where we track the selected items
        selectedItems = new boolean[getResources().getStringArray(R.array.units).length];
        courseSelector = (EditText) findViewById(R.id.course_multi_selector);
        courseSelector.setInputType(InputType.TYPE_NULL);
        courseSelector.setOnClickListener(view -> {
            // 1. Instantiate an AlertDialog.Builder with its constructor
            Log.i(Subscription.class.getName(), "Click Course Selector");
            AlertDialog.Builder builder = new AlertDialog.Builder(Subscription.this);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    String courseSelectorContent = "";
                    for (Integer item : mSelectedItems){
                        selectedItems[item] = true;
                        courseSelectorContent += getResources().getStringArray(R.array.units)[item] + ",";
                    }
                    Log.i(Subscription.class.getName(), courseSelectorContent);
                    ((TextView)view.findViewById(R.id.course_multi_selector)).setText(courseSelectorContent);
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            // 2. Chain together various setter methods to set the dialog characteristics
//                selectedItems = new boolean[getResources().getStringArray(R.array.units).length];
//                for (Integer item : mSelectedItems){
//                    selectedItems[item] = true;
//                }
            builder.setTitle(R.string.course_select)
                    .setMultiChoiceItems(R.array.units, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                            if (isChecked) {
                                // If the user checked the item, add it to the selected items
                                mSelectedItems.add(which);
                            } else if (mSelectedItems.contains(which)) {
                                // Else, if the item is already in the array, remove it
                                mSelectedItems.remove(Integer.valueOf(which));
                            }
                        }
                    });

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        //set email input listener
        emailEditText = (EditText) findViewById(R.id.subscription_email_text);
        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容

                } else {
                    // 此处为失去焦点时的处理内容
                    // 用异步任务去查数据库
                    if (StringUtils.isBlank(emailEditText.getText().toString()))return;
                    CheckDuplicateEmailTask checkemail = new CheckDuplicateEmailTask();
                    checkemail.execute(emailEditText.getText().toString());
                }
            }
        });

        //set password check

        //set Subcribe Now button
        SubcriptionButton = (Button) findViewById(R.id.SubscribeNowBtn);
        SubcriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwd_1 = ((EditText)findViewById(R.id.subscription_user_password_text)).getText().toString();
                String passwd_2 = ((EditText)findViewById(R.id.subscription_user_password_repeat_text)).getText().toString();
                if (!passwd_1.equals(passwd_2)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Subscription.this);
                    builder.setMessage("Re-entered Password is not identical!");
                    builder.show();
                    return;
                }
                if(isEmailDuplicated == true){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Subscription.this);
                    builder.setMessage("The email has been registered!");
                    builder.show();
                    return;
                }

                //collection from the screen
                String email = ((EditText)findViewById(R.id.subscription_email_text)).getText().toString();
                String pwd = ((EditText)findViewById(R.id.subscription_user_password_text)).getText().toString();
                String fisrtName = ((EditText)findViewById(R.id.firstName_text)).getText().toString();
                String surname = ((EditText)findViewById(R.id.Surname_text)).getText().toString();

                RadioButton male = (RadioButton)findViewById(R.id.radio_male);
                RadioButton female = (RadioButton)findViewById(R.id.radio_female);
                String gender = null;
                if (male.isChecked()) gender = "male";
                if (female.isChecked()) gender = "female";

                String dateOfBirth = ((EditText)findViewById(R.id.dateOfBirth)).getText().toString();
                try {
                    SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = df1.parse(dateOfBirth);
                    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
                    dateOfBirth = df2.format(date) + "+08:00";
                    dateOfBirth = dateOfBirth.replace("/","T");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String nation = ((Spinner)findViewById(R.id.nation_spinner)).getSelectedItem().toString();
                String lang = ((Spinner)findViewById(R.id.language_spinner)).getSelectedItem().toString();
                String suburb = ((EditText)findViewById(R.id.suburb_text)).getText().toString();
                String address = ((EditText)findViewById(R.id.address_text)).getText().toString();
                String job = ((EditText)findViewById(R.id.current_job)).getText().toString();

                RadioButton part = (RadioButton)findViewById(R.id.radio_part_time);
                RadioButton full = (RadioButton)findViewById(R.id.radio_full_time);
                String studyMode = null;
                if (part.isChecked()) studyMode = "Part Time";
                if (full.isChecked()) studyMode = "Full Time";

                String course = ((EditText)findViewById(R.id.course_multi_selector)).getText().toString();
                if (!StringUtils.isBlank(course)) course = course.substring(0,course.length()-1);
                String favorUnit = ((Spinner)findViewById(R.id.favor_unit_spinner)).getSelectedItem().toString();
                String favorSport = ((EditText)findViewById(R.id.favor_sport_text)).getText().toString();
                String favorMovie = ((EditText)findViewById(R.id.favor_movie_text)).getText().toString();

//                String currentDateTime = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss").format(new Date());
//                String currentDate = currentDateTime.split("/")[0];
//                String currentTime = currentDateTime.split("/")[1];
//                Log.i(Subscription.class.getName(), currentDateTime + "/" + currentDate + "/" + currentTime);
                String subscriptionData = "New User Subscription";
                String currentTime = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss").format(new Date()) + "+08:00";
                currentTime = currentTime.replace("/","T");
                StudentProfile newStudent = new StudentProfile()
                        .setEmail(email)
                        .setPassword(MD5Util.GetMD5Code(pwd))
                        .setFirstName(fisrtName)
                        .setSurname(surname)
                        .setGender(gender)
                        .setDateOfBirth(dateOfBirth)
                        .setNationality(nation)
                        .setNativeLanguage(lang)
                        .setSuburb(suburb)
                        .setAddress(address)
                        .setCurrentJob(job)
                        .setStudyMode(studyMode)
                        .setCourse(course)
                        .setFavouriteUnit(favorUnit)
                        .setFavouriteSport(favorSport)
                        .setFavouriteMovie(favorMovie)
                        .setSubscriptionData(subscriptionData)
                        .setSubscriptionTime(currentTime);

                //传服务器
                new SubScriptionNewUserTask().execute(newStudent);
            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.toolbar_items, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_home) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private class CheckDuplicateEmailTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            //这里只有email一个参数
            Log.i(Subscription.class.getName(),"Check Email:" + strings[0]);
            try {
                String prolist = HttpUtil.get("Profile", "findByEmail/" + strings[0]);
                Log.i(Subscription.class.getName(),prolist);
                return prolist;
            } catch (IOException e) {
                e.printStackTrace();
//                Log.e(Subscription.class.getName(), "Check Email Fail...");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
//            Log.i(Subscription.class.getName(), result);
            if (result == null) return;
            JSONArray array = JSON.parseArray(result);
            if (array.size() > 0){
                //有重复项
                AlertDialog.Builder builder = new AlertDialog.Builder(Subscription.this);
                builder.setMessage("The email has been registered!");
                builder.show();
                isEmailDuplicated = true;
            }else{
                isEmailDuplicated = false;
            }
        }
    }

    private class SubScriptionNewUserTask extends AsyncTask<Object, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(Object... objs) {
            Log.i("New User Subscription:", JSON.toJSONString(objs[0]));
            try {
                HttpUtil.post("Profile", "", objs[0]); // 无返回值

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            super.onPostExecute(isSuccess);
            if (isSuccess){
                //注册成功
                startActivity(new Intent(Subscription.this, Login.class));
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(Subscription.this);
                builder.setMessage("Subscription Failure!");
                builder.show();
            }
        }
    }



}
