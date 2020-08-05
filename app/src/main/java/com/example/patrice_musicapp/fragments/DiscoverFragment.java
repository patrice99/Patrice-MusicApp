package com.example.patrice_musicapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.MainActivity;
import com.example.patrice_musicapp.adapters.SearchAdapter;
import com.example.patrice_musicapp.adapters.UserAdapter;
import com.example.patrice_musicapp.models.Event;
import com.example.patrice_musicapp.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiscoverFragment extends Fragment {
    public static final String TAG = DiscoverFragment.class.getSimpleName();
    private Toolbar toolbar;
    private SearchAdapter searchAdapter;
    private UserAdapter userAdapter;
    private List<Object> objects;
    private List<ParseUser> users;
    private RecyclerView rvSearch;
    private RecyclerView rvUsers;
    private SearchView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar =view.findViewById(R.id.toolbar_discover);
        if(toolbar != null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        }
        toolbar.setTitle("Discover");

        objects = new ArrayList<>();
        rvSearch = view.findViewById(R.id.rvSearch);
        //set adapter on rvSearch
        searchAdapter = new SearchAdapter(getContext(), objects, clickListener);
        rvSearch.setAdapter(searchAdapter);

        //set layout manager on recycler view
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvSearch.setLayoutManager(linearLayoutManager);


        users = new ArrayList<>();
        rvUsers = view.findViewById(R.id.rvUsers);
        userAdapter = new UserAdapter(getContext(), users, clickListenerUser);
        rvUsers.setAdapter(userAdapter);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvUsers.setLayoutManager(gridLayoutManager);

        if (searchAdapter.getItemCount() == 0){
            rvSearch.setAlpha(0);
            rvUsers.setAlpha(1);
            queryUsers();
        } else {
            rvUsers.setAlpha(0);
            rvSearch.setAlpha(1);
        }


        searchView = view.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()!= 0) {
                    rvUsers.setAlpha(0);
                    rvUsers.setVisibility(View.GONE);
                    rvUsers.setLayoutManager(null);
                    rvUsers.setAdapter(null);
                    rvSearch.setAlpha(1);
                    rvSearch.setVisibility(View.VISIBLE);
                    rvSearch.setLayoutManager(linearLayoutManager);
                    rvSearch.setAdapter(searchAdapter);
                    filter(newText);
                } else {
                    rvSearch.setAlpha(0);
                    rvSearch.setVisibility(View.GONE);
                    rvSearch.setLayoutManager(null);
                    rvSearch.setAdapter(null);
                    rvUsers.setAlpha(1);
                    rvUsers.setVisibility(View.VISIBLE);
                    rvUsers.setLayoutManager(gridLayoutManager);
                    rvUsers.setAdapter(userAdapter);
                }
                return true;
            }
        });






    }

    public void filter(String characterText) {
        final List<ParseUser> allParseUsers = new ArrayList<>();
        final List<Event> allEvents = new ArrayList<>();
        characterText = characterText.toLowerCase(Locale.getDefault());
        searchAdapter.clear();
        if (characterText.length() != 0) {
            //get all users
            ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
            final String finalCharacterText = characterText;
            queryUser.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> allUsers, ParseException e) {
                    if (e!= null){
                        Log.e(TAG, "Issue with getting all users from Parse");
                    }
                    Log.i(TAG, "Got all users from parse Successfully");
                    allParseUsers.addAll(allUsers);

                    for (ParseUser parseUser: allParseUsers) {
                        if (parseUser.getUsername().toLowerCase(Locale.getDefault()).contains(finalCharacterText)) {
                            objects.add(new User(parseUser));
                        }
                    }
                    searchAdapter.notifyDataSetChanged();
                }
            });


            ParseQuery<Event> queryEvent = ParseQuery.getQuery("Event");
            queryEvent.include("Host");
            queryEvent.findInBackground(new FindCallback<Event>() {
                @Override
                public void done(List<Event> events, ParseException e) {
                    if (e!= null){
                        Log.e(TAG, "Issue with getting all events from Parse");
                    }
                    Log.i(TAG, "Got all events from parse Successfully");
                    allEvents.addAll(events);

                    for  (Event event: events){
                        if (event.getName().toLowerCase(Locale.getDefault()).contains(finalCharacterText)) {
                            objects.add(event);
                        }
                    }
                    searchAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    SearchAdapter.onClickListener clickListener = new SearchAdapter.onClickListener() {
        @Override
        public void onEventClick(int position) {
            //get event position
            //get the post at that position
            Event event = (Event) objects.get(position);
            Log.i(TAG, "Event at Position " + position + "clicked.");
            //if any post clicked, take to the Maps Fragment and bring up a bottom sheet.
            //pass the event to maps for the bottom sheet
            // Create new fragment and transaction
            Fragment newFragment = new MapsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("event", event);
            newFragment.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.flContainer, newFragment);
            // Commit the transaction
            transaction.commit();
        }

        @Override
        public void onUserClick(int position) {
            //go to profile Fragment
            //get user of that specific post
            User user = (User) objects.get(position);

            //pass this info to profile fragment
            Fragment fragment = new ProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user.getParseUser());
            fragment.setArguments(bundle);

            //Go from this fragment to profile fragment
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    };

    UserAdapter.onClickListener clickListenerUser = new UserAdapter.onClickListener() {
        @Override
        public void onUserClick(int position) {
            //go to profile Fragment
            //get user of that specific post
            ParseUser user = users.get(position);

            //pass this info to profile fragment
            Fragment fragment = new ProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            fragment.setArguments(bundle);

            //Go from this fragment to profile fragment
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    };


    private void queryUsers() {
        User.queryUsers(10, ParseUser.getCurrentUser(), new FindCallback<ParseUser>(){
            @Override
            public void done(List<ParseUser> users2Follow, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues with getting users to follow", e);
                }
                userAdapter.clear();
                users.addAll(users2Follow);
                userAdapter.notifyDataSetChanged();
            }
        });

    }


}