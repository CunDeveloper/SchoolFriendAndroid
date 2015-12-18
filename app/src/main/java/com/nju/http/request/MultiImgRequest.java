package com.nju.http.request;

import com.nju.http.RequestRunnable;
import com.nju.http.ResponseCallback;
import com.nju.http.SchoolFriendHttp;
import com.nju.model.BitmaWrapper;
import com.nju.model.Image;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xiaojuzhang on 2015/12/17.
 */
public class MultiImgRequest implements RequestRunnable {
    private static final String TAG = MultiImgRequest.class.getSimpleName();
    private final ResponseCallback mCallback;
    private final HashMap<String,String> mParams;
    private final String mUrl;
    private final Request.Builder mBuilder;
    private final ArrayList<BitmaWrapper> mBitmapWrapers;

    public MultiImgRequest(String url, HashMap<String, String> params, ArrayList<BitmaWrapper> bitmaWrappers, ResponseCallback callback){
        mCallback = callback;
        mParams = params;
        mUrl = url;
        mBitmapWrapers = bitmaWrappers;
        mBuilder = new Request.Builder().tag(TAG);
    }
    @Override
    public void run() {
        SchoolFriendHttp.getInstance().postMultiImg(mBuilder,mUrl,mParams,mBitmapWrapers,mCallback);
    }

    public  String tag(){
        return TAG;
    }
}
