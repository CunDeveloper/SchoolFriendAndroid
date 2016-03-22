package com.nju.util;

import com.nju.http.request.PostRequestJson;
import com.squareup.okhttp.Call;

/**
 * Created by xiaojuzhang on 2016/3/22.
 */
public class CloseRequestUtil {

    public static void close(final PostRequestJson requestJson){
        new Thread(){
            @Override public void run(){
                Call call = requestJson.getCall();
                if (call != null){
                    call.cancel();
                }
            }
        }.start();
    }
}
