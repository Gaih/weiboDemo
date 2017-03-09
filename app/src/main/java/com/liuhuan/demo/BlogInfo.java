package com.liuhuan.demo;

/**
 * Created by Administrator on 2017/3/9.
 */

public class BlogInfo {
    private String id;
    private String content;
    private String title;
    private String user_id;
    private String pub_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setPub_date(String pub_date) {
        this.pub_date = pub_date;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPub_date() {
        return pub_date;
    }
}
