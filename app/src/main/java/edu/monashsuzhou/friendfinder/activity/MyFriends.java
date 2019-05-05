package edu.monashsuzhou.friendfinder.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import edu.monashsuzhou.friendfinder.R;

import edu.monashsuzhou.friendfinder.MainActivity;
import edu.monashsuzhou.friendfinder.util.StudentsAdapter;

public class MyFriends extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_friends);
        Toolbar toolbar = findViewById(R.id.friends_toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        initData();

        recyclerView = (RecyclerView) findViewById(R.id.rv_studentsCards);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new StudentsAdapter(students);
        recyclerView.setAdapter(mAdapter);

    }


    private void initData() {
        students = new ArrayList<>();
        students.add(new Student("Monny", R.drawable.female, "Suzhou", "Haha"));
        students.add(new Student("Tom", R.drawable.male, "Hangzhou", "Ha"));
        students.add(new Student("Tim", R.drawable.female, "Hangzhou", "Ha"));
        students.add(new Student("Puppy", R.drawable.male, "Hangzhou", "Ha"));
        students.add(new Student("Puppy", R.drawable.male, "Hangzhou", "Ha"));
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

        Student(String fName, int fGender, String fSuburb, String fMovie) {
            this.fName = fName;
            this.fGender = fGender;
            this.fSuburb = fSuburb;
            this.fMovie = fMovie;
        }
    }
}
