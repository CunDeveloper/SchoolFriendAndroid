package com.nju.event;

/**
 * Created by cun on 2016/4/16.
 */
public class MessageContentIdEvent {
    private int id;

    public MessageContentIdEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
