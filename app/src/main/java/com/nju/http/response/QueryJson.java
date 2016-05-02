package com.nju.http.response;

import android.util.Log;

import com.nju.activity.BaseActivity;
import com.nju.fragment.AlumniDynamicFragment;
import com.nju.fragment.AlumniVoiceFragment;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.MajorAskFragment;
import com.nju.fragment.MyAskFragment;
import com.nju.fragment.MyDynamicFragment;
import com.nju.fragment.MyRecommendFragment;
import com.nju.fragment.MyVoiceFragment;
import com.nju.fragment.RecommendWorkFragment;
import com.nju.http.request.CollectParam;
import com.nju.http.request.CommentParam;
import com.nju.http.request.CommentParamId;
import com.nju.http.request.ContentIdParam;
import com.nju.http.request.IdParam;
import com.nju.http.request.QueryLimit;
import com.nju.http.request.QueryLimitLabel;
import com.nju.http.request.QueryLimitType;
import com.nju.http.request.RequestBodyJson;
import com.nju.model.AuthenticationAccessToken;
import com.nju.model.Author;
import com.nju.model.AuthorInfo;
import com.nju.test.TestToken;
import com.nju.util.Constant;
import com.nju.util.CryptUtil;
import com.nju.util.SchoolFriendGson;

import java.util.ArrayList;

/**
 * Created by cun on 2016/3/24.
 */
public class QueryJson {
    private static final SchoolFriendGson gson = SchoolFriendGson.newInstance();
    private static final String TAG = QueryJson.class.getSimpleName();

    public static String queryLimitToString(final BaseFragment fragment,String dir){
        RequestBodyJson<QueryLimit> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(fragment.getHostActivity().token());
        bodyJson.setBody(queryLimit(fragment,dir));
        return gson.toJson(bodyJson);
    }

    public static String queryLimitToString(final BaseFragment fragment,int rowId,String dir){
        RequestBodyJson<QueryLimit> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(fragment.getHostActivity().token());
        QueryLimit limit = new QueryLimit();
        limit.setLimit(Constant.LIMIT);
        limit.setRowId(rowId);
        limit.setDir(dir);
        bodyJson.setBody(limit);
        return gson.toJson(bodyJson);
    }

    public static String saveAuthorToString(final BaseFragment fragment,Author authorInfo){
        RequestBodyJson<Author> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization("");
        bodyJson.setBody(authorInfo);
        return gson.toJson(bodyJson);
    }


    public static String queryLimitLabelToString(final BaseFragment fragment,int rowId,String dir,String label){
        RequestBodyJson<QueryLimitLabel> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(fragment.getHostActivity().token());
        QueryLimitLabel limit = new QueryLimitLabel();
        limit.setLimit(Constant.LIMIT);
        limit.setRowId(rowId);
        limit.setDir(dir);
        limit.setLabel(label);
        bodyJson.setBody(limit);
        return gson.toJson(bodyJson);
    }

    public static String queryLimitByTypeToString(final BaseFragment fragment,String dir,int type){
        RequestBodyJson<QueryLimit> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(fragment.getHostActivity().token());
        QueryLimitType limit = new QueryLimitType();
        QueryLimit queryLimit = queryLimit(fragment,dir);
        limit.setRowId(queryLimit.getRowId());
        limit.setDir(queryLimit.getDir());
        limit.setLimit(queryLimit.getLimit());
        limit.setType(type);
        bodyJson.setBody(limit);
        return gson.toJson(bodyJson);
    }

    public static String queryCommentToString(final BaseFragment fragment,ArrayList<IdParam> idParams){
        RequestBodyJson<ArrayList> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(fragment.getHostActivity().token());
        bodyJson.setBody(idParams);
        return gson.toJson(bodyJson);
    }

    public static String emptyBodyToString(final BaseFragment fragment){
        RequestBodyJson<String> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(fragment.getHostActivity().token());
        String token = CryptUtil.dataDecryption(fragment.getHostActivity().token());
        Log.i(TAG,token);
        bodyJson.setBody("");
        return gson.toJson(bodyJson);
    }

    public static String commentAuthorToString(final BaseFragment fragment,final CommentParam commentParam){
        RequestBodyJson<CommentParam> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(fragment.getHostActivity().token());
        bodyJson.setBody(commentParam);
        return gson.toJson(bodyJson);
    }

