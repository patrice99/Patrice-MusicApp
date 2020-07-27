package com.example.patrice_musicapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.adapters.SearchAdapter;
import com.example.patrice_musicapp.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiscoverFragment extends Fragment {
    public static final String TAG = DiscoverFragment.class.getSimpleName();
    private Toolbar toolbar;
    private SearchAdapter searchAdapter;
    private List<User> users;
    private RecyclerView rvSearch;
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

        users = new ArrayList<>();
        rvSearch = view.findViewById(R.id.rvSearch);
        //set adapter on rvSearch
        searchAdapter = new SearchAdapter(getContext(), users);
        rvSearch.setAdapter(searchAdapter);

        //set layout manager on recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvSearch.setLayoutManager(linearLayoutManager);

        searchView = view.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

    }

    public void filter(String characterText) {
        final List<ParseUser> allParseUsers = new ArrayList<>();
        characterText = characterText.toLowerCase(Locale.getDefault());
        if (characterText.length() != 0) {
            searchAdapter.clear();
            for (User user : users) {
                //do a query
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> allUsers, ParseException e) {
                        if (e!= null){
                            Log.e(TAG, "Issue with getting all users from Parse");
                        }
                        Log.i(TAG, "Got all users from parse Successfully");
                        allParseUsers.addAll(allUsers);
                    }
                });
                //get all users
                if (user.getUsername().toLowerCase(Locale.getDefault()).contains(characterText)) {
                    users.add(user);
                }
            }
        }
        searchAdapter.notifyDataSetChanged();
    }

}