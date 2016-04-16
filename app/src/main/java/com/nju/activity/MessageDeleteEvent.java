package com.nju.activity;

/**
 * Created by cun on 2016/4/16.
 */
public class MessageDeleteEvent {
    private String message;

    public MessageDeleteEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
