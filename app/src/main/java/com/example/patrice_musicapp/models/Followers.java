package com.example.patrice_musicapp.models;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Followers")
public class Followers extends ParseObject {
    public static final String KEY_SUBJECT_USER = "subjectUser";
    public static final String KEY_FOLLOWER = "follower";

    public ParseUser getSubjectUser() {
        return getParseUser(KEY_SUBJECT_USER);
    }

    public void setSubjectUser(ParseUser parseUser){
        put(KEY_SUBJECT_USER, parseUser);
    }

    public ParseUser getFollower() {
        return getParseUser(KEY_FOLLOWER);
    }


    public void addFollower(ParseUser user2follow, ParseUser subjectUser){
        //add follower to the follower array
        put(KEY_SUBJECT_USER, user2follow);
        put(KEY_FOLLOWER, subjectUser);

    }

    public static void getFollowers(ParseUser subjectUser, FindCallback callback){
        final List<Followers> follows = new ArrayList<>();
        final List<ParseUser> followers = new ArrayList<>();
        //query followers
        ParseQuery<Followers> query = ParseQuery.getQuery(Followers.class);
        query.whereEqualTo("subjectUser", subjectUser);
        query.include(KEY_FOLLOWER);
        // execute the query
        query.findInBackground(callback);
    }

}
