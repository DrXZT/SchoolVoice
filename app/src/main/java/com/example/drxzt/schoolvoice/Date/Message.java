package com.example.drxzt.schoolvoice.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/5/23.
 */

public class Message extends BmobObject {
    private String title;
    private String content;
    private String time;
    private User user;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;

    }

    public void setTitle(String title) {
        this.title = title;
    }
}
