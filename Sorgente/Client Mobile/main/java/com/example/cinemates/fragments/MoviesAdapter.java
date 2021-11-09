package com.example.cinemates.fragments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.example.cinemates.activities.MovieActivity;
import com.example.cinemates.GlideApp;
import com.example.cinemates.model.Movie;
import com.example.cinemates.R;
import com.example.cinemates.helper.StatisticsUpdates;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private final Context context;
    public final List<Movie> movie;

    public MoviesAdapter (Context context, List<Movie> movie) {
        this.context = context;
        this.movie = movie;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_movie, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull MovieHolder holder, int position) {

        int id;
        String poster, titolo, data;

        id = movie.get(position).getId();
        poster = movie.get(position).getPoster();
        titolo = movie.get(position).getTitle();
        data = movie.get(position).getDate();

        GlideApp
            .with(context)
            .load(poster)
            .apply(new RequestOptions().error(R.drawable.movie_poster_placeholder).override(90, 120))
            .into(holder.posterView);

        holder.titleTextView.setText(titolo);
        holder.dateTextView.setText(data);

        holder.layout.setOnClickListener(v -> {
            StatisticsUpdates.updateSearches();
            Intent intent = new Intent(context, MovieActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("titolo", titolo);
            intent.putExtra("poster", poster);
            intent.putExtra("data", data);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount () {
        return (movie != null ? movie.size() : 0);
    }



    public static class MovieHolder extends RecyclerView.ViewHolder {
        private final ImageView posterView;
        private final TextView titleTextView;
        private final TextView dateTextView;
        private final LinearLayout layout;

        public MovieHolder (@NonNull View itemView) {
            super(itemView);

            posterView = itemView.findViewById(R.id.poster_imgview);
            titleTextView = itemView.findViewById(R.id.titolo_row_txtview);
            dateTextView = itemView.findViewById(R.id.data_row_txtview);
            layout = itemView.findViewById(R.id.row_film_layout);
        }
    }

}
