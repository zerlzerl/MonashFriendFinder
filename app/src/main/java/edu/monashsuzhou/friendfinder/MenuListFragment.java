
package edu.monashsuzhou.friendfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.monashsuzhou.friendfinder.activity.Login;


public class MenuListFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container,
                false);

        NavigationView vNavigation = (NavigationView) view.findViewById(R.id.vNavigation);
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(getActivity(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                try {
                    intent.setClass(getActivity(), Class.forName("edu.monashsuzhou.friendfinder.activity." + menuItem.getTitle()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if(menuItem.getTitle().equals("Logout")) {
                    Login.setCurrentId(-1);
                }
                startActivity(intent);
                return false;
            }
        }) ;

        return  view ;
    }



}
