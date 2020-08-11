package com.example.patrice_musicapp.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.patrice_musicapp.R;
import com.example.patrice_musicapp.adapters.ChatAdapter;
import com.example.patrice_musicapp.databinding.ActivityChatBinding;
import com.example.patrice_musicapp.models.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = ChatActivity.class.getSimpleName();
    private ParseUser sendingUser;
    private ParseUser receivingUser;

    private List<Message> messages;
    private ChatAdapter adapter;
    private boolean firstLoad;
    private ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar_chat);


        //get Intent extras
        receivingUser = getIntent().getParcelableExtra("user");
        sendingUser = ParseUser.getCurrentUser();

        toolbar.setBackgroundColor(getResources().getColor(R.color.grey));
        toolbar.setTitle("Chat with " + receivingUser.getUsername());
        toolbar.setTitleTextColor(getResources().getColor(R.color.light_pink));

        messages = new ArrayList<>();
        firstLoad = true;
        adapter = new ChatAdapter(ChatActivity.this, messages);
        binding.rvChat.setAdapter(adapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        binding.rvChat.setLayoutManager(linearLayoutManager);

        queryPosts();


        // When send button is clicked, create message object on Parse
        binding.btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = binding.etMessage.getText().toString();
                Message message = new Message();
                message.setMessageText(messageText);
                message.setSendingUser(sendingUser);
                message.setReceivingUser(receivingUser);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null) {
                            Log.e(TAG, "Failed to save message", e);
                        }
                    }
                });
                binding.etMessage.setText(null);
            }
        });

    }

    private void queryPosts() {
        Message.queryMessages(receivingUser, sendingUser, new FindCallback<Message>() {
            @Override
            public void done(List<Message> objects, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue with getting messages", e);
                }
                messages.clear();
                messages.addAll(objects);
                adapter.notifyDataSetChanged();

                if (firstLoad) {
                    binding.rvChat.scrollToPosition(messages.size()-1);
                    firstLoad = false;
                }
            }
        });

    }

    //poll for messages
    static final long POLL_INTERVAL = TimeUnit.SECONDS.toMillis(3);
    Handler myHandler = new android.os.Handler();
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            queryPosts();
            myHandler.postDelayed(this, POLL_INTERVAL);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        // Only start checking for new messages when the app becomes active in foreground
        myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
    }

    @Override
    protected void onPause() {
        // Stop background task from refreshing messages, to avoid unnecessary traffic & battery drain
        myHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }



}