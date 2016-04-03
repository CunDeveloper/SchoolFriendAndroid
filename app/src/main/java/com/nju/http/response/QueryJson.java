package com.nju.http.response;

import android.util.Log;

import com.nju.fragment.BaseFragment;
import com.nju.http.request.CommentParam;
import com.nju.http.request.CommentParamId;
import com.nju.http.request.ContentIdParam;
import com.nju.http.request.IdParam;
import com.nju.http.request.QueryLimit;
import com.nju.http.request.RequestBodyJson;
import com.nju.test.TestToken;
import com.nju.util.CryptUtil;
import com.nju.util.SchoolFriendGson;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/24.
 */
public class QueryJson {
    private static final SchoolFriendGson gson = SchoolFriendGson.newInstance();
    public static String queryLimitToString(final BaseFragment fragment,int offset){
        RequestBodyJson<QueryLimit> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(fragment.getHostActivity().token());
        QueryLimit limit = new QueryLimit();
        limit.setOffset(offset);limit.setTotal(20);
        bodyJson.setBody(limit);
        return gson.toJson(bodyJson);
    }

    public static String queryCommentToString(final BaseFragment fragment,ArrayList<IdParam> idParams){
        RequestBodyJson<ArrayList> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(fragment.getHostActivity().token());
        bodyJson.setBody(idParams);
        return gson.toJson(bodyJson);
    }

    public static String commentAuthorToString(final BaseFragment fragment,final CommentParam commentParam){
        RequestBodyJson<CommentParam> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(fragment.getHostActivity().token());
        bodyJson.setBody(commentParam);
        return gson.toJson(bodyJson);
    }

    public static String commentOtherAuthorToString(final BaseFragment fragment,final CommentParamId commentParam){
        RequestBodyJson<CommentParamId> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(fragment.getHostActivity().token());
        bodyJson.setBody(commentParam);
        return gson.toJson(bodyJson);
    }

    public static String praiseContentIdToString(final BaseFragment fragment,final ContentIdParam idParam){
        RequestBodyJson<ContentIdParam> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(fragment.getHostActivity().token());
        bodyJson.setBody(idParam);
        return gson.toJson(bodyJson);
    }

    public static String deleteContentById(final BaseFragment fragment,final IdParam idParam){
        RequestBodyJson<IdParam> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(fragment.getHostActivity().token());
        bodyJson.setBody(idParam);
        return gson.toJson(bodyJson);
    }
}
