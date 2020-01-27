package com.example.uniapp_20.model;

import java.io.Serializable;

public class Directory implements Serializable {

    String name, number, title, pid;

    public Directory(){}

    public Directory(String name, String title, String pid) {
        this.name = name;
        this.title = title;
        this.pid = pid;
    }

    public Directory(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
