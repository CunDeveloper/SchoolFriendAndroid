package com.nju.http;

import android.graphics.Bitmap;
import android.util.Log;

import com.nju.model.BitmapWrapper;
import com.nju.model.Image;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by cun on 2015/12/5.
 */
public class SchoolFriendHttp {

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");;
    private static final String TAG = SchoolFriendHttp.class.getSimpleName();
    private static SchoolFriendHttp mInstance;
    private static OkHttpClient mClient ;
    private static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final String FILE = "file";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static BlockingDeque<Request> requestQueue;

    private SchoolFriendHttp() {
        if (mClient == null) {
            mClient = new OkHttpClient();
            requestQueue = new LinkedBlockingDeque<>();
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            mClient.setCookieHandler(cookieManager);
        }
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

    public InputStream SynGetStream (final String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = mClient.newCall(request).execute();
        return response.body().byteStream();
    }

    /**
     * The callback is made after the response headers are ready.
     * Reading the response body may still block.
     * OkHttp doesn't currently offer asynchronous APIs to receive a response body in parts.
     * @param url
     * @param callback
     */
    public  void AsyncGet(final String url, final ResponseCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        mClient.newCall(request).enqueue(callback);
    }

    public  void AsyncPost(final Request.Builder builder, final String url, final HashMap<String, String> params, final Callback callback) {
        final FormEncodingBuilder formBuilder = new FormEncodingBuilder();
        for (Map.Entry<String,String> entry:params.entrySet()) {
            formBuilder.add(entry.getKey(),entry.getValue());
            Log.e(TAG,entry.getKey()+"===="+entry.getValue());
        }
        Request request = builder
                .url(url)
                .post(formBuilder.build())
                .build();
        requestQueue.addFirst(request);
        mClient.newCall(request).enqueue(callback);
    }

    public Call AsyncPostJson( final String url, final String json, final Callback callback) {
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        requestQueue.addFirst(request);
        Call call= mClient.newCall(request);
        call.enqueue(callback);
        return call;
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
        multipartBuilder.type(MultipartBuilder.MIXED);
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
        requestQueue.add(request);
        mClient.newCall(request).enqueue(callback);
    }

    public void postMultiImg(final Request.Builder builder,final String url,final HashMap<String,String> params,final ArrayList<BitmapWrapper> bitmapWrappers,ResponseCallback callback) {
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM);
        for (Map.Entry<String,String> entry:params.entrySet()) {
            multipartBuilder.addFormDataPart(entry.getKey(),entry.getValue());
        }

        for (BitmapWrapper bitmapWrapper : bitmapWrappers) {
            Bitmap bitmap = bitmapWrapper.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            multipartBuilder.addFormDataPart(FILE, bitmapWrapper.getFileName(), RequestBody.create(MediaType.parse(bitmapWrapper.getFileType()),b));
        }
        Request request = builder
                .url(url)
                .post(multipartBuilder.build())
                .build();
        mClient.newCall(request).enqueue(callback);
    }

    public static void close() {
        if (requestQueue != null && !requestQueue.isEmpty()) {
            Request request = requestQueue.removeFirst();
            if (request != null) {
                mClient.cancel(request.tag());
            }
        }
    }
}
