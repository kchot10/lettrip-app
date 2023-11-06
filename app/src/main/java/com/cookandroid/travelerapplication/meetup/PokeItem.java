package com.cookandroid.travelerapplication.meetup;

import java.io.Serializable;

public class PokeItem implements Serializable {
    private String meet_up_post_id;
    private String userName;
    private String userSex;
    private String meetupSuccessNum;
    private String meetupFailNum;
    private String oneLineReview;
    private String imageResource;
    private String birthDate;
    private String write_user_id;
    private String request_user_id;
    private String meet_up_id;

    public String getMeet_up_id() {
        return meet_up_id;
    }

    public void setMeet_up_id(String meet_up_id) {
        this.meet_up_id = meet_up_id;
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
    public String getMeet_up_post_id() {
        return meet_up_post_id;
    }
    public void setMeet_up_post_id(String meet_up_post_id) {
        this.meet_up_post_id = meet_up_post_id;
    }

    public String getMeetupFailNum() {
        return meetupFailNum;
    }

    public void setMeetupFailNum(String meetupFailNum) {
        this.meetupFailNum = meetupFailNum;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public PokeItem() {
    }

    public PokeItem(String userName, String userSex, String meetupSuccessNum, String getMeetupFailNum, String oneLineReview, String imageResource) {
        this.userName = userName;
        this.userSex = userSex;
        this.meetupSuccessNum = meetupSuccessNum;
        this.meetupFailNum = meetupFailNum;
        this.oneLineReview = oneLineReview;
        this.imageResource = imageResource;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getMeetupSuccessNum() {
        return meetupSuccessNum;
    }

    public void setMeetupSuccessNum(String meetupSuccessNum) {
        this.meetupSuccessNum = meetupSuccessNum;
    }

    public String getOneLineReview() {
        return oneLineReview;
    }

    public void setOneLineReview(String oneLineReview) {
        this.oneLineReview = oneLineReview;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }
}
