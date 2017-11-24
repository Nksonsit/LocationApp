package com.myapp.locationapp.model;

/**
 * Created by ishan on 22-11-2017.
 */

public class Ranking {
    private int UserId;
    private String Name;
    private String UserImage;
    private int Rank;

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getRank() {
        return Rank;
    }

    public void setRank(int rank) {
        Rank = rank;
    }
}
