package com.example.patrice_musicapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.activities.PostDetailsActivity;
import com.example.patrice_musicapp.adapters.PostAdapter;
import com.example.patrice_musicapp.adapters.SearchAdapter;
import com.example.patrice_musicapp.adapters.UserAdapter;
import com.example.patrice_musicapp.models.Event;
import com.example.patrice_musicapp.models.Post;
import com.example.patrice_musicapp.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;

public class DiscoverFragment extends Fragment {
    public static final String TAG = DiscoverFragment.class.getSimpleName();
    private Toolbar toolbar;
    private SearchAdapter searchAdapter;
    private UserAdapter userAdapter;
    private List<Object> objects;
    private List<ParseUser> users; // all the users
    private List<ParseUser> usersToShow; //top 10 from priority queue
    private RecyclerView rvSearch;
    private RecyclerView rvUsers;
    private SearchView searchView;
    private PriorityQueue<User> pqGenres;
    private PriorityQueue<User> pqInstruments;
    private PriorityQueue<User> pqProximity;
    private String chipGenre;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        toolbar =view.findViewById(R.id.toolbar_discover);
        toolbar.setTitle("Discover");
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_baseline_sort_24));
        toolbar.getOverflowIcon().setTint(getResources().getColor(R.color.pink));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {int id = item.getItemId();
                switch (id) {
                    case R.id.genresButton:
                        sortByGenre();
                        break;
                    case R.id.instrumentsButton:
                        sortByInstruments();
                        break;
                    case R.id.nearMeButton:
                        sortByProximity();
                        break;
                    case R.id.activeUsers:
                        sortByPostCount();
                        break;
                }
                return true;
            }
        });


        objects = new ArrayList<>();
        rvSearch = view.findViewById(R.id.rvSearch);
        //set adapter on rvSearch
        searchAdapter = new SearchAdapter(getContext(), objects, clickListener);
        rvSearch.setAdapter(searchAdapter);

        //set layout manager on recycler view
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvSearch.setLayoutManager(linearLayoutManager);


        users = new ArrayList<>();
        usersToShow = new ArrayList<>();
        rvUsers = view.findViewById(R.id.rvUsers);
        userAdapter = new UserAdapter(getContext(), usersToShow, clickListenerUser);
        rvUsers.setAdapter(userAdapter);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvUsers.setLayoutManager(gridLayoutManager);
        rvUsers.setHasFixedSize(true);

        pqGenres = new PriorityQueue<User>(10, new UserGenreComparator());
        pqInstruments = new PriorityQueue<User>(10, new UserInstrumentComparator());
        pqProximity = new PriorityQueue<User>(10, new UserProximityComparator());

        if (searchAdapter.getItemCount() == 0){
            rvSearch.setAlpha(0);
            rvUsers.setAlpha(1);
            queryUsers();
        } else {
            rvUsers.setAlpha(0);
            rvSearch.setAlpha(1);
        }



        searchView = view.findViewById(R.id.searchView);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()!= 0) {
                    rvUsers.setAlpha(0);
                    rvUsers.setVisibility(View.GONE);
                    rvUsers.setLayoutManager(null);
                    rvUsers.setAdapter(null);
                    rvSearch.setAlpha(1);
                    rvSearch.setVisibility(View.VISIBLE);
                    rvSearch.setLayoutManager(linearLayoutManager);
                    rvSearch.setAdapter(searchAdapter);
                    filter(newText);
                } else {
                    rvSearch.setAlpha(0);
                    rvSearch.setVisibility(View.GONE);
                    rvSearch.setLayoutManager(null);
                    rvSearch.setAdapter(null);
                    rvUsers.setAlpha(1);
                    rvUsers.setVisibility(View.VISIBLE);
                    rvUsers.setLayoutManager(gridLayoutManager);
                    rvUsers.setAdapter(userAdapter);
                }
                return true;
            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.getString("chipGenre") != null) {
                chipGenre = bundle.getString("chipGenre");
                searchView.setQuery(chipGenre, false);
            }
        }



    }


    public void filter(String characterText) {
        characterText = characterText.toLowerCase(Locale.getDefault());
        searchAdapter.clear();
        if (characterText.length() != 0) {
            //search through users
            ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
            final String finalCharacterText = characterText;
            queryUser.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> allUsers, ParseException e) {
                    if (e!= null){
                        Log.e(TAG, "Issue with getting all users from Parse");
                    }
                    Log.i(TAG, "Got all users from parse Successfully");

                    for (ParseUser parseUser: allUsers) {
                        if (parseUser.getUsername().toLowerCase(Locale.getDefault()).startsWith(finalCharacterText)) {
                            objects.add(new User(parseUser));
                        }
                    }
                    searchAdapter.notifyDataSetChanged();
                }
            });

            //search through events
            ParseQuery<Event> queryEvent = ParseQuery.getQuery("Event");
            queryEvent.include("Host");
            queryEvent.findInBackground(new FindCallback<Event>() {
                @Override
                public void done(List<Event> events, ParseException e) {
                    if (e!= null){
                        Log.e(TAG, "Issue with getting all events from Parse");
                    }
                    Log.i(TAG, "Got all events from parse Successfully");

                    for  (Event event: events){
                        if (event.getName().toLowerCase(Locale.getDefault()).startsWith(finalCharacterText)) {
                            objects.add(event);
                        }
                    }
                    searchAdapter.notifyDataSetChanged();
                }
            });

            //search through posts
            ParseQuery<Post> queryPost = ParseQuery.getQuery("Post");
            queryPost.include("user");
            queryPost.findInBackground(new FindCallback<Post>() {
                @Override
                public void done(List<Post> posts, ParseException e) {
                    if (e!= null){
                        Log.e(TAG, "Issue with getting all posts from Parse");
                    }
                    Log.i(TAG, "Got all posts from parse Successfully");

                    for  (Post post: posts){
                        if (post.getGenreFilters()!=null) {
                            for (String genre : post.getGenreFilters()) {
                                if (genre.toLowerCase(Locale.getDefault()).startsWith(finalCharacterText)) {
                                    objects.add(post);
                                }
                            }
                        }
                    }

                    searchAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    SearchAdapter.onClickListener clickListener = new SearchAdapter.onClickListener() {
        @Override
        public void onEventClick(int position) {
            //get event position
            //get the post at that position
            Event event = (Event) objects.get(position);
            Log.i(TAG, "Event at Position " + position + "clicked.");
            //if any post clicked, take to the Maps Fragment and bring up a bottom sheet.
            //pass the event to maps for the bottom sheet
            // Create new fragment and transaction
            Fragment newFragment = new MapsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("event", event);
            newFragment.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.flContainer, newFragment);
            transaction.disallowAddToBackStack();
            // Commit the transaction
            transaction.commit();
        }

        @Override
        public void onUserClick(int position) {
            //go to profile Fragment
            //get user of that specific post
            User user = (User) objects.get(position);

            //pass this info to profile fragment
            Fragment fragment = new ProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user.getParseUser());
            fragment.setArguments(bundle);

            //Go from this fragment to profile fragment
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContainer, fragment);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();

        }

        @Override
        public void onPostClick(int position) {
            Post post = (Post) objects.get(position);
            //go to post details activity
            Log.i(PostAdapter.class.getSimpleName(), "Post at Position " + position + "clicked.");
            //if any post clicked, take to the PostDetailsActivity with the post
            Intent intent = new Intent(getContext(), PostDetailsActivity.class);
            //pass post into PostDetailsActivity
            intent.putExtra("post", post);
            intent.putExtra("user", post.getUser());

            getContext().startActivity(intent);
        }
    };

    UserAdapter.onClickListener clickListenerUser = new UserAdapter.onClickListener() {
        @Override
        public void onUserClick(int position) {
            //go to profile Fragment
            //get user of that specific post
            ParseUser user = usersToShow.get(position);

            //pass this info to profile fragment
            Fragment fragment = new ProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            fragment.setArguments(bundle);

            //Go from this fragment to profile fragment
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContainer, fragment);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        }
    };


    private void queryUsers() {
        User.queryUsers(35, ParseUser.getCurrentUser(), new FindCallback<ParseUser>(){
            @Override
            public void done(List<ParseUser> users2Follow, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues with getting users to follow", e);
                }
                userAdapter.clear();
                users.addAll(users2Follow);

                sortByProximity();
            }

        });

    }

    private void sortByGenre() {
        usersToShow.clear();
        pqGenres.clear();
            for (ParseUser user: users){
                User userpq = new User(user);
                pqGenres.add(userpq);
            }
            while (!pqGenres.isEmpty() && pqGenres.size() > (users.size() - 10)) {
                usersToShow.add((pqGenres.poll()).getParseUser());
            }

            userAdapter.notifyDataSetChanged();
    }


    private void sortByInstruments() {
        usersToShow.clear();
        pqInstruments.clear();
        for (ParseUser user: users){
            User userpq = new User(user);
            pqInstruments.add(userpq);
        }
        while (!pqInstruments.isEmpty() && pqInstruments.size() > (users.size() - 10)) {
            usersToShow.add((pqInstruments.poll()).getParseUser());
        }

        userAdapter.notifyDataSetChanged();
    }


    private void sortByPostCount() {
        usersToShow.clear();
        for (int i = 0 ; i < 10; i++){
            usersToShow.add(users.get(i));
        }

        userAdapter.notifyDataSetChanged();

    }

    private void sortByProximity(){
        usersToShow.clear();
        pqProximity.clear();
        for (ParseUser user: users){
            User userpq = new User(user);
            pqProximity.add(userpq);
        }
        while (!pqProximity.isEmpty() && pqProximity.size() > (users.size() - 10)) {
            usersToShow.add((pqProximity.poll()).getParseUser());
        }

        userAdapter.notifyDataSetChanged();

    }

    public class UserGenreComparator implements Comparator<User> {
        // Overriding compare()method of Comparator

        @Override
        public int compare(User user1, User user2) {
            User currentUser = new User(ParseUser.getCurrentUser());
            List<String> currentUserGenre = currentUser.getGenres();
            List<String> user1Genre = new ArrayList<>();
            if(user1.getGenres()!=null) {
                user1Genre.addAll(user1.getGenres());
            }
            List<String> user2Genre = new ArrayList<>();
            if(user2.getGenres() !=null) {
                user2Genre.addAll(user2.getGenres());
            }

            List<String> commonGenre1 = new ArrayList<>();
            commonGenre1.addAll(user1Genre);
            List<String> commonGenre2 = new ArrayList<>();
            commonGenre2.addAll(user2Genre);

            commonGenre1.retainAll(currentUserGenre);
            commonGenre2.retainAll(currentUserGenre);

            int matches1 = commonGenre1.size();
            int matches2 = commonGenre2.size();

            if (matches1 > matches2){
                return -1;
            } else if (matches2 > matches1) {
                return 1;
            }

            return 0;
        }
    }

    public class UserInstrumentComparator implements Comparator<User> {

        @Override
        public int compare(User user1, User user2) {
            User currentUser = new User(ParseUser.getCurrentUser());
            List<String> currentUserInstrument = currentUser.getInstruments();
            List<String> user1Instruments = new ArrayList<>();
            if(user1.getInstruments()!=null) {
                user1Instruments.addAll(user1.getInstruments());
            }
            List<String> user2Instuments = new ArrayList<>();
            if(user2.getInstruments() !=null) {
                user2Instuments.addAll(user2.getInstruments());
            }

            List<String> commonInstrument1 = new ArrayList<>();
            commonInstrument1.addAll(user1Instruments);
            List<String> commonInstrument2 = new ArrayList<>();
            commonInstrument2.addAll(user2Instuments);

            commonInstrument1.retainAll(currentUserInstrument);
            commonInstrument2.retainAll(currentUserInstrument);

            int matches1 = commonInstrument1.size();
            int matches2 = commonInstrument2.size();

            if (matches1 > matches2){
                return -1;
            } else if (matches2 > matches1) {
                return 1;
            }

            return 0;
        }
    }

    public class UserProximityComparator implements Comparator<User> {

        @Override
        public int compare(User user1, User user2) {
            User currentUser = new User(ParseUser.getCurrentUser());

            ParseGeoPoint currentUserLocation = new ParseGeoPoint();
            if (currentUser.getLocation()!=null) {
               currentUserLocation = currentUser.getLocation();
            }
            ParseGeoPoint user1Location = new ParseGeoPoint();
            if(user1.getLocation()!=null) {
               user1Location = user1.getLocation();
            }
            ParseGeoPoint user2Location = new ParseGeoPoint();
            if(user2.getLocation() !=null) {
                user2Location = user2.getLocation();
            }

            double distance1 = user1Location.distanceInMilesTo(currentUserLocation);
            double distance2 = user2Location.distanceInMilesTo(currentUserLocation);

            if (distance1 > distance2){
                return 1;
            } else if (distance2 > distance1) {
                return -1;
            }
            return 0;

        }
    }



}