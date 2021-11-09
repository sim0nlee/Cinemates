package com.example.cinemates.data;

import android.net.Uri;

import com.example.cinemates.control.Controller;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FirebaseStorageHandler {

    private static final String USER_PICTURES_PATH = "IMMAGINI_PROFILO_UTENTI/";

    private static final StorageReference storage =  FirebaseStorage.getInstance().getReference();



    public static void uploadThenSetUserPicture (String uid, String propicURI) {

        String propicPath = USER_PICTURES_PATH + uid;
        StorageReference propicReference = storage.child(propicPath);

        UploadTask uploadTask = propicReference.putFile(Uri.parse(propicURI));

        uploadTask
        .addOnSuccessListener(taskSnapshot ->

            taskSnapshot.getStorage().getDownloadUrl()
            .addOnSuccessListener(uri -> {
                Controller.getUserDataHandler().setDownloadedUserPicture(uid, uri);
            })

        );

    }

    public static void uploadThenSetUserPictureFromFacebook (String uid, String propicURI) {

        String propicPath = USER_PICTURES_PATH + uid;
        StorageReference propicReference = storage.child(propicPath);

        Thread uploadThread = new Thread() {
                                    @Override
                                    public void run () {
                                        try {
                                            InputStream stream = new URL(propicURI).openStream();

                                            UploadTask uploadTask = propicReference.putStream(stream);
                                            uploadTask.addOnSuccessListener(taskSnapshot ->

                                                taskSnapshot.getStorage().getDownloadUrl()
                                                .addOnSuccessListener(uri -> {
                                                    Controller.getUserDataHandler().setDownloadedUserPicture(uid, uri);
                                                })

                                            );
                                        }
                                        catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                     }
                              };

        Controller.runSeparateThread(uploadThread);

    }

}
