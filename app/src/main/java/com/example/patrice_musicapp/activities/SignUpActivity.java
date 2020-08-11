package com.example.patrice_musicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.patrice_musicapp.databinding.ActivitySignUpBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = SignUpActivity.class.getSimpleName();
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick SignUp button");
                String username = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();
                String email = binding.etEmail.getText().toString();
                signUpUser(username, password, email);
            }
        });
    }

    private void signUpUser(String username, String password, String email) {
        Log.i(TAG, "Attempting to sign up user" + username);
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    goOnboarding();
                } else {
                    Log.e(TAG, "Sign Up Failed", e);
                }
            }
        });

    }

    protected void goOnboarding() {
        Intent intent = new Intent(SignUpActivity.this, OnboardingActivity.class);
        startActivity(intent);
        finish();
    }


}