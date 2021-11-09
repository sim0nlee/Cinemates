package com.example.cinemates.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.cinemates.control.AuthHandler;
import com.example.cinemates.helper.InputChecker;
import com.example.cinemates.R;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = LogInActivity.class.getSimpleName();

    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setProgressDialog();
        setFacebookLoginButton();
        setLoginButton();
        setRegisterBtn();
    }

    @Override
    public void onStart() {
        super.onStart();
        AuthHandler.checkUserStatus(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AuthHandler.callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void setProgressDialog () {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Effettuando il Log In...");
    }
    private void setFacebookLoginButton () {
        LoginButton mFacebookLoginButton = findViewById(R.id.facebook_login_button);
        mFacebookLoginButton.setReadPermissions("email", "public_profile");
        mFacebookLoginButton.registerCallback(AuthHandler.callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AuthHandler.facebookLogin(LogInActivity.this, loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }
    private void setLoginButton() {

        Button mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(v -> {

            emailEditText = findViewById(R.id.email_login_input);
            passwordEditText = findViewById(R.id.password_login_input);

            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();

            if (InputChecker.isEmail(emailEditText, email)) {
                if (!password.isEmpty())
                    AuthHandler.login(LogInActivity.this, email, password);
                else {
                    passwordEditText.setError("Inserisci la password");
                }
            }

        });

    }
    private void setRegisterBtn () {
        Button mRegisterButton = findViewById(R.id.goto_registration_button);
        mRegisterButton.setOnClickListener(v -> startActivity(new Intent(LogInActivity.this, RegistrationActivity.class)));
    }

    public ProgressDialog getProgressDialog () {
        return progressDialog;
    }

}