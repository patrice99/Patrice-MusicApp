package com.example.patrice_musicapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.adapters.EventAdapter;
import com.example.patrice_musicapp.models.Event;

import java.util.ArrayList;
import java.util.List;


public class EventsPostsFragment extends Fragment {
    RecyclerView rvEventPosts;
    EventAdapter adapter;
    List<Event> events;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvEventPosts = view.findViewById(R.id.rvEventPosts);
        events = new ArrayList<>();
        //set adapter on rvEventPosts
        adapter = new EventAdapter(getContext(), events);
        rvEventPosts.setAdapter(adapter);
        //set Layout manager on rvEventPosts
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvEventPosts.setLayoutManager(linearLayoutManager);





    }
}