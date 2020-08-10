package com.example.patrice_musicapp.fragments.onboarding;

import android.content.Intent;
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
import android.widget.Toast;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.EditProfileActivity;
import com.example.patrice_musicapp.models.User;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

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
        countries.add(0, "United States of America");

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

        //initalize Google Places API
        Places.initialize(getContext(), getResources().getString(R.string.google_maps_key));
        etCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSave.setText("Save");
                btnSave.setBackgroundColor(getResources().getColor(R.color.white));
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);

                //Create intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                        .build(getContext());
                startActivityForResult(intent, 100);
            }
        });

        //set click listeners
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            ParseGeoPoint geoPoint = new ParseGeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
            User user = new User(ParseUser.getCurrentUser());
            user.setLocation(geoPoint);
            etCity.setText(place.getName());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Log.i(TAG, status.getStatusMessage());
            Toast.makeText(getContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
            return;
        }
    }
}