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
import com.example.patrice_musicapp.models.Event;
import com.example.patrice_musicapp.models.Post;
import com.example.patrice_musicapp.models.User;
import com.parse.ParseFile;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.BaseViewHolder>{
    public static final String TAG = SearchAdapter.class.getSimpleName();
    public static final int TYPE_USER = 0;
    public static final int TYPE_EVENT = 1;
    public Context context;
    public List<Object> objects;

    //constructor
    public SearchAdapter(Context context, List<Object> objects){
        this.context = context;
        this.objects = objects;

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

    public static class UserViewHolder extends BaseViewHolder<User> implements View.OnClickListener{
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

    public static class EventViewHolder extends BaseViewHolder<Event> implements View.OnClickListener {
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

        }
    }

    public void clear() {
        objects.clear();
        notifyDataSetChanged();
    }


}
