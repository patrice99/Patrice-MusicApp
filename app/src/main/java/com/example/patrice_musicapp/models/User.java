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
    public static final String KEY_FOLLOWING = "following";
    public static final String KEY_GENRE = "genre";
    public static final String KEY_INSTRUMENT = "instrument";
    public static final String KEY_HOUR_RATE = "hourRate";
    public static final String KEY_SOLO_ARTIST = "soloArtist";
    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_YOUTUBE = "youtube";
    public static final String KEY_IG_USERNAME = "igUsername";



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
        if (parseUser.getJSONArray(KEY_FOLLOWING) == null){
            return 0;
        }
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


    public List<String> getGenres() {
       return parseUser.getList(KEY_GENRE);

    }

    public void setGenre(List<String> genres){
        parseUser.put(KEY_GENRE, genres);
    }

    public List<String> getInstruments() {
        return parseUser.getList(KEY_INSTRUMENT);

    }

    //for onboarding fragment
    public void setInstrument(Instruments instrument){
        parseUser.addUnique(KEY_INSTRUMENT, instrument.toString());
    }

    public void setInstrumentList(List<String> instrumentList){
        parseUser.put(KEY_INSTRUMENT, instrumentList);
    }



    public void removeInstrument(Instruments instruments){
        //Loop through array looking for instrument string
        List<String> instrumentList = ParseUser.getCurrentUser().getList(KEY_INSTRUMENT);
        if (instrumentList != null && instrumentList.size()!= 0) {
            if (instrumentList.contains(instruments.toString())) {
                instrumentList.remove(instruments.toString());
            }
            parseUser.put(KEY_INSTRUMENT, instrumentList);
        }

    }

    public String getYoutubeUrl() {
        return parseUser.getString(KEY_YOUTUBE);
    }

    public void setYoutubeUrl(String url){
        parseUser.put(KEY_YOUTUBE, url);
    }

    public String getInstagramUsername() {
        return parseUser.getString(KEY_IG_USERNAME);
    }

    public void setInstagramUsername(String username) {
        parseUser.put(KEY_IG_USERNAME, username);

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

    public static void queryUsers(int limit, ParseUser filterForUser, FindCallback callback){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        User user = new User(filterForUser);
        try {
            if (user.getFollowingIds()!= null){
                query.whereNotContainedIn("objectId", user.getFollowingIds());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (filterForUser != null) {
            query.whereNotEqualTo(KEY_USERNAME, filterForUser.getUsername());
        }
        query.setLimit(limit);
        query.addDescendingOrder(KEY_POST_COUNT);
        query.findInBackground(callback);
    }

}