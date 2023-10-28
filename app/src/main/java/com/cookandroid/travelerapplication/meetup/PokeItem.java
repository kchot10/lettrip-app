package com.cookandroid.travelerapplication.meetup;

public class PokeItem {
    private String userName;
    private String userSex;
    private String meetupSuccessNum;
    private String meetupFailNum;
    private String oneLineReview;
    private String imageResource;
    private String birthDate;

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
