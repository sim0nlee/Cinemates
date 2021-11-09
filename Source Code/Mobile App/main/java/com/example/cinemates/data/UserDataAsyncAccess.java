package com.example.cinemates.data;

import android.net.Uri;

import com.example.cinemates.fragments.ProfileFragment;
import com.google.android.gms.tasks.Task;

@SuppressWarnings("All")
public interface UserDataAsyncAccess {

    Task addUser (String name, String surname, String email, String uid);

    Task setDownloadedUserPicture (String uid, Uri uri);

    Task fetchUser (String uid);

    Task updateUserName (ProfileFragment fragment, String uid, String key, String newName);

    Task fetchUsersByNameOrSurname (String name);

}
