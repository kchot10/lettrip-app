package com.cookandroid.travelerapplication.mission;

public class UserInfo {
    String name;
    String point;
    String nickname;
    String stored_file_url;
    String email;
    String sex;
    String birth_date;
    String meet_up_cancelled_count;
    String meet_up_completed_count;
    String user_id;

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public String getMeet_up_cancelled_count() {
        return meet_up_cancelled_count;
    }

    public String getMeet_up_completed_count() {
        return meet_up_completed_count;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public void setMeet_up_cancelled_count(String meet_up_cancelled_count) {
        this.meet_up_cancelled_count = meet_up_cancelled_count;
    }

    public void setMeet_up_completed_count(String meet_up_completed_count) {
        this.meet_up_completed_count = meet_up_completed_count;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStored_file_url() {
        return stored_file_url;
    }

    public void setStored_file_url(String stored_file_url) {
        this.stored_file_url = stored_file_url;
    }
}
