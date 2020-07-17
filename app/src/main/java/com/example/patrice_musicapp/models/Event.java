package com.example.patrice_musicapp.models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Event")
public class Event extends ParseObject {
    public static final String KEY_HOST= "host";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_DATE = "date";

    public ParseUser getHost() {
        return getParseUser(KEY_HOST);
    }

    public void setHost(ParseUser parseUser){
        put(KEY_HOST, parseUser);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name){
        put(KEY_NAME, name);
    }

    public String getDescription() {
        return getString(KEY_NAME);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(KEY_LOCATION);
    }

    public void setLocation(ParseGeoPoint location){
        put(KEY_LOCATION, location);

    }

    public Date getDate() {
        return getDate(KEY_DATE);
    }

    public void setDate(Date date){
        put(KEY_DATE, date);
    }



}
