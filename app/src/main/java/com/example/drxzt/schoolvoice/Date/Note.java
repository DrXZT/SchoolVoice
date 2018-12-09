package com.example.drxzt.schoolvoice.Date;
import cn.bmob.v3.BmobObject;

public class Note extends BmobObject {
    private User user;
    private String content;
    private String time,remindtime;
    private Boolean complete =false;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user= user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content= content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemindtime() {
        return remindtime;
    }

    public void setRemindtime(String remindtime) {
        this.remindtime = remindtime;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete){
        this.complete= complete;
    }
}