package com.nju.event;

/**
 * Created by cun on 2016/5/1.
 */
public class MessageComplainEvent {
    private String message;

    public MessageComplainEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
