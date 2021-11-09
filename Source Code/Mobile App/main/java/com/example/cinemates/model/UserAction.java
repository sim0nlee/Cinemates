package com.example.cinemates.model;

import java.util.Date;

public abstract class UserAction {

    public enum ActionType {FOLLOW, ADD_MOVIE}

    ActionType actionType;
    Date date;
    AppUser user;

    public UserAction () {}

    public UserAction (Date date, AppUser user) {
        this.date = date;
        this.user = user;
    }

    public Date getDate () {
        return date;
    }

    public AppUser getUser () {
        return user;
    }
}
