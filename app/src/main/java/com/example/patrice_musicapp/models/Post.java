package com.example.patrice_musicapp.models;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.format.DateUtils;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_VIDEO = "video";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CAPTION = "caption";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_LIKES_COUNT = "likesCount";
    public static final String KEY_LIKES_ARRAY = "likesUserArray";
    public static final String KEY_SOUND_CLOUD_URL = "soundCloudUrl";
    public static final String KEY_GENRE_FILTER= "genreFilter";

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

    public String getRelativeTimeAgo(String rawDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        return relativeDate;
    }

    public int getLikesCount() {
        return (int) getNumber(KEY_LIKES_COUNT);
    }


    public ParseGeoPoint getLocation(){
        return getParseGeoPoint(KEY_LOCATION);
    }

    public void setLocation(ParseGeoPoint parseGeoPoint){
        put(KEY_LOCATION, parseGeoPoint);
    }

    public String getSoundCloudUrl(){
        return getString(KEY_SOUND_CLOUD_URL);
    }

    public void setSoundCloudUrl(String soundCloudUrl) {
        put(KEY_SOUND_CLOUD_URL, soundCloudUrl);
    }

    public List<String> getGenreFilters(){
        return getList(KEY_GENRE_FILTER);
    }

    public void setGenreFilter(List<String> genreFilters){
        put (KEY_GENRE_FILTER, genreFilters);
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

}
