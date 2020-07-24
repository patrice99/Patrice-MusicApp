package com.example.patrice_musicapp.models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcelable;
import android.util.Log;

import com.example.patrice_musicapp.activities.EditProfileActivity;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class User {
    public ParseUser parseUser;
    public static final String KEY_PROFILE_IMAGE = "profileImage";
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
        parseUser = ParseUser.getCurrentUser();
    }

    public User(Parcelable user){
        parseUser = (ParseUser) user;
    }

    public ParseUser getParseUser(){
        return parseUser;
    }

    public String getUsername() { return parseUser.getUsername(); }

    public void setUsername(String username) {
        parseUser.setUsername(username);}

    public String getEmail() { return parseUser.getEmail(); }

    public void setEmail(String email) {
        parseUser.setEmail(email);}

    public ParseFile getImage() {
        return parseUser.getParseFile(KEY_PROFILE_IMAGE);
    }

    public void setImage(ParseFile image) {
        parseUser.put(KEY_PROFILE_IMAGE, image);
    }

    public String getBio() {
        return parseUser.getString(KEY_BIO);
    }

    public void setBio(String bio) {
        parseUser.put(KEY_BIO, bio);
    }

    public String getName(){return parseUser.getString(KEY_NAME);}

    public void setName(String name) { parseUser.put(KEY_NAME, name);}

    public int getPostCount() {
        return (int) parseUser.getNumber(KEY_POST_COUNT);
    }

    public void setPostCount(int postCount) {
        parseUser.put(KEY_POST_COUNT, postCount);
    }

    public ParseGeoPoint getLocation() {
        return parseUser.getParseGeoPoint(KEY_LOCATION);
    }

    public void setLocation(ParseGeoPoint parseGeoPoint){
        parseUser.put(KEY_LOCATION, parseGeoPoint);
    }

    public int getFollowersCount() {
        //length of the followersArray
        return parseUser.getJSONArray(KEY_FOLLOWERS).length();
    }

    public int getFollowingCount() {
        return parseUser.getJSONArray(KEY_FOLLOWING).length();
    }

    public List<String> getFollowingIds() throws JSONException {
        List<String> following = new ArrayList<>();
        //get the JSONArray of followers and make a list of users
        JSONArray jsonArray = parseUser.getJSONArray(KEY_FOLLOWING);
        for (int i = 0; i < jsonArray.length(); i++){
            //get the IDs
            following.add(jsonArray.getString(i));
        }
        return following;
    }

    //takes in parameters of the person who the current user just followed
    public void addFollowing(User user){
        ParseUser.getCurrentUser().addUnique(KEY_FOLLOWING, user.getParseUser());
    }

    public void deleteFollowing(User user) throws JSONException {
        //loop through user like array and delete user object
        JSONArray jsonArray = ParseUser.getCurrentUser().getJSONArray(KEY_FOLLOWING);
        if (jsonArray!= null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String objectId = jsonObject.getString("objectId");
                if (objectId.equals(user.getParseUser().getObjectId())) {
                    //delete that user object
                    jsonArray.remove(i);
                    ParseUser.getCurrentUser().put(KEY_FOLLOWING, jsonArray);
                }
            }
        }

    }

    public List<String> getFollowersIds() throws JSONException {
        List<String> followers = new ArrayList<>();
        //get the JSONArray of followers and make a list of users
        JSONArray jsonArray = parseUser.getJSONArray(KEY_FOLLOWERS);
        for (int i = 0; i < jsonArray.length(); i++){
            followers.add(jsonArray.getString(i));
        }
        return followers;
    }

    //takes in parameters of the person who just followed the current user
    public void addFollowers(ParseUser parseUser){
        //get JSONArray of Users and add the passed in user to the array
        JSONArray jsonArray = this.parseUser.getJSONArray(KEY_FOLLOWERS);
        if (jsonArray == null){
            jsonArray = new JSONArray();
        }
        jsonArray.put(parseUser.getObjectId());
    }


    public List<Genres> getGenres() throws JSONException {
       //get the JSONArray and parse through it to make a list
        List<Genres> genres = new ArrayList<>();
        JSONArray jsonArray = parseUser.getJSONArray(KEY_GENRE);
        for (int i = 0 ; i < jsonArray.length(); i++){
             genres.add(Genres.valueOf(jsonArray.getString(i)));
        }
        return genres;

    }

    public void setGenre(Genres genre){
        //get the existing genre JSONArray and add a genre to the end of it
        JSONArray jsonArray = parseUser.getJSONArray(KEY_GENRE);
        //check to see if null
        if (jsonArray == null){
            jsonArray = new JSONArray();
        }
        jsonArray.put(genre.toString());

    }

    public List<Instruments> getInstruments() throws JSONException {
        //get the JSONArray and parse through it to make a list
        List<Instruments> instruments = new ArrayList<>();
        JSONArray jsonArray = parseUser.getJSONArray(KEY_INSTRUMENT);
        for (int i = 0 ; i < jsonArray.length(); i++){
            instruments.add(Instruments.valueOf(jsonArray.getString(i)));
        }
        return instruments;

    }

    public void setInstrument(Instruments instrument){
        //get the existing instrument JSONArray and add a genre to the end of it
        JSONArray jsonArray = parseUser.getJSONArray(KEY_INSTRUMENT);
        //check to see if null
        if (jsonArray == null){
            jsonArray = new JSONArray();
        }
        jsonArray.put(instrument.toString());
    }

    public float getHourRate() {
        return (float) parseUser.getNumber(KEY_HOUR_RATE);
    }

    public void setHourRate(float hourRate) {
        parseUser.put(KEY_HOUR_RATE, hourRate);
    }

    public boolean getSoloArtist() {
        return parseUser.getBoolean(KEY_SOLO_ARTIST);
    }

    public void setSoloArtist(boolean bool) {
        parseUser.put(KEY_SOLO_ARTIST, bool);
    }

    public void save(){
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(User.class.getSimpleName(), "Issue with save", e);
                }
                Log.i(User.class.getSimpleName(), "Save complete");
            }
        });
    }


    public static ParseGeoPoint getLocationFromString(String location, Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.US);
        List<Address> addresses = geocoder.getFromLocationName(location, 5);
        Address address = addresses.get(0); //get the first address for right now

        return new ParseGeoPoint(address.getLatitude(), address.getLongitude());

    }

    public static String getStringFromLocation(ParseGeoPoint parseGeoPoint, Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.US);
        List<Address> addresses = geocoder.getFromLocation(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude(), 5);
        Address address = addresses.get(0); //get the first address for right now
        return address.getLocality();
    }


    public boolean isFollowed(User following) throws JSONException {
        JSONArray jsonArray = ParseUser.getCurrentUser().getJSONArray(KEY_FOLLOWING);
        if (jsonArray!= null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String objectId = jsonObject.getString("objectId");
                if (objectId.equals(following.getParseUser().getObjectId())) {
                    return true;
                }
            }
        }
        return false;
    }
}