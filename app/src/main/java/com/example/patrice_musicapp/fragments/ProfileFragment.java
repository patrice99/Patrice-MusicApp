package com.example.patrice_musicapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.Portfolio;
import com.google.android.material.tabs.TabLayout;


public class ProfileFragment extends Fragment {
    private Toolbar toolbar;
    FragmentManager fragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        toolbar =view.findViewById(R.id.toolbar_profile);
        if(toolbar != null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        }
        toolbar.setTitle("Profile");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


    }

}