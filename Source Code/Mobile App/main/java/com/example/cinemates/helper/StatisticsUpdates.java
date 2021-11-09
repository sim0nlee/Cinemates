package com.example.cinemates.helper;

import android.util.Log;

import com.example.cinemates.model.UserActionMovieList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import org.jetbrains.annotations.NotNull;

public class StatisticsUpdates {

    public enum AccessType {IN, OUT}
    private static final String TAG = StatisticsUpdates.class.getSimpleName();

    private static final String DATABASE_URL = "https://cinemates-e0934-default-rtdb.europe-west1.firebasedatabase.app";

    private static final String STATS_PATH = "Statistiche";

    private static final String ACCESSES_PATH = "Accessi";
    private static final String SEARCHES_PATH = "Ricerche";
    private static final String USERS_PATH = "Utenti";
    private static final String FOLLOWINGS_NUMBERS = "Collegamenti";

    private static final DatabaseReference database = FirebaseDatabase.getInstance(DATABASE_URL).getReference();

    public static void updateAccesses (AccessType accessType) {

        DatabaseReference accesses = database.child(STATS_PATH).child(ACCESSES_PATH);

        accesses.runTransaction(new Transaction.Handler() {

            @NotNull
            @Override
            public Transaction.Result doTransaction(@NotNull MutableData accesses) {
                Integer currentValue = accesses.getValue(Integer.class);
                if (currentValue == null) {
                    accesses.setValue(1);
                }
                else if (accessType == AccessType.IN) {
                    accesses.setValue(currentValue + 1);
                }
                else {
                    accesses.setValue(currentValue - 1);
                }
                return Transaction.success(accesses);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    Log.d(TAG, databaseError.getMessage());
                }
                else {
                    Log.d(TAG, "onComplete");
                }
            }

        });

    }

    public static void updateUsers () {

        DatabaseReference users = database.child(STATS_PATH).child(USERS_PATH);

        users.runTransaction(new Transaction.Handler() {

            @NotNull
            @Override
            public Transaction.Result doTransaction(@NotNull MutableData users) {
                Integer currentValue = users.getValue(Integer.class);
                if (currentValue == null) {
                    users.setValue(1);
                }
                else {
                    users.setValue(currentValue + 1);
                }
                return Transaction.success(users);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    Log.d(TAG, databaseError.getMessage());
                }
                else {
                    Log.d(TAG, "onComplete");
                }
            }

        });

    }

    public static void updateSearches () {

        DatabaseReference searches = database.child(STATS_PATH).child(SEARCHES_PATH);

        searches.runTransaction(new Transaction.Handler() {

            @NotNull
            @Override
            public Transaction.Result doTransaction(@NotNull MutableData searches) {
                Integer currentValue = searches.getValue(Integer.class);
                if (currentValue == null) {
                    searches.setValue(1);
                }
                else {
                    searches.setValue(currentValue + 1);
                }
                return Transaction.success(searches);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    Log.d(TAG, databaseError.getMessage());
                }
                else {
                    Log.d(TAG, "onComplete");
                }
            }

        });

    }

    public static void updateFollowingsNumber () {

        DatabaseReference users = database.child(STATS_PATH).child(FOLLOWINGS_NUMBERS);

        users.runTransaction(new Transaction.Handler() {

            @NotNull
            @Override
            public Transaction.Result doTransaction(@NotNull MutableData followings) {
                Integer currentValue = followings.getValue(Integer.class);
                if (currentValue == null) {
                    followings.setValue(1);
                }
                else {
                    followings.setValue(currentValue + 1);
                }
                return Transaction.success(followings);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    Log.d(TAG, databaseError.getMessage());
                }
                else {
                    Log.d(TAG, "onComplete");
                }
            }

        });

    }

    public static void updateMoviesInLists (String genresString, UserActionMovieList.MovieActionType actionType) {

        String[] genres = genresString.split(", ");

        for (String genre : genres) {

            genre = fixTMDBGenreName(genre);

            final String PATH = genre + " nelle liste";

            DatabaseReference numberOfMoviesInList = database.child(STATS_PATH).child(PATH);

            numberOfMoviesInList.runTransaction(new Transaction.Handler() {

                @NotNull
                @Override
                public Transaction.Result doTransaction(@NotNull MutableData numberOfMoviesInList) {
                    Integer currentValue = numberOfMoviesInList.getValue(Integer.class);
                    if (currentValue == null) {
                        numberOfMoviesInList.setValue(1);
                    }
                    else if (actionType == UserActionMovieList.MovieActionType.ADDED) {
                        numberOfMoviesInList.setValue(currentValue + 1);
                    }
                    else {
                        numberOfMoviesInList.setValue(currentValue - 1);
                    }
                    return Transaction.success(numberOfMoviesInList);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                    if (databaseError != null) {
                        Log.d(TAG, databaseError.getMessage());
                    }
                    else {
                        Log.d(TAG, "onComplete");
                    }
                }

            });

        }

    }
    private static String fixTMDBGenreName (String genre) {

        if ("Azione".equals(genre) ||
            "Avventura".equals(genre) ||
            "Animazione".equals(genre) ||
            "Fantascienza".equals(genre) ||
            "Guerra".equals(genre))
            genre = "Film di " + genre;

        if ("Commedia".equals(genre))
            genre = "Commedie";

        if ("Crime".equals(genre))
            genre = "Gialli";

        if ("Documentario".equals(genre))
            genre = "Documentari";

        if ("Dramma".equals(genre))
            genre = "Drammi";

        if ("Famiglia".equals(genre))
            genre = "Film per la famiglia";

        if ("Storia".equals(genre))
            genre = "Film storici";

        if ("Musica".equals(genre))
            genre = "Film musicali";

        if ("Mistero".equals(genre))
            genre = "Film di mistero";

        if ("Romance".equals(genre))
            genre = "Film romantici";

        if ("televisione film".equals(genre))
            genre = "Film per la televisione";

        return genre;

    }

}
