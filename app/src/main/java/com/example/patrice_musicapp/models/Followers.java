package com.example.patrice_musicapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
@ParseClassName("Followers")
public class Followers extends ParseObject {
    public static final String KEY_SUBJECT_USER = "subjectUser";
    public static final String KEY_SUBJECT_FOLLOWERS = "followers";

    public static String getSubjectUser() {
        return KEY_SUBJECT_USER;
    }

    public static String getKeySubjectFollowers() {
        return KEY_SUBJECT_FOLLOWERS;
    }


}
