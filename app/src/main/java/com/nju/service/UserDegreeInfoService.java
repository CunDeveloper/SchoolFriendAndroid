package com.nju.service;

import android.util.Log;

import com.nju.fragment.BaseFragment;
import com.nju.http.HttpManager;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.QueryJson;
import com.nju.model.AuthorInfo;
import com.nju.util.PathConstant;
import com.squareup.okhttp.Callback;

/**
 * Created by cun on 2016/5/2.
 */
public class UserDegreeInfoService {

    private static final String TAG = UserDegreeInfoService.class.getSimpleName();

    public static PostRequestJson isAuthorization(BaseFragment fragment, Callback callback) {
        final String json = QueryJson.emptyBodyToString(fragment);
        String url = PathConstant.BASE_URL + PathConstant.USER_DEGREE_INFO_PATH + PathConstant.USER_DEGREE_IS_AUTHORIZATION;
        PostRequestJson mRequestQueryJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestQueryJson);
        return mRequestQueryJson;
    }

    public static PostRequestJson query(BaseFragment fragment, Callback callback) {
        final String json = QueryJson.emptyBodyToString(fragment);
        String url = PathConstant.BASE_URL + PathConstant.USER_DEGREE_INFO_PATH + PathConstant.USER_DEGREE_QUERY;
        PostRequestJson mRequestQueryJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestQueryJson);
        return mRequestQueryJson;
    }

    public static PostRequestJson queryOtherAuthorDegrees(BaseFragment fragment,AuthorInfo authorInfo, Callback callback) {
        final String json = QueryJson.authorToString(fragment, authorInfo);
        String url = PathConstant.BASE_URL + PathConstant.USER_DEGREE_INFO_PATH + PathConstant.USER_DEGREE_QUERY_OTHER_AUTHOR;
        PostRequestJson mRequestQueryJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestQueryJson);
        return mRequestQueryJson;
    }

}
