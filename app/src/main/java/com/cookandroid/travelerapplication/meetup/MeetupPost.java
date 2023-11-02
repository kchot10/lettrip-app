package com.cookandroid.travelerapplication.meetup;

public class MeetupPost {
    private String is_gps_enabled;
    private String city;
    private String content;
    private String created_date;
    private String nickname;
    private String sex;
    private String image_url;
    public MeetupPost() {
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
}


