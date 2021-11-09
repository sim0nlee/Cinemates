package com.example.cinemates.model;

import java.util.Objects;

public class AppUser {

    private String name;
    private String surname;
    private String searchindex;
    private String email;
    private String propic;
    private String uid;

    public AppUser (String name, String surname, String email, String propic, String uid) {
        this.name = name;
        this.surname = surname;
        this.searchindex = name + " " + surname;
        this.email = email;
        this.propic = propic;
        this.uid = uid;
    }

    public AppUser () {}

    public AppUser (String name, String surname, String email, String uid) {
        this(name, surname, email, null, uid);
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {this.name = name;}

    public String getSurname () {
        return surname;
    }

    public void setSurname (String surname) {this.surname = surname;}

    public String getSearchindex () {
        return searchindex;
    }

    public void setSearchindex (String searchindex) {
        this.searchindex = searchindex;
    }

    public String getEmail () {
        return email;
    }

    public String getPropic () {
        return propic;
    }

    public void setpropic (String propic) {
        this.propic = propic;
    }

    public String getUid () {
        return uid;
    }

    public boolean hasPropic () {
        return propic != null;
    }

    public boolean isValid () {
        return
            name != null &&
            surname != null &&
            email != null;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return uid.equals(appUser.uid);
    }

    @Override
    public int hashCode () {
        return Objects.hash(uid);
    }
}
