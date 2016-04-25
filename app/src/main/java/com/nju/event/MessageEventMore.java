package com.nju.event;

/**
 * Created by cun on 2016/4/25.
 */
public class MessageEventMore {
    private String message;

    public MessageEventMore(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
