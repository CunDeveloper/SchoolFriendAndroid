package com.nju.activity;

/**
 * Created by cun on 2016/3/31.
 */
public class CommentEvent {

    public CommentEvent(int id) {
        this.id = id;
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
