package com.example.cinemates.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.control.TMDBRequestManager;
import com.example.cinemates.model.Movie;
import com.example.cinemates.R;
import com.example.cinemates.helper.TMDB;

import java.util.ArrayList;
import java.util.List;


public class MovieSearchFragment extends Fragment {

    private static final String TAG = MovieSearchFragment.class.getSimpleName();

    private static final int BASE_PAGE = 1;

    private final List<Movie> movies = new ArrayList<>();

    private int currPage = BASE_PAGE;
    private int lastPage = BASE_PAGE;
    private String currQuery = "";

    private SearchView searchView;
    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;
    private LinearLayoutManager linearLayoutManager;

    private TextView searchForAMovieTextView;
    private TextView noResultsTextView;

    private ProgressBar progressBar;


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_search, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        searchForAMovieTextView = view.findViewById(R.id.cerca_un_film_txtview);
        noResultsTextView = view.findViewById(R.id.nessun_film_txtview);

        initRecyclerView(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu (@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_ricerca_film, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu (@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d(TAG, "onPrepareOptionsMenu");
        initSearchViewFromMenu(menu);
        hideKeyboard(getActivity());
    }

    @Override
    public void onResume () {
        super.onResume();
        if (searchView != null)
            searchView.clearFocus();
    }


    private void initRecyclerView (View inflatedView) {
        recyclerView = inflatedView.findViewById(R.id.ricerca_film_recyclerview);
        moviesAdapter = new MoviesAdapter(getContext(), movies);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(moviesAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled (@NonNull RecyclerView rv, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy != 0)
                    hideKeyboard(getActivity());

                if (moviesAdapter.getItemCount() != 0) {

                    int lastItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                    if (lastItemPosition != RecyclerView.NO_POSITION &&
                        lastItemPosition == moviesAdapter.getItemCount() - 1 &&
                        currPage < lastPage) {

                        progressBar.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(() -> {
                            TMDBRequestManager.handleRequestMoviesByTitle(MovieSearchFragment.this,
                                                                  TMDB.requestMoviesByTitle(currQuery, ++currPage), true);
                        }, 1000);

                    }

                }

            }

        });

    }

    private void initSearchViewFromMenu (Menu menu) {

        searchView = (SearchView) menu.findItem(R.id.ricerca_film).getActionView();
        searchView.setQueryHint("Cerca un film");
        changeSearchViewTextColor(searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit (String query) {
                handleOnQueryTextSubmit(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange (String newText) {
                handleOnQueryTextChange(newText);
                return true;
            }

        });

        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Log.d(TAG, "SEARCH VIEW FOCUS GAINED");
                searchView.setQuery(currQuery, false);
            }
            else {
                Log.d(TAG, "SEARCH VIEW FOCUS LOST");
            }
        });

        searchView.setOnCloseListener(() -> {
            currQuery="";
            movies.clear();
            moviesAdapter.notifyDataSetChanged();
            searchForAMovieTextView.setVisibility(View.VISIBLE);
            noResultsTextView.setVisibility(View.GONE);
            return false;
        });

        searchView.setIconifiedByDefault(true);

    }
    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.dark_grey));
                ((TextView) view).setHintTextColor(getResources().getColor(R.color.dark_grey));
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }

    private void handleOnQueryTextSubmit (String text) {
        final String query = text.trim().toLowerCase();
        if (!query.isEmpty()) {
            movies.clear();
            currPage = BASE_PAGE;
            currQuery = query;
            hideKeyboard(getActivity());
            TMDBRequestManager.handleRequestMoviesByTitle(this, TMDB.requestMoviesByTitle(query, BASE_PAGE), true);
        }
    }
    private void handleOnQueryTextChange (String text) {
        final String query = text.trim().toLowerCase();
        movies.clear();
        currPage = BASE_PAGE;
        currQuery = query;
        if (!query.isEmpty()) {
            TMDBRequestManager.handleRequestMoviesByTitle(this, TMDB.requestMoviesByTitle(query, BASE_PAGE), false);
        }
        else {
            moviesAdapter.notifyDataSetChanged();
        }
    }


    private void hideKeyboard (Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null)
            view = new View(activity);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    public List<Movie> getMovies () {
        return movies;
    }

    public MoviesAdapter getMoviesAdapter () {
        return moviesAdapter;
    }

    public void setLastPage (int page) {
        this.lastPage = page;
    }

    public TextView getNoResultsTextView () {
        return noResultsTextView;
    }

    public TextView getSearchForAMovieTextView () {
        return searchForAMovieTextView;
    }

    public ProgressBar getProgressBar () {
        return progressBar;
    }

}