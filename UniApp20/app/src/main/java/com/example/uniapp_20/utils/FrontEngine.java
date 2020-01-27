package com.example.uniapp_20.utils;

import android.util.Log;

import com.example.uniapp_20.model.Event;
import com.example.uniapp_20.model.Mensa;
import com.example.uniapp_20.model.News;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class FrontEngine {

    private static FrontEngine mInstance;

    public Boolean isHomburg = false;

    public Boolean isSB = false;

    public String language = "de";

    public String tag_json_obj = "json_obj_req";

    public String base_url = "http://192.168.0.102:3000/";

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_TEXT = "text";
    public static final String EXTRA_ID = "id";

    public static final String TITLE = "Today in Mensa";
    public static final String TEXT = "Hello, this is a reminder of your Mensa time";

    private List<String> moreList;
    private List<String> helpFullNumberList;
    private List<String> categoryList;
    private List<Mensa> mensaListFirst;
    private List<Mensa> mensaListSecond;
    private List<Event> eventList;
    private List<News> newsList;

    public String mensaFirstDate = "";
    public String mensaSecondDate = "";


    public static FrontEngine getInstance() {

        if (mInstance == null) {
            mInstance = new FrontEngine();

        }
        return mInstance;
    }
    public void setLanguage(String lang){
        language = lang;
    }

    public String get_news_url(String page, String pageSize, String lang) {
        return base_url + "news/mainScreen?page=" + page + "&pageSize=" + pageSize + "&language=" + lang;
    }

    public String get_directory_url(String page, String pageSize, String query) {
        return base_url + "directory/search?page=" + page + "&pageSize=" + pageSize + "&query=" + query;
    }

    public String get_directory_data_url(String id) {
        return base_url + "directory/personDetails?pid=" + id + "&language=" + FrontEngine.getInstance().language;
    }

    public String get_map_url(){
        return base_url + "map";
    }

    public static int getHexColor(int[] color) {
        return android.graphics.Color.rgb(color[0], color[1], color[2]);
    }


    public void addCategory(String category) {
        if (categoryList == null) {
            categoryList = new ArrayList<>();
        }
        categoryList.add(category);
    }

    public List<String> getCategoryList() {
        if (categoryList == null) {
            categoryList = new ArrayList<>();
        }
        return categoryList;
    }


    public void addMenuFirst(Mensa mensa) {
        if (mensaListFirst == null) {
            mensaListFirst = new ArrayList<>();
        }
        mensaListFirst.add(mensa);
    }

    public List<Mensa> getmensaListFirst() {
        if (mensaListFirst == null) {
            mensaListFirst = new ArrayList<>();
        }
        return mensaListFirst;
    }

    public void addMenuSecond(Mensa mensa) {
        if (mensaListSecond== null) {
            mensaListSecond= new ArrayList<>();
        }
        mensaListSecond.add(mensa);
    }

    public List<Mensa> getMensaListSecond() {
        if (mensaListSecond== null) {
            mensaListSecond= new ArrayList<>();
        }
        return mensaListSecond;
    }

    public void addEvent(Event event) {
        if (eventList == null) {
            eventList = new ArrayList<>();
        }
        eventList.add(event);
    }

    public List<Event> getEventList() {
        if (eventList == null) {
            eventList = new ArrayList<>();
        }
        Collections.reverse(eventList);
        return eventList;
    }


    public void addNews(News news) {
        if (newsList == null) {
            newsList = new ArrayList<>();
        }
        newsList.add(news);
    }

    public List<News> getNewsList() {
        if (newsList == null) {
            newsList = new ArrayList<>();
        }
        return newsList;
    }


    public void addMore(String val) {
        if (moreList == null) {
            moreList = new ArrayList<>();
        }
        moreList.add(val);
    }

    public List<String> getMoreList() {
        if (moreList== null) {
            moreList = new ArrayList<>();
        }
        return moreList;
    }

    public List<String> getHelpFullNumberList() {
        if (helpFullNumberList== null) {
            helpFullNumberList = new ArrayList<>();
        }
        return helpFullNumberList;
    }

    public void addHelpFullNumberList(String val) {
        if (helpFullNumberList == null) {
            helpFullNumberList = new ArrayList<>();
        }
        helpFullNumberList.add(val);
    }
}
