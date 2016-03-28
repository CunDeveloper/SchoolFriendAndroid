package com.nju.service;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.http.HttpManager;
import com.nju.http.request.CommentParam;
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

    public static PostRequestJson saveComment(BaseFragment fragment,View view,int id,Callback callback){
        EditText contentET = (EditText) view.findViewById(R.id.comment_edittext);
        CommentParam param = new CommentParam();
        Log.i(TAG, contentET.getText().toString());
        param.setContent(StringBase64.encode(contentET.getText().toString()));
        param.setContentId(id);
        final String json = QueryJson.commentAuthorToString(fragment, param);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNIS_QUESTION_ANSWER_PATH+PathConstant.ALUMNIS_QUESTION_ANSWER_SUB_PATH_SAVE;
        PostRequestJson mRequestJson = new PostRequestJson(url,json,callback);
        Log.e(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson queryComment(BaseFragment fragment,int id,Callback callback){
        ArrayList<IdParam> idParams = new ArrayList<>();
        IdParam idParam = new IdParam();
        idParam.setId(id);
        idParams.add(idParam);
        final String json = QueryJson.queryCommentToString(fragment, idParams);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNS_VOICE_PATH+PathConstant.ALUMNS_VOICE_SUB_PATH_VIEW_GET_COMMENT;
        PostRequestJson mRequestQueryJson = new PostRequestJson(url,json,callback);
        Log.e(TAG, url);
        Log.i(TAG, json);
        HttpManager.getInstance().exeRequest(mRequestQueryJson);
        return mRequestQueryJson;
    }

    public static PostRequestJson  queryMyAsk(BaseFragment fragment,Callback callback,final String level){
        final String json = QueryJson.queryLimitToString(fragment);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNIS_QUESTION_PATH+PathConstant.ALUMNIS_QUESTION_SUB_PATH_VIEW_OWN+"?level="+level;
        PostRequestJson mRequestJson = new PostRequestJson(url,json,callback);
        Log.e(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return  mRequestJson;
    }
}
