package com.nju.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by xiaojuzhang on 2015/12/14.
 */
public class GsonRequest<T> extends Request<T> {
    private final Gson mGson = new Gson();
    private Map<String,String> mHeaders;
    private final Response.Listener<T> mListener;
    private final Type mType;

    public GsonRequest(String url ,Type type,Response.Listener<T> listener) {
        super(Method.POST,url,new SchoolFriendErrorRep());
        mType = type;
        mListener = listener;
    }

    public void setHeaders(Map<String,String> headers) {
        mHeaders = headers;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            T parseObject = mGson.fromJson(json,mType);
            return Response.success(parseObject,HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}
