package com.nju.http.request;

import com.nju.http.RequestRunnable;
import com.nju.http.SchoolFriendHttp;
import com.squareup.okhttp.Callback;

import java.util.HashMap;

/**
 * Created by xiaojuzhang on 2015/12/17.
 */
public class PostRequest extends RequestRunnable {
    private static final String TAG = PostRequest.class.getSimpleName();
    private Callback mCallback;
    private HashMap<String, String> mParams;
    private String mUrl;
    private String tag;

    public PostRequest() {

    }

    public PostRequest(final String url, final HashMap<String, String> params, final Callback callback, final String TAG) {
        mCallback = callback;
        mParams = params;
        mUrl = url;
        mBuilder.tag(TAG);
    }

    public Callback getCallback() {
        return mCallback;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public HashMap<String, String> getParams() {
        return mParams;
    }

    public void setParams(HashMap<String, String> params) {
        this.mParams = params;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void run() {
        SchoolFriendHttp.getInstance().AsyncPost(mBuilder, mUrl, mParams, mCallback);
    }
}
