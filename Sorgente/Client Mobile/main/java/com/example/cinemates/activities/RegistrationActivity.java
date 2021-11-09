package com.example.cinemates.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.cinemates.control.AuthHandler;
import com.example.cinemates.helper.InputChecker;
import com.example.cinemates.R;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = RegistrationActivity.class.getSimpleName();

    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setProgressDialog();
        setRegisterButton();
        setBackToLoginButton();
    }


    private void setProgressDialog () {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Effettuando la registrazione...");
    }
    private void setRegisterButton () {
        Button RegisterButton = findViewById(R.id.goto_registration_button);
        RegisterButton.setClickable(false);
        RegisterButton.setOnClickListener(v -> {

            progressDialog.show();

            nameEditText = findViewById(R.id.nome_registrazione_input);
            surnameEditText = findViewById(R.id.cognome_registrazione_input);
            emailEditText = findViewById(R.id.email_registrazione_input);
            passwordEditText = findViewById(R.id.password_registrazione_input);

            String name = nameEditText.getText().toString().toLowerCase().trim();
            String surname = surnameEditText.getText().toString().toLowerCase().trim();
            String email = emailEditText.getText().toString().toLowerCase().trim();
            String password = passwordEditText.getText().toString();

            boolean exit = false;
            if (!InputChecker.isName(nameEditText, name)) {
                exit = true;
            }
            if (!InputChecker.isName(surnameEditText, surname)) {
                exit = true;
            }
            if (!InputChecker.isEmail(emailEditText, email)) {
                exit = true;
            }
            if (!InputChecker.isPassword(passwordEditText, password)) {
                exit = true;
            }
            if (!exit) {
                AuthHandler.registerUser(RegistrationActivity.this, name, surname, email, password);
            }
            else {
                progressDialog.dismiss();
            }

        });
    }
    private void setBackToLoginButton () {
        Button BackToLoginButton = findViewById(R.id.torna_al_login_btn);
        BackToLoginButton.setOnClickListener(v -> {
            startActivity(new Intent(RegistrationActivity.this, LogInActivity.class));
            finishAffinity();
        });
    }


    public ProgressDialog getProgressDialog () {
        return progressDialog;
    }
}

