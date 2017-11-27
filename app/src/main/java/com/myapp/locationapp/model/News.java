package com.myapp.locationapp.model;

/**
 * Created by ishan on 22-11-2017.
 */

public class News {
    private int Id;
    private String News;
    private String UserId;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNews() {
        return News;
    }

    public void setNews(String news) {
        News = news;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
