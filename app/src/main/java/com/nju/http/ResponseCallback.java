package com.nju.http;

import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by cun on 2015/12/9.
 */
public abstract class ResponseCallback implements Callback {

    abstract public void onFail(final Exception error);
    abstract public void onSuccess(final String responseBody);

    @Override
    public void onFailure(final Request request, final IOException e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                e.printStackTrace();
                if (e.getMessage().contains("Canceled") || e.getMessage().contains("Socket closed")) {
                   // ToastUtil.show(R.string.canceled);
                } else {
                    onFail(e);
                }
            }
        });
    }

    @Override
    public void onResponse(final Response response) throws IOException {
        if (!response.isSuccessful() || response.body() == null) {
            onFailure(response.request(), new IOException("Failed"));
            return;
        }
        final String result = response.body().string();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onSuccess(result);
            }
        });
    }

    private void runOnUiThread(Runnable task) {
        new Handler(Looper.getMainLooper()).post(task);
    }
}
