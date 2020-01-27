package com.example.uniapp_20.model;

import java.io.Serializable;
import java.util.ArrayList;

public class News implements Serializable {

    private String name;
    private String date;
    private String description;
    private String thumbnail;
    private int id;
    private ArrayList<String> arrayList;

    public News(){
    }

    public News(int id, String date, String name, String description, String thumbnail, ArrayList<String> category) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.arrayList = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }
}
