package com.nju.http;

import com.squareup.okhttp.Request;

/**
 * Created by xiaojuzhang on 2015/12/11.
 */
public class SchoolRequestBuilder extends Request.Builder
{
    private Request mRequest;
    public SchoolRequestBuilder (String url) {
        mRequest = new Request.Builder().build();
    }
}
