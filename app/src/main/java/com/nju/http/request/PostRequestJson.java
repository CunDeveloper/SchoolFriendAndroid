package com.nju.http.request;

import com.nju.http.RequestRunnable;
import com.nju.http.SchoolFriendHttp;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;

/**
 * Created by xiaojuzhang on 2016/3/22.
 */
public class PostRequestJson extends RequestRunnable {

    private Callback mCallback;
    private String mUrl;
    private String mJson;
    private Call call;


    public PostRequestJson(String url, String json, Callback callback) {
        this.mCallback = callback;
        this.mUrl = url;
        this.mJson = json;
    }

    public Call getCall() {
        return call;
    }

    @Override
    public void run() {
        call = SchoolFriendHttp.getInstance().AsyncPostJson(mUrl, mJson, mCallback);
    }


}
