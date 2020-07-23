package com.example.patrice_musicapp.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.MainActivity;
import com.example.patrice_musicapp.models.Event;
import com.example.patrice_musicapp.models.Post;
import com.example.patrice_musicapp.utils.ImageUtil;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ComposeEventFragment extends Fragment {
    final Calendar myCalendar = Calendar.getInstance();
    public static final String TAG = ComposeEventFragment.class.getSimpleName();
    Date date;
    EditText etDate;
    EditText etName;
    EditText etDescription;
    EditText etLocation;
    ImageView ivEventImage;
    Button btnDone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etDate = view.findViewById(R.id.etDate);
        etName = view.findViewById(R.id.etName);
        etDescription = view.findViewById(R.id.etDescription);
        etLocation = view.findViewById(R.id.etLocation);
        ivEventImage = view.findViewById(R.id.ivEventImage);
        btnDone = view.findViewById(R.id.btnDone);

        final DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                date = calendar.getTime();
            }
        };

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //set on click listeners
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //collect all three pieces of info and make a post out of it
                //get the description
                String description = etDescription.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                //get the user
                ParseUser currentUser = ParseUser.getCurrentUser();
                if(ImageUtil.photoFile == null || ivEventImage.getDrawable() == null) {
                    Toast.makeText(getContext(), "There is no image", Toast.LENGTH_SHORT).show();
                    return;
                }
                //save the location
                String locationString = etLocation.getText().toString();
                ParseGeoPoint location = null;
                try {
                    location = Event.getLocationFromString(locationString, getContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String name = etName.getText().toString();
                //save the post into
                saveEvents(description, currentUser, ImageUtil.photoFile, location, date, name);
                //go back to post fragment(which is in MainActivity)
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveEvents(String description, ParseUser currentUser, File photoFile, ParseGeoPoint location, Date date, String name) {
       Event event = new Event();
       event.setDescription(description);
       event.setName(name);
       event.setDate(date);
       event.setLocation(location);
       event.setHost(currentUser);
       event.saveInBackground(new SaveCallback() {
           @Override
           public void done(ParseException e) {
               if (e != null){
                   Log.e(TAG, "Error while saving", e);
                   Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
               }

               Log.i(TAG, "Post save was successful!");
               etDescription.setText(""); // clear out edit text so user does not save the same post twice
               ivEventImage.setImageResource(0); //clear the image view
           }
       });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etDate.setText(sdf.format(myCalendar.getTime()));
    }
}