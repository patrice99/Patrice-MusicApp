package com.example.patrice_musicapp.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.databinding.ActivityPostDetailsBinding;
import com.example.patrice_musicapp.models.Post;
import com.example.patrice_musicapp.utils.MediaUtil;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

public class PostDetailsActivity extends AppCompatActivity {
    public static final String TAG = PostDetailsActivity.class.getSimpleName();
    private Toolbar toolbar;
    private Post post;
    private ActivityPostDetailsBinding binding;
    String addS = "";
    private ParseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_post_details);
        toolbar.setTitle("Post Details");

        //get the intent and post passed in
        post = getIntent().getParcelableExtra("post");
        user = getIntent().getParcelableExtra("user");


        //bind views
        binding.tvTitle.setText(post.getTitle());
        binding.tvUsername.setText(user.getUsername());
        binding.tvCaption.setText(post.getCaption());
        binding.tvTimeStamp.setText(post.getTimeStamp());

        if (post.getLikesCount() != 1) {
            addS = "s";
        }
        binding.tvLikeCount.setText(String.valueOf(post.getLikesCount()) + " Like" + addS);



        //check if the post has a valid image
        ParseFile image = post.getImage();
        if (image != null) {
            binding.vvPostVideo.setVisibility(View.GONE);
            binding.webviewSoundCloud.setVisibility(View.GONE);
            binding.ivPostImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(post.getImage().getUrl()).into(binding.ivPostImage);
        }

        ParseFile video = post.getVideo();
        if (video != null) {
            binding.ivPostImage.setVisibility(View.GONE);
            binding.webviewSoundCloud.setVisibility(View.GONE);
            binding.vvPostVideo.setVisibility(View.VISIBLE);
            try {
                MediaUtil.videoUri = Uri.fromFile(post.getVideo().getFile());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            MediaUtil.playbackRecordedVideo(binding.vvPostVideo, this);
            binding.vvPostVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
        }

        if(post.getSoundCloudUrl()!= null){
            binding.ivPostImage.setVisibility(View.GONE);
            binding.vvPostVideo.setVisibility(View.GONE);
            binding.webviewSoundCloud.setVisibility(View.VISIBLE);
            MediaUtil.showSoundCloudPlayer(binding.webviewSoundCloud, post.getSoundCloudUrl());
        }

        //check if the user has a valid profilePic
        ParseFile image2 = user.getParseFile("profileImage");
        if (image2 != null) {
            Glide.with(this)
                    .load(user.getParseFile("profileImage").getUrl())
                    .circleCrop()
                    .into(binding.ivProfilePic);
        } else {
            Glide.with(this)
                    .load(getResources().getString(R.string.DEFAULT_PROFILE_PIC))
                    .circleCrop()
                    .into(binding.ivProfilePic);
        }

        //change image for ivLike for liked and unliked
        try {
            if (post.isLiked(ParseUser.getCurrentUser())) {
                Glide.with(this).load(getDrawable(R.drawable.ic_ufi_heart_active)).into(binding.ivLike);
            } else {
                Glide.with(this).load(getDrawable(R.drawable.ic_ufi_heart)).into(binding.ivLike);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //set onClick listeners
        binding.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (post.isLiked(ParseUser.getCurrentUser())) {
                        //unlike post
                        post.destroyLike(ParseUser.getCurrentUser());
                        Glide.with(PostDetailsActivity.this).load(getDrawable(R.drawable.ic_ufi_heart)).into(binding.ivLike);
                        addS = "";
                        if ((post.getLikesCount()) != 1) {
                            addS = "s";
                        }
                        binding.tvLikeCount.setText(String.valueOf(post.getLikesCount()) + " Like" + addS);
                    } else {
                        //like post
                        post.addLike(ParseUser.getCurrentUser());
                        Glide.with(PostDetailsActivity.this).load(getDrawable(R.drawable.ic_ufi_heart_active)).into(binding.ivLike);
                        addS = "";
                        if ((post.getLikesCount()) != 1) {
                            addS = "s";
                        }
                        binding.tvLikeCount.setText(String.valueOf(post.getLikesCount()) + " Like" + addS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i(TAG, "Saved like successfully");
                    }
                });

            }
        });
    }
}
