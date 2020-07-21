package com.example.patrice_musicapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cocosw.bottomsheet.BottomSheet;
import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.Genres;
import com.example.patrice_musicapp.models.Instruments;
import com.example.patrice_musicapp.models.User;
import com.example.patrice_musicapp.utils.ImageUtil;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {
    public static final String TAG = EditProfileActivity.class.getSimpleName();
    private Toolbar toolbar;
    private User user;
    private ImageView ivProfilePic;
    private TextView tvChangePhoto;
    private EditText etName;
    private EditText etUsername;
    private EditText etBio;
    private EditText etLocation;
    private Button btnDone;
    private Spinner spinnerGenres;
    private Spinner spinnerInstruments;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView tvTakePhoto;
    private TextView tvChoosePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = findViewById(R.id.toolbar_edit_profile);
        toolbar.setTitle("Edit Profile");


        //get intent
        user = (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));

        //find views
        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvChangePhoto = findViewById(R.id.tvChangePhoto);
        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etBio = findViewById(R.id.etBio);
        etLocation = findViewById(R.id.etLocation);
        btnDone = findViewById(R.id.btnDone);
        spinnerGenres = findViewById(R.id.spinnerGenres);
        spinnerInstruments = findViewById(R.id.spinnerInstruments);

        bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        tvTakePhoto = findViewById(R.id.tvTakePhoto);
        tvChoosePhoto = findViewById(R.id.tvChoosePhoto);

        //bind views with data from user
        //check to see if user has image
        ParseFile image = user.getImage();
        if (image != null){
            Glide.with(this)
                    .load(image.getUrl())
                    .circleCrop()
                    .into(ivProfilePic);
        } else {
            tvChangePhoto.setText("Add Profile Pic");
            Glide.with(this)
                    .load(getResources().getString(R.string.DEFAULT_PROFILE_PIC))
                    .circleCrop()
                    .into(ivProfilePic);
        }

        String name = user.getName();
        if (name != null){
            etName.setText(name);
        }

        etUsername.setText(user.getUsername());

        String bio = user.getBio();
        if (bio != null){
            etBio.setText(bio);
        }

        try {
            String location = getStringFromLocation(user.getLocation());
            if(location!= null){
                etLocation.setText(location);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        spinnerGenres.setAdapter(new ArrayAdapter<Genres>(this, android.R.layout.simple_spinner_item, Genres.values()));
        spinnerInstruments.setAdapter(new ArrayAdapter<Instruments>(this, android.R.layout.simple_spinner_item, Instruments.values()));


        //set on click listeners
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save all new attributes of user to Parse
                user.setUsername(etUsername.getText().toString());
                user.setName(etName.getText().toString());
                user.setBio( etBio.getText().toString());
                try {
                    user.setLocation(getLocationFromString(etLocation.getText().toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                user.save();
                finish();

            }
        });
        //user can either tap this
        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //bring up bottom sheet
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        //or user can tap this to change photo
        tvChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //bring up bottom sheet
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtil.onLaunchCamera(EditProfileActivity.this);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                //save photo in parse
                saveProfilePic(ImageUtil.photoFile);
            }
        });

        tvChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtil.onChoosePhoto(EditProfileActivity.this);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                //save photo in parse
                saveProfilePic(ImageUtil.photoFile);

            }
        });



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageUtil.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) { //make sure a photo was taken
                // by this point we have the camera photo on disk
                //decode the file
                Bitmap takenImage = BitmapFactory.decodeFile(ImageUtil.photoFile.getAbsolutePath());
                // Load the taken image into a preview
                ivProfilePic.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if ((data != null) && requestCode == ImageUtil.PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();
            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = ImageUtil.loadFromUri(photoUri, EditProfileActivity.this);

            // Load the selected image into a preview
            ivProfilePic.setImageBitmap(selectedImage);

            //save the Image to Parse by converting it to byte Array
            // Configure byte output stream
            FileOutputStream outStream = null;
            try {
                outStream = new FileOutputStream(ImageUtil.photoFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //Compress the image further 
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

        }
    }


    private void saveProfilePic(File photoFile) {
        user.setImage(new ParseFile(photoFile));
        user.getParseUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(EditProfileActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
                }
                
                Log.i(TAG, "Post save was successful!");
            }
        });
    }



    public ParseGeoPoint getLocationFromString(String location) throws IOException {
        Geocoder geocoder = new Geocoder(EditProfileActivity.this, Locale.US);
        List<Address> addresses = geocoder.getFromLocationName(location, 5);
        Address address = addresses.get(0); //get the first address for right now

        return new ParseGeoPoint(address.getLatitude(), address.getLongitude());

    }

    public String getStringFromLocation(ParseGeoPoint parseGeoPoint) throws IOException {
        Geocoder geocoder = new Geocoder(EditProfileActivity.this, Locale.US);
        List<Address> addresses = geocoder.getFromLocation(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude(), 5);
        Address address = addresses.get(0); //get the first address for right now

        return address.getAddressLine(0); //city
    }







}