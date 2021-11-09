package com.example.cinemates.model;

import com.example.cinemates.helper.InputChecker;

import java.util.Date;

public class UserActionMovieList extends UserAction {

    public enum ListType {SALVATI, PREFERITI}
    public enum MovieActionType {ADDED, REMOVED}

    private ActionType actionType;
    private String movieTitle;
    private ListType listType;
    private MovieActionType movieActionType;
    private String description;

    public UserActionMovieList () {}

    public UserActionMovieList (Date date, AppUser user, String movieTitle, ListType listType, MovieActionType movieActionType) {
        super(date, user);
        this.actionType = ActionType.ADD_MOVIE;
        this.movieTitle = movieTitle;
        this.listType = listType;
        this.movieActionType = movieActionType;

        switch (movieActionType) {

            case ADDED:
                this.description = InputChecker.capitalizeNames(user.getName()) + " " + InputChecker.capitalizeNames(user.getSurname()) +
                                    " ha aggiunto " + "\"" + movieTitle + "\"" +
                                    " alla sua lista di film " + listType.toString().toLowerCase();
                break;

            case REMOVED:
                this.description =  InputChecker.capitalizeNames(user.getName()) + " " + InputChecker.capitalizeNames(user.getSurname()) +
                                    " ha rimosso " + "\"" + movieTitle + "\"" +
                                    " dalla sua lista di film " + listType.toString().toLowerCase();
                break;

        }

    }

    public Date getDate () { return date; }

    public AppUser getUser () {return user; }

    public String getMovieTitle () {
        return movieTitle;
    }

    public ListType getListType () {
        return listType;
    }

    public ActionType getActionType () {
        return actionType;
    }

    public String getDescription () {return description; }

}
