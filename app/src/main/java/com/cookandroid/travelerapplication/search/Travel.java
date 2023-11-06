package com.cookandroid.travelerapplication.search;

import java.io.Serializable;

public class Travel implements Serializable {
    String title;
    String depart_date;
    String last_date;
    String city;
    String total_cost;
    String travel_theme;
    String places;
    String travel_id;
    String number_of_courses;
    String user_id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepart_date() {
        return depart_date;
    }

    public void setDepart_date(String depart_date) {
        this.depart_date = depart_date;
    }

    public String getLast_date() {
        return last_date;
    }

    public void setLast_date(String last_date) {
        this.last_date = last_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTravel_id() {
        return travel_id;
    }

    public void setTravel_id(String travel_id) {
        this.travel_id = travel_id;
    }

    public String getNumber_of_courses() {
        return number_of_courses;
    }

    public void setNumber_of_courses(String number_of_courses) {
        this.number_of_courses = number_of_courses;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public String getTravel_theme() {
        return travel_theme;
    }

    public void setTravel_theme(String travel_theme) {
        this.travel_theme = travel_theme;
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }
}
