package com.example.patrice_musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.example.patrice_musicapp.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }


    private void doMySearch(String query) {


    }
}