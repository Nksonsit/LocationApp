package com.myapp.locationapp.model;

/**
 * Created by ishan on 27-11-2017.
 */

public class DlUl {
    private int id;
    private String UserId;
    private String Dl;
    private String Ul;
    private String Latitude;
    private String Longitude;
    private String Timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDl() {
        return Dl;
    }

    public void setDl(String dl) {
        Dl = dl;
    }

    public String getUl() {
        return Ul;
    }

    public void setUl(String ul) {
        Ul = ul;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }
}
