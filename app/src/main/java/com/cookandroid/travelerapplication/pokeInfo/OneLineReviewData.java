package com.cookandroid.travelerapplication.pokeInfo;

import android.provider.ContactsContract;

public class OneLineReviewData {
    private String NickName;
    private String profilePhoto;
    private String oneLineReview;

    public OneLineReviewData(String profilePhoto, String oneLineReview, String NickName) {
        this.profilePhoto = profilePhoto;
        this.oneLineReview = oneLineReview;
        this.NickName = NickName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getOneLineReview() {
        return oneLineReview;
    }

    public String getNickName() {
        return NickName;
    }
}
