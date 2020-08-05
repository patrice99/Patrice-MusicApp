package com.example.patrice_musicapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.core.content.FileProvider;

import com.example.patrice_musicapp.activities.ComposeActivity;

import java.io.File;
import java.io.IOException;

public class MediaUtil {
    public static String photoFilename = "photo.jpg";
    public static String videoFilename = "video.mp4";
    public static File photoFile;
    public static File videoFile;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public final static int PICK_PHOTO_CODE = 1046;
    public static final int VIDEO_CAPTURE = 101;
    public static Uri videoUri;






    public static void onLaunchCamera(Context context){
        //create an implicit intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFilename, context);

        // wrap File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(context, "com.musicApp.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            ((Activity)context).startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private static File getPhotoFileUri(String fileName, Context context) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), context.getClass().getSimpleName());

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(context.getClass().getSimpleName(), "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }



    //chosing photo from gallery
    public static void onChoosePhoto(Context context) {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFilename, context);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            // Bring up gallery to select a photo
            ((Activity) context).startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    public static Bitmap loadFromUri(Uri photoUri, Context context) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


    public static void startRecordingVideo(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            videoFile = getPhotoFileUri(videoFilename, context);

            // wrap File object into a content provider
            videoUri = FileProvider.getUriForFile(context, "com.musicApp.fileprovider", videoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);

            ((Activity) context).startActivityForResult(intent, VIDEO_CAPTURE);
        } else {
            Toast.makeText(context, "No camera on device", Toast.LENGTH_LONG).show();
        }
    }

    public static void playbackRecordedVideo(VideoView mVideoView, Context context) {
        mVideoView.setVideoURI(MediaUtil.videoUri);
        mVideoView.setMediaController(new MediaController(context));
        mVideoView.requestFocus();
        mVideoView.start();
    }

    public static void showSoundCloudPlayer(WebView webviewSoundCloud, String soundCloudUrl) {
        String SOUND_URL = soundCloudUrl;

        String html = "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> <link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" /></head> <body style=\"background:black;margin:0 0 0 0; padding:0 0 0 0;\"> <iframe id=\"sc-widget " +
                "\" width=\"100%\" height=\"166\"" + // Set Appropriate Width and Height that you want for SoundCloud Player
                " src=\"https://w.soundcloud.com/player/?url=" + SOUND_URL + "&color=%23ff5500&auto_play=false&hide_related=true&show_comments=false&show_user=true&show_reposts=false&show_teaser=false"
                + "\" frameborder=\"no\" scrolling=\"no\"></iframe>" +
                "<script src=\"https://w.soundcloud.com/player/?\" type=\"text/javascript\"></script> </body> </html> ";

        webviewSoundCloud.setVisibility(View.VISIBLE);
        webviewSoundCloud.getSettings().setJavaScriptEnabled(true);
        webviewSoundCloud.getSettings().setLoadWithOverviewMode(true);
        webviewSoundCloud.getSettings().setUseWideViewPort(true);
        webviewSoundCloud.loadDataWithBaseURL("",html,"text/html", "UTF-8", "");

    }







}
