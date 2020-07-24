package com.example.patrice_musicapp.models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

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


    public void addFollower(ParseUser parseUser, Followers follow){
        //add follower to the follower array
        put(KEY_SUBJECT_USER, parseUser);
        put(KEY_FOLLOWER, follow);

    }

    public List<User>getFollowers(ParseUser subjectUser){
        return null;
    }


    public void deleteFollower(ParseUser parseUser, Followers follow) {
    }
}
