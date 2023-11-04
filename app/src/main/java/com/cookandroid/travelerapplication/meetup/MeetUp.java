package com.cookandroid.travelerapplication.meetup;

public class MeetUp {
    private String meet_up_id;
    private String meet_up_date;
    private String meet_up_status;
    private String meet_up_post_id;
    private String request_user_id;
    private String write_user_id;

    public MeetUp() {
    }

    public String getMeet_up_id() {
        return meet_up_id;
    }

    public void setMeet_up_id(String meet_up_id) {
        this.meet_up_id = meet_up_id;
    }

    public String getMeet_up_date() {
        return meet_up_date;
    }

    public void setMeet_up_date(String meet_up_date) {
        this.meet_up_date = meet_up_date;
    }

    public String getMeet_up_status() {
        return meet_up_status;
    }

    public void setMeet_up_status(String meet_up_status) {
        this.meet_up_status = meet_up_status;
    }

    public String getMeet_up_post_id() {
        return meet_up_post_id;
    }

    public void setMeet_up_post_id(String meet_up_post_id) {
        this.meet_up_post_id = meet_up_post_id;
    }

    public String getRequest_user_id() {
        return request_user_id;
    }

    public void setRequest_user_id(String request_user_id) {
        this.request_user_id = request_user_id;
    }

    public String getWrite_user_id() {
        return write_user_id;
    }

    public void setWrite_user_id(String write_user_id) {
        this.write_user_id = write_user_id;
    }
}
