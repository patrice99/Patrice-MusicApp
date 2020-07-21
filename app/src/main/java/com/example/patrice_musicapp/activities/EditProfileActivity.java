package com.example.patrice_musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cocosw.bottomsheet.BottomSheet;
import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.Genres;
import com.example.patrice_musicapp.models.Instruments;
import com.example.patrice_musicapp.models.User;
import com.example.patrice_musicapp.utils.ImageUtil;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

public class EditProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    private User user;
    private ImageView ivProfilePic;
    private TextView tvChangePhoto;
    private EditText etName;
    private EditText etUsername;
    private EditText etBio;
    private Button btnDone;
    private Spinner spinnerGenres;
    private Spinner spinnerInstruments;

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
        btnDone = findViewById(R.id.btnDone);
        spinnerGenres = findViewById(R.id.spinnerGenres);
        spinnerInstruments = findViewById(R.id.spinnerInstruments);

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
                user.save();
                finish();

            }
        });

        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //bring up bottom sheet
                new BottomSheet.Builder(EditProfileActivity.this, R.style.BottomSheet_Dialog)
                        .title("Change Profile Photo")
                        .sheet(R.menu.menu_bottom_sheet)
                        .listener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO
                            }
                        }).show();
            }
        });

        tvChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onChoosePhoto();
            }
        });



    }



}