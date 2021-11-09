package com.example.cinemates.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemates.R;
import com.example.cinemates.helper.TMDB;
import com.example.cinemates.control.ListenerDispenser;
import com.example.cinemates.control.TMDBRequestManager;

public class MovieActivity extends AppCompatActivity {

    private static final String TAG = MovieActivity.class.getSimpleName();

    private ImageButton favoriteButton;
    private ImageButton saveButton;

    private int movieID;
    private String moviePoster;
    private String movieTitle;
    private String movieDate;
    private String movieGenres;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie);

        favoriteButton = findViewById(R.id.favorite_button);
        saveButton = findViewById(R.id.save_button);

        movieID = getIntent().getIntExtra("id", -1);
        moviePoster = getIntent().getStringExtra("poster");
        movieTitle = getIntent().getStringExtra("titolo");
        movieDate = getIntent().getStringExtra("data");

        TMDBRequestManager.handleRequestMovieById(this, TMDB.requestMovieByID(movieID));

    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        ListenerDispenser.detachFavoriteMoviesButtonListener();
        ListenerDispenser.detachSavedMoviesButtonListener();
    }


    public ImageButton getFavoriteButton () {
        return favoriteButton;
    }

    public ImageButton getSaveButton () {
        return saveButton;
    }

    public int getMovieID () {
        return movieID;
    }

    public String getMoviePoster () {
        return moviePoster;
    }

    public String getMovieTitle () {
        return movieTitle;
    }

    public String getMovieDate () {
        return movieDate;
    }

    public void setMovieGenres (String genres) {this.movieGenres = genres;}
    public String getMovieGenres () {
        return movieGenres;
    }

}