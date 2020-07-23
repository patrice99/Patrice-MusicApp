package com.example.patrice_musicapp.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.fragments.ComposePostFragment;
import com.example.patrice_musicapp.fragments.EventsPostsFragment;
import com.example.patrice_musicapp.fragments.MapsFragment;
import com.example.patrice_musicapp.models.Post;
import com.example.patrice_musicapp.utils.ImageUtil;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ComposeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public static final String TAG = ComposeActivity.class.getSimpleName();
    private ImageView ivPostImage;
    private EditText etCaption;
    private ImageButton btnCaptureImage;
    private Button btnSubmit;
    private Button btnGallery;
    private TabLayout tabLayout;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        toolbar = (Toolbar) findViewById(R.id.toolbar_compose);
        toolbar.setTitle("Compose");

        setUpTabs();


        //find views
        ivPostImage = findViewById(R.id.ivPostImage);
        etCaption = findViewById(R.id.etCaption);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnGallery = findViewById(R.id.btnGallery);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtil.onLaunchCamera(ComposeActivity.this);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //collect all three pieces of info and make a post out of it
                //get the description
                String description = etCaption.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                //get the user
                ParseUser currentUser = ParseUser.getCurrentUser();
                if(ImageUtil.photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(ComposeActivity.this, "There is no image", Toast.LENGTH_SHORT).show();
                    return;
                }
                //save the post into
                savePosts(description, currentUser, ImageUtil.photoFile);
                //go back to post fragment(which is in MainActivity)
                Intent intent = new Intent(ComposeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtil.onChoosePhoto(ComposeActivity.this);
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
                ivPostImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if ((data != null) && requestCode == ImageUtil.PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();
            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = ImageUtil.loadFromUri(photoUri, ComposeActivity.this);

            // Load the selected image into a preview
            ivPostImage.setImageBitmap(selectedImage);

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

    private void savePosts(String caption, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setCaption(caption);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        //post.setImage();
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(ComposeActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
                }

                Log.i(TAG, "Post save was successful!");
                etCaption.setText(""); // clear out edit text so user does not save the same post twice
                ivPostImage.setImageResource(0); //clear the image view
            }
        });
    }

    private void setupTabs() {
        tabLayout = findViewById(R.id.tabLayout);
        final Fragment fragmentComposePost = new ComposePostFragment();
        final Fragment fragmentComposeEvent = new ComposeEventFragment();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    //PostCompose positionSelected
                    //go to EventCompose Fragment
                    fragmentManager.beginTransaction().replace(R.id.flContainer3, fragmentEventPosts).commit();
                } else {
                    //Maps Position Selected
                    fragmentManager.beginTransaction().replace(R.id.flContainer3, fragment).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });
        tabLayout.getTabAt(1).select();
        tabLayout.getTabAt(0).select();


    }




}