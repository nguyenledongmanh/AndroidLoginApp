package com.example.usermanagementproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.usermanagementproject.Modules.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    ImageView back_press;
    EditText edt_email, edt_password, edt_password_confirm, edt_phoneNumber, edt_fullName;
    Button btn_register;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        initUi();
        back_press.setOnClickListener(view -> finish()); //back to previous activity
        btn_register.setOnClickListener(view -> {
            String mEmail = edt_email.getText().toString().trim();
            String mPassword = edt_password.getText().toString().trim();
            String mPasswordConfirmation = edt_password_confirm.getText().toString().trim();
            String mFullName = edt_fullName.getText().toString().trim();
            String mPhone = edt_phoneNumber.getText().toString().trim();

            if (checkSignUp(mEmail, mPassword, mPasswordConfirmation) && !mFullName.isEmpty() && !mPhone.isEmpty()) {
                mAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                // set all static user's information to assign with UserConvert fields
                                Toast.makeText(this, "Successfully Register", Toast.LENGTH_SHORT).show();
                                User.full_name = mFullName;
                                User.user_image = R.mipmap.ic_launcher;
                                User.email = mEmail;
                                User.password = mPassword;
                                User.phone_number = mPhone;
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(this, "Make sure your information is identifiable !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean checkSignUp(String mEmail, String mPassword, String mPasswordConfirmation) {
        return checkEmail(mEmail) && checkPassword(mPassword, mPasswordConfirmation);
    }

    private boolean checkEmail(String mEmail) {
        if (mEmail.isEmpty()) {
            edt_email.setError("Field can't be empty.");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            edt_email.setError("Re-enter valid email value !");
            return false;
        }
        edt_email.setError(null);
        return true;
    }

    private boolean checkPassword(String mPassword, String mPasswordConfirmation) {
        if (mPassword.equals("")) {
            edt_password.setError("Field can't be empty");
            return false;
        }

        if (mPasswordConfirmation.equals("")) {
            edt_password_confirm.setError("Field can't be empty");
            return false;
        }

        if (!mPassword.equals(mPasswordConfirmation)) {
            edt_password_confirm.setError("Password Confirmation doesn't match!");
            return false;
        }

        if (mPassword.length() < 6) {
            edt_password.setError("Length of password is at least 6 characters");
            return false;
        }

        edt_password_confirm.setError(null);
        edt_password.setError(null);
        return true;
    }

    private void initUi() {
        back_press = findViewById(R.id.back_to_parent);
        edt_email = findViewById(R.id.emailInput_register);
        edt_password = findViewById(R.id.passwordInput_register);
        edt_password_confirm = findViewById(R.id.passwordInput_register_confirm);
        edt_phoneNumber = findViewById(R.id.phoneNumber);
        btn_register = findViewById(R.id.btn_register);
        edt_fullName = findViewById(R.id.fullName);
    }
}