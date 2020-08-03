package com.example.patrice_musicapp.fragments.onboarding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class OnboardingFragment2 extends Fragment {
    public static final String TAG = OnboardingFragment2.class.getSimpleName();
    private AutoCompleteTextView editTextFilledExposedDropdown;
    private Button btnSave;
    private EditText etCity;
    private boolean isSaved;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_menu,
                        countries);

        editTextFilledExposedDropdown = view.findViewById(R.id.filled_exposed_dropdown);
        btnSave = view.findViewById(R.id.btnSave);
        etCity = view.findViewById(R.id.etCity);

        editTextFilledExposedDropdown.setAdapter(adapter);
        editTextFilledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                btnSave.setText("Save");
                btnSave.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });
        etCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSave.setText("Save");
                btnSave.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        //set click listeners
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save location of the new user in their city
                User user = new User(ParseUser.getCurrentUser());
                try {
                    user.setLocation(User.getLocationFromString(String.valueOf(etCity.getText()), getContext()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                user.getParseUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e!=null){
                            Log.e(TAG, "Issue with saving new user's location", e);
                        }
                        Log.i(TAG, "User's location saved successfully");
                        btnSave.setText("Saved!");
                        btnSave.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    }
                });
            }
        });
    }
}