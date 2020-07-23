package com.example.patrice_musicapp.models;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.example.patrice_musicapp.activities.EditProfileActivity;
import com.google.android.gms.maps.MapFragment;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@ParseClassName("Event")
public class Event extends ParseObject {
    public static final String KEY_HOST= "Host";
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
        return getString(KEY_DESCRIPTION);
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

    public static void query(int page, int limit, FindCallback callback){
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.include(Event.KEY_HOST);
        query.setLimit(limit);
        query.setSkip(page * limit);
        query.addDescendingOrder(Event.KEY_CREATED_AT);
        query.findInBackground(callback);
    }

    public static ParseGeoPoint getLocationFromString(String location, Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.US);
        List<Address> addresses = geocoder.getFromLocationName(location, 5);
        Address address = addresses.get(0); //get the first address for right now

        return new ParseGeoPoint(address.getLatitude(), address.getLongitude());

    }

    public static String getStringFromLocation(ParseGeoPoint parseGeoPoint, Context context, String fromWhere) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.US);
        List<Address> addresses = geocoder.getFromLocation(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude(), 5);
        Address address = addresses.get(0); //get the first address for right now

        if (fromWhere == MapFragment.class.getSimpleName()){
            return address.getAddressLine(0);
        }
        return address.getFeatureName();
    }



}
