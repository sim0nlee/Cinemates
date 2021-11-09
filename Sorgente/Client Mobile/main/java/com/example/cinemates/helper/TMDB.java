package com.example.cinemates.helper;

import com.squareup.okhttp.Request;

public class TMDB {

    private static final String TAG = TMDB.class.getSimpleName();

    private static final String API_KEY = "4aa4a2dbd6bc38f0bc6aac4d8e69acde";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String LANG = "it-IT";
    private static final String INCLUDE_ADULT = "false";

    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original";


    public static Request requestMoviesByTitle (String search, int page) {

        final String query = search.trim().toLowerCase();
        final String url =  BASE_URL + "/search/movie?api_key=" + API_KEY
                            + "&language=" + LANG
                            + "&query=" + query
                            + "&page=" + page
                            + "&include_adult=" + INCLUDE_ADULT;

        return new Request.Builder().url(url).build();

    }

    public static Request requestMovieByID (int id) {

        final String url = BASE_URL + "/movie/" + id + "?api_key=" + API_KEY
                           + "&include_adult=" + INCLUDE_ADULT
                           + "&language=" + LANG + "&append_to_response=credits";

        return new Request.Builder().url(url).build();

    }

}
