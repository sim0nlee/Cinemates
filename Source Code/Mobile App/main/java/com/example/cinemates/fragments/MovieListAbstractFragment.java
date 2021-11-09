package com.example.cinemates.fragments;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.cinemates.model.Movie;

import java.util.ArrayList;

public abstract class MovieListAbstractFragment extends Fragment {

    private static final String TAG = MovieListAbstractFragment.class.getSimpleName();

    private final ArrayList<Movie> movies = new ArrayList<>();
    private MoviesAdapter moviesAdapter;

    private TextView noMoviesTextView;


    public void addMovie (Movie movie) {
        movies.add(movie);
    }
    public void removeMovie (Movie movie) {
        for (Movie f : movies) {
            if (f.equals(movie)) {
                movies.remove(f);
                break;
            }
        }
    }


    public ArrayList<Movie> getMovies () {
        return movies;
    }

    public MoviesAdapter getMoviesAdapter () {
        return moviesAdapter;
    }
    public void setMoviesAdapter (MoviesAdapter moviesAdapter) {
        this.moviesAdapter = moviesAdapter;
    }

    public TextView getNoMoviesTextView () {
        return noMoviesTextView;
    }
    public void setNoMoviesTextView (TextView noMoviesTextView) {
        this.noMoviesTextView = noMoviesTextView;
    }

}
