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

public class SavedMoviesListFragment extends MovieListAbstractFragment {

    private static final String TAG = SavedMoviesListFragment.class.getSimpleName();

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists_saved, container, false);
        setNoMoviesTextView(view.findViewById(R.id.nessun_salvato_txtview));

        ListenerDispenser.attachSavedMoviesListener(this);
        initRecyclerView(view);

        return view;
    }

    @Override
    public void onDestroy () {
        ListenerDispenser.detachSavedMoviesListener();
        super.onDestroy();
    }


    protected void initRecyclerView (View inflatedView) {
        RecyclerView recyclerView = inflatedView.findViewById(R.id.salvati_recyclerview);
        setMoviesAdapter(new MoviesAdapter(getContext(), getMovies()));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(getMoviesAdapter());
    }

}