package com.example.uniapp_20.model;

import java.io.Serializable;

public class Mensa implements Serializable {
    private String date, time, headline, description, name, prices;
    private String notices;
    int id, dayCount;
    int[] color;

    public Mensa(){

    }

    public Mensa(int id, int dayCount, String date, String time, String headline, String description, String name, int[] color, String prices, String notices) {
        this.dayCount = dayCount;
        this.id = id;
        this.date = date;
        this.time = time;
        this.headline = headline;
        this.description = description;
        this.name = name;
        this.color = color;
        this.prices = prices;
        this.notices = notices;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date){this.date = date;}
    public String getDate(){ return this.date;}

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public int[] getColor() {
        return color;
    }

    public void setColor(int[] color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotices() {
        return notices;
    }

    public void setNotices(String notices) {
        this.notices = notices;
    }

}