    public static String collectToString(final BaseFragment fragment,final CollectParam collectParam){
        RequestBodyJson<CollectParam> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(fragment.getHostActivity().token());
        bodyJson.setBody(collectParam);
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

    public static String emptyBodyToString(final BaseActivity activity){
        RequestBodyJson<String> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(activity.token());
        bodyJson.setBody("");
        return gson.toJson(bodyJson);
    }

    public static String deleteContentById(final BaseFragment fragment,final IdParam idParam){
        RequestBodyJson<IdParam> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(fragment.getHostActivity().token());
        bodyJson.setBody(idParam);
        return gson.toJson(bodyJson);
    }

    private static QueryLimit queryLimit(BaseFragment fragment,String dir){
        QueryLimit limit = new QueryLimit();
        if (dir != null && dir.equals(Constant.NEXT) ){
            if (fragment instanceof AlumniDynamicFragment){
                final int rowId = fragment.getHostActivity()
                        .getSharedPreferences().getInt(Constant.DYNAMIC_NEXT_ID,0);
                limit.setRowId(rowId);
            }else if (fragment instanceof AlumniVoiceFragment){
                final int rowId = fragment.getHostActivity()
                        .getSharedPreferences().getInt(Constant.VOICE_NEXT_ID,0);
                limit.setRowId(rowId);
            }else if (fragment instanceof RecommendWorkFragment){
                final int rowId = fragment.getHostActivity()
                        .getSharedPreferences().getInt(Constant.RECOMMEND_NEXT_ID,0);
                limit.setRowId(rowId);
            }else if (fragment instanceof MajorAskFragment){
                final int rowId = fragment.getHostActivity()
                        .getSharedPreferences().getInt(Constant.ASK_NEXT_ID,0);
                limit.setRowId(rowId);
            }else if (fragment instanceof MyVoiceFragment){
                final int rowId = fragment.getHostActivity()
                        .getSharedPreferences().getInt(Constant.MY_VOICE_NEXT_ID,0);
                limit.setRowId(rowId);
            }else if (fragment instanceof MyAskFragment){
                final int rowId = fragment.getHostActivity()
                        .getSharedPreferences().getInt(Constant.MY_ASK_NEXT_ID,0);
                limit.setRowId(rowId);
            }else if (fragment instanceof MyRecommendFragment){
                final int rowId = fragment.getHostActivity()
                        .getSharedPreferences().getInt(Constant.MY_RECOMMEND_NEXT_ID,0);
                limit.setRowId(rowId);
            }else if (fragment instanceof MyDynamicFragment){
                final int rowId = fragment.getHostActivity()
                        .getSharedPreferences().getInt(Constant.MY_DYNAMIC_NEXT_ID,0);
                limit.setRowId(rowId);
            }

        }else {
            if (fragment instanceof AlumniDynamicFragment){
                final int rowId = fragment.getHostActivity()
                        .getSharedPreferences().getInt(Constant.DYNAMIC_PRE_ID,0);
                limit.setRowId(rowId);
            }else if (fragment instanceof AlumniVoiceFragment){
                final int rowId = fragment.getHostActivity()
                        .getSharedPreferences().getInt(Constant.VOICE_PRE_ID,0);
                limit.setRowId(rowId);
            }else if (fragment instanceof RecommendWorkFragment){
                final int rowId = fragment.getHostActivity()
                        .getSharedPreferences().getInt(Constant.RECOMMEND_PRE_ID,0);
                limit.setRowId(rowId);
            }else if (fragment instanceof MajorAskFragment){
                final int rowId = fragment.getHostActivity()
                        .getSharedPreferences().getInt(Constant.ASK_PRE_ID,0);
                limit.setRowId(rowId);
            }else if (fragment instanceof MyVoiceFragment){
                final int rowId = fragment.getHostActivity()
                        .getSharedPreferences().getInt(Constant.MY_VOICE_PRE_ID,0);
                limit.setRowId(rowId);
            }else if (fragment instanceof MyAskFragment){
                final int rowId = fragment.getHostActivity()
                        .getSharedPreferences().getInt(Constant.MY_ASK_PRE_ID,0);
                limit.setRowId(rowId);
            }else if (fragment instanceof MyRecommendFragment){
                final int rowId = fragment.getHostActivity()
                        .getSharedPreferences().getInt(Constant.MY_RECOMMEND_PRE_ID,0);
                limit.setRowId(rowId);
            }else if (fragment instanceof MyDynamicFragment){
                final int rowId = fragment.getHostActivity()
                        .getSharedPreferences().getInt(Constant.MY_DYNAMIC_PRE_ID,0);
                limit.setRowId(rowId);
            }
        }
        limit.setDir(dir);
        limit.setLimit(Constant.LIMIT);
        return limit;
    }
}
