package com.example.cinemates.data;

import android.net.Uri;
import android.widget.Toast;

import com.example.cinemates.control.AuthHandler;
import com.example.cinemates.fragments.ProfileFragment;
import com.example.cinemates.model.AppUser;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.List;

@SuppressWarnings("All")
public class FirestoreUserData implements UserDataAsyncAccess {

    private static final FirestoreUserData instance = new FirestoreUserData();
    public static FirestoreUserData getInstance () {
        return instance;
    }

    private FirestoreUserData () {}

    private static final String USERS_COLLECTION = "utenti";
    private static final String USER_NAME_FIELD = "name";
    private static final String USER_SURNAME_FIELD = "surname";
    private static final String USER_INDEX_FIELD = "searchindex";
    private static final String USER_PROPIC_FIELD = "propic";


    public Task<Void> addUser (String name, String surname, String email, String uid) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        AppUser user = new AppUser(name.toLowerCase(), surname.toLowerCase(), email, uid);

        return database
                .collection(USERS_COLLECTION)
                .document(uid)
                .set(user, SetOptions.merge());

    }

    public Task<Void> setDownloadedUserPicture (String uid, Uri uri) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        return database
                .collection(USERS_COLLECTION)
                .document(uid)
                .update(USER_PROPIC_FIELD, uri.toString())
                .addOnSuccessListener(aVoid -> AuthHandler.getCurrentUserInstance().setpropic(uri.toString()));

    }

    public Task<DocumentSnapshot> fetchUser (String uid) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        return database
                .collection(USERS_COLLECTION)
                .document(uid)
                .get();

    }

    public Task<Void> updateUserName (ProfileFragment fragment, String uid, String key, String newName) {

        if (!"name".equals(key) && !"surname".equals(key))
            throw new IllegalArgumentException("Key must be either \"name\" or \"surname\"");

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        return database
                .collection(USERS_COLLECTION)
                .document(uid)
                .update(key, newName.toLowerCase())
                .addOnSuccessListener(aVoid -> {
                    if ("name".equals(key)) AuthHandler.getCurrentUserInstance().setName(newName.toLowerCase());
                    if ("surname".equals(key)) AuthHandler.getCurrentUserInstance().setSurname(newName.toLowerCase());
                })
                .addOnFailureListener(e -> Toast.makeText(fragment.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    public Task<List<Object>> fetchUsersByNameOrSurname (String name) {

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        Task<QuerySnapshot> nameTask = database
                .collection(USERS_COLLECTION)
                .orderBy(USER_NAME_FIELD)
                .startAt(name)
                .endAt(name + "\uf8ff")
                .get();

        Task<QuerySnapshot> surnameTask = database
                .collection(USERS_COLLECTION)
                .orderBy(USER_SURNAME_FIELD)
                .startAt(name)
                .endAt(name + "\uf8ff")
                .get();

        Task<QuerySnapshot> fullnameTask = database
                .collection(USERS_COLLECTION)
                .orderBy(USER_INDEX_FIELD)
                .startAt(name)
                .endAt(name + "\uf8ff")
                .get();

        return Tasks.whenAllSuccess(nameTask, surnameTask, fullnameTask);

    }

}
