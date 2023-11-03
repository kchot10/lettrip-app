package com.cookandroid.travelerapplication.mission;

public class UserInfo {
    String point;
    String nickname;
    String stored_file_url;
    String email;
    String sex;
    String birth_date;
    String meet_up_cancelled_count;
    String meet_up_completed_count;
    String user_id;

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
