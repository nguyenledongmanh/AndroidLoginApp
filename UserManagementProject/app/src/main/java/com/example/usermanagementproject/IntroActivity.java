package com.example.usermanagementproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IntroActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        loadData();
    }

    private void loadData() {
        new Handler().postDelayed(()-> {
            if (user == null) {
                // if user is not existed => move to Login
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                // user is existed but not existed on Firebase Auth => Log out and move to Login Activity
                String email = user.getEmail();
                assert email != null;
                mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean check = task.getResult().getSignInMethods().isEmpty();
                        if (check) {
                            mAuth.signOut();
                            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }
                });
            }
        }, 3000);
    }
}