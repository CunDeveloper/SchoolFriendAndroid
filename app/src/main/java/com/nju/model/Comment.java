package com.nju.model;

/**
 * Created by xiaojuzhang on 2015/11/23.
 */
public class Comment {

    private int id;
    private String content;
    private String cUserName;
    private String reUserName;
    private String praiseUserName ="";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getcUserName() {
        return cUserName;
    }

    public void setcUserName(String cUserName) {
        this.cUserName = cUserName;
    }

    public String getReUserName() {
        return reUserName;
    }

    public void setReUserName(String reUserName) {
        this.reUserName = reUserName;
    }

    public String getPraiseUserName() {
        return praiseUserName;
    }

    public void setPraiseUserName(String praiseUserName) {
        this.praiseUserName = praiseUserName;
    }
}
