package com.nju.http.request;

import com.nju.http.RequestRunnable;
import com.nju.http.ResponseCallback;
import com.nju.http.SchoolFriendHttp;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;

import java.util.HashMap;

/**
 * Created by xiaojuzhang on 2015/12/17.
 */
public class PostRequest1 implements RequestRunnable {
    private static final String TAG = PostRequest1.class.getSimpleName();
    private  Callback mCallback;
    private  HashMap<String,String> mParams;
    private  String mUrl;
    private  Request.Builder mBuilder;

    public PostRequest1(){
        mBuilder = new Request.Builder();
    }

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

    public PostRequest1(String url, HashMap<String, String> params, Callback callback){
        mCallback = callback;
        mParams = params;
        mUrl = url;
        mBuilder = new Request.Builder().tag(TAG);
    }
    @Override
    public void run() {
        SchoolFriendHttp.getInstance().AsynPost1(mBuilder,mUrl,mParams,mCallback);
    }

    public  String tag(){
        return TAG;
    }
}
