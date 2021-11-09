package com.example.cinemates.data;

import com.example.cinemates.control.AuthHandler;
import com.example.cinemates.model.UserAction;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

@SuppressWarnings("All")
public class FirestoreUserFeedData implements UserFeedDataAsyncAccess {

    private static final FirestoreUserFeedData instance = new FirestoreUserFeedData();
    public static FirestoreUserFeedData getInstance () {
        return instance;
    }

    private FirestoreUserFeedData () {}

    private static final String USERS_COLLECTION = "utenti";

    private static final String USER_FOLLOWERS_COLLECTION = "followers";

    private static final String USER_FEED_COLLECTION = "feed";
    private static final String USER_FEED_ACTION_DATE_FIELD = "date";


    public Task<QuerySnapshot> addUserActionToFollowersFeed (String userId, UserAction userAction) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        return database
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(USER_FOLLOWERS_COLLECTION)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot followerSnapshot : queryDocumentSnapshots) {
                        String followerUid = followerSnapshot.getString("follower");
                        database
                            .collection(USERS_COLLECTION)
                            .document(followerUid)
                            .collection(USER_FEED_COLLECTION)
                            .add(userAction);
                    }
                });

    }

    public Task<QuerySnapshot> getUserFeed () {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        String uid = AuthHandler.getCurrentUserInstance().getUid();

        return database
                .collection(USERS_COLLECTION)
                .document(uid)
                .collection(USER_FEED_COLLECTION)
                .orderBy(USER_FEED_ACTION_DATE_FIELD , Query.Direction.ASCENDING)
                .limit(20)
                .get();

    }

    public Task<QuerySnapshot> getFeedUpdatesStartingFrom (Date startingFrom) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        return database
                .collection(USERS_COLLECTION)
                .document(AuthHandler.getCurrentUserInstance().getUid())
                .collection(USER_FEED_COLLECTION)
                .whereGreaterThan(USER_FEED_ACTION_DATE_FIELD, startingFrom)
                .orderBy(USER_FEED_ACTION_DATE_FIELD, Query.Direction.ASCENDING)
                .get();

    }

}
