package com.example.cinemates.data;

import com.example.cinemates.control.AuthHandler;
import com.example.cinemates.model.Movie;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreUserMovieListsData implements UserMovieListsDataAsyncAccess {

    private static final FirestoreUserMovieListsData instance = new FirestoreUserMovieListsData();
    public static FirestoreUserMovieListsData getInstance () {
        return instance;
    }

    private FirestoreUserMovieListsData () {}

    private static final String USERS_COLLECTION = "utenti";

    private static final String USER_FAVORITES_COLLECTION = "preferiti";
    private static final String USER_SAVED_COLLECTION = "salvati";


    public Task<Void> addMovieToCollection (String collection, int id, String poster, String title, String date) {

        if (!USER_FAVORITES_COLLECTION.equals(collection) & !USER_SAVED_COLLECTION.equals(collection))
            throw new IllegalArgumentException("The collection must be either \"preferiti\" or \"salvati\"");

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        return database
                .collection(USERS_COLLECTION)
                .document(AuthHandler.getCurrentUserInstance().getUid())
                .collection(collection)
                .document(String.valueOf(id))
                .set(new Movie(id, poster, title, date));

    }

    public Task<Void> deleteMovieFromCollection (String collection, int id) {

        if (!USER_FAVORITES_COLLECTION.equals(collection) & !USER_SAVED_COLLECTION.equals(collection))
            throw new IllegalArgumentException("Collection must be either \"preferiti\" or \"salvati\"");

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        return database
                .collection(USERS_COLLECTION)
                .document(AuthHandler.getCurrentUserInstance().getUid())
                .collection(collection)
                .document(String.valueOf(id))
                .delete();

    }

}
