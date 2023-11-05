package com.cookandroid.travelerapplication.chat;

import android.net.Uri;

import androidx.annotation.Nullable;

public class ChatRoom {
    private Uri profileURI;
    private String userName;
    private String time;
    private String chatContent;

    public ChatRoom(@Nullable Uri profileURI, String userName, String time, String chatContent) {
        this.profileURI = profileURI;
        this.userName = userName;
        this.time = time;
        this.chatContent = chatContent;
    }

    public Uri getProfileURI() {
        return profileURI;
    }

    public void setProfileURI(Uri profileURI) {
        this.profileURI = profileURI;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }
}
