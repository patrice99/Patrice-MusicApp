package com.example.patrice_musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.models.Message;
import com.example.patrice_musicapp.models.User;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

public class ChatActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "userId";
    private static final String BODY_KEY = "body";
    private static final String TAG = ChatActivity.class.getSimpleName();
    private ParseUser sendingUser;
    private ParseUser receivingUser;

    private EditText etMessage;
    private ImageButton btSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etMessage = findViewById(R.id.etMessage);
        btSend = findViewById(R.id.btSend);

        //get Intent extras
        receivingUser = getIntent().getParcelableExtra("user");
        sendingUser = ParseUser.getCurrentUser();

        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = etMessage.getText().toString();
                Message message = new Message();
                message.setMessageText(messageText);
                message.setSendingUser(sendingUser);
                message.setReceivingUser(receivingUser);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Failed to save message", e);
                        }
                    }
                });
                etMessage.setText(null);
            }
        });

    }


    // Get the userId from the cached currentUser object
    private void startWithCurrentUser() {
        setupMessagePosting();
    }

    // Setup button event handler which posts the entered message to Parse
    private void setupMessagePosting() {


    }
}