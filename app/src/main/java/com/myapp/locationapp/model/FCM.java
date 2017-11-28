package com.myapp.locationapp.model;

/**
 * Created by ishan on 28-11-2017.
 */

public class FCM {
    private String UserId;
    private String Token;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
