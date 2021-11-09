package com.example.cinemates.data;

import com.google.android.gms.tasks.Task;

@SuppressWarnings("All")
public interface UserMovieListsDataAsyncAccess {

    Task addMovieToCollection (String collection, int id, String poster, String title, String date);

    Task deleteMovieFromCollection (String collection, int id);

}
