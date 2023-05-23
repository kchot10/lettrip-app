package com.cookandroid.travelerapplication.record;

public class Course {
    private String arrived_time;
    private String cost;
    private String day_count;
    private String place_name;
    private String stored_file_url;
    private String review_id;

    private String category_name;
    private String rating;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public String getStored_file_url() {
        return stored_file_url;
    }

    public void setStored_file_url(String stored_file_url) {
        this.stored_file_url = stored_file_url;
    }


    public String getArrived_time() {
        return arrived_time;
    }

    public void setArrived_time(String arrived_time) {
        this.arrived_time = arrived_time;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDay_count() {
        return day_count;
    }

    public void setDay_count(String day_count) {
        this.day_count = day_count;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }
}
