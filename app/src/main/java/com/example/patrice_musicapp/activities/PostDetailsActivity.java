package com.example.patrice_musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.Post;
import com.example.patrice_musicapp.utils.MediaUtil;
import com.parse.ParseException;
import com.parse.ParseFile;

public class PostDetailsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Post post;
    private ImageView ivProfilePic;
    private ImageView ivPostImage;
    private TextView tvTitle;
    private TextView tvUsername;
    private TextView tvCaption;
    private TextView tvLocation;
    private TextView tvTimeStamp;
    private VideoView vvPostVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_post_details);
        toolbar.setTitle("Post Details");

        //get the intent and post passed in
        post = getIntent().getParcelableExtra("post");

        //find views
        ivProfilePic = findViewById(R.id.ivProfilePic);
        ivPostImage = findViewById(R.id.ivPostImage);
        tvTitle = findViewById(R.id.tvTitle);
        tvUsername = findViewById(R.id.tvUsername);
        tvCaption = findViewById(R.id.tvCaption);
        tvLocation = findViewById(R.id.tvLocation);
        tvTimeStamp = findViewById(R.id.tvTimeStamp);
        vvPostVideo = findViewById(R.id.vvPostVideo);


        //bind views
        tvTitle.setText(post.getTitle());
        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getCaption());
        tvTimeStamp.setText(post.getTimeStamp());

        //check if the post has a valid image
        ParseFile image = post.getImage();
        if (image != null) {
            vvPostVideo.setVisibility(View.GONE);
            ivPostImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(post.getImage().getUrl()).into(ivPostImage);
        }

        ParseFile video = post.getVideo();
        if (video != null){
            ivPostImage.setVisibility(View.GONE);
            vvPostVideo.setVisibility(View.VISIBLE);
            try {
                MediaUtil.videoUri = Uri.fromFile(post.getVideo().getFile());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            MediaUtil.playbackRecordedVideo(vvPostVideo, this);
            vvPostVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
        }

        //check if the user has a valid profilePic
        ParseFile image2 = post.getUser().getParseFile("profileImage");
        if (image2 != null) {
            Glide.with(this)
                    .load(post.getUser().getParseFile("profileImage").getUrl())
                    .circleCrop()
                    .into(ivProfilePic);
        } else {
            Glide.with(this)
                    .load(getResources().getString(R.string.DEFAULT_PROFILE_PIC))
                    .circleCrop()
                    .into(ivProfilePic);
        }

    }
}