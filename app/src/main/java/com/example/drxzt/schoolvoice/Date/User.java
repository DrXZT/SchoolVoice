package com.example.drxzt.schoolvoice.Date;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class User extends BmobUser {
    private BmobRelation collect;
    private String sex;
    private String img_url;
    private BmobFile icon;
    private String college,major,banji,realname,nickname,association,school;
    private List<String>root;

    public List<String> getRoot() {
        return root;
    }

    public String getAssociation() { return association; }
    public void setAssociation(String association){this.association= association;}//社团

    public String getNickname() { return nickname; }
    public void setNickname(String nickname){this.nickname= nickname;}//昵称

    public String getRealname() { return realname; }
    public void setRealname(String realname){this.realname = realname;}//真名

    public String getBanji() { return banji; }
    public void setBanji(String banji){this.banji = banji;}//班级


    public String getMajor() { return major; }
    public void setMajor(String major){this.major = major;}//专业


    public String getCollege() { return college; }
    public void setCollege(String college){this.college = college;}//学院

    public String getSchool() { return school; }
    public void setSchool(String school){this.school = school;}//学院


    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    private BmobRelation myPost;
    private String words;

    public BmobFile getPhoto() {
        return photo;
    }

    public void setPhoto(BmobFile photo) {
        this.photo = photo;
    }

    private BmobFile photo;
    public BmobRelation getCollect() {
        return collect;
    }
    public void setCollect(BmobRelation collect) {
        this.collect = collect;
    }
    public BmobRelation getMyPost() {
        return myPost;
    }
    public void setMyPost(BmobRelation myPost) {
        this.myPost = myPost;
    }


}
