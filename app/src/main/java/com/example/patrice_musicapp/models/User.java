package com.example.patrice_musicapp.models;

import android.util.Log;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Parcel
public class User {
    private ParseUser user;
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
    public static final String KEY_NAME = "name";

    public User() {
        user = ParseUser.getCurrentUser();
    }

    public String getUsername() { return user.getUsername(); }

    public void setUsername(String username) {user.setUsername(username);}

    public String getEmail() { return user.getEmail(); }

    public void setEmail(String email) {user.setEmail(email);}

    public ParseFile getImage() {
        return user.getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        user.put(KEY_IMAGE, image);
    }

    public String getBio() {
        return user.getString(KEY_BIO);
    }

    public void setBio(String bio) {
        user.put(KEY_BIO, bio);
    }

    public String getName(){return user.getString(KEY_NAME);}

    public void setName(String name) { user.put (KEY_BIO, name);}

    public int getPostCount() {
        return (int) user.getNumber(KEY_POST_COUNT);
    }

    public void setPostCount(int postCount) {
        user.put(KEY_POST_COUNT, postCount);
    }

    public ParseGeoPoint getLocation() {
        return user.getParseGeoPoint(KEY_LOCATION);
    }

    public void setLocation(ParseGeoPoint parseGeoPoint){
        user.put(KEY_LOCATION, parseGeoPoint);
    }

    public int getFollowersCount() {
        //length of the followersArray
        return user.getJSONArray(KEY_FOLLOWERS).length();
    }

    public int getFollowingCount() {
        return user.getJSONArray(KEY_FOLLOWING).length();
    }

    public List<String> getFollowingIds() throws JSONException {
        List<String> following = new ArrayList<>();
        //get the JSONArray of followers and make a list of users
        JSONArray jsonArray = user.getJSONArray(KEY_FOLLOWING);
        for (int i = 0; i < jsonArray.length(); i++){
            //get the IDs
            following.add(jsonArray.getString(i));
        }
        return following;
    }

    //takes in parameters of the person who the current user just followed
    public void addFollowing(ParseUser parseUser){
        //get JSONArray of Users and add the passed in user to the array
        JSONArray jsonArray = user.getJSONArray(KEY_FOLLOWING);
        if (jsonArray == null){
            jsonArray = new JSONArray();
        }
        jsonArray.put(parseUser.getObjectId());
    }

    public List<String> getFollowersIds() throws JSONException {
        List<String> followers = new ArrayList<>();
        //get the JSONArray of followers and make a list of users
        JSONArray jsonArray = user.getJSONArray(KEY_FOLLOWERS);
        for (int i = 0; i < jsonArray.length(); i++){
            followers.add(jsonArray.getString(i));
        }
        return followers;
    }

    //takes in parameters of the person who just followed the current user
    public void addFollowers(ParseUser parseUser){
        //get JSONArray of Users and add the passed in user to the array
        JSONArray jsonArray = user.getJSONArray(KEY_FOLLOWERS);
        if (jsonArray == null){
            jsonArray = new JSONArray();
        }
        jsonArray.put(parseUser.getObjectId());
    }


    public List<Genres> getGenres() throws JSONException {
       //get the JSONArray and parse through it to make a list
        List<Genres> genres = new ArrayList<>();
        JSONArray jsonArray = user.getJSONArray(KEY_GENRE);
        for (int i = 0 ; i < jsonArray.length(); i++){
             genres.add(Genres.valueOf(jsonArray.getString(i)));
        }
        return genres;

    }

    public void setGenre(Genres genre){
        //get the existing genre JSONArray and add a genre to the end of it
        JSONArray jsonArray = user.getJSONArray(KEY_GENRE);
        //check to see if null
        if (jsonArray == null){
            jsonArray = new JSONArray();
        }
        jsonArray.put(genre.toString());

    }

    public List<Instruments> getInstruments() throws JSONException {
        //get the JSONArray and parse through it to make a list
        List<Instruments> instruments = new ArrayList<>();
        JSONArray jsonArray = user.getJSONArray(KEY_INSTRUMENT);
        for (int i = 0 ; i < jsonArray.length(); i++){
            instruments.add(Instruments.valueOf(jsonArray.getString(i)));
        }
        return instruments;

    }

    public void setInstrument(Instruments instrument){
        //get the existing instrument JSONArray and add a genre to the end of it
        JSONArray jsonArray = user.getJSONArray(KEY_INSTRUMENT);
        //check to see if null
        if (jsonArray == null){
            jsonArray = new JSONArray();
        }
        jsonArray.put(instrument.toString());
    }

    public float getHourRate() {
        return (float) user.getNumber(KEY_HOUR_RATE);
    }

    public void setHourRate(float hourRate) {
        user.put(KEY_HOUR_RATE, hourRate);
    }

    public boolean getSoloArtist() {
        return user.getBoolean(KEY_SOLO_ARTIST);
    }

    public void setSoloArtist(boolean bool) {
        user.put(KEY_SOLO_ARTIST, bool);
    }

    public void save(){
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(User.class.getSimpleName(), "Issue with save", e);
                }
                Log.i(User.class.getSimpleName(), "Save complete");
            }
        });
    }




}