package com.nju.service;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.http.HttpManager;
import com.nju.http.request.CollectParam;
import com.nju.http.request.CommentParam;
import com.nju.http.request.CommentParamId;
import com.nju.http.request.IdParam;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.QueryJson;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;
import com.squareup.okhttp.Callback;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/28.
 */
public class RecommendWorkService {

    private static final String TAG = RecommendWorkService.class.getSimpleName();

    public static PostRequestJson queryRecommendWork(BaseFragment fragment, Callback callback, final String level, String dir) {
        final String json = QueryJson.queryLimitToString(fragment, dir);
        Log.i(TAG, json);
        String url = PathConstant.BASE_URL + PathConstant.RECOMMEND_WORK_PATH + PathConstant.RECOMMEND_WORK_SUB_PATH_VIEW_OWN + "?level=" + level;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson queryRecommendWork(BaseFragment fragment, Callback callback, final String level, String dir, int rowId) {
        final String json = QueryJson.queryLimitToString(fragment, rowId, dir);
        Log.i(TAG, json);
        String url = PathConstant.BASE_URL + PathConstant.RECOMMEND_WORK_PATH + PathConstant.RECOMMEND_WORK_SUB_PATH_VIEW_OWN + "?level=" + level;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson queryOtherAuthorRecommendWork(BaseFragment fragment, Callback callback, String dir, int rowId,int authorId) {
        final String json = QueryJson.queryLimitToString(fragment, rowId, dir,authorId);
        Log.i(TAG, json);
        String url = PathConstant.BASE_URL + PathConstant.RECOMMEND_WORK_PATH + PathConstant.RECOMMEND_WORK_SUB_PATH_GET_BYAUTHOR_ID ;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.i(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson queryRecommendWorkByType(BaseFragment fragment, Callback callback, final String level, int type, String dir) {
        final String json = QueryJson.queryLimitByTypeToString(fragment, dir, type);
        Log.i(TAG, json);
        String url = PathConstant.BASE_URL + PathConstant.RECOMMEND_WORK_PATH + PathConstant.RECOMMEND_WORK_SUB_PATH_BY_TYPE + "?level=" + level;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        Log.e(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson querySingleComment(BaseFragment fragment, int id, Callback callback) {
        ArrayList<IdParam> idParams = new ArrayList<>();
        IdParam idParam = new IdParam();
        idParam.setId(id);
        idParams.add(idParam);
        final String json = QueryJson.queryCommentToString(fragment, idParams);
        String url = PathConstant.BASE_URL + PathConstant.RECOMMEND_WORK_PATH + PathConstant.RECOMMED_WORK_SUB_PATH_GET_ASKS;
        PostRequestJson mRequestQueryJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestQueryJson);
        return mRequestQueryJson;
    }

    public static PostRequestJson saveAsk(BaseFragment fragment, View view, int id, Callback callback) {
        EditText contentET = (EditText) view.findViewById(R.id.comment_edittext);
        CommentParam param = new CommentParam();
        Log.i(TAG, contentET.getText().toString());
        param.setContent(StringBase64.encode(contentET.getText().toString()));
        param.setContentId(id);
        final String json = QueryJson.commentAuthorToString(fragment, param);
        Log.i(TAG, json);
        String url = PathConstant.BASE_URL + PathConstant.RECOMMEND_WORK_PATH + PathConstant.RECOMMEND_WORK_SUB_PATH_ASK;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson saveAskForOther(BaseFragment fragment, View view, int id, Callback callback) {
        EditText contentET = (EditText) view.findViewById(R.id.comment_edittext);
        CommentParamId param = new CommentParamId();
        Log.i(TAG, contentET.getText().toString());
        param.setContent(StringBase64.encode(contentET.getText().toString()));
        param.setId(id);
        final String json = QueryJson.commentOtherAuthorToString(fragment, param);
        Log.i(TAG, json);
        String url = PathConstant.BASE_URL + PathConstant.RECOMMEND_WORK_PATH + PathConstant.RECOMMEND_WORK_SUB_PATH_ASK;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson queryMyRecommendWork(BaseFragment fragment, Callback callback, final String level, String dir) {
        final String json = QueryJson.queryLimitToString(fragment, dir);
        String url = PathConstant.BASE_URL + PathConstant.RECOMMEND_WORK_PATH + PathConstant.RECOMMEND_WORK_SUB_PATH_VIEW_OWN + "?level=" + level;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson queryMyRecommendWork(BaseFragment fragment, Callback callback, final String level, String dir, int rowId) {
        final String json = QueryJson.queryLimitToString(fragment, rowId, dir);
        String url = PathConstant.BASE_URL + PathConstant.RECOMMEND_WORK_PATH + PathConstant.RECOMMEND_WORK_SUB_PATH_VIEW_OWN + "?level=" + level;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson deleteMyRecommend(BaseFragment fragment, int id, Callback callback) {
        IdParam idParam = new IdParam();
        idParam.setId(id);
        final String json = QueryJson.deleteContentById(fragment, idParam);
        String url = PathConstant.BASE_URL + PathConstant.RECOMMEND_WORK_PATH + PathConstant.RECOMMEND_WORK_SUB_PATH_CANCEL;
        PostRequestJson mRequestQueryJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestQueryJson);
        return mRequestQueryJson;
    }

    public static PostRequestJson saveCollect(BaseFragment fragment, int id, Callback callback) {
        CollectParam param = new CollectParam(id);
        final String json = QueryJson.collectToString(fragment, param);
        String url = PathConstant.BASE_URL + PathConstant.RECOMMEND_COLLECT + PathConstant.SAVE_RECOMMEND_COLLECT;
        PostRequestJson mRequestQueryJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestQueryJson);
        return mRequestQueryJson;
    }

    public static PostRequestJson queryCollects(BaseFragment fragment, Callback callback) {
        final String json = QueryJson.emptyBodyToString(fragment);
        String url = PathConstant.BASE_URL + PathConstant.RECOMMEND_COLLECT + PathConstant.GET_RECOMMEND_COLLECT;
        PostRequestJson mRequestQueryJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestQueryJson);
        return mRequestQueryJson;
    }

    public static PostRequestJson deleteComment(BaseFragment fragment, int commentId, Callback callback) {
        IdParam idParam = new IdParam(commentId);
        final String json = QueryJson.deleteContentById(fragment, idParam);
        String url = PathConstant.BASE_URL + PathConstant.RECOMMEND_COLLECT + PathConstant.ALUMNI_TALK_COMMENT_SUB_PATH_CANCEL;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.i(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson isCollected(BaseFragment fragment, int commentId, Callback callback) {
        IdParam idParam = new IdParam(commentId);
        final String json = QueryJson.idToString(fragment, idParam);
        String url = PathConstant.BASE_URL + PathConstant.RECOMMEND_COLLECT + PathConstant.IS_COLLECTED;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.i(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }
}
