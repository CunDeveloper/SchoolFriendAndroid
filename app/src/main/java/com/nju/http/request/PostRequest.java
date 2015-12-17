package com.nju.http.request;

import com.nju.http.RequestRunnable;
import com.nju.http.ResponseCallback;
import com.nju.http.SchoolFriendHttp;
import com.squareup.okhttp.Request;

import java.util.HashMap;

/**
 * Created by xiaojuzhang on 2015/12/17.
 */
public class PostRequest implements RequestRunnable {
    private static final String TAG = PostRequest.class.getSimpleName();
    private final ResponseCallback mCallback;
    private final HashMap<String,String> mParams;
    private final String mUrl;
    private final Request.Builder mBuilder;

    public PostRequest(String url,HashMap<String,String> params,ResponseCallback callback){
        mCallback = callback;
        mParams = params;
        mUrl = url;
        mBuilder = new Request.Builder().tag(TAG);
    }
    @Override
    public void run() {
        SchoolFriendHttp.getInstance().AsynPost(mBuilder,mUrl,mParams,mCallback);
    }

    public  String tag(){
        return TAG;
    }
}
