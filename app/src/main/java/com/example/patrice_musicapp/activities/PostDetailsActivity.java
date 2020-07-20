package com.example.patrice_musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.Post;

public class PostDetailsActivity extends AppCompatActivity {
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        //get the intent and post passed in
        post = getIntent().getParcelableExtra("post");


    }
}