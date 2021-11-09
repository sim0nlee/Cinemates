package com.example.cinemates.data;

import com.example.cinemates.control.AuthHandler;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreReferences {

    private static final String USERS_COLLECTION = "utenti";

    private static final String USER_FOLLOWINGS_COLLECTION = "seguiti";

    private static final String USER_REQUESTS_COLLECTION = "richieste";

    private static final String USER_FAVORITES_COLLECTION = "preferiti";

    private static final String USER_SAVED_COLLECTION = "salvati";



    public static DocumentReference getUserReference (String uid) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        return  database
                .collection(USERS_COLLECTION)
                .document(uid);

    }

    public static CollectionReference getUserFollowingsReference () {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        return database
                .collection(USERS_COLLECTION)
                .document(AuthHandler.getCurrentUserInstance().getUid())
                .collection(USER_FOLLOWINGS_COLLECTION);

    }

    public static CollectionReference getUserRequestsReference () {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        return database
                .collection(USERS_COLLECTION)
                .document(AuthHandler.getCurrentUserInstance().getUid())
                .collection(USER_REQUESTS_COLLECTION);

    }

    public static CollectionReference getUserFavoriteMoviesReference () {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        return database
                .collection(USERS_COLLECTION)
                .document(AuthHandler.getCurrentUserInstance().getUid())
                .collection(USER_FAVORITES_COLLECTION);

    }

    public static CollectionReference getUserSavedMoviesReference () {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        return database
                .collection(USERS_COLLECTION)
                .document(AuthHandler.getCurrentUserInstance().getUid())
                .collection(USER_SAVED_COLLECTION);

    }

}
