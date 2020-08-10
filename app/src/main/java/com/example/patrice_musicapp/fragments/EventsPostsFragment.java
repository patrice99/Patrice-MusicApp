package com.example.patrice_musicapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.adapters.EventAdapter;
import com.example.patrice_musicapp.models.Event;
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
        adapter = new EventAdapter(getContext(), allEvents, clickListener);
        rvEventPosts.setAdapter(adapter);
        //set Layout manager on rvEventPosts
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvEventPosts.setLayoutManager(linearLayoutManager);

        queryEvents(0);


    }

    private void queryEvents(final int page) {
        Event.query(page, DISPLAY_LIMIT, null, new FindCallback<Event>() {
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

    EventAdapter.onClickListener clickListener = new EventAdapter.onClickListener() {
        @Override
        public void onEventClick(int position) {
            //get event position
            //get the post at that position
            Event event = allEvents.get(position);
            Log.i(EventAdapter.class.getSimpleName(), "Event at Position " + position + "clicked.");
            //if any post clicked, take to the Maps Fragment and bring up a bottom sheet.
            //pass the event to maps for the bottom sheet
            // Create new fragment and transaction
            Fragment newFragment = new MapsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("event", event);
            newFragment.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.flContainer2, newFragment);
            // Commit the transaction
            transaction.commit();

        }
    };
}