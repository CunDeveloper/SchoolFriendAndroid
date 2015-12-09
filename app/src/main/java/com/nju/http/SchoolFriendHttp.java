package com.nju.http;

import com.nju.model.School;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Callback;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by cun on 2015/12/5.
 */
public class SchoolFriendHttp {

    private static final OkHttpClient mClient = new OkHttpClient() ;
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    public static String postForm(String url,Map<String,String> params) throws IOException {
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

    public static String SynGet (String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * The callback is made after the response headers are ready.
     * Reading the response body may still block.
     * OkHttp doesn't currently offer asynchronous APIs to receive a response body in parts.
     * @param url
     * @param callback
     */
    public static void AsynGet(String url,ResponseCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        mClient.newCall(request).enqueue(callback);
    }

    /**
     * Use an HTTP POST to send a request body to a service
     * osts a markdown document to a web service that renders markdown as HTML
     *  the entire request body is in memory simultaneously
     *  avoid posting large (greater than 1 MiB) documents using this API
     * @param url
     * @param postBody
     * @return
     * @throws IOException
     */
    public static String postString (String url,String postBody) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

    public static String postStream (String url,SchFriendRequestBody requestBody )throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

    public static String postFile (String url,SchFriendRequestBody requestBody,File file) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }


}
