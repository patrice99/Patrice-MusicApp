package com.example.patrice_musicapp.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

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


    public static void queryMessages(ParseUser otherUser, ParseUser currentUser, FindCallback<Message> findCallback){
        // first AND condition
        ParseQuery<Message> querySentMessages = ParseQuery.getQuery(Message.class);
        querySentMessages.whereEqualTo(KEY_SENDING_USER, otherUser);
        querySentMessages.whereEqualTo(KEY_RECEIVING_USER, currentUser);

        //second AND condition
        ParseQuery<Message> queryReceivedMessages = ParseQuery.getQuery(Message.class);
        queryReceivedMessages.whereEqualTo(KEY_RECEIVING_USER, otherUser);
        queryReceivedMessages.whereEqualTo(KEY_SENDING_USER, currentUser);

        //all queries
        List<ParseQuery<Message>> queries = new ArrayList<ParseQuery<Message>>();
        queries.add(queryReceivedMessages);
        queries.add(querySentMessages);

        //OR condition
        ParseQuery<Message> mainQuery = ParseQuery.or(queries);
        mainQuery.include(KEY_RECEIVING_USER);
        mainQuery.include(KEY_SENDING_USER);
        mainQuery.addAscendingOrder("createdAt");
        mainQuery.findInBackground(findCallback);
    }
}
