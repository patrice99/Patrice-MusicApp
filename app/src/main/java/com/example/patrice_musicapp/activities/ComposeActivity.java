package com.example.patrice_musicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.fragments.ComposeEventFragment;
import com.example.patrice_musicapp.fragments.ComposePostFragment;
import com.google.android.material.tabs.TabLayout;

public class ComposeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public static final String TAG = ComposeActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        toolbar = (Toolbar) findViewById(R.id.toolbar_compose);
        toolbar.setTitle("Compose");
        setupTabs();
    }

    private void setupTabs() {
        tabLayout = findViewById(R.id.tabLayout);
        final Fragment fragmentComposePost = new ComposePostFragment();
        final Fragment fragmentComposeEvent = new ComposeEventFragment();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    //PostCompose positionSelected
                    //go to EventCompose Fragment
                    fragmentManager.beginTransaction().replace(R.id.flContainer3, fragmentComposePost).commit();
                } else {
                    //Maps Position Selected
                    fragmentManager.beginTransaction().replace(R.id.flContainer3, fragmentComposeEvent).commit();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flContainer3);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}