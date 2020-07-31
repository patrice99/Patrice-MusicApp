package com.example.patrice_musicapp.models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcelable;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
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
    public static final String TAG = User.class.getSimpleName();
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

    public int getFollowingCount() {
        return parseUser.getJSONArray(KEY_FOLLOWING).length();
    }

    //takes in parameters of the person who the current user just followed
    public void addFollowing(User user){
        ParseUser.getCurrentUser().addUnique(KEY_FOLLOWING, user.getParseUser());
    }

    public void deleteFollowing(User user) throws JSONException {
        //loop through currentUser following array and delete user object
        JSONArray jsonArray = ParseUser.getCurrentUser().getJSONArray(KEY_FOLLOWING);
        if (jsonArray!= null && jsonArray.length()!=0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String objectId = jsonObject.getString("objectId");
                if (objectId.equals(user.getParseUser().getObjectId())) {
                    //delete that user object from current user's following JsonArray
                    jsonArray.remove(i);
                    ParseUser.getCurrentUser().put(KEY_FOLLOWING, jsonArray);
                }
            }
        }

    }

    public List<String> getFollowingIds() throws JSONException {
        final List<String> followingIds = new ArrayList<>();
        JSONArray jsonArray = ParseUser.getCurrentUser().getJSONArray(KEY_FOLLOWING);
        if (jsonArray!= null && jsonArray.length()!=0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String objectId = jsonObject.getString("objectId");
                followingIds.add(objectId);
            }
        }

        return followingIds ;
    }

    public void queryUserFollowing(FindCallback callback) throws JSONException {
        List<String> userIds = getFollowingIds();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn("objectId", userIds);
        query.findInBackground(callback);
    }


    public List<String> getGenres() throws JSONException {
       //get the JSONArray and parse through it to make a list
        List<String> genres = new ArrayList<>();
        JSONArray jsonArray = parseUser.getJSONArray(KEY_GENRE);
        for (int i = 0 ; i < jsonArray.length(); i++){
             genres.add(jsonArray.getString(i));
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

    public List<String> getInstruments() throws JSONException {
        return getParseUser().getList(KEY_INSTRUMENT);

    }

    public void setInstrument(Instruments instrument){
        ParseUser.getCurrentUser().addUnique(KEY_INSTRUMENT, instrument.toString());
    }

    public void removeInstrument(Instruments instruments){
        //Loop through array looking for instrument string
        List<String> instrumentList = ParseUser.getCurrentUser().getList(KEY_INSTRUMENT);
        if (instrumentList != null && instrumentList.size()!= 0) {
            if (instrumentList.contains(instruments.toString())) {
                instrumentList.remove(instruments.toString());
            }
            ParseUser.getCurrentUser().put(KEY_INSTRUMENT, instrumentList);
        }

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
        if (jsonArray!= null && jsonArray.length() != 0) {
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