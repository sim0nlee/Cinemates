package com.example.cinemates.control;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.cinemates.helper.TMDB;
import com.example.cinemates.activities.MovieActivity;
import com.example.cinemates.fragments.MovieSearchFragment;
import com.example.cinemates.model.Movie;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TMDBRequestManager {

    private static final OkHttpClient client = new OkHttpClient();

    public static void handleRequestMoviesByTitle (MovieSearchFragment fragment, Request request, boolean showErrorToastOnFailure) {

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure (Request request, IOException e) {
                Controller.runOnUIThread(() -> {
                    fragment.getProgressBar().setVisibility(View.GONE);
                    fragment.getMovies().clear();
                    fragment.getMoviesAdapter().notifyDataSetChanged();
                    if (showErrorToastOnFailure)
                        Controller.runOnUIThread(() -> Toast.makeText(fragment.getContext(), "Errore di connessione", Toast.LENGTH_SHORT).show());
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse (Response response) {

                try (ResponseBody body = response.body()) {

                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code: " + response);

                    JSONObject JSONResponse = new JSONObject(body.string());

                    fragment.setLastPage(JSONResponse.getInt("total_pages"));

                    JSONArray movies = JSONResponse.getJSONArray("results");
                    List<Movie> tempResults = processJSONMovieArray(movies);

                    Controller.runOnUIThread(() -> Controller.updateFragmentAfterMovieSearch(fragment, tempResults));

                }
                catch (IOException e) {
                    Log.d("IOEX", "Errore nella connessione all'API");
                    e.printStackTrace();
                }
                catch (JSONException e) {
                    Log.d("JSONEX", "Errore nell'elaborazione del JSON");
                    e.printStackTrace();
                }

            }

        });

    }
    private static List<Movie> processJSONMovieArray (JSONArray movies) throws JSONException {

        JSONObject movie;
        int id;
        double popularity;
        String poster, title, date;

        List<Movie> list = new ArrayList<>();
        for (int i = 0; i < movies.length(); i++) {

            movie = movies.getJSONObject(i);

            id = movie.getInt("id");
            popularity = movie.isNull("popularity") ? 0 : movie.getDouble("popularity");
            poster = movie.isNull("poster_path") ? null : TMDB.IMAGE_BASE_URL + movie.getString("poster_path");
            title = movie.getString("title");
            date = movie.has("release_date") ? movie.getString("release_date") : "-";

            list.add(new Movie(id, popularity, poster, title, date));
        }
        return list;

    }

    public static void handleRequestMovieById (MovieActivity activity, Request request) {

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure (Request request, IOException e) {
                Controller.runOnUIThread(() -> Toast.makeText(activity, "Errore di connessione", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }

            @Override
            public void onResponse (Response response) {

                try (ResponseBody body = response.body()) {

                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code: " + response);

                    JSONObject movie = new JSONObject(body.string());

                    String[] movieData = getMovieData(movie);

                    Controller.runOnUIThread(() -> {
                        Controller.setMovieActivity(activity,
                                                    movieData[0], movieData[1], movieData[2], movieData[3],
                                                    movieData[4], movieData[5], movieData[6]);
                    });

                }
                catch (IOException e) {
                    Log.d("IOEX", "Errore nella connessione all'API");
                    e.printStackTrace();
                }
                catch (JSONException e) {
                    Log.d("JSONEX", "Errore nell'elaborazione del JSON");
                    e.printStackTrace();
                }

            }

        });

    }
    private static String[] getMovieData (JSONObject movie) throws JSONException {

        JSONArray crew, genresJSONArray;
        String backdrop, title, runtime, year, description, directors, genres;
        StringBuilder directorsStringBuilder = new StringBuilder();
        StringBuilder genresStringBuilder = new StringBuilder();
        int runtimeInMins;

        backdrop = movie.isNull("backdrop_path") ? null : TMDB.IMAGE_BASE_URL + movie.getString("backdrop_path");
        title = movie.getString("title");

        runtimeInMins = movie.getInt("runtime");
        runtime = runtimeInMins/60 + "h " + runtimeInMins%60 + "m";

        year = !movie.isNull("release_date") && !movie.getString("release_date").isEmpty() ? movie.getString("release_date").substring(0, 4) : "-";
        description = movie.isNull("overview") ? "Nessuna descrizione" : movie.getString("overview");

        crew = movie.getJSONObject("credits").getJSONArray("crew");
        for (int i = 0; i < crew.length(); i++) {
            JSONObject crewMember = crew.getJSONObject(i);
            String crewMemberJob = crewMember.getString("job");
            if ("Director".equals(crewMemberJob))
                directorsStringBuilder.append(crewMember.getString("name")).append(", ");
        }
        directors = !directorsStringBuilder.toString().isEmpty() ? directorsStringBuilder.toString().substring(0, directorsStringBuilder.length()-2) : "Regista sconosciuto";

        genresJSONArray = movie.getJSONArray("genres");
        for (int i = 0; i < genresJSONArray.length(); i++) {
            JSONObject genre = genresJSONArray.getJSONObject(i);
            genresStringBuilder.append(genre.getString("name")).append(", ");
        }
        genres = !genresStringBuilder.toString().isEmpty() ? genresStringBuilder.toString().substring(0, genresStringBuilder.length()-2) : "";

        return new String[] {backdrop, title, directors, runtime, genres, year, description};

    }

}
