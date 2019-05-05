package edu.monashsuzhou.friendfinder.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.monashsuzhou.friendfinder.R;

import edu.monashsuzhou.friendfinder.MainActivity;


public class Searching extends AppCompatActivity {
    private static ViewPager viewPager;

    private static String searchCriteria = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searching);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Filter"));
        tabLayout.addTab(tabLayout.newTab().setText("Result"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    //选中filter页面时不需要做操作
                } else{
                    //切换到search页面的时候去做搜索和渲染工作
                    View currentView = viewPager.getChildAt(position);
                    ((TextView)currentView.findViewById(R.id.search_result)).setText(Searching.searchCriteria);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //手动点击Tab时的事件
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


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

    public static ViewPager getViewPager(){
        return viewPager;
    }

    //page adapter for filter and result page
    public static class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    FilterFragment tab1 = new FilterFragment();
                    return tab1;
                case 1:
                    ResultFragment tab2 = new ResultFragment();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    public static class ResultFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.result_fragment, container, false);
//            Button switch2filterBtn = view.findViewById(R.id.searchBtn);
//            Log.i("onCreateView","跳转到ResultFragment");
//            switch2filterBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Searching.getViewPager().setCurrentItem(0);
//                }
//            });
            return view;
        }
    }

    public static class FilterFragment extends Fragment {
        private List<Integer> mSelectedItems; //已选中条件
        private boolean[] selectedItems; //已选中条件的布尔值
        private String[] criterias; // 所有条件

        private EditText courseSelector;
        private List<Integer> cSelectedItems;
        private boolean[] cselectedItems;

        private DatePickerDialog datePicker;
        private EditText dateOfBirth;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.filter_fragment, container, false);
