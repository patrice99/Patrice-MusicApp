package com.example.patrice_musicapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.fragments.DiscoverFragment;
import com.example.patrice_musicapp.fragments.FeedFragment;
import com.example.patrice_musicapp.fragments.EventsFragment;
import com.example.patrice_musicapp.fragments.ProfileFragment;
import com.example.patrice_musicapp.utils.FragmentUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment feedFragment = new FeedFragment();
    Fragment discoverFragment = new DiscoverFragment();
    Fragment eventsFragment = new EventsFragment();
    Fragment profileFragment = new ProfileFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.action_feed:
                        FragmentUtils.displayFragment(fragmentManager, feedFragment,eventsFragment, discoverFragment, profileFragment);
                        break;
                    case R.id.action_discover:
                        FragmentUtils.displayFragment(fragmentManager, discoverFragment,feedFragment, eventsFragment, profileFragment);
                        break;
                    case R.id.action_map:
                        FragmentUtils.displayFragment(fragmentManager, eventsFragment,feedFragment, discoverFragment, profileFragment);
                        break;
                    case R.id.action_profile:
                        FragmentUtils.displayFragment(fragmentManager, profileFragment,feedFragment, discoverFragment, eventsFragment);
                        break;
                }
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_feed);





    }

}