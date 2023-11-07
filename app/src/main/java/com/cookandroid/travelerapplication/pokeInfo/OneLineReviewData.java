package com.cookandroid.travelerapplication.pokeInfo;

public class OneLineReviewData {
    private String profilePhoto;
    private String oneLineReview;

    public OneLineReviewData(String profilePhoto, String oneLineReview) {
        this.profilePhoto = profilePhoto;
        this.oneLineReview = oneLineReview;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getOneLineReview() {
        return oneLineReview;
    }
}
