package com.example.patrice_musicapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.PostDetailsActivity;
import com.example.patrice_musicapp.models.Post;
import com.example.patrice_musicapp.utils.MediaUtil;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONException;

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
        void onCommentAction(int position);
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
        private TextView tvTitle;
        private TextView tvUsername;
        private TextView tvCaption;
        private TextView tvLocation;
        private TextView tvTimeStamp;
        private TextView tvLikeCount;
        private VideoView vvPostVideo;

        public ViewHolder (@NonNull View itemView){
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            ivLike = itemView.findViewById(R.id.ivLike);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            vvPostVideo = itemView.findViewById(R.id.vvPostVideo);
            itemView.setOnClickListener(this);
        }

        public void bind(final Post post) {
            //here is where we bind views
            tvTitle.setText(post.getTitle());
            tvUsername.setText(post.getUser().getUsername());
            tvCaption.setText(post.getCaption());
            tvTimeStamp.setText(post.getTimeStamp());
            String addS = "";
            if (post.getLikesCount()!= 1){
                addS = "s";
            }
            tvLikeCount.setText(String.valueOf(post.getLikesCount()) + " Like" + addS);

            //check if the post has a valid image
            ParseFile image = post.getImage();
            if (image != null) {
                vvPostVideo.setVisibility(View.GONE);
                ivPostImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(post.getImage().getUrl()).into(ivPostImage);
            }

            ParseFile video = post.getVideo();
            if(video != null){
                ivPostImage.setVisibility(View.GONE);
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
            context.startActivity(intent);
        }
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
}
