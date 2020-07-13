package com.example.patrice_musicapp.models;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class User extends ParseObject {
    public static final String KEY_IMAGE = "image";
    public static final String KEY_BIO = "bio";
    public static final String KEY_POST_COUNT = "postCount";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_FOLLOWER_COUNT = "followerCount";
    public static final String KEY_FOLLOWING_COUNT= "followingCount";
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
        return (int) getNumber(KEY_FOLLOWER_COUNT);
    }

    public int getFollowingCount() {
        return (int) getNumber(KEY_FOLLOWING_COUNT);
    }

    public ParseUser[] getFollowers(){
        //TODO: initialize followers Array
        return (ParseUser[]) get(KEY_FOLLOWERS);
    }

    public void setFollowers(ParseUser[] followers){
        put(KEY_FOLLOWERS, followers);
    }

    public ParseUser[] getFollowing(){
        return (ParseUser[]) get(KEY_FOLLOWING);
    }

    public void setFollowing(ParseUser[] following){
        put(KEY_FOLLOWING, following);
    }

    public String[] getGenre(){
        return (String[]) get(KEY_GENRE);
    }

    public void setGenre(){
        //TODO: pass in enumerated genre type
    }

    public String[] getInstrument(){
        return (String[]) get(KEY_INSTRUMENT);
    }

    public void setInstrument(){
        //TODO: pass in enumerated instrument type
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