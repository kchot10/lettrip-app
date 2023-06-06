package com.cookandroid.travelerapplication.mypage;

import androidx.recyclerview.widget.RecyclerView;

public class LikeTrip {
    String tripType;
    String cost;
    String city;


    public LikeTrip(String tripType, String cost, String city) {
        this.tripType = tripType;
        this.cost = cost;
        this.city = city;
    }



    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
