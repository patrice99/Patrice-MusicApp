package com.example.patrice_musicapp.fragments.onboarding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.adapters.UserAdapter;
import com.example.patrice_musicapp.models.User;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class OnboardingFragment4 extends Fragment {
    public static final String TAG = OnboardingFragment4.class.getSimpleName();
    private RecyclerView rvUsers;
    private List<ParseUser> users;
    private UserAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding4, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvUsers = view.findViewById(R.id.rvUsers);
        rvUsers.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvUsers.setLayoutManager(layoutManager);
        users = new ArrayList<>();
        adapter = new UserAdapter(getContext(), users);
        rvUsers.setAdapter(adapter);

        queryUsers();

    }

    private void queryUsers() {
        User.queryUsers(10, ParseUser.getCurrentUser(), new FindCallback<ParseUser>(){
            @Override
            public void done(List<ParseUser> users2Follow, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues with getting users to follow", e);
                }
                adapter.clear();
                users.addAll(users2Follow);
                adapter.notifyDataSetChanged();
            }
        });

    }

}