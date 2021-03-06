package com.example.patrice_musicapp.utils;

import android.app.Application;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.Event;
import com.example.patrice_musicapp.models.Followers;
import com.example.patrice_musicapp.models.Message;
import com.example.patrice_musicapp.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

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
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(Followers.class);
        ParseObject.registerSubclass(Message.class);

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
