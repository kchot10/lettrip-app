package com.cookandroid.travelerapplication.meetup;

public class PokeItem {
    private String userName;
    private String userSex;
    private String meetupSuccessNum;
    private String getMeetupFailNum;
    private String oneLineReview;
    private int imageResource; //프로필 사진 리소스 id

    public PokeItem(String userName, String userSex, String meetupSuccessNum, String getMeetupFailNum, String oneLineReview, int imageResource) {
        this.userName = userName;
        this.userSex = userSex;
        this.meetupSuccessNum = meetupSuccessNum;
        this.getMeetupFailNum = getMeetupFailNum;
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

    public String getMeetupFailNum() {
        return getMeetupFailNum;
    }

    public void setMeetupFailNum(String getMeetupFailNum) {
        this.getMeetupFailNum = getMeetupFailNum;
    }

    public String getOneLineReview() {
        return oneLineReview;
    }

    public void setOneLineReview(String oneLineReview) {
        this.oneLineReview = oneLineReview;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
