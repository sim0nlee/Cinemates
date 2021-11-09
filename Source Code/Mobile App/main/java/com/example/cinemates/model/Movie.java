package com.example.cinemates.model;

public class Movie implements Comparable<Movie> {

    private int id;
    private double popularity;
    private String image;
    private String poster;
    private String title;
    private String director;
    private String runtime;
    private String[] genres;
    private String date;
    private String description;

    public Movie () {}

    public Movie (int id, String poster, String title, String date) {
        this.id = id;
        this.poster = poster;
        this.title = title;
        this.date = date;
    }

    public Movie (int id, double popularity, String poster, String title, String date) {
        this.id = id;
        this.popularity = popularity;
        this.poster = poster;
        this.title = title;
        this.date = date;
    }

    public int getId () {
        return id;
    }

    public double getPopularity () { return popularity; }

    public String getImage () {
        return image;
    }

    public String getPoster () {
        return poster;
    }

    public String getTitle () {
        return title;
    }

    public String getDirector () {
        return director;
    }

    public String getRuntime () {
        return runtime;
    }

    public String[] getGenres () {
        return genres;
    }

    public String getDate () {
        return date;
    }

    public String getDescription () {
        return description;
    }

    public boolean equals (Movie movie) {
        return (this.id == movie.id);
    }

    @Override
    public int compareTo (Movie movie) {
        if (movie.popularity < this.popularity)
            return -1;
        else if (movie.popularity == this.popularity)
            return 0;
        else
            return 1;
    }
}
