package com.cookandroid.travelerapplication.meetup;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class MeetupPost implements Serializable {
    private String meet_up_post_id;
    private String is_gps_enabled;
    private String travel_id;
    private String place_id;

    public MeetupPost(String city, String created_date, String content, String nickname, String sex, String image_url) {
        this.image_url = image_url;
        this.city = city;
        this.created_date = created_date;
        this.content = content;
        this.nickname = nickname;
        this.sex = sex;

    }

    public MeetupPost(String meet_up_post_id, String is_gps_enabled, String city, String content, String created_date, String nickname, String sex, @Nullable String image_url, String province, @Nullable String birth_date, String user_id, String postTitle) {
        this.meet_up_post_id = meet_up_post_id;
        this.is_gps_enabled = is_gps_enabled;
        this.city = city;
        this.content = content;
        this.created_date = created_date;
        this.nickname = nickname;
        this.sex = sex;
        this.image_url = image_url;
        this.province = province;
        this.birth_date = birth_date;
        this.user_id = user_id;
        this.postTitle = postTitle;
    }

    private String city;
    private String content;
    private String created_date;
    private String nickname;
    private String sex;
    private String image_url;
    private String province;
    private String birth_date;
    private String user_id;
    private String postTitle;
    private String meet_up_id;




    public String getTravel_id() {
        return travel_id;
    }

    public void setTravel_id(String travel_id) {
        this.travel_id = travel_id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getMeet_up_id() {
        return meet_up_id;
    }

    public void setMeet_up_id(String meet_up_id) {
        this.meet_up_id = meet_up_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public MeetupPost() {
    }

    public String getMeet_up_post_id() {
        return meet_up_post_id;
    }

    public void setMeet_up_post_id(String meet_up_post_id) {
        this.meet_up_post_id = meet_up_post_id;
    }

    public String getIs_gps_enabled() {
        return is_gps_enabled;
    }

    public void setIs_gps_enabled(String is_gps_enabled) {
        this.is_gps_enabled = is_gps_enabled;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }


}


