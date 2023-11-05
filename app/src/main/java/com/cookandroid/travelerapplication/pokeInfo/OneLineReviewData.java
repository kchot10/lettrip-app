package com.cookandroid.travelerapplication.pokeInfo;

public class OneLineReviewData {
    private int profilePhoto;
    private String oneLineReview;

    public OneLineReviewData(int profilePhoto, String oneLineReview) {
        this.profilePhoto = profilePhoto;
        this.oneLineReview = oneLineReview;
    }

    public int getProfilePhoto() {
        return profilePhoto;
    }

    public String getOneLineReview() {
        return oneLineReview;
    }
}
