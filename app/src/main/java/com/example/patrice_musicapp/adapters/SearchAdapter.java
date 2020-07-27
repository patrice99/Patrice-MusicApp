package com.example.patrice_musicapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.PostDetailsActivity;
import com.example.patrice_musicapp.fragments.ProfileFragment;
import com.example.patrice_musicapp.models.Post;
import com.example.patrice_musicapp.models.User;
import com.parse.ParseFile;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    public Context context;
    public List<Object> objects;

    //constructor
    public SearchAdapter(Context context, List<Object> objects){
        this.context = context;
        this.objects = objects;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = (User) objects.get(position);
        holder.bind(user);

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public static abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView ivProfilePic;
        private TextView tvUsername;
        private TextView tvBio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvBio = itemView.findViewById(R.id.tvBio);
        }

        @Override
        public void onClick(View view) {
            //when a user is clicked, take them to the profile page

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

    public void clear() {
        objects.clear();
        notifyDataSetChanged();
    }


}
