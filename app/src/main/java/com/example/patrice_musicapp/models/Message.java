package com.example.patrice_musicapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String KEY_SENDING_USER= "sendingUser";
    public static final String KEY_RECEIVING_USER = "receivingUser";
    public static final String KEY_MESSAGE_TEXT = "messageText";


    public ParseUser getSendingUser() {
        return getParseUser(KEY_SENDING_USER);
    }

    public void setSendingUser(ParseUser sendingUser){
        put(KEY_SENDING_USER, sendingUser);
    }

    public ParseUser getReceivingUser() {
        return getParseUser(KEY_RECEIVING_USER);
    }

    public void setReceivingUser(ParseUser receivingUser){
        put(KEY_RECEIVING_USER, receivingUser);
    }

    public String getMessageText() {
        return getString(KEY_MESSAGE_TEXT);
    }

    public void setMessageText(String messageText) {
        put(KEY_MESSAGE_TEXT, messageText);
    }
}
