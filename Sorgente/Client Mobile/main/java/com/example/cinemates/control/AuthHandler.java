package com.example.cinemates.control;

import android.widget.Toast;

import com.example.cinemates.activities.LogInActivity;
import com.example.cinemates.activities.MainActivity;
import com.example.cinemates.activities.RegistrationActivity;
import com.example.cinemates.fragments.ProfileFragment;
import com.example.cinemates.model.AppUser;
import com.example.cinemates.helper.StatisticsUpdates;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

@SuppressWarnings("All")
public class AuthHandler {

    public static final FirebaseAuth auth = FirebaseAuth.getInstance();
    public static final LoginManager loginManager = LoginManager.getInstance();
    public static final CallbackManager callbackManager = CallbackManager.Factory.create();

    private static AppUser currentUserInstance;



    public static void registerUser (RegistrationActivity registrationActivity,
                                     String nome, String cognome, String email, String password) {

        auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(registrationActivity, task -> {

            if (task.isSuccessful()) {
                Controller.onUserRegistrationSuccessful(registrationActivity, nome, cognome, email);
            }
            else {
                Controller.onUserRegistrationFailed(registrationActivity, task.getException());
            }

        });

    }

    public static void login (LogInActivity logInActivity, String email, String password) {

        logInActivity.getProgressDialog().show();

        auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(logInActivity, task -> {
            if (task.isSuccessful()) {
                StatisticsUpdates.updateAccesses(StatisticsUpdates.AccessType.IN);
                Controller.onLogInSuccessful(logInActivity);
            }
            else {
                Controller.onLogInFailed(logInActivity, task.getException());
            }
        });

    }
    public static void facebookLogin (LogInActivity logInActivity, AccessToken token) {

        logInActivity.getProgressDialog().show();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        auth.signInWithCredential(credential)
        .addOnCompleteListener(logInActivity, task -> {

            if (task.isSuccessful()) {
                StatisticsUpdates.updateAccesses(StatisticsUpdates.AccessType.IN);
                final boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                Controller.onFacebookLoginSuccessful(logInActivity, isNewUser);
            }
            else {
                Controller.onLogInFailed(logInActivity, task.getException());
            }

        });

    }
    public static void logout (MainActivity activity) {
        StatisticsUpdates.updateAccesses(StatisticsUpdates.AccessType.OUT);
        auth.signOut();
        loginManager.logOut();
        currentUserInstance = null;
        Controller.onLogout(activity);
    }

    public static void checkUserStatus (LogInActivity logInActivity) {

        if (auth.getCurrentUser() != null) {
            Controller.onLogInSuccessful(logInActivity);
        }

    }

    public static void changeUserPassword (ProfileFragment fragment, String newpassword) {
        auth.getCurrentUser().updatePassword(newpassword)
        .addOnSuccessListener(aVoid -> Toast.makeText(fragment.getActivity(), "Password modificata", Toast.LENGTH_SHORT).show())
        .addOnFailureListener(e -> {
            String message = null;
            if (e.getMessage().contains("requires recent authentication")) {
                message = "Devi aver effettuato il Log In di recente per poter cambiare la password";
            }
            Toast.makeText(fragment.getActivity(), message, Toast.LENGTH_SHORT).show();
        });
    }


    public static AppUser getCurrentUserInstance () {
        return currentUserInstance;
    }
    public static void setCurrentUserInstance (AppUser u) {
        currentUserInstance = u;
    }

}
