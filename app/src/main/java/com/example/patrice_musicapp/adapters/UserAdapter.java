package com.example.patrice_musicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.User;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<ParseUser> users;
    private onClickListener clickListener;

    public interface onClickListener {
        void onFollowClick(int position);
    }

    public UserAdapter(Context context, List<ParseUser> users, onClickListener clickListener){
        this.context = context;
        this.users = users;
        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = new User(users.get(position));
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivProfilePic;
        private TextView tvUsername;
        private TextView tvBio;
        private Button btnFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvBio = itemView.findViewById(R.id.tvBio);

        }

        public void bind(User user) {
            //check if the user has a valid profilePic
            ParseFile image = user.getImage();
            if (image != null) {
                Glide.with(context)
                        .load(user.getImage().getUrl())
                        .circleCrop()
                        .into(ivProfilePic);
            } else {
                Glide.with(context)
                        .load(context.getResources().getString(R.string.DEFAULT_PROFILE_PIC))
                        .circleCrop()
                        .into(ivProfilePic);
            }

            tvUsername.setText(user.getUsername());
            tvBio.setText(user.getBio());

            final User subjectUser = new User(ParseUser.getCurrentUser());
            final User user2follow = new User(user.getParseUser());
            try {
                if(subjectUser.isFollowed(user2follow)) {
                    //change color to green
                    btnFollow.setBackgroundColor(context.getResources().getColor(R.color.green));
                    //change text to following
                    btnFollow.setText(context.getResources().getString(R.string.following));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onFollowClick(getAdapterPosition());
                }
            });

        }


    }
}
