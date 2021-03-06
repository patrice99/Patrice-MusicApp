package com.example.patrice_musicapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.ComposeActivity;
import com.example.patrice_musicapp.activities.MainActivity;
import com.example.patrice_musicapp.models.Genres;
import com.example.patrice_musicapp.models.Post;
import com.example.patrice_musicapp.models.User;
import com.example.patrice_musicapp.utils.MediaUtil;
import com.example.patrice_musicapp.utils.SocialsUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ComposePostFragment extends Fragment {
    public static final String TAG = ComposeActivity.class.getSimpleName();
    private ParseUser user;
    private ImageView ivPostImage;
    private EditText etCaption;
    private EditText etLocation;
    private ImageButton btnCaptureImage;
    private ImageButton btnCaptureVideo;
    private ImageButton btnSoundCloud;
    private Button btnSubmit;
    private ImageButton btnGallery;
    private VideoView mVideoView;
    private WebView webviewSoundCloud;
    private ProgressBar pb;
    private ParseGeoPoint geoPoint;
    private ChipGroup chipGroup;
    private List<String> checkedGenres = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compose_post, container, false);
        setHasOptionsMenu(true);

        chipGroup = view.findViewById(R.id.chip_group_genres);
        Genres[] genres = Genres.values();
        for(Genres genre: genres) {
            final Chip chip = (Chip) inflater.inflate(R.layout.filter_chip, chipGroup, false);
            chip.setText(genre.toString());
            chip.isCheckable();
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        checkedGenres.add(chip.getText().toString());
                    } else {
                        checkedGenres.remove(chip.getText().toString());
                    }
                }
            });
            chipGroup.addView(chip);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //find views
        ivPostImage = view.findViewById(R.id.ivPostImage);
        etCaption = view.findViewById(R.id.etCaption);
        etLocation = view.findViewById(R.id.etLocation);
        btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
        btnCaptureVideo = view.findViewById(R.id.btnCaptureVideo);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnGallery = view.findViewById(R.id.btnGallery);
        btnSoundCloud = view.findViewById(R.id.btnSoundCloud);
        mVideoView = view.findViewById(R.id.videoView);
        webviewSoundCloud = view.findViewById(R.id.webviewSoundCloud);
        pb = view.findViewById(R.id.pbLoading);

        mVideoView.setVisibility(View.GONE);
        ivPostImage.setImageDrawable(getResources().getDrawable(R.drawable.image_placeholder));


        //initalize Google Places API
        Places.initialize(getContext(), getResources().getString(R.string.google_maps_key));
        etLocation.setFocusable(false);
        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialize place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);

                //Create intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                        .build(getContext());
                startActivityForResult(intent, 100);

            }
        });



        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoView.setVisibility(View.GONE);
                webviewSoundCloud.setVisibility(View.GONE);
                ivPostImage.setVisibility(View.VISIBLE);
                MediaUtil.onLaunchCamera(getContext());
            }
        });

        btnCaptureVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivPostImage.setVisibility(View.GONE);
                webviewSoundCloud.setVisibility(View.GONE);
                mVideoView.setVisibility(View.VISIBLE);
                MediaUtil.startRecordingVideo(getContext());
            }
        });

        btnSoundCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivPostImage.setVisibility(View.GONE);
                mVideoView.setVisibility(View.GONE);
                webviewSoundCloud.setVisibility(View.VISIBLE);
                SocialsUtils.popUpEditText(getContext(), 2, new User(user), webviewSoundCloud);

            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoView.setVisibility(View.GONE);
                webviewSoundCloud.setVisibility(View.GONE);
                ivPostImage.setVisibility(View.VISIBLE);
                MediaUtil.onChoosePhoto(getContext());
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
                user = ParseUser.getCurrentUser();
                if((MediaUtil.photoFile == null || ivPostImage.getDrawable() == null) && MediaUtil.videoFile == null && SocialsUtils.soundCloudUrl == null) {
                    Toast.makeText(getContext(), "There is no media", Toast.LENGTH_SHORT).show();
                    return;
                }
                //save the post into
                savePosts(description, user, MediaUtil.photoFile, MediaUtil.videoFile, SocialsUtils.soundCloudUrl, geoPoint, checkedGenres);
                //go back to post fragment(which is in MainActivity)
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });



    }

    private void savePosts(String caption, final ParseUser currentUser, File photoFile, final File videoFile, final String soundCloudUrl, ParseGeoPoint geoPoint, List<String> checkedGenres) {
        final Post post = new Post();
        post.setCaption(caption);
        if (photoFile != null) {
            post.setImage(new ParseFile(photoFile));
        }
        post.setUser(currentUser);
        if (videoFile != null) {
            post.setVideo(new ParseFile(videoFile));
        }
        if(soundCloudUrl != null){
            post.setSoundCloudUrl(soundCloudUrl);
        }

        if(geoPoint!=null){
            post.setLocation(geoPoint);
        }
        if (checkedGenres!= null){
            post.setGenreFilter(checkedGenres);
        }
        pb.setVisibility(ProgressBar.VISIBLE);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                }

                Log.i(TAG, "Post save was successful!");
                etCaption.setText(""); // clear out edit text so user does not save the same post twice
                ivPostImage.setImageResource(0); //clear the image view
                User user = new User(currentUser);
                user.setPostCount(user.getPostCount() + 1);

                post.getUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving post author");
                        }
                        pb.setVisibility(ProgressBar.INVISIBLE);

                    }
                });
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
                ivPostImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if ((data != null) && requestCode == MediaUtil.PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();
            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = MediaUtil.loadFromUri(photoUri, getContext());

            // Load the selected image into a preview
            ivPostImage.setImageBitmap(selectedImage);

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

        } else if (requestCode == MediaUtil.VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getContext(), "Video has been saved to:\n" , Toast.LENGTH_LONG).show();
                MediaUtil.playbackRecordedVideo(mVideoView, getContext());
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getContext(), "Video recording cancelled.",  Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Failed to record video",  Toast.LENGTH_LONG).show();
            }
        } else if(requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            geoPoint = new ParseGeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
            etLocation.setText(place.getName());
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