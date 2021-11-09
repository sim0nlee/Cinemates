package com.example.cinemates.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemates.R;
import com.example.cinemates.control.ListenerDispenser;

public class FavoriteMoviesListFragment extends MovieListAbstractFragment {

    private static final String TAG = FavoriteMoviesListFragment.class.getSimpleName();


    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists_favorites, container, false);
        setNoMoviesTextView(view.findViewById(R.id.nessun_preferito_txtview));

        ListenerDispenser.attachFavoriteMoviesListener(this);
        initRecyclerView(view);

        return view;
    }

    @Override
    public void onDestroy () {
        ListenerDispenser.detachFavoriteMoviesListener();
        super.onDestroy();
    }


    protected void initRecyclerView (View inflatedView) {
        RecyclerView recyclerView = inflatedView.findViewById(R.id.preferiti_recyclerview);
        setMoviesAdapter(new MoviesAdapter(getContext(), getMovies()));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(getMoviesAdapter());
    }

}

