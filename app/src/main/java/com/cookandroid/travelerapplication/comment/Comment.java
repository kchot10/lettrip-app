package com.cookandroid.travelerapplication.comment;

public class Comment {

    private String comment_id;
    private String created_date;
    private String modified_date;
    private String content;
    private String article_id;
    private String user_id;
    private String mentioned_user_id;
    private String parent_comment_id;
    private String name;
    private String mentioned_user_name;
    private String image_url;
    private String comment_count;

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getMentioned_user_name() {
        return mentioned_user_name;
    }

    public void setMentioned_user_name(String mentioned_user_name) {
        this.mentioned_user_name = mentioned_user_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMentioned_user_id() {
        return mentioned_user_id;
    }

    public void setMentioned_user_id(String mentioned_user_id) {
        this.mentioned_user_id = mentioned_user_id;
    }

    public String getParent_comment_id() {
        return parent_comment_id;
    }

    public void setParent_comment_id(String parent_comment_id) {
        this.parent_comment_id = parent_comment_id;
    }
}
