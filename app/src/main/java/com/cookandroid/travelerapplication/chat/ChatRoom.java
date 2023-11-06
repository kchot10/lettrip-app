package com.cookandroid.travelerapplication.chat;

import android.net.Uri;

import androidx.annotation.Nullable;

public class ChatRoom {
    private String room_id;
    private String meet_up_post_id;
    private String profileURI;
    private String write_user_id;
    private String request_user_id;
    private String userName;
    private String time;
    private String chatContent;
    private String meet_up_id;
    private String meet_up_status;

    public ChatRoom() {
    }

    public ChatRoom(String profileURI, String userName, String time, String chatContent) {
        this.profileURI = profileURI;
        this.userName = userName;
        this.time = time;
        this.chatContent = chatContent;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getMeet_up_status() {
        return meet_up_status;
    }

    public void setMeet_up_status(String meet_up_status) {
        this.meet_up_status = meet_up_status;
    }

    public String getMeet_up_id() {
        return meet_up_id;
    }

    public void setMeet_up_id(String meet_up_id) {
        this.meet_up_id = meet_up_id;
    }

    public String getMeet_up_post_id() {
        return meet_up_post_id;
    }

    public void setMeet_up_post_id(String meet_up_post_id) {
        this.meet_up_post_id = meet_up_post_id;
    }

    public String getWrite_user_id() {
        return write_user_id;
    }

    public void setWrite_user_id(String write_user_id) {
        this.write_user_id = write_user_id;
    }

    public String getRequest_user_id() {
        return request_user_id;
    }

    public void setRequest_user_id(String request_user_id) {
        this.request_user_id = request_user_id;
    }

    public String getProfileURI() {
        return profileURI;
    }

    public void setProfileURI(String profileURI) {
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
