package com.nju.http;

import com.nju.model.Image;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cun on 2015/12/5.
 */
public class SchoolFriendHttp {

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");;
    private static SchoolFriendHttp mInstance;
    private OkHttpClient mClient ;
    private static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final String FILE = "file";

    private SchoolFriendHttp() {
        mClient = new OkHttpClient();
    }

    public static SchoolFriendHttp getInstance () {
        if (mInstance == null) {
            mInstance = new SchoolFriendHttp();
        }

        return  mInstance;
    }

    public  String postForm(final String url,final Map<String,String> params) throws IOException {
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

    public  String SynGet (final String url) throws IOException {
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
    public  void AsynGet(final String url,final ResponseCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        mClient.newCall(request).enqueue(callback);
    }

    public  void AsynPost(final Request.Builder builder,final String url,final HashMap<String,String> params,final ResponseCallback callback) {
        final FormEncodingBuilder formBuilder = new FormEncodingBuilder();
        for (Map.Entry<String,String> entry:params.entrySet()) {
            formBuilder.add(entry.getKey(),entry.getValue());
        }
        Request request = builder
                .url(url)
                .post(formBuilder.build())
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
    public String postString (final String url,final String postBody) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

    public  String postStream (final String url,final SchFriendRequestBody requestBody )throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

    public  String postFile (final String url,final SchFriendRequestBody requestBody,final File file) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

    public void postMultiFile(final Request.Builder builder,final String url,final HashMap<String,String> params,final ArrayList<Image> fileNames,ResponseCallback callback) {
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM);
        for (Map.Entry<String,String> entry:params.entrySet()) {
            multipartBuilder.addFormDataPart(entry.getKey(),entry.getValue());
        }
        File sourceFile;
        for (Image image:fileNames) {
            sourceFile = new File(image.getData());
            try {
                multipartBuilder.addFormDataPart(FILE, sourceFile.getName(), RequestBody.create(MediaType.parse(sourceFile.toURL().openConnection().getContentType()),sourceFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Request request = builder
                .url(url)
                .post(multipartBuilder.build())
                .build();
        mClient.newCall(request).enqueue(callback);
    }
}
