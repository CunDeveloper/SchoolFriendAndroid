package com.nju.http.request;

import com.nju.http.RequestRunnable;
import com.nju.http.SchoolFriendHttp;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;

import java.util.HashMap;

/**
 * Created by xiaojuzhang on 2015/12/17.
 */
public class PostRequest extends RequestRunnable {
    private static final String TAG = PostRequest.class.getSimpleName();
    private  Callback mCallback;
    private  HashMap<String,String> mParams;
    private  String mUrl;
    private String tag;

    public Callback getmCallback() {
        return mCallback;
    }

    public void setmCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public HashMap<String, String> getmParams() {
        return mParams;
    }

    public void setmParams(HashMap<String, String> mParams) {
        this.mParams = mParams;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public PostRequest() {

    }

    public PostRequest(final String url, final HashMap<String, String> params,final Callback callback,final String TAG){
        mCallback = callback;
        mParams = params;
        mUrl = url;
        mBuilder.tag(TAG);
    }
    @Override
    public void run() {
        SchoolFriendHttp.getInstance().AsynPost(mBuilder, mUrl, mParams, mCallback);
    }
}
