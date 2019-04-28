package edu.monashsuzhou.friendfinder.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import edu.monashsuzhou.friendfinder.R;

import edu.monashsuzhou.friendfinder.MainActivity;

public class Subscription extends AppCompatActivity {
    private DatePickerDialog datePicker;
    private EditText dateOfBirth;

    private EditText courseSelector;
    private ArrayList<Integer> mSelectedItems;
    private boolean[] selectedItems;

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
        courseSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


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

}
