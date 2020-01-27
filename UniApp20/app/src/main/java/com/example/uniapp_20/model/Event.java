package com.example.uniapp_20.model;

import java.io.Serializable;

public class Event implements Serializable {
    String happeningDate, headline, location;
    int id;

    public Event(){}

    public Event(int id, String happeningDate, String headline, String location) {
        this.id = id;
        this.happeningDate = happeningDate;
        this.headline = headline;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHappeningDate() {
        return happeningDate;
    }

    public void setHappeningDate(String happeningDate) {
        this.happeningDate = happeningDate;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
