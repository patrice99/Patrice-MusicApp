package com.example.patrice_musicapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.adapters.EventAdapter;
import com.example.patrice_musicapp.models.Event;
import com.example.patrice_musicapp.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class EventsPostsFragment extends Fragment {
    private static final int DISPLAY_LIMIT = 20;
    public static final String TAG = EventsPostsFragment.class.getSimpleName();
    RecyclerView rvEventPosts;
    EventAdapter adapter;
    List<Event> allEvents;

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
        allEvents = new ArrayList<>();
        //set adapter on rvEventPosts
        adapter = new EventAdapter(getContext(), allEvents);
        rvEventPosts.setAdapter(adapter);
        //set Layout manager on rvEventPosts
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvEventPosts.setLayoutManager(linearLayoutManager);

        queryEvents(0);

        //pass events to the Map Fragment
        Fragment fragment = new MapsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("events", (Parcelable) allEvents);
        fragment.setArguments(bundle);


    }

    private void queryEvents(final int page) {
        Event.query(page, DISPLAY_LIMIT, new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for(Event event: events){
                    Log.i(TAG, "Post: " + event.getDescription() + " Username: " + event.getHost().getUsername());
                }
                if(page == 0) {
                    adapter.clear();
                }
                allEvents.addAll(events);
                adapter.notifyDataSetChanged();
            }
        });
    }
}