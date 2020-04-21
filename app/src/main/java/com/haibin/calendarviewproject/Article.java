package com.haibin.calendarviewproject;

import java.io.Serializable;

/**
 * 一个简单的bean
 * Created by huanghaibin on 2017/12/4.
 */
@SuppressWarnings("all")
public class Article implements Serializable {
    private int id;
    private String title;
    private String content;
    private String imgUrl;
    private String desc;
    private String date;
    private int type;

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
