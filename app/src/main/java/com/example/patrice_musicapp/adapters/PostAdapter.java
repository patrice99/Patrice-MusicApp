package com.example.patrice_musicapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.PostDetailsActivity;
import com.example.patrice_musicapp.models.Post;
import com.parse.ParseFile;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    Context context;
    List<Post> posts;

    //constructor
    public PostAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
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
        private TextView tvTitle;
        private TextView tvUsername;
        private TextView tvCaption;
        private TextView tvLocation;
        private TextView tvTimeStamp;

        public ViewHolder (@NonNull View itemView){
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            itemView.setOnClickListener(this);
        }

        public void bind(Post post) {
            //here is where we bind views
            tvTitle.setText(post.getTitle());
            tvUsername.setText(post.getUser().getUsername());
            tvCaption.setText(post.getCaption());
            tvTimeStamp.setText(post.getTimeStamp());

            //check if the post has a valid image
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(post.getImage().getUrl()).into(ivPostImage);
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
}
