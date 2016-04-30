package com.nju.service;

import android.util.Log;

import com.nju.fragment.BaseFragment;
import com.nju.http.HttpManager;
import com.nju.http.request.IdParam;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.QueryJson;
import com.nju.util.PathConstant;
import com.squareup.okhttp.Callback;

import java.util.ArrayList;

/**
 * Created by cun on 2016/4/29.
 */
public class AuthorService {

    private static final String TAG = AuthorService.class.getSimpleName();
    public static PostRequestJson queryAuthorImage(BaseFragment fragment,Callback callback){
        final String json = QueryJson.emptyBodyToString(fragment);
        String url = PathConstant.BASE_URL+PathConstant.AUTHOR_PATH+PathConstant.AUTHOR_SUB_PATH_GET_IMAGE;
        PostRequestJson mRequestQueryJson = new PostRequestJson(url,json,callback);
        Log.e(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestQueryJson);
        return mRequestQueryJson;
    }

}
