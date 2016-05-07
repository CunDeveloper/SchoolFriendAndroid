package com.nju.http.request;

/**
 * Created by xiaojuzhang on 2016/3/24.
 */
public class IdParam {
    private int id;

    public IdParam() {
    }

    public IdParam(int commentId) {
        this.id = commentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
