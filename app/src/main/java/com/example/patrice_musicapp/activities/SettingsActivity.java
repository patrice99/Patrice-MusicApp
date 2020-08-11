package com.example.patrice_musicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.databinding.ActivitySettingsBinding;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingsBinding binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        toolbar = findViewById(R.id.toolbar_settings);
        toolbar.setTitle("Settings");


        cardView = binding.cardView;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log out user
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){
                            Log.e(SettingsActivity.class.getSimpleName(), "Issues with Logging out" ,e);
                            return;
                        }
                        //fire an intent to LoginActivity
                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        startActivity(intent);

                    }
                });

            }
        });


    }


}