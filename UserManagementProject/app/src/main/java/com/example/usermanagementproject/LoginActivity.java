package com.example.usermanagementproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usermanagementproject.Modules.User;
import com.example.usermanagementproject.Modules.UserConvert;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText edt_email, edt_password;
    Button btn_login, btn_facebook, btn_gmail;
    TextView tv_signUp;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        initUi();
    }

    private boolean checkLogin(String mEmail, String mPassword) {
        return validateEmail(mEmail) && validatePassword(mPassword);
    }

    private boolean validatePassword(String mPassword) {
        if (mPassword.isEmpty()) {
            edt_password.setError("Field can't be empty.");
            return false;
        }
        edt_password.setError(null);
        return true;
    }

    private boolean validateEmail(String mEmail) {
        if (mEmail.isEmpty()) {
            edt_email.setError("Field can't be empty.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            edt_email.setError("Re-enter valid email value !");
            return false;
        }
        edt_email.setError(null);
        return true;
    }

    private void initUi() {
        edt_email = findViewById(R.id.emailInput);
        edt_password = findViewById(R.id.passwordInput);
        btn_login = findViewById(R.id.btn_login);
        btn_facebook = findViewById(R.id.facebook);
        btn_gmail = findViewById(R.id.gmail);
        tv_signUp = findViewById(R.id.signUp);

        btn_login.setOnClickListener(view -> {
            String mEmail = edt_email.getText().toString().trim();
            String mPassword = edt_password.getText().toString().trim();
            if (checkLogin(mEmail, mPassword)) {
                mAuth.signInWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                            user = mAuth.getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            //if email is verified, push data into realtime DB
                            DatabaseReference rootRef = database.getReference();
                            DatabaseReference uId_manager = rootRef.child("managers").child(user.getUid());
                            ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        UserConvert userConvert = new UserConvert(User.user_image, User.full_name, User.email, User.phone_number, User.password);
                                        uId_manager.setValue(userConvert);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("TAG",error.getMessage());
                                }
                            };
                            uId_manager.addListenerForSingleValueEvent(eventListener);
                            // Move to MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Please check your email and verify your account!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        edt_password.setError("Either email or password is wrong!");
                        edt_email.setError("Either email or password is wrong!");
                    }
                });
            }
        });
        // Move to sign up activity
        tv_signUp.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}