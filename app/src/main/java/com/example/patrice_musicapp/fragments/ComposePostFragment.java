package com.example.patrice_musicapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.ComposeActivity;
import com.example.patrice_musicapp.activities.MainActivity;
import com.example.patrice_musicapp.models.Post;
import com.example.patrice_musicapp.utils.ImageUtil;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static android.app.Activity.RESULT_OK;

public class ComposePostFragment extends Fragment {
    public static final String TAG = ComposeActivity.class.getSimpleName();
    private ImageView ivPostImage;
    private EditText etCaption;
    private ImageButton btnCaptureImage;
    private Button btnSubmit;
    private Button btnGallery;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compose_post, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //find views
        ivPostImage = view.findViewById(R.id.ivPostImage);
        etCaption = view.findViewById(R.id.etCaption);
        btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnGallery = view.findViewById(R.id.btnGallery);


        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtil.onLaunchCamera(getContext());
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //collect all three pieces of info and make a post out of it
                //get the description
                String description = etCaption.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                //get the user
                ParseUser currentUser = ParseUser.getCurrentUser();
                if(ImageUtil.photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(getContext(), "There is no image", Toast.LENGTH_SHORT).show();
                    return;
                }
                //save the post into
                savePosts(description, currentUser, ImageUtil.photoFile);
                //go back to post fragment(which is in MainActivity)
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtil.onChoosePhoto(getContext());
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
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if ((data != null) && requestCode == ImageUtil.PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();
            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = ImageUtil.loadFromUri(photoUri, getContext());

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
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                }

                Log.i(TAG, "Post save was successful!");
                etCaption.setText(""); // clear out edit text so user does not save the same post twice
                ivPostImage.setImageResource(0); //clear the image view
            }
        });
    }
}