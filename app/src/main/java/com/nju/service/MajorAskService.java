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
public class MajorAskService {
    private static final String TAG = MajorAskService.class.getSimpleName();

    public static PostRequestJson saveComment(BaseFragment fragment, View view, int id, Callback callback) {
        EditText contentET = (EditText) view.findViewById(R.id.comment_edittext);
        CommentParam param = new CommentParam();
        Log.i(TAG, contentET.getText().toString());
        param.setContent(StringBase64.encode(contentET.getText().toString()));
        param.setContentId(id);
        final String json = QueryJson.commentAuthorToString(fragment, param);
        String url = PathConstant.BASE_URL + PathConstant.ALUMNIS_QUESTION_ANSWER_PATH + PathConstant.ALUMNIS_QUESTION_ANSWER_SUB_PATH_SAVE;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson saveCommentForOther(BaseFragment fragment, View view, int id, Callback callback) {
        EditText contentET = (EditText) view.findViewById(R.id.comment_edittext);
        CommentParamId param = new CommentParamId();
        Log.i(TAG, contentET.getText().toString());
        param.setContent(StringBase64.encode(contentET.getText().toString()));
        param.setId(id);
        final String json = QueryJson.commentOtherAuthorToString(fragment, param);
        String url = PathConstant.BASE_URL + PathConstant.ALUMNIS_QUESTION_ANSWER_PATH + PathConstant.ALUMNIS_QUESTION_ANSWER_SUB_PATH_SAVE;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson queryComment(BaseFragment fragment, int id, Callback callback) {
        ArrayList<IdParam> idParams = new ArrayList<>();
        IdParam idParam = new IdParam();
        idParam.setId(id);
        idParams.add(idParam);
        final String json = QueryJson.queryCommentToString(fragment, idParams);
        String url = PathConstant.BASE_URL + PathConstant.ALUMNIS_QUESTION_PATH + PathConstant.ALUMNIS_QUESTION_SUB_PATH_GET_COMMENTS;
        PostRequestJson mRequestQueryJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestQueryJson);
        return mRequestQueryJson;
    }

    public static PostRequestJson saveCollect(BaseFragment fragment, int id, Callback callback) {
        CollectParam param = new CollectParam(id);
        final String json = QueryJson.collectToString(fragment, param);
        String url = PathConstant.BASE_URL + PathConstant.QUESTION_COLLECT + PathConstant.SAVE_QUESTION_COLLECT;
        PostRequestJson mRequestQueryJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestQueryJson);
        return mRequestQueryJson;
    }

    public static PostRequestJson queryMyAsk(BaseFragment fragment, Callback callback, final String level, String dir) {
        final String json = QueryJson.queryLimitToString(fragment, dir);
        String url = PathConstant.BASE_URL + PathConstant.ALUMNIS_QUESTION_PATH + PathConstant.ALUMNIS_QUESTION_SUB_PATH_VIEW_OWN + "?level=" + level;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson queryMyAsk(BaseFragment fragment, Callback callback, final String level, String dir, int rowId) {
        final String json = QueryJson.queryLimitToString(fragment, rowId, dir);
        String url = PathConstant.BASE_URL + PathConstant.ALUMNIS_QUESTION_PATH + PathConstant.ALUMNIS_QUESTION_SUB_PATH_VIEW_OWN + "?level=" + level;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson queryMajorAsk(BaseFragment fragment, Callback callback, final String level, String dir) {
        final String json = QueryJson.queryLimitToString(fragment, dir);
        Log.i(TAG, json);
        String url = PathConstant.BASE_URL + PathConstant.ALUMNIS_QUESTION_PATH + PathConstant.ALUMNIS_QUESTION_SUB_PATH_VIEW + "?level=" + level;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.i(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson queryMajorAsk(BaseFragment fragment, Callback callback, final String level, String dir, int rowId) {
        final String json = QueryJson.queryLimitToString(fragment, rowId, dir);
        Log.i(TAG, json);
        String url = PathConstant.BASE_URL + PathConstant.ALUMNIS_QUESTION_PATH + PathConstant.ALUMNIS_QUESTION_SUB_PATH_VIEW + "?level=" + level;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.i(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson queryOtherAuthorMajorAsk(BaseFragment fragment, Callback callback, String dir, int rowId,int authorId) {
        final String json = QueryJson.queryLimitToString(fragment, rowId, dir,authorId);
        Log.i(TAG, json);
        String url = PathConstant.BASE_URL + PathConstant.ALUMNIS_QUESTION_PATH + PathConstant.ALUMNIS_QUESTION_SUB_PATH_GET_BYAUTHOR_ID ;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.i(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson queryMajorAskByLabel(BaseFragment fragment, Callback callback, String level, String label, String dir, int rowId) {
        final String json = QueryJson.queryLimitLabelToString(fragment, rowId, dir, label);
        Log.i(TAG, json);
        String url = PathConstant.BASE_URL + PathConstant.ALUMNIS_QUESTION_PATH + PathConstant.ALUMNIS_QUESTION_SUB_PATH_VIEW_LABEL + "?level=" + level;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.i(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson deleteQuestion(BaseFragment fragment, int commentId, Callback callback) {
        IdParam idParam = new IdParam(commentId);
        final String json = QueryJson.deleteContentById(fragment, idParam);
        String url = PathConstant.BASE_URL + PathConstant.ALUMNIS_QUESTION_PATH + PathConstant.ALUMNIS_QUESTION_SUB_PATH_CANCEL;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.i(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson isCollected(BaseFragment fragment, int commentId, Callback callback) {
        IdParam idParam = new IdParam(commentId);
        final String json = QueryJson.idToString(fragment, idParam);
        String url = PathConstant.BASE_URL + PathConstant.QUESTION_COLLECT + PathConstant.IS_COLLECTED;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.i(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson deleteComment(BaseFragment fragment, int commentId, Callback callback) {
        IdParam idParam = new IdParam(commentId);
        final String json = QueryJson.deleteContentById(fragment, idParam);
        String url = PathConstant.BASE_URL + PathConstant.ALUMNIS_QUESTION_ANSWER_PATH + PathConstant.ALUMNIS_QUESTION_ANSWER_SUB_PATH_CANCEL;
        PostRequestJson mRequestJson = new PostRequestJson(url, json, callback);
        Log.i(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson queryCollects(BaseFragment fragment, Callback callback) {
        final String json = QueryJson.emptyBodyToString(fragment);
        String url = PathConstant.BASE_URL + PathConstant.QUESTION_COLLECT + PathConstant.GET_QUESTION_COLLECT;
        PostRequestJson mRequestQueryJson = new PostRequestJson(url, json, callback);
        Log.e(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestQueryJson);
        return mRequestQueryJson;
    }
}
