package com.example.patrice_musicapp.models;


import android.location.Location;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_VIDEO = "video";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CAPTION = "caption";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_LIKES_COUNT = "likesCount";
    public static final String KEY_IS_NOTICE = "isNotice";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }


    public ParseFile getVideo() {
        return getParseFile(KEY_VIDEO);
    }

    public void setVideo(ParseFile video){
        put(KEY_VIDEO, video);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title){
        put(KEY_TITLE, title);
    }

    public String getCaption() {
        return getString(KEY_CAPTION);
    }

    public void setCaption(String caption){
        put(KEY_CAPTION, caption);
    }


    public String getTimeStamp() {
        Date date = getCreatedAt();
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy hh:mm aa");
        return formatter.format(date);
    }

    public int getLikesCount() {
        return (int) getNumber(KEY_LIKES_COUNT);
    }

    public void setLikesCount(int likesCount){
        put(KEY_LIKES_COUNT, likesCount);
    }

    public boolean getisNotice() {
        return getBoolean(KEY_IS_NOTICE);
    }

    public void setIsNotice(boolean bool){
        put(KEY_IS_NOTICE, bool);
    }

    public ParseGeoPoint getLocation(){
        return getParseGeoPoint(KEY_LOCATION);
    }

    public void setLocation(ParseGeoPoint parseGeoPoint){
        put(KEY_LOCATION, parseGeoPoint);
    }

    public static void query(int page, int limit, ParseUser filterForUser, FindCallback callback){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        if(filterForUser != null) {
            query.whereEqualTo(Post.KEY_USER, filterForUser);
        }
        query.setLimit(limit);
        query.setSkip(page * limit);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(callback);
    }

}
