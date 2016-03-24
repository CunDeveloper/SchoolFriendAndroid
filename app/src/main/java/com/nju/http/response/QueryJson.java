package com.nju.http.response;

import android.util.Log;

import com.nju.http.request.QueryLimit;
import com.nju.http.request.RequestBodyJson;
import com.nju.test.TestToken;
import com.nju.util.CryptUtil;
import com.nju.util.SchoolFriendGson;

/**
 * Created by cun on 2016/3/24.
 */
public class QueryJson {
    private static final SchoolFriendGson gson = SchoolFriendGson.newInstance();
    public static String queryLimitToString(){
        RequestBodyJson<QueryLimit> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(CryptUtil.getEncryptiedData(gson.toJson(TestToken.getToken())));
        QueryLimit limit = new QueryLimit();
        limit.setOffset(0);limit.setTotal(20);
        bodyJson.setBody(limit);
        String json = gson.toJson(bodyJson);
        return json;
    }
}