//            EditText filterContent = view.findViewById(R.id.filterContent);

            //状态操作变量初始化
            criterias = getResources().getStringArray(R.array.criteria);
            mSelectedItems = new ArrayList();
            selectedItems = new boolean[criterias.length];

            //set birthday picker
            dateOfBirth = (EditText) view.findViewById(R.id.filter_dateOfBirth);
            dateOfBirth.setInputType(InputType.TYPE_NULL);
            dateOfBirth.setOnClickListener(view12 -> {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datePicker = new DatePickerDialog(getActivity(),
                        (view1, year1, monthOfYear, dayOfMonth) -> dateOfBirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
                datePicker.show();
            });

            //set course selector
            cSelectedItems = new ArrayList();  // Where we track the selected items
            cselectedItems = new boolean[getResources().getStringArray(R.array.units).length];
            courseSelector = (EditText) view.findViewById(R.id.filter_course_multi_selector);
            courseSelector.setInputType(InputType.TYPE_NULL);
            courseSelector.setOnClickListener(view1 -> {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                Log.i(Subscription.class.getName(), "Click Course Selector");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        String courseSelectorContent = "";
                        for (Integer item : cSelectedItems){
                            cselectedItems[item] = true;
                            courseSelectorContent += getResources().getStringArray(R.array.units)[item] + ",";
                        }
                        Log.i(Subscription.class.getName(), courseSelectorContent);
                        ((TextView)view.findViewById(R.id.filter_course_multi_selector)).setText(courseSelectorContent);
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
                        .setMultiChoiceItems(R.array.units, cselectedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    cSelectedItems.add(which);
                                } else if (cSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    cSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        });

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            });


            //set add criteria button
            EditText addCriteriaButtion = (EditText) view.findViewById(R.id.criteria_select);
            addCriteriaButtion.setInputType(InputType.TYPE_NULL);
            addCriteriaButtion.setOnClickListener(view1 -> {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setTitle("Select Criteria");
                //set choices
                builder.setMultiChoiceItems(R.array.criteria, selectedItems,
                        (dialog, which, isChecked) -> {
                            if (isChecked) {
                                // If the user checked the item, add it to the selected items
                                mSelectedItems.add(which);
                            } else if (mSelectedItems.contains(which)) {
                                // Else, if the item is already in the array, remove it
                                mSelectedItems.remove(Integer.valueOf(which));
                            }
                        });

                // Add the buttons
                builder.setPositiveButton(R.string.ok, (dialog, id) -> {
                    // User clicked OK button
                    for (Integer item : mSelectedItems){
                        selectedItems[item] = true;
                    }
                    // set selected criteria
                    int selectedCriterias = mSelectedItems.size();
                    if (selectedCriterias == 0){
                        ((EditText) view1.findViewById(R.id.criteria_select)).setText("");
                    } else {
                        ((EditText) view1.findViewById(R.id.criteria_select))
                                .setText("Selected " + selectedCriterias + " Criterias as Following");
                    }


                    //根据选中的项动态显示或隐藏筛选条件
                    for (int i = 0; i < selectedItems.length; i++){
                        if (selectedItems[i]){
                            //当前选中
                            showCriteria(view, i);
                        } else {
                            //当前未选中
                            goneCriteria(view, i);
                        }
                    }



                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            });
            Log.i("onCreateView","跳转到FilterFragment");
            Button switch2resultBtn = view.findViewById(R.id.searchBtn);
            switch2resultBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Searching.searchCriteria = filterContent.getText().toString();
                    InputMethodManager imm = (InputMethodManager) view.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    if( imm != null){
                        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                    }
                    ViewPager viewPager = Searching.getViewPager();
                    viewPager.setCurrentItem(1);
                }
            });
            return view;
        }

        private void goneCriteria(View view, int i) {
            //根据index选择对应的控件清空并隐藏
            switch (i){
                case 0:
                {
                    ((EditText)view.findViewById(R.id.filter_dateOfBirth)).setText("");
                    view.findViewById(R.id.filter_dateOfBirth_layout).setVisibility(View.GONE);
                    break;
                }
                case 1:
                {
                    ((EditText)view.findViewById(R.id.filter_course_multi_selector)).setText("");
                    view.findViewById(R.id.course_multi_selector_layout).setVisibility(View.GONE);
                    break;
                }
                case 2:
                {
                    ((EditText)view.findViewById(R.id.filter_favor_sport_text)).setText("");
                    view.findViewById(R.id.filter_favor_sport_text_layout).setVisibility(View.GONE);
                    break;
                }
                case 3:
                {
                    ((EditText)view.findViewById(R.id.filter_favor_movie_text)).setText("");
                    view.findViewById(R.id.filter_favor_movie_text_layout).setVisibility(View.GONE);
                    break;
                }
                case 4:
                {
                    ((Spinner)view.findViewById(R.id.filter_favor_unit_spinner)).setSelection(0);
                    view.findViewById(R.id.filter_favor_unit_spinner_layout).setVisibility(View.GONE);
                    break;
                }
                case 5:
                {
                    ((Spinner)view.findViewById(R.id.filter_nation_spinner)).setSelection(0);
                    ((Spinner)view.findViewById(R.id.filter_language_spinner)).setSelection(0);
                    view.findViewById(R.id.filter_nation_language_spinner_layout).setVisibility(View.GONE);
                    break;
                }
                case 6:
                {
                    ((RadioButton)view.findViewById(R.id.filter_radio_part_time)).setChecked(false);
                    ((RadioButton)view.findViewById(R.id.filter_radio_full_time)).setChecked(false);
                    view.findViewById(R.id.filter_radio_mode_layout).setVisibility(View.GONE);
                    break;
                }
                case 7:
                {
                    ((EditText)view.findViewById(R.id.filter_address_text)).setText("");
                    view.findViewById(R.id.filter_address_text_layout).setVisibility(View.GONE);
                    break;
                }
                case 8:
                {
                    ((EditText)view.findViewById(R.id.filter_current_job)).setText("");
                    view.findViewById(R.id.filter_current_job_layout).setVisibility(View.GONE);
                    break;
                }
                default:
                    break;
            }
        }

        private void showCriteria(View view, int i) {
            //根据index选中对应的控件显示
            switch (i){
                case 0:
                {
                    view.findViewById(R.id.filter_dateOfBirth_layout).setVisibility(View.VISIBLE);
                    break;
                }
                case 1:
                {
                    view.findViewById(R.id.course_multi_selector_layout).setVisibility(View.VISIBLE);
                    break;
                }
                case 2:
                {
                    view.findViewById(R.id.filter_favor_sport_text_layout).setVisibility(View.VISIBLE);
                    break;
                }
                case 3:
                {
                    view.findViewById(R.id.filter_favor_movie_text_layout).setVisibility(View.VISIBLE);
                    break;
                }
                case 4:
                {
                    view.findViewById(R.id.filter_favor_unit_spinner_layout).setVisibility(View.VISIBLE);
                    break;
                }
                case 5:
                {
                    view.findViewById(R.id.filter_nation_language_spinner_layout).setVisibility(View.VISIBLE);
                    break;
                }
                case 6:
                {
                    view.findViewById(R.id.filter_radio_mode_layout).setVisibility(View.VISIBLE);
                    break;
                }
                case 7:
                {
                    view.findViewById(R.id.filter_address_text_layout).setVisibility(View.VISIBLE);
                    break;
                }
                case 8:
                {
                    view.findViewById(R.id.filter_current_job_layout).setVisibility(View.VISIBLE);
                    break;
                }
                default:
                    break;
            }
        }
    }
}