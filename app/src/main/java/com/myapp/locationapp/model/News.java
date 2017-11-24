package com.myapp.locationapp.model;

/**
 * Created by ishan on 22-11-2017.
 */

public class News {
    private int NewsId;
    private String News;
    private String NewsImgUrl;

    public int getNewsId() {
        return NewsId;
    }

    public void setNewsId(int newsId) {
        NewsId = newsId;
    }

    public String getNews() {
        return News;
    }

    public void setNews(String news) {
        News = news;
    }

    public String getNewsImgUrl() {
        return NewsImgUrl;
    }

    public void setNewsImgUrl(String newsImgUrl) {
        NewsImgUrl = newsImgUrl;
    }
}
