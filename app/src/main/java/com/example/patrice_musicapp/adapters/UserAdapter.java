package com.example.patrice_musicapp.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.Followers;
import com.example.patrice_musicapp.models.User;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    public static final String TAG = UserAdapter.class.getSimpleName();
    private Context context;
    private List<ParseUser> users;
    private Followers follow;
    private onClickListener clickListener;

    public interface onClickListener {
        void onUserClick(int position);
    }

    public UserAdapter(Context context, List<ParseUser> users, onClickListener clickListener){
        this.context = context;
        this.users = users;
        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = new User(users.get(position));
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivProfilePic;
        private TextView tvUsername;
        private TextView tvBio;
        private TextView tvGenres;
        private Button btnFollow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvBio = itemView.findViewById(R.id.tvBio);
            tvGenres = itemView.findViewById(R.id.tvGenres);
            btnFollow = itemView.findViewById(R.id.btnFollow);
            itemView.setOnClickListener(this);

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
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

            if (user.getGenres() != null) {
                String genreStr = String.join(", ", user.getGenres());
                genreStr = genreStr.replaceAll("_", " ").toLowerCase();
                tvGenres.setText(genreStr);
            }

            final User subjectUser = new User(ParseUser.getCurrentUser());
            final User user2follow = new User(user.getParseUser());
            follow = new Followers();
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
                    try {
                        if (!(subjectUser.isFollowed(user2follow))){
                            //update in ParseUser Dashboard
                            subjectUser.addFollowing(user2follow);
                            follow.addFollower(user2follow.getParseUser(), subjectUser.getParseUser());
                            //change color to green
                            btnFollow.setBackgroundColor(context.getResources().getColor(R.color.green));
                            //change text to following
                            btnFollow.setText(context.getResources().getString(R.string.following));
                        } else {
                            user2follow.deleteFollowing(user2follow);
                            follow.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e!=null){
                                        Log.e(TAG, "Issue with deleting this follow",e);
                                    }
                                    Log.i(TAG, "Follow successfully deleted");
                                }
                            });
                            //change color back to blue
                            btnFollow.setBackgroundColor(context.getResources().getColor(R.color.blue));
                            //change text to following
                            btnFollow.setText(context.getResources().getString(R.string.follow));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    subjectUser.getParseUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e!= null){
                                Log.e(TAG, "Issue with saving", e);
                            }
                            Log.i(TAG, "Following of" + user2follow.getParseUser().getUsername() + "successfully added");
                        }
                    });

                    follow.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e!= null){
                                Log.e(TAG, "Issue with saving", e);
                            }
                            Log.i(TAG, "Follower of successfully added");

                        }
                    });
                }
            });

        }

        @Override
        public void onClick(View view) {
            //go to profile fragment
            clickListener.onUserClick(getAdapterPosition());
        }
    }

    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }
}
