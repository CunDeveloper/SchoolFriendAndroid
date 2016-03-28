package com.nju.http;

import com.squareup.okhttp.Request;

/**
 * Created by xiaojuzhang on 2015/12/17.
 */
public abstract class RequestRunnable implements Runnable {
    protected final Request.Builder mBuilder;

    public RequestRunnable() {
        mBuilder = new Request.Builder();
    }

    public Request.Builder getBuilder() {
        return mBuilder;
    }
}
