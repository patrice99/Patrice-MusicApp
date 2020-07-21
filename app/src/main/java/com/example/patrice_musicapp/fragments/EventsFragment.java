package com.example.patrice_musicapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.ComposeActivity;
import com.example.patrice_musicapp.activities.SettingsActivity;
import com.google.android.material.tabs.TabLayout;

public class EventsFragment extends Fragment {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    FragmentManager fragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar =view.findViewById(R.id.toolbar_events);
        if(toolbar != null){
            toolbar.setTitle("Events");
        }

        fragmentManager = getFragmentManager();
        tabLayout = view.findViewById(R.id.tabLayout);
        final Fragment fragmentEventPosts = new EventsPostsFragment();
        final Fragment fragmentMap = new MapsFragment();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    //Events positionSelected
                    //go to EventsPost Fragment
                    fragmentManager.beginTransaction().replace(R.id.flContainer2, fragmentEventPosts).commit();
                } else {
                    //Maps Position Selected
                    fragmentManager.beginTransaction().replace(R.id.flContainer2, fragmentMap).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });
        tabLayout.getTabAt(1).select();
        tabLayout.getTabAt(0).select();



    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_events, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();

        if (id == R.id.addEventButton) {
            //go to compose activity
            Intent intent = new Intent(getContext(), ComposeActivity.class);
            startActivity(intent);
        }
        return true;
    }
}