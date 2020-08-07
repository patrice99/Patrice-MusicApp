package com.example.patrice_musicapp.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.Followers;
import com.example.patrice_musicapp.models.User;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.math.BigDecimal;
import java.math.MathContext;
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
        private TextView tvInstruments;
        private TextView tvDistance;
        private Button btnFollow;
        private ChipGroup chipGroupGenres;
        private HorizontalScrollView hsvInstruments;
        private HorizontalScrollView hsvGenres;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvBio = itemView.findViewById(R.id.tvBio);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvInstruments = itemView.findViewById(R.id.tvInstruments);
            btnFollow = itemView.findViewById(R.id.btnFollow);
            chipGroupGenres = itemView.findViewById(R.id.chip_group_genres);
            hsvInstruments = itemView.findViewById(R.id.hsvInstruments);
            hsvGenres = itemView.findViewById(R.id.hsvGenres);


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

            User currentUser = new User(ParseUser.getCurrentUser());
            if (user.getLocation() != null && currentUser.getLocation() != null){
                tvDistance.setVisibility(View.VISIBLE);
                double distance = user.getLocation().distanceInMilesTo(currentUser.getLocation());

                BigDecimal bd = new BigDecimal(distance);
                bd = bd.round(new MathContext(1));
                int rounded = (int) bd.doubleValue();

                String str = String.valueOf(rounded);
                tvDistance.setText(str + " miles away");
            } else {
                tvDistance.setVisibility(View.INVISIBLE);
            }

            if (user.getInstruments() != null) {
                hsvInstruments.setVisibility(View.VISIBLE);
                tvInstruments.setVisibility(View.VISIBLE);
                String instrumentStr = String.join(", ", user.getInstruments());
                instrumentStr = instrumentStr.replaceAll("_", " ").toLowerCase();
                tvInstruments.setText(instrumentStr);
            } else {
                hsvInstruments.setVisibility(View.INVISIBLE);
                tvInstruments.setVisibility(View.INVISIBLE);
            }

            List<String> genres;
            if (user.getGenres()!=null) {
                hsvGenres.setVisibility(View.VISIBLE);
                chipGroupGenres.setVisibility(View.VISIBLE);
                genres = user.getGenres();
                chipGroupGenres.removeAllViews();
                for (String genre : genres) {
                    Chip chip = new Chip(context);
                    genre = genre.replace("_", " ");
                    chip.setText(genre);
                    chip.isCheckable();
                    chipGroupGenres.addView(chip);
                }
            } else {
                hsvGenres.setVisibility(View.INVISIBLE);
                chipGroupGenres.setVisibility(View.INVISIBLE);
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
