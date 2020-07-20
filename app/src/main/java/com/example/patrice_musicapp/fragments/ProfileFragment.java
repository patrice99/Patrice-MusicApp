package com.example.patrice_musicapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.EditProfileActivity;
import com.example.patrice_musicapp.activities.SettingsActivity;
import com.example.patrice_musicapp.adapters.PostAdapter;
import com.example.patrice_musicapp.models.Post;
import com.example.patrice_musicapp.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {
    private User user;
    private Toolbar toolbar;
    private RecyclerView rvProfilePosts;
    private PostAdapter userAdapter;
    private List<Post> userPosts;
    private TextView tvUsername;
    private ImageView ivProfilePic;
    private TextView tvName;
    private Button btnEditProfile;
    public static final int DISPLAY_LIMIT= 20;
    public static final String TAG = ProfileFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar =view.findViewById(R.id.toolbar_profile);
        if(toolbar != null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        }
        toolbar.setTitle("Profile");


        //Get the bundle to determine user
        Bundle bundle = this.getArguments();
        if (bundle == null){
            user = new User();
        } else {
            user = new User(bundle.getParcelable("user"));
        }

        rvProfilePosts = view.findViewById(R.id.rvProfilePosts);

        userPosts = new ArrayList<>();

        //instantiate adapter
        userAdapter = new PostAdapter(getContext(), userPosts, onClickListener);
        //set adapter on recycler view
        rvProfilePosts.setAdapter(userAdapter);

        //set layout manager on recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvProfilePosts.setLayoutManager(linearLayoutManager);

        //query posts
        queryPosts(0);



        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvName = view.findViewById(R.id.tvName);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);


        tvUsername.setText(user.getUsername());
        tvName.setText(user.getName());
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


        //set on click listeners
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //When clicked, it launches the EditProfile Activity
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                //pass info from that post into Details Activity
                intent.putExtra("user", Parcels.wrap(user));
                getContext().startActivity(intent);
            }
        });




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
                    userAdapter.clear();
                }
                userPosts.addAll(posts);
                userAdapter.notifyDataSetChanged();
            }
        });
    }

    PostAdapter.onClickListener onClickListener = new PostAdapter.onClickListener() {
        @Override
        public void onProfilePicAction(int position) {
            //do nothing
        }

        @Override
        public void onUsernameAction(int position) {
            //do nothing
        }

        @Override
        public void onLikeAction(int position) {

        }

        @Override
        public void onCommentAction(int position) {

        }
    };

}