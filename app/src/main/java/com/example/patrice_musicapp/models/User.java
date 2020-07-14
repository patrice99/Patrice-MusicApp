package com.example.patrice_musicapp.models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@ParseClassName("User")
public class User extends ParseUser {
    public static final String KEY_IMAGE = "image";
    public static final String KEY_BIO = "bio";
    public static final String KEY_POST_COUNT = "postCount";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_FOLLOWERS = "followers";
    public static final String KEY_FOLLOWING = "following";
    public static final String KEY_GENRE = "genre";
    public static final String KEY_INSTRUMENT = "instrument";
    public static final String KEY_HOUR_RATE = "hourRate";
    public static final String KEY_SOLO_ARTIST = "soloArtist";

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public String getBio() {
        return getString(KEY_BIO);
    }

    public void setBio(String bio) {
        put(KEY_BIO, bio);
    }

    public int getPostCount() {
        return (int) getNumber(KEY_POST_COUNT);

    }

    public void setPostCount(int postCount) {
        put(KEY_POST_COUNT, postCount);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(KEY_LOCATION);
    }

    public void setLocation(ParseGeoPoint parseGeoPoint){
        put(KEY_LOCATION, parseGeoPoint);
    }

    public int getFollowersCount() {
        //length of the followersArray
        return getJSONArray(KEY_FOLLOWERS).length();
    }

    public int getFollowingCount() {
        return getJSONArray(KEY_FOLLOWING).length();
    }

    public List<String> getFollowingIds() throws JSONException {
        List<String> following = new ArrayList<>();
        //get the JSONArray of followers and make a list of users
        JSONArray jsonArray = getJSONArray(KEY_FOLLOWING);
        for (int i = 0; i < jsonArray.length(); i++){
            //get the IDs
            following.add(jsonArray.getString(i));
        }
        return following;
    }

    //takes in parameters of the person who the current user just followed
    public void addFollowing(ParseUser parseUser){
        //get JSONArray of Users and add the passed in user to the array
        JSONArray jsonArray = getJSONArray(KEY_FOLLOWING);
        if (jsonArray == null){
            jsonArray = new JSONArray();
        }
        jsonArray.put(parseUser.getObjectId());
    }

    public List<String> getFollowersIds() throws JSONException {
        List<String> followers = new ArrayList<>();
        //get the JSONArray of followers and make a list of users
        JSONArray jsonArray = getJSONArray(KEY_FOLLOWERS);
        for (int i = 0; i < jsonArray.length(); i++){
            followers.add(jsonArray.getString(i));
        }
        return followers;
    }

    //takes in parameters of the person who just followed the current user
    public void addFollowers(ParseUser parseUser){
        //get JSONArray of Users and add the passed in user to the array
        JSONArray jsonArray = getJSONArray(KEY_FOLLOWERS);
        if (jsonArray == null){
            jsonArray = new JSONArray();
        }
        jsonArray.put(parseUser.getObjectId());
    }


    public List<Genres> getGenres() throws JSONException {
       //get the JSONArray and parse through it to make a list
        List<Genres> genres = new ArrayList<>();
        JSONArray jsonArray = getJSONArray(KEY_GENRE);
        for (int i = 0 ; i < jsonArray.length(); i++){
             genres.add(Genres.valueOf(jsonArray.getString(i)));
        }
        return genres;

    }

    public void setGenre(Genres genre){
        //get the existing genre JSONArray and add a genre to the end of it
        JSONArray jsonArray = getJSONArray(KEY_GENRE);
        //check to see if null
        if (jsonArray == null){
            jsonArray = new JSONArray();
        }
        jsonArray.put(genre.toString());

    }

    public List<Instruments> getInstruments() throws JSONException {
        //get the JSONArray and parse through it to make a list
        List<Instruments> instruments = new ArrayList<>();
        JSONArray jsonArray = getJSONArray(KEY_INSTRUMENT);
        for (int i = 0 ; i < jsonArray.length(); i++){
            instruments.add(Instruments.valueOf(jsonArray.getString(i)));
        }
        return instruments;

    }

    public void setInstrument(Instruments instrument){
        //get the existing instrument JSONArray and add a genre to the end of it
        JSONArray jsonArray = getJSONArray(KEY_INSTRUMENT);
        //check to see if null
        if (jsonArray == null){
            jsonArray = new JSONArray();
        }
        jsonArray.put(instrument.toString());
    }

    public float getHourRate() {
        return (float) getNumber(KEY_HOUR_RATE);
    }

    public void setHourRate(float hourRate) {
        put(KEY_HOUR_RATE, hourRate);
    }

    public boolean getSoloArtist() {
        return getBoolean(KEY_SOLO_ARTIST);
    }

    public void setSoloArtist(boolean bool) {
        put(KEY_SOLO_ARTIST, bool);
    }




}