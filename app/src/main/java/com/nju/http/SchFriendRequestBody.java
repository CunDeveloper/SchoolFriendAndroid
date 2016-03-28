package com.nju.http;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;

import okio.BufferedSink;

/**
 * Created by cun on 2015/12/9.
 */
public abstract class SchFriendRequestBody extends RequestBody {

    @Override
    public MediaType contentType() {
        return null;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
    }
}
