package com.example.patrice_musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.User;
import com.parse.ParseObject;
import com.parse.ParseUser;

import static com.parse.ParseUser.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logInInBackground("patrice", "password");

    }
}