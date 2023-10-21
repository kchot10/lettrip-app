package com.cookandroid.travelerapplication.meetup;

public class MeetupPost {
    private boolean isGPSEnabled;
    private String cityName;
    private String date;
    private String content;
    private String nickName;
    private int sexIconResource;
    private int userId;

    public MeetupPost(boolean isGPSEnabled, String cityName, String date, String content) {
        this.isGPSEnabled = isGPSEnabled;
        this.cityName = cityName;
        this.date = date;
        this.content = content;
    }

    public boolean isGPSEnabled() {
        return isGPSEnabled;
    }

    public void setGPSEnabled(boolean GPSEnabled) {
        isGPSEnabled = GPSEnabled;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSexIconResource() {
        return sexIconResource;
    }

    public void setSexIconResource(int sexIconResource) {
        this.sexIconResource = sexIconResource;
    }

    public int getUserId() {
        return userId;
    }
}


