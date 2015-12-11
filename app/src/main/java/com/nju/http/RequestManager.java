package com.nju.http;

import android.os.Handler;

/**
 * Created by xiaojuzhang on 2015/12/11.
 */
public class RequestManager {
    private static RequestManager mInstance;
    private Handler mHandler;

    static {
        mInstance = new RequestManager();
    }

    private RequestManager () {

    }

}
