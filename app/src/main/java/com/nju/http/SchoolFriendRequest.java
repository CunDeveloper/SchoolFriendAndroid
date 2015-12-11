package com.nju.http;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by xiaojuzhang on 2015/12/11.
 */
public class SchoolFriendRequest extends StringRequest {

    public SchoolFriendRequest(int method,String url,SchoolFriendOkRep okRep,SchoolFriendErrorRep errorRep) {
        super(method,url,okRep,errorRep);
    }

    public SchoolFriendRequest(String url,SchoolFriendOkRep okRep,SchoolFriendErrorRep errorRep) {
        super(url,okRep,errorRep);
    }

    private SchoolFriendRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    private SchoolFriendRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }


}
