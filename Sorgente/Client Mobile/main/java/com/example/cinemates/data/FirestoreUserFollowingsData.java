package com.example.cinemates.data;

import android.content.Context;
import android.widget.Toast;

import com.example.cinemates.helper.StatisticsUpdates;
import com.example.cinemates.control.AuthHandler;
import com.example.cinemates.control.Controller;
import com.example.cinemates.model.AppUser;
import com.example.cinemates.model.UserActionFollow;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

public class FirestoreUserFollowingsData implements UserFollowingsDataAsyncAccess {

    private static final FirestoreUserFollowingsData instance = new FirestoreUserFollowingsData();
    public static FirestoreUserFollowingsData getInstance () {
        return instance;
    }

    private FirestoreUserFollowingsData () {}

    private static final String USERS_COLLECTION = "utenti";

    private static final String USER_FOLLOWERS_COLLECTION = "followers";
    private static final String USER_FOLLOWER_ID_FIELD = "follower";

    private static final String USER_FOLLOWINGS_COLLECTION = "seguiti";
    private static final String USER_FOLLOWING_ID_FIELD = "amico";

    private static final String USER_REQUESTS_COLLECTION = "richieste";
    private static final String REQUEST_SENDER_FIELD = "sender";


    public Task<Void> sendFriendRequest (String target_uid) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        HashMap<String, Object> senderMap = new HashMap<>();
        senderMap.put(REQUEST_SENDER_FIELD, AuthHandler.getCurrentUserInstance().getUid());

        //Aggiungi alle richieste dell'utente target la richiesta di questo utente
        return database
                .collection(USERS_COLLECTION)
                .document(target_uid)
                .collection(USER_REQUESTS_COLLECTION)
                .document(AuthHandler.getCurrentUserInstance().getUid())
                .set(senderMap);

    }

    public Task<Void> deleteFriendRequest (String senderUid) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        return database
                .collection(USERS_COLLECTION)
                .document(senderUid)
                .collection(USER_REQUESTS_COLLECTION)
                .document(AuthHandler.getCurrentUserInstance().getUid())
                .delete();

    }

    public Task<DocumentSnapshot> getSentFriendRequest (String targetUid) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        return database
                .collection(USERS_COLLECTION)
                .document(targetUid)
                .collection(USER_REQUESTS_COLLECTION)
                .document(AuthHandler.getCurrentUserInstance().getUid())
                .get();

    }

    public Task<DocumentSnapshot> getFriend (String uid) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        return database
                .collection(USERS_COLLECTION)
                .document(AuthHandler.getCurrentUserInstance().getUid())
                .collection(USER_FOLLOWINGS_COLLECTION)
                .document(uid)
                .get();

    }

    public Task<Void> acceptFriendRequest (Context context, String senderUid) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        HashMap<String, Object> currentUserMap = new HashMap<>();
        currentUserMap.put(USER_FOLLOWING_ID_FIELD, AuthHandler.getCurrentUserInstance().getUid());

        HashMap<String, Object> senderMap = new HashMap<>();
        senderMap.put(USER_FOLLOWER_ID_FIELD, senderUid);

        return database
                .collection(USERS_COLLECTION)
                .document(senderUid)
                .collection(USER_FOLLOWINGS_COLLECTION)
                .document(AuthHandler.getCurrentUserInstance().getUid())
                .set(currentUserMap)
                .addOnSuccessListener(v -> {

                    Toast.makeText(context, "Richiesta accettata", Toast.LENGTH_SHORT).show();

                    database
                        .collection(USERS_COLLECTION)
                        .document(AuthHandler.getCurrentUserInstance().getUid())
                        .collection(USER_FOLLOWERS_COLLECTION)
                        .document(senderUid)
                        .set(senderMap)
                        .addOnSuccessListener(aVoid -> {
                            database
                                .collection(USERS_COLLECTION)
                                .document(AuthHandler.getCurrentUserInstance().getUid())
                                .collection(USER_REQUESTS_COLLECTION)
                                .document(senderUid)
                                .delete()
                                .addOnSuccessListener(aVoid1 -> {
                                    database
                                        .collection(USERS_COLLECTION)
                                        .document(senderUid)
                                        .get()
                                        .addOnSuccessListener(documentSnapshot -> {
                                            StatisticsUpdates.updateFollowingsNumber();
                                            AppUser user1 = documentSnapshot.toObject(AppUser.class);
                                            AppUser user2 = AuthHandler.getCurrentUserInstance();

                                            UserActionFollow userAction = new UserActionFollow(new Date(), user1, user2);
                                            Controller.getUserFeedDataHandler().addUserActionToFollowersFeed(senderUid, userAction);
                                        });
                                });
                        });

                });

    }

    public Task<Void> refuseFriendRequest (Context context, String senderUid) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        return database
                .collection(USERS_COLLECTION)
                .document(AuthHandler.getCurrentUserInstance().getUid())
                .collection(USER_REQUESTS_COLLECTION)
                .document(senderUid)
                .delete()
                .addOnSuccessListener(Void -> Toast.makeText(context, "Richiesta rifiutata", Toast.LENGTH_SHORT).show());

    }

}
