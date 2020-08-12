package com.example.patrice_musicapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.ComposeActivity;
import com.google.android.material.tabs.TabLayout;

public class EventsFragment extends Fragment {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    FragmentManager fragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar_events);
        toolbar.setTitle("Events");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.addEventButton) {
                    //go to compose activity
                    Intent intent = new Intent(getContext(), ComposeActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });


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


}