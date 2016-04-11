package com.nju.http.request;

import com.nju.http.RequestRunnable;
import com.nju.http.SchoolFriendHttp;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;

/**
 * Created by cun on 2016/4/11.
 */
public class RequestImage extends RequestRunnable {
    private Callback mCallback;
    private String mUrl;
    private Call call;


    public RequestImage(String url,Callback callback) {
        this.mCallback = callback;
        this.mUrl = url;
    }

    public Call getCall(){
        return call;
    }

    @Override
    public void run() {
        call = SchoolFriendHttp.getInstance().AsyncGetStream(mUrl,mCallback);
    }
}
