package com.cookandroid.travelerapplication;

public class Board {
    private String title;
    private String author;
    private String date_of_writing;
    private String content;
    private String count_views;


    public String getCount_views() {
        return count_views;
    }

    public void setCount_views(String count_views) {
        this.count_views = count_views;
    }


    public Board() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate_of_writing() {
        return date_of_writing;
    }

    public void setDate_of_writing(String date_of_writing) {
        this.date_of_writing = date_of_writing;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
