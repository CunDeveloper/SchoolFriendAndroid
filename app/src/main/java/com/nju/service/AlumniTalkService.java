package com.nju.service;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.http.HttpManager;
import com.nju.http.request.CommentParam;
import com.nju.http.request.CommentParamId;
import com.nju.http.request.ContentIdParam;
import com.nju.http.request.IdParam;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.QueryJson;
import com.nju.model.AlumniTalk;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/30.
 */
public class AlumniTalkService {

    private static final String TAG = AlumniTalkService.class.getSimpleName();
    public static PostRequestJson queryAlumniTalks(BaseFragment fragment,Callback callback,final String level,String dir){
        final String json = QueryJson.queryLimitToString(fragment,dir);
        Log.i(TAG,json);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNI_TALK_PATH+PathConstant.ALUMNI_TALK_SUB_PATH_VIEW_ALL+"?level="+level;
        PostRequestJson mRequestJson = new PostRequestJson(url,json,callback);
        Log.i(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson queryAlumniTalks(BaseFragment fragment,Callback callback,final String level,String dir,int rowId){
        final String json = QueryJson.queryLimitToString(fragment,rowId,dir);
        Log.i(TAG,json);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNI_TALK_PATH+PathConstant.ALUMNI_TALK_SUB_PATH_VIEW_ALL+"?level="+level;
        PostRequestJson mRequestJson = new PostRequestJson(url,json,callback);
        Log.i(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson queryOwnAlumniTalks(BaseFragment fragment,Callback callback,final String level,String dir,int rowId){
        final String json = QueryJson.queryLimitToString(fragment,rowId,dir);
        Log.i(TAG,json);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNI_TALK_PATH+PathConstant.ALUMNI_TALK_SUB_PATH_VIEW_OWN_TALKS+"?level="+level;
        PostRequestJson mRequestJson = new PostRequestJson(url,json,callback);
        Log.i(TAG, url);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson saveComment(BaseFragment fragment,View view,int talkId,Callback callback){
        EditText contentET = (EditText) view.findViewById(R.id.comment_edittext);
        CommentParam param = new CommentParam();
        Log.i(TAG,contentET.getText().toString());
        param.setContent(StringBase64.encode(contentET.getText().toString()));
        param.setContentId(talkId);
        final String json = QueryJson.commentAuthorToString(fragment, param);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNI_TALK_COMMENT_PATH+PathConstant.ALUMNI_TALK_COMMENT_SUB_PATH_SAVE;
        PostRequestJson mRequestJson = new PostRequestJson(url,json,callback);
        Log.i(TAG, url);
        Log.i(TAG,json);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson saveOtherComment(BaseFragment fragment,View view,int commentId,Callback callback){
        EditText contentET = (EditText) view.findViewById(R.id.comment_edittext);
        CommentParamId param = new CommentParamId();
        Log.i(TAG, contentET.getText().toString());
        param.setContent(StringBase64.encode(contentET.getText().toString()));
        param.setId(commentId);
        final String json = QueryJson.commentOtherAuthorToString(fragment, param);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNI_TALK_COMMENT_PATH+PathConstant.ALUMNI_TALK_COMMENT_SUB_PATH_SAVE;
        PostRequestJson mRequestJson = new PostRequestJson(url,json,callback);
        Log.i(TAG, url);
        Log.i(TAG,json);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson savePraise(BaseFragment fragment,int talkId,Callback callback){
        ContentIdParam param = new ContentIdParam();
        param.setContentId(talkId);
        final String json = QueryJson.praiseContentIdToString(fragment, param);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNI_TALK_PRAISE_PATH+PathConstant.ALUMNI_TALK_PRAISE_SUB_PATH_SAVE;
        PostRequestJson mRequestJson = new PostRequestJson(url,json,callback);
        Log.i(TAG, url);
        Log.i(TAG,json);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson queryPraise(BaseFragment fragment,ArrayList<AlumniTalk> alumniTalks,Callback callback){
        ArrayList<IdParam> idParams = new ArrayList<>();
        IdParam idParam;
        for (AlumniTalk alumniTalk:alumniTalks){
            idParam = new IdParam(alumniTalk.getId());
            idParams.add(idParam);
        }
        final String json = QueryJson.queryCommentToString(fragment, idParams);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNI_TALK_PATH+PathConstant.ALUMNI_TALK_SUB_PATH_VIEW_GET_PRAISE;
        PostRequestJson mRequestJson = new PostRequestJson(url,json,callback);
        Log.i(TAG, url);
        Log.i(TAG,json);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson deleteComment(BaseFragment fragment,int commentId,Callback callback){
        IdParam idParam = new IdParam(commentId);
        final String json = QueryJson.deleteContentById(fragment, idParam);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNI_TALK_COMMENT_PATH+PathConstant.ALUMNI_TALK_COMMENT_SUB_PATH_CANCEL;
        PostRequestJson mRequestJson = new PostRequestJson(url,json,callback);
        Log.i(TAG, url);
        Log.i(TAG,json);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }

    public static PostRequestJson deleteDynamci(BaseFragment fragment,int commentId,Callback callback){
        IdParam idParam = new IdParam(commentId);
        final String json = QueryJson.deleteContentById(fragment, idParam);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNI_TALK_PATH+PathConstant.ALUMNI_TALK_SUB_PATH_CANCEL;
        PostRequestJson mRequestJson = new PostRequestJson(url,json,callback);
        Log.i(TAG, url);
        Log.i(TAG,json);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }



    public static PostRequestJson queryComment(BaseFragment fragment,ArrayList<AlumniTalk> alumniTalks,Callback callback){
        ArrayList<IdParam> idParams = new ArrayList<>();
        IdParam idParam;
        for (AlumniTalk alumniTalk:alumniTalks){
            idParam = new IdParam(alumniTalk.getId());
            idParams.add(idParam);
        }
        final String json = QueryJson.queryCommentToString(fragment,idParams);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNI_TALK_PATH+PathConstant.ALUMNI_TALK_SUB_PATH_VIEW_GET_COMMENTS;
        PostRequestJson mRequestJson = new PostRequestJson(url,json,callback);
        Log.i(TAG, url);
        Log.i(TAG,json);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return mRequestJson;
    }
}
