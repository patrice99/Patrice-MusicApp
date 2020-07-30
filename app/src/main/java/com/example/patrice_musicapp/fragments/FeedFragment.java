package com.example.patrice_musicapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.ComposeActivity;
import com.example.patrice_musicapp.adapters.PostAdapter;
import com.example.patrice_musicapp.models.Post;
import com.example.patrice_musicapp.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedFragment extends Fragment {
    public static final int DISPLAY_LIMIT = 20;
    public static final String TAG = FeedFragment.class.getSimpleName();
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private RecyclerView rvFeedPosts;
    private PostAdapter adapter;
    private List<Post> allPosts;
    private List<ParseUser> following = new ArrayList<>();
    private User user = new User(ParseUser.getCurrentUser());
    private ParseUser filterForUser;
    private SwipeRefreshLayout swipeContainer;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar_feed);
        toolbar.setTitle("Feed");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = view.findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //respond to fab click
                //go to compose Activity
                Intent intent = new Intent(getContext(), ComposeActivity.class);
                startActivity(intent);
            }
        });

        rvFeedPosts = view.findViewById(R.id.rvFeedPosts);
        swipeContainer = view.findViewById(R.id.swipeContainer);


        allPosts = new ArrayList<>();
        //instantiate the adapter
        adapter = new PostAdapter(getContext(), allPosts, onClickListener);
        //set adapter on recycler view
        rvFeedPosts.setAdapter(adapter);
        //set layout manager on recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvFeedPosts.setLayoutManager(linearLayoutManager);

        //get the posts from the parse dashboard
        try {
            getUserFollowing();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getUserFollowing();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Stop animation (This will be after 2 seconds)
                        swipeContainer.setRefreshing(false);
                    }
                }, 2000); //Delay in millis
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    private void getUserFollowing() throws JSONException {
        following.add(ParseUser.getCurrentUser());
        user.queryUserFollowing(new FindCallback<ParseUser>() {
            @Override
            public void done(List objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting following users list to populate the feed fragment", e);
                }
                Log.i(TAG, "Got the followers successfully");
                following.addAll(objects);
                try {
                    queryPosts(0);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }

        });

    }

    private void queryPosts(final int page) throws JSONException {
        Post.query(page, DISPLAY_LIMIT, filterForUser, new FindCallback<Post>() {
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
                    adapter.clear();
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        }, following);
    }

    PostAdapter.onClickListener onClickListener = new PostAdapter.onClickListener() {
        @Override
        public void onProfilePicAction(int position) {
            //get user of that specific post
            ParseUser user = allPosts.get(position).getUser();

            //pass this info to profile fragment
            Fragment fragment = new ProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            fragment.setArguments(bundle);

            //Go from this fragment to profile fragment
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }


        @Override
        public void onLikeAction(int position) {
            // add like to post
            Post post = allPosts.get(position);
            post.addLike(ParseUser.getCurrentUser());
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Log.i(TAG, "Saved like successfully");
                }
            });
            adapter.notifyItemChanged(position);
        }

        @Override
        public void onUnlikeAction(int position) {
            Post post = allPosts.get(position);
            try {
                post.destroyLike(ParseUser.getCurrentUser());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Log.i(TAG, "DestroyedLike successfully");
                }
            });
            adapter.notifyItemChanged(position);
        }

        @Override
        public void onCommentAction(int position) {

        }
    };



}