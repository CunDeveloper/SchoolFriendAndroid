package com.nju.http;

import com.nju.model.School;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Created by cun on 2015/12/5.
 */
public class SchoolFriendHttp {

    private static OkHttpClient mClient = null;

    private SchoolFriendHttp() {
        mClient = new OkHttpClient();
    }

    private static void init() {
        if (mClient == null) {
            new SchoolFriendHttp();
        }
    }

    public static String postForm(String url,Map<String,String> params) throws IOException {
        init();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Map.Entry<String,String> entry:params.entrySet()) {
            builder.add(entry.getKey(),entry.getValue());
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

}
