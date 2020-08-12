package com.example.patrice_musicapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.PostDetailsActivity;
import com.example.patrice_musicapp.models.Post;
import com.example.patrice_musicapp.utils.MediaUtil;
import com.example.patrice_musicapp.utils.OnDoubleTapListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;
    private onClickListener clickListener;

    //interface for click actions
    public interface onClickListener {
        void onProfilePicAction(int position);
        void onLikeAction(int position);
        void onUnlikeAction(int position);
        void onLocationAction(int position);
        void onChipAction(String chipGenre);
    }

    //constructor
    public PostAdapter(Context context, List<Post> posts, onClickListener clickListener){
        this.context = context;
        this.posts = posts;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivProfilePic;
        private ImageView ivPostImage;
        private ImageView ivLike;
        private ImageView ivLikeAnim;
        private TextView tvUsername;
        private TextView tvCaption;
        private TextView tvLocation;
        private TextView tvTimeStamp;
        private TextView tvLikeCount;
        private VideoView vvPostVideo;
        private WebView webviewSoundCloud;
        private AnimatedVectorDrawableCompat avd;
        private AnimatedVectorDrawable avd2;
        private ChipGroup chipGroupGenres;
        private HorizontalScrollView hsvGenres;

        public ViewHolder (@NonNull View itemView){
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            ivLike = itemView.findViewById(R.id.ivLike);
            ivLikeAnim = itemView.findViewById(R.id.ivLikeAnim);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            vvPostVideo = itemView.findViewById(R.id.vvPostVideo);
            webviewSoundCloud = itemView.findViewById(R.id.webviewSoundCloud);
            chipGroupGenres = itemView.findViewById(R.id.chip_group_genres);
            hsvGenres = itemView.findViewById(R.id.hsvGenres);

            itemView.setOnClickListener(this);
        }

        @SuppressLint("ClickableViewAccessibility")
        public void bind(final Post post) {
            //here is where we bind views
            tvUsername.setText(post.getUser().getUsername());
            tvCaption.setText(post.getCaption());
            tvTimeStamp.setText(post.getRelativeTimeAgo(post.getCreatedAt().toString()));
            try {
                if (post.getLocation()!=null) {
                    tvLocation.setVisibility(View.VISIBLE);
                    tvLocation.setText(Post.getStringFromLocation(post.getLocation(), context));
                } else {
                    tvLocation.setVisibility(View.INVISIBLE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String addS = "";
            if (post.getLikesCount()!= 1){
                addS = "s";
            }
            tvLikeCount.setText(String.valueOf(post.getLikesCount()) + " Like" + addS);

            List<String> genres;
            genres = post.getGenreFilters();
            if (genres!=null) {
                hsvGenres.setVisibility(View.VISIBLE);
                chipGroupGenres.setVisibility(View.VISIBLE);
                chipGroupGenres.removeAllViews();
                for (String genre : genres) {
                    Chip chip = new Chip(context);
                    final String finalGenre = genre;
                    chip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clickListener.onChipAction(finalGenre);
                        }
                    });
                    genre = genre.replace("_", " ");
                    chip.setText(genre);
                    chip.isCheckable();
                    chipGroupGenres.addView(chip);
                }
            } else {
                hsvGenres.setVisibility(View.INVISIBLE);
                chipGroupGenres.setVisibility(View.INVISIBLE);
            }

            //check if the post has a valid image
            ParseFile image = post.getImage();
            if (image != null) {
                vvPostVideo.setVisibility(View.GONE);
                webviewSoundCloud.setVisibility(View.GONE);
                ivPostImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(post.getImage().getUrl()).into(ivPostImage);
            }

            ParseFile video = post.getVideo();
            if(video != null){
                ivPostImage.setVisibility(View.GONE);
                webviewSoundCloud.setVisibility(View.GONE);
                vvPostVideo.setVisibility(View.VISIBLE);
                try {
                    MediaUtil.videoUri = Uri.fromFile(post.getVideo().getFile());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                MediaUtil.playbackRecordedVideo(vvPostVideo, context);
                vvPostVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setLooping(true);
                    }
                });
            }

            if(post.getSoundCloudUrl() != null){
                ivPostImage.setVisibility(View.GONE);
                vvPostVideo.setVisibility(View.GONE);
                webviewSoundCloud.setVisibility(View.VISIBLE);
                MediaUtil.showSoundCloudPlayer(webviewSoundCloud, post.getSoundCloudUrl());
            }

            //check if the user has a valid profilePic
            ParseFile image2 = post.getUser().getParseFile("profileImage");
            if (image2 != null) {
                Glide.with(context)
                        .load(post.getUser().getParseFile("profileImage").getUrl())
                        .circleCrop()
                        .into(ivProfilePic);
            } else {
                Glide.with(context)
                        .load(context.getResources().getString(R.string.DEFAULT_PROFILE_PIC))
                        .circleCrop()
                        .into(ivProfilePic);
            }

            //change image for ivLike for liked and unliked
            try {
                if (post.isLiked(ParseUser.getCurrentUser())){
                    Glide.with(context).load(context.getDrawable(R.drawable.ic_ufi_heart_active)).into(ivLike);
                } else {
                    Glide.with(context).load(context.getDrawable(R.drawable.ic_ufi_heart)).into(ivLike);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //set onclickListeners
            ivProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onProfilePicAction(getAdapterPosition());
                }
            });

            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (post.isLiked(ParseUser.getCurrentUser())) {
                            clickListener.onUnlikeAction(getAdapterPosition());
                        } else {
                            clickListener.onLikeAction(getAdapterPosition());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            final Drawable drawable = ivLikeAnim.getDrawable();
            ivPostImage.setOnTouchListener(new OnDoubleTapListener(context) {
                @Override
                public void onDoubleTap(MotionEvent e) {

                    try {
                        if (post.isLiked(ParseUser.getCurrentUser())) {
                            clickListener.onUnlikeAction(getAdapterPosition());
                        } else {
                            ivLikeAnim.setAlpha(0.70f);
                            if (drawable instanceof AnimatedVectorDrawableCompat){
                                avd = (AnimatedVectorDrawableCompat) drawable;
                                avd.start();
                            } else if (drawable instanceof  AnimatedVectorDrawable){
                                avd2 = (AnimatedVectorDrawable) drawable;
                                avd2.start();
                            }
                            clickListener.onLikeAction(getAdapterPosition());
                        }
                    } catch (JSONException er) {
                        er.printStackTrace();
                    }
                }

            });

            tvLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onLocationAction(getAdapterPosition());
                }
            });

        }

        @Override
        public void onClick(View view) {
            //get post position
            int position = getAdapterPosition();
            //get the post at that position
            Post post = posts.get(position);
            Log.i(PostAdapter.class.getSimpleName(), "Post at Position " + position + "clicked.");
            //if any post clicked, take to the PostDetailsActivity with the post
            Intent intent = new Intent(context, PostDetailsActivity.class);
            //pass post into PostDetailsActivity
            intent.putExtra("post", post);
            intent.putExtra("user", post.getUser());

            context.startActivity(intent);
        }
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

}