package com.example.patrice_musicapp.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.Genres;
import com.example.patrice_musicapp.models.Instruments;
import com.example.patrice_musicapp.models.User;
import com.example.patrice_musicapp.utils.MediaUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hootsuite.nachos.NachoTextView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class EditProfileActivity extends AppCompatActivity {
    public static final String TAG = EditProfileActivity.class.getSimpleName();
    private Toolbar toolbar;
    private User user;
    private ImageView ivProfilePic;
    private ImageButton youtubeButton;
    private TextView tvChangePhoto;
    private EditText etName;
    private EditText etUsername;
    private EditText etBio;
    private EditText etLocation;
    private Button btnDone;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView tvTakePhoto;
    private TextView tvChoosePhoto;
    private AutoCompleteTextView editTextFilledExposedDropdownInstruments;
    private NachoTextView nachoTextViewGenres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = findViewById(R.id.toolbar_edit_profile);
        toolbar.setTitle("Edit Profile");


        //get intent
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        //find views
        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvChangePhoto = findViewById(R.id.tvChangePhoto);
        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etBio = findViewById(R.id.etBio);
        etLocation = findViewById(R.id.etLocation);
        btnDone = findViewById(R.id.btnDone);
        editTextFilledExposedDropdownInstruments = findViewById(R.id.filled_exposed_dropdown_Instruments);
        nachoTextViewGenres = findViewById(R.id.nacho_text_view_genres);
        youtubeButton = findViewById(R.id.youtubeButton);


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

        //make sure the location isn't null or [0,0]
        try {
            if (user.getLocation()!= null && (user.getLocation().getLatitude() != 0.0 && user.getLocation().getLongitude() != 0.0)){
                String location = User.getStringFromLocation(user.getLocation(), EditProfileActivity.this);
                etLocation.setText(location);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayAdapter<Genres> genreAdapter = new ArrayAdapter<>
                (this,
                R.layout.dropdown_menu,
                Arrays.asList(Genres.values()));

//        editTextFilledExposedDropdownGenres.setAdapter(genreAdapter);

        ArrayAdapter<Instruments> instrumentAdapter = new ArrayAdapter<>
                (this,
                        R.layout.dropdown_menu,
                        Arrays.asList(Instruments.values()));

        editTextFilledExposedDropdownInstruments.setAdapter(instrumentAdapter);

        nachoTextViewGenres.setAdapter(genreAdapter);





        //set on click listeners
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save all new attributes of user to Parse
                user.setUsername(etUsername.getText().toString());
                user.setName(etName.getText().toString());
                user.setBio( etBio.getText().toString());
                try {
                    user.setLocation(User.getLocationFromString(etLocation.getText().toString(),EditProfileActivity.this));
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
                MediaUtil.onLaunchCamera(EditProfileActivity.this);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                //save photo in parse
                saveProfilePic(MediaUtil.photoFile);
            }
        });

        tvChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaUtil.onChoosePhoto(EditProfileActivity.this);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                //save photo in parse
                saveProfilePic(MediaUtil.photoFile);

            }
        });

        youtubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpEditText();
            }
        });



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MediaUtil.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) { //make sure a photo was taken
                // by this point we have the camera photo on disk
                //decode the file
                Bitmap takenImage = BitmapFactory.decodeFile(MediaUtil.photoFile.getAbsolutePath());
                // Load the taken image into a preview
                ivProfilePic.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if ((data != null) && requestCode == MediaUtil.PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();
            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = MediaUtil.loadFromUri(photoUri, EditProfileActivity.this);

            // Load the selected image into a preview
            ivProfilePic.setImageBitmap(selectedImage);

            //save the Image to Parse by converting it to byte Array
            // Configure byte output stream
            FileOutputStream outStream = null;
            try {
                outStream = new FileOutputStream(MediaUtil.photoFile);
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


    private void popUpEditText() {
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogueView = inflater.inflate(R.layout.custom_dialogue, null);
        new MaterialAlertDialogBuilder(this)
                .setTitle("Enter your YouTube Channel URL")
                .setView(dialogueView)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //save youtube url to Parse as String

                    }
                })
                .show();
    }



}