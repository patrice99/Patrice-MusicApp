package com.example.patrice_musicapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class SocialsUtils {
    public static String soundCloudUrl;

    public static void popUpEditText(Context context, final int indicator, final User user) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogueView = inflater.inflate(R.layout.custom_dialogue, null);
        final TextInputEditText input = dialogueView.findViewById(R.id.etInput);

        if (indicator == 0) {
            if (user.getYoutubeUrl() == null) {
                input.setText(context.getResources().getString(R.string.youtube_start_url));
            } else {
                input.setText(user.getYoutubeUrl());
            }
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Enter your YouTube Channel URL")
                    .setView(dialogueView)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            user.setYoutubeUrl(input.getText().toString());
                        }
                    })
                    .show();
        } else if (indicator == 1){
            if (user.getInstagramUsername() == null) {
                input.setText("");
            } else {
                input.setText(user.getInstagramUsername());
            }
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Enter your Instagram Username")
                    .setView(dialogueView)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            user.setInstagramUsername(input.getText().toString());
                        }
                    })
                    .show();
        } else if (indicator == 2){
            input.setText("");
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Enter the URL of your SoundCloud song")
                    .setView(dialogueView)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            soundCloudUrl = input.getText().toString();
                        }
                    })
                    .show();

        }
    }
}
