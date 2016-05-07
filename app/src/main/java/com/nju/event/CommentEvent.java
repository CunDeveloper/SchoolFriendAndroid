package com.nju.event;

/**
 * Created by cun on 2016/3/31.
 */
public class CommentEvent {

    private int id;

    public CommentEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
