package edu.monashsuzhou.friendfinder.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.ferfalk.simplesearchview.SimpleSearchView;
import com.ferfalk.simplesearchview.utils.DimensUtils;

import edu.monashsuzhou.friendfinder.R;

import java.util.ArrayList;
import java.util.HashMap;

import edu.monashsuzhou.friendfinder.MainActivity;


public class Searching extends AppCompatActivity {
    private static final String TAG = "Searching";
    public static final int EXTRA_REVEAL_CENTER_PADDING = 40;
    private SimpleSearchView searchView;
    private TabLayout tabLayout;

    private SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "open" item
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            openItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0x66,
                    0xff)));
            // set item width
            openItem.setWidth(170);
            // set item title
            openItem.setTitle("Detail");
            // set item title fontsize
            openItem.setTitleSize(18);
            // set item title font color
            openItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(openItem);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(170);
            // set a icon
//            deleteItem.setIcon(R.drawable.ic_delete_24px);
            deleteItem.setTitle("Add");
            deleteItem.setTitleSize(18);
            // set item title font color
            deleteItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searching);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = findViewById(R.id.searchView);

        searchView.setOnSearchViewListener(new SimpleSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                // 搜索框展示之后触发
                Log.d("SimpleSearchView", "onSearchViewShown");
                SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.listView);
                ArrayList<String> list = new ArrayList<>();

                ArrayAdapter adapter = new ArrayAdapter(Searching.this, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);

                listView.setMenuCreator(creator);
            }

            @Override
            public void onSearchViewClosed() {
                // 搜索框结束之后触发
                Log.d("SimpleSearchView", "onSearchViewClosed");
                //test SwipeMenuListView
                SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.listView);
                ArrayList<String> list = new ArrayList<>();
                list.add("Micheal");
                list.add("Monica");
                list.add("Dylan");
                list.add("Charice");

                ArrayAdapter adapter = new ArrayAdapter(Searching.this, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);


                listView.setMenuCreator(creator);

                listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                        switch (index) {
                            case 0:
                                Log.d(TAG, "onMenuItemClick: clicked item " + index);
                                showDetailDialog();
                                break;
                            case 1:
                                Log.d(TAG, "onMenuItemClick: clicked item " + index);
                                break;
                        }
                        // false : close the menu; true : not close the menu
                        return false;
                    }
                });
            }

            @Override
            public void onSearchViewShownAnimation() {
                // 搜索动画展示之后触发
                Log.d("SimpleSearchView", "onSearchViewShownAnimation");
            }

            @Override
            public void onSearchViewClosedAnimation() {
                // 搜索框动画结束之后触发
                Log.d("SimpleSearchView", "onSearchViewClosedAnimation");
            }
        });

        tabLayout = findViewById(R.id.tabLayout);


    }

    public void showDetailDialog(){
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(Searching.this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("这是一个消息")
                        .setTitle("这是标题");

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        setupSearchView(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_action_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setupSearchView(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        searchView.setTabLayout(tabLayout);

        // Adding padding to the animation because of the hidden menu item
        Point revealCenter = searchView.getRevealAnimationCenter();
        revealCenter.x -= DimensUtils.convertDpToPx(EXTRA_REVEAL_CENTER_PADDING, this);
    }

    @Override
    public void onBackPressed() {
        if (searchView.onBackPressed()) {
            Log.i(TAG, "===========");

            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (searchView.onActivityResult(requestCode, resultCode, data)) {
            Log.i(TAG, "===========");
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
            // Empty
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.search_fregment, container, false);
//            TextView textView = rootView.findViewById(R.id.section_label_1);
            // 设置查询历史
//            textView.setText("查询历史");
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
