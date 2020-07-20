package com.example.patrice_musicapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.ComposeActivity;
import com.example.patrice_musicapp.adapters.PostAdapter;
import com.example.patrice_musicapp.models.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
    public static final int DISPLAY_LIMIT = 20;
    public static final String TAG = FeedFragment.class.getSimpleName();
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private RecyclerView rvFeedPosts;
    private PostAdapter adapter;
    private List<Post> allPosts;
    private ParseUser filterForUser;


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

        allPosts = new ArrayList<>();
        //instantiate the adapter
        adapter = new PostAdapter(getContext(), allPosts, onClickListener);
        //set adapter on recycler view
        rvFeedPosts.setAdapter(adapter);
        //set layout manager on recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvFeedPosts.setLayoutManager(linearLayoutManager);

        //get the posts from the parse dashboard
        queryPosts(0);

    }

    private void queryPosts(final int page) {
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
        });
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
        public void onUsernameAction(int position) {

        }

        @Override
        public void onLikeAction(int position) {

        }

        @Override
        public void onCommentAction(int position) {

        }
    };


}