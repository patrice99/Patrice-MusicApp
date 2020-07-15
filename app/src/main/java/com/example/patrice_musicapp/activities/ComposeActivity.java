package com.example.patrice_musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.example.patrice_musicapp.R;

public class ComposeActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        toolbar = (Toolbar) findViewById(R.id.toolbar_compose);
        toolbar.setTitle("Make a feed post!");
    }
}