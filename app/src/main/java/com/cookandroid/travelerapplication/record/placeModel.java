package com.cookandroid.travelerapplication.record;

public class placeModel {
    private String placeName;
    private String date;
    private String cost;

    public placeModel(String placeName, String date, String cost) {
        this.placeName = placeName;
        this.date = date;
        this.cost = cost;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
