package com.example.patrice_musicapp;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.example.patrice_musicapp.models.Portfolio;
import com.example.patrice_musicapp.models.Post;
import com.example.patrice_musicapp.models.User;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        //register m model classes
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Portfolio.class);
        ParseObject.registerSubclass(User.class);


        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("patrice-music-app") // should correspond to APP_ID env variable
                .clientKey(getResources().getString(R.string.MASTER_KEY))  // set explicitly unless clientKey is explicitly configured on Parse server
                .clientBuilder(builder)
                .server("https://patrice-music-app.herokuapp.com/parse").build());
        
    }
}
