package com.example.patrice_musicapp.models;


import android.location.Location;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    public static final String KEY_LIKES_ARRAY = "likesUserArray";
    public static final String TAG = Post.class.getSimpleName();
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

    public static void query(int page, int limit, ParseUser filterForUser, FindCallback callback, List<ParseUser> following){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        if(filterForUser != null) {
            query.whereEqualTo(Post.KEY_USER, filterForUser);
        } else{
            query.whereContainedIn(KEY_USER, following);
        }
        query.setLimit(limit);
        query.setSkip(page * limit);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(callback);
    }

    public void addLike(ParseUser parseUser){
        //increase like count
        put(KEY_LIKES_COUNT, getLikesCount()+ 1);
        addUnique(KEY_LIKES_ARRAY, parseUser);
    }

    public boolean isLiked(ParseUser parseUser) throws JSONException {
        JSONArray jsonArray = getJSONArray(KEY_LIKES_ARRAY);
        if (jsonArray!= null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String objectId = jsonObject.getString("objectId");
                if (objectId.equals(parseUser.getObjectId())) {
                    return true;
                }
            }
        }
        return false;
    }


    public void destroyLike(ParseUser parseUser) throws JSONException {
        //reduce count
        put(KEY_LIKES_COUNT, getLikesCount() - 1);
        //loop through user like array and delete user object
        JSONArray jsonArray = getJSONArray(KEY_LIKES_ARRAY);
        if (jsonArray!= null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String objectId = jsonObject.getString("objectId");
                if (objectId.equals(parseUser.getObjectId())) {
                    //delete that user object
                    jsonArray.remove(i);
                    put(KEY_LIKES_ARRAY, jsonArray);
                }
            }
        }
    }
}
