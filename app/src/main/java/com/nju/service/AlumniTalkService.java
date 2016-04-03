package com.nju.service;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.http.HttpManager;
import com.nju.http.request.CommentParam;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.QueryJson;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;
import com.squareup.okhttp.Callback;

/**
 * Created by cun on 2016/3/30.
 */
public class AlumniTalkService {

    private static final String TAG = AlumniTalkService.class.getSimpleName();
    public static PostRequestJson queryAlumniTalks(BaseFragment fragment,Callback callback,final String level){
        final String json = QueryJson.queryLimitToString(fragment,0);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNI_TALK_PATH+PathConstant.ALUMNI_TALK_SUB_PATH_VIEW_ALL+"?level="+level;
        PostRequestJson mRequestJson = new PostRequestJson(url,json,callback);
        Log.i(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson saveComment(BaseFragment fragment,View view,int id,Callback callback){
        EditText contentET = (EditText) view.findViewById(R.id.comment_edittext);
        CommentParam param = new CommentParam();
        Log.i(TAG,contentET.getText().toString());
        param.setContent(StringBase64.encode(contentET.getText().toString()));
        param.setContentId(id);
        final String json = QueryJson.commentAuthorToString(fragment, param);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNI_TALK_COMMENT_PATH+PathConstant.ALUMNI_TALK_COMMENT_SUB_PATH_SAVE;
        PostRequestJson mRequestJson = new PostRequestJson(url,json,callback);
        Log.e(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }
}
