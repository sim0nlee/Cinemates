package com.example.cinemates.model;

import com.example.cinemates.helper.InputChecker;

import java.util.Date;

public class UserActionFollow extends UserAction {

    private ActionType actionType;
    private AppUser followedUser;
    private String description;

    public UserActionFollow () {}

    public UserActionFollow (Date date, AppUser user, AppUser followedUser) {
        super(date, user);
        this.actionType = ActionType.FOLLOW;
        this.followedUser = followedUser;
        this.description = InputChecker.capitalizeNames(user.getName()) + " " +
                           InputChecker.capitalizeNames(user.getSurname()) +
                           " ha iniziato a seguire " +
                           InputChecker.capitalizeNames(followedUser.getName()) + " " +
                           InputChecker.capitalizeNames(followedUser.getSurname());
    }

    public Date getDate () { return date; }

    public AppUser getUser () {return user; }

    public AppUser getFollowedUser () {
        return followedUser;
    }

    public ActionType getActionType () {
        return actionType;
    }

    public String getDescription () { return description; }

}
