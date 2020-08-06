package com.example.patrice_musicapp.fragments;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.EditProfileActivity;
import com.example.patrice_musicapp.activities.MainActivity;
import com.example.patrice_musicapp.activities.SettingsActivity;
import com.example.patrice_musicapp.adapters.EventAdapter;
import com.example.patrice_musicapp.adapters.PostAdapter;
import com.example.patrice_musicapp.models.Event;
import com.example.patrice_musicapp.models.Followers;
import com.example.patrice_musicapp.models.Post;
import com.example.patrice_musicapp.models.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {
    private static final int RESULT_OK = 10001 ;
    private User user;
    private Toolbar toolbar;
    private RecyclerView rvProfileContent;
    private PostAdapter userPostAdapter;
    private EventAdapter userEventsAdapter;
    private List<Post> userPosts;
    private List<Event> userEvents;
    private TextView tvUsername;
    private ImageView ivProfilePic;
    private ImageView ivYoutube;
    private ImageView ivInstagram;
    private TextView tvName;
    private TextView tvLocation;
    private TextView tvBio;
    private TextView tvFollowing;
    private TextView tvFollowers;
    private TextView tvGenres;
    private Button btnEditProfile;
    private Button btnFollow;
    private ChipGroup chipGroupInstruments;
    private AutoCompleteTextView editTextFilledExposedDropdown;
    public static final int DISPLAY_LIMIT= 20;
    public static final String TAG = ProfileFragment.class.getSimpleName();
    private Followers follow = new Followers();
    private String selected;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar =view.findViewById(R.id.toolbar_profile);
        if(toolbar != null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        }
        toolbar.setTitle("Profile");
        toolbar.setTitleTextColor(getResources().getColor(R.color.pink));


        //Get the bundle to determine user
        Bundle bundle = this.getArguments();
        if (bundle == null){
            user = new User();
        } else {
            user = new User(bundle.getParcelable("user"));
        }

        //for menu to determine which adapter to set on the recycler view
        editTextFilledExposedDropdown = view.findViewById(R.id.filled_exposed_dropdown);
        String[] profileChoice = new String[] {"Posts", "Events"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_menu,
                        profileChoice);
        editTextFilledExposedDropdown.setText("Posts",true);
        selected = "Posts";
        editTextFilledExposedDropdown.setAdapter(adapter);


        rvProfileContent = view.findViewById(R.id.rvProfileContent);
        //set layout manager on recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvProfileContent.setLayoutManager(linearLayoutManager);

        checkChoice();
        editTextFilledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = editTextFilledExposedDropdown.getText().toString();
                checkChoice();
            }
        });


        //For ProfileDetails CardView

        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvName = view.findViewById(R.id.tvName);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvBio = view.findViewById(R.id.tvBio);
        tvGenres = view.findViewById(R.id.tvGenres);
        tvFollowers = view.findViewById(R.id.tvFollowers);
        tvFollowing = view.findViewById(R.id.tvFollowing);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnFollow = view.findViewById(R.id.btnFollow);
        chipGroupInstruments = view.findViewById(R.id.chip_group_instruments);
        ivYoutube = view.findViewById(R.id.ivYoutube);
        ivInstagram = view.findViewById(R.id.ivInstagram);



        tvUsername.setText(user.getUsername());
        tvName.setText(user.getName());
        tvBio.setText(user.getBio());
        if (user.getGenres() != null) {
            String genreStr = String.join(", ", user.getGenres());
            genreStr = genreStr.replaceAll("_", " ").toLowerCase();
            tvGenres.setText(genreStr);
            tvGenres.setMovementMethod(new ScrollingMovementMethod());

        }
        tvFollowing.setText(String.valueOf(user.getFollowingCount()));
        final List<ParseUser> followers = new ArrayList<>();
        Followers.getFollowers(user.getParseUser(), new FindCallback<Followers>() {
            @Override
            public void done(List<Followers> followersList, ParseException e) {
                List<Followers> follows = new ArrayList<>();
                follows.addAll(followersList);
                for (Followers follow : follows){
                    followers.add(follow.getFollower());
                }

                tvFollowers.setText(String.valueOf(followers.size()));
            }
        });
        ParseFile image = user.getImage();
        if (image == null){
            Glide.with(getContext())
                    .load(getResources().getString(R.string.DEFAULT_PROFILE_PIC))
                    .circleCrop()
                    .into(ivProfilePic);
        } else {
            Glide.with(getContext())
                    .load(image.getUrl())
                    .circleCrop()
                    .into(ivProfilePic);
        }


        List<String> instruments;
        instruments = user.getInstruments();
        if (instruments!=null) {
            for (String instrument : instruments) {
                Chip chip = new Chip(getContext());
                instrument = instrument.replace("_", " ");
                chip.setText(instrument);
                chip.isCheckable();
                chipGroupInstruments.addView(chip);
            }
        }



        final User subjectUser = new User(ParseUser.getCurrentUser());
        final User user2follow = new User(user.getParseUser());
        try {
            if(subjectUser.isFollowed(user2follow)) {
                //change color to green
                btnFollow.setBackgroundColor(getResources().getColor(R.color.green));
                //change text to following
                btnFollow.setText(getResources().getString(R.string.following));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //set on click listeners an visibility
        if (user.getParseUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
            btnFollow.setVisibility(View.GONE);
            btnEditProfile.setVisibility(View.VISIBLE);
            //some EditProfile functionality
            btnEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //When clicked, it launches the EditProfile Activity
                    Intent intent = new Intent(getContext(), EditProfileActivity.class);
                    //pass info from that post into Details Activity
                    intent.putExtra("user", Parcels.wrap(user));
                    startActivityForResult(intent, RESULT_OK);
                }
            });
        } else {
            btnEditProfile.setVisibility(View.GONE);
            btnFollow.setVisibility(View.VISIBLE);
            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //check if following or unfollowing
                    try {
                        if (!(subjectUser.isFollowed(user2follow))){
                            //update in ParseUser Dashboard
                            subjectUser.addFollowing(user2follow);
                            follow.addFollower(user2follow.getParseUser(), subjectUser.getParseUser());
                            //change color to green
                            btnFollow.setBackgroundColor(getResources().getColor(R.color.green));
                            //change text to following
                            btnFollow.setText(getResources().getString(R.string.following));
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
                            btnFollow.setBackgroundColor(getResources().getColor(R.color.blue));
                            //change text to following
                            btnFollow.setText(getResources().getString(R.string.follow));

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

        if (user.getLocation()!= null && (user.getLocation().getLatitude() != 0.0 && user.getLocation().getLongitude() != 0.0)) {
            try {
                tvLocation.setText(User.getStringFromLocation(user.getLocation(), getContext()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to maps fragment at that location
                LatLng location = new LatLng(user.getLocation().getLatitude(), user.getLocation().getLongitude());
                Fragment newFragment = new MapsFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("location", location);
                newFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.flContainer, newFragment);
                transaction.commit();
            }
        });

        if(user.getYoutubeUrl() == null){
            ivYoutube.setVisibility(View.GONE);
        } else {
            ivYoutube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(user.getYoutubeUrl()));
                    try {
                       getContext().startActivity(webIntent);
                    } catch (ActivityNotFoundException ex) {
                    }
                }
            });
        }

        if(user.getInstagramUsername() == null){
            ivInstagram.setVisibility(View.GONE);
        } else {
            ivInstagram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("http://instagram.com/_u/" + user.getInstagramUsername());
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                    likeIng.setPackage("com.instagram.android");

                    try {
                        startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://instagram.com/" + user.getInstagramUsername())));
                    }
                }
            });
        }


    }

    private void checkChoice() {
        switch (selected){
            case "Posts":
                userPosts = new ArrayList<>();
                userPostAdapter = new PostAdapter(getContext(), userPosts, onClickListenerPost);
                rvProfileContent.setAdapter(userPostAdapter);
                queryPosts(0);
                break;
            case "Events":
                userEvents = new ArrayList<>();
                userEventsAdapter = new EventAdapter(getContext(), userEvents, onClickListenerEvent);
                rvProfileContent.setAdapter(userEventsAdapter);
                queryEvents(0);
                break;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings_button) {
            //go to settings activity
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    protected void queryPosts(final int page) {
        Post.query(page, DISPLAY_LIMIT, user.parseUser, new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;

                }
                for(Post post: posts){
                    Log.i(TAG, "Post: " + post.getCaption() + " Username: " + post.getUser().getUsername());
                }
                if(page == 0) {
                    userPostAdapter.clear();
                }
                userPosts.addAll(posts);
                userPostAdapter.notifyDataSetChanged();
            }
        }, null);
    }


    protected void queryEvents(final int page) {
        Event.query(page, DISPLAY_LIMIT, user.getParseUser(), new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue with getting events", e);
                    return;
                }

                for (Event event: events){
                    Log.i(TAG, "Event" + event.getName() + "Host: " + event.getHost().getUsername());
                }
                if (page == 0) {
                    userEventsAdapter.clear();
                }

                userEvents.addAll(events);
                userEventsAdapter.notifyDataSetChanged();
            }
        });
    }

    PostAdapter.onClickListener onClickListenerPost = new PostAdapter.onClickListener() {
        @Override
        public void onProfilePicAction(int position) {
            //do nothing
        }

        @Override
        public void onLikeAction(int position) {

        }

        @Override
        public void onUnlikeAction(int position) {

        }

        @Override
        public void onCommentAction(int position) {

        }
    };

    EventAdapter.onClickListener onClickListenerEvent = new EventAdapter.onClickListener() {
        @Override
        public void onEventClick(int position) {

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == 10001) && (resultCode == EditProfileActivity.RESULT_OK)) {
            Fragment frag = new ProfileFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, frag);
            ft.commit();
        }

    }
}