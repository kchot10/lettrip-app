package com.cookandroid.travelerapplication.chat;

import java.util.Date;

public class Chat {
    String room_id;
    String message;
    int send_user_id;
    int receive_user_id;
    String created_at;
    Boolean is_image;

    public Chat() {
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSend_user_id() {
        return send_user_id;
    }

    public void setSend_user_id(int send_user_id) {
        this.send_user_id = send_user_id;
    }

    public int getReceive_user_id() {
        return receive_user_id;
    }

    public void setReceive_user_id(int receive_user_id) {
        this.receive_user_id = receive_user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Boolean getIs_image() {
        return is_image;
    }

    public void setIs_image(Boolean is_image) {
        this.is_image = is_image;
    }
}
