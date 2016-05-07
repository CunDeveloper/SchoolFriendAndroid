package com.nju.activity;

import android.app.Application;
import android.util.Log;

import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.splunk.mint.Mint;

/**
 * Created by xiaojuzhang on 2016/3/25.
 */
public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();
    ResponseCallback collegeCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {

        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
        }
    };
    private PostRequestJson mRequestJson;

    @Override
    public void onCreate() {
        super.onCreate();
        Mint.initAndStartSession(getApplicationContext(), "ba2df536");
    }


}
