package com.nju.http;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaojuzhang on 2015/12/11.
 */
public class SchoolFriendRequest extends StringRequest {

    private Map<String,String> mParams;
    public SchoolFriendRequest(int method,String url,SchoolFriendOkRep okRep) {
        super(method,url,okRep,new SchoolFriendErrorRep());
    }

    public SchoolFriendRequest(String url,SchoolFriendOkRep okRep) {
        super(url,okRep,new SchoolFriendErrorRep());
    }

    public void setParams(HashMap params) {
        mParams = params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (mParams == null) {
            return super.getParams();
        }
        else {
            return mParams;
        }
    }
}
