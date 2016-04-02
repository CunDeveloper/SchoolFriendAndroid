package com.nju.activity;

/**
 * Created by cun on 2016/4/2.
 */
public class PersonInfoEvent {
    private String userName;

    public PersonInfoEvent(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
