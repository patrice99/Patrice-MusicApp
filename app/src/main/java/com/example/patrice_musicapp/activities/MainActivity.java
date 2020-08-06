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
                        displayFragment(feedFragment,eventsFragment, discoverFragment, profileFragment);
                        break;
                    case R.id.action_discover:
                        displayFragment(discoverFragment,feedFragment, eventsFragment, profileFragment);
                        break;
                    case R.id.action_map:
                        displayFragment(eventsFragment,feedFragment, discoverFragment, profileFragment);
                        break;
                    case R.id.action_profile:
                        displayFragment(profileFragment,feedFragment, discoverFragment, eventsFragment);
                        break;
                }
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_feed);





    }

    private void displayFragment(Fragment showFragment, Fragment hideFragment1, Fragment hideFragment2, Fragment hideFragment3) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (showFragment.isAdded()){
            ft.show(showFragment);
        } else {
            ft.add(R.id.flContainer, showFragment, TAG);
        }

        if(hideFragment1.isAdded()){
            ft.hide(hideFragment1);
        }

        if(hideFragment2.isAdded()){
            ft.hide(hideFragment2);
        }

        if(hideFragment3.isAdded()){
            ft.hide(hideFragment3);
        }

        ft.commit();

    }
}