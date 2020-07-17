package com.example.patrice_musicapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.ComposeActivity;
import com.example.patrice_musicapp.adapters.PostAdapter;
import com.example.patrice_musicapp.models.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private RecyclerView rvFeedPosts;
    private PostAdapter adapter;
    private List<Post> allPosts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar_feed);
        toolbar.setTitle("Feed");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = view.findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //respond to fab click
                //go to compose Activity
                Intent intent = new Intent(getContext(), ComposeActivity.class);
                startActivity(intent);
            }
        });

        rvFeedPosts = view.findViewById(R.id.rvFeedPosts);

        allPosts = new ArrayList<>();
        //instantiate the adapter
        adapter = new PostAdapter(getContext(), allPosts);
        //set adapter on recycler view
        rvFeedPosts.setAdapter(adapter);
        //set layout manager on recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvFeedPosts.setLayoutManager(linearLayoutManager);


    }

    
}