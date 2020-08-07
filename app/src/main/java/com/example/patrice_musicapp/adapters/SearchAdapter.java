package com.example.patrice_musicapp.adapters;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
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
import com.example.patrice_musicapp.models.Event;
import com.example.patrice_musicapp.models.Post;
import com.example.patrice_musicapp.models.User;
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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.BaseViewHolder>{
    public static final String TAG = SearchAdapter.class.getSimpleName();
    public static final int TYPE_USER = 0;
    public static final int TYPE_EVENT = 1;
    public static final int TYPE_POST = 2;
    public Context context;
    public List<Object> objects;
    public SearchAdapter.onClickListener clickListener;


    public interface onClickListener{
        void onEventClick(int position);
        void onUserClick(int position);
        void onPostClick(int position);
    }

    //constructor
    public SearchAdapter(Context context, List<Object> objects, onClickListener clickListener){
        this.context = context;
        this.objects = objects;
        this.clickListener = clickListener;

    }
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        switch (viewType) {
            case TYPE_USER: {
                View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
                return new UserViewHolder(view, context);
            }
            case TYPE_EVENT: {
                View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
                return new EventViewHolder(view, context);
            }
            case TYPE_POST: {
                View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
                return new PostViewHolder(view, context);
            }
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Object object = objects.get(position);
        holder.bind(object);
    }

    @Override
    public int getItemViewType(int position) {
        Object object = objects.get(position);
        if (object instanceof User) {
            return TYPE_USER;
        } else if (object instanceof Event) {
            return TYPE_EVENT;
        } else if (object instanceof Post) {
            return TYPE_POST;
        }

        throw new IllegalArgumentException("Invalid position " + position);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

        private BaseViewHolder(View itemView) {
            super(itemView);
        }
        public abstract void bind(T type);
    }

    public class UserViewHolder extends BaseViewHolder<User> implements View.OnClickListener{
        Context context;
        private ImageView ivProfilePic;
        private TextView tvUsername;
        private TextView tvBio;

        public UserViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvBio = itemView.findViewById(R.id.tvBio);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //when clicked go to the profile fragment
            clickListener.onUserClick(getAdapterPosition());

        }

        public void bind(User user) {
            tvUsername.setText(user.getUsername());
            tvBio.setText(user.getBio());

            //check if the user has a valid profilePic
            ParseFile image = user.getImage();
            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .circleCrop()
                        .into(ivProfilePic);
            } else {
                Glide.with(context)
                        .load(context.getResources().getString(R.string.DEFAULT_PROFILE_PIC))
                        .circleCrop()
                        .into(ivProfilePic);
            }


        }
    }

    public class EventViewHolder extends BaseViewHolder<Event> implements View.OnClickListener {
        private Context context;
        private ImageView ivEventImage;
        private ImageView ivHostProfilePic;
        private TextView tvEventName;
        private TextView tvEventLocation;
        private TextView tvEventDate;
        private TextView tvHostUsername;

        private EventViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            ivEventImage = itemView.findViewById(R.id.ivEventImage);
            ivHostProfilePic = itemView.findViewById(R.id.ivHostProfilePic);
            tvEventName = itemView.findViewById(R.id.tvName);
            tvEventLocation = itemView.findViewById(R.id.tvEventLocation);
            tvEventDate = itemView.findViewById(R.id.tvEventDate);
            tvHostUsername = itemView.findViewById(R.id.tvHostUsername);
            itemView.setOnClickListener(this);
        }

        @Override
        public void bind(Event event) {
            tvEventName.setText(event.getName());
            try {
                tvEventLocation.setText(Event.getStringFromLocation(event.getLocation(), context, TAG));
            } catch (IOException e) {
                e.printStackTrace();
            }
            tvEventDate.setText(event.getDate().toString());
            tvHostUsername.setText(event.getHost().getUsername());

            ParseFile image = event.getImage();
            if (image != null) {
                Glide.with(context).load(event.getImage().getUrl()).into(ivEventImage);
            }

            //check if the user has a valid profilePic
            ParseFile image2 = event.getHost().getParseFile("profileImage");
            if (image2 != null) {
                Glide.with(context)
                        .load(event.getHost().getParseFile("profileImage").getUrl())
                        .circleCrop()
                        .into(ivHostProfilePic);
            } else {
                Glide.with(context)
                        .load(context.getResources().getString(R.string.DEFAULT_PROFILE_PIC))
                        .circleCrop()
                        .into(ivHostProfilePic);
            }
        }

        @Override
        public void onClick(View view) {
            //when clicked go to the map fragment and pull up bottom sheet
            clickListener.onEventClick(getAdapterPosition());

        }
    }

    public class PostViewHolder extends BaseViewHolder<Post> implements View.OnClickListener{
        private Context context;
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

        private PostViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
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

        @Override
        public void onClick(View view) {
            clickListener.onPostClick(getAdapterPosition());
        }

        @Override
        public void bind(Post post) {

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




        }
    }

    public void clear() {
        objects.clear();
        notifyDataSetChanged();
    }




}
