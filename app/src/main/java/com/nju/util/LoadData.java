package com.nju.util;

import android.util.Log;

import com.nju.activity.BaseActivity;
import com.nju.activity.FragmentHostActivity;
import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.GetRequest;
import com.nju.http.request.PostRequestJson;
import com.nju.http.request.RequestBodyJson;
import com.nju.http.response.ParseResponse;
import com.nju.http.response.QueryJson;
import com.nju.model.DegreeInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by cun on 2016/4/11.
 */
public class LoadData {
    private static final String TAG = LoadData.class.getSimpleName();
    private static SchoolFriendGson gson;
    private BaseActivity mBaseActivity;
    private BaseFragment mFragment;
    private GetRequest voiceRequest, askRequest;
    private PostRequestJson collegeRequestJson;
    private ResponseCallback getCollegesCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG, "error" + error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            ParseResponse parseResponse = new ParseResponse();
            try {
                String info = parseResponse.getInfo(responseBody);
                if (info != null) {
                    Log.i(TAG, info + "info");
                    Map<String, ArrayList<String>> colleges = gson.fromJsonToMap(info);
                    for (Map.Entry<String, ArrayList<String>> entry : colleges.entrySet()) {
                        Log.i(TAG, entry.getKey());
                        for (String str : entry.getValue()) {
                            Log.i(TAG, str);
                        }
                    }
                    mBaseActivity.getSharedPreferences().edit()
                            .putString(Constant.COLLEGES, info).commit();
                }
            } catch (IOException e) {

            }
        }
    };
    private ResponseCallback voicelabelsCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG, "error" + error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            ArrayList<String> labels = new ArrayList<>();
            ParseResponse parseResponse = new ParseResponse();
            try {
                Object object = parseResponse.getInfo(responseBody, String.class);
                if (object != null) {
                    ArrayList majorAsks = (ArrayList) object;
                    if (majorAsks.size() > 0) {
                        for (Object obj : majorAsks) {
                            String label = (String) obj;
                            Log.i(TAG, "voice label = " + label);
                            labels.add(label);
                        }
                    }
                }
                Set<String> askLabelSet = new HashSet<>(labels);
                mBaseActivity.getSharedPreferences().edit()
                        .putStringSet(Constant.VOICE_LABEL, askLabelSet).commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private ResponseCallback askLabelsCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG, "error" + error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            ArrayList<String> labels = new ArrayList<>();
            ParseResponse parseResponse = new ParseResponse();
            try {
                Object object = parseResponse.getInfo(responseBody, String.class);
                if (object != null) {
                    ArrayList majorAsks = (ArrayList) object;
                    if (majorAsks.size() > 0) {
                        for (Object obj : majorAsks) {
                            String label = (String) obj;
                            Log.i(TAG, "ask label = " + label);
                            labels.add(label);
                        }
                    }
                }
                Set<String> askLabelSet = new HashSet<>(labels);
                mBaseActivity.getSharedPreferences().edit()
                        .putStringSet(Constant.ASK_LABEL, askLabelSet).commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public LoadData(BaseActivity baseActivity) {
        mBaseActivity = baseActivity;
        if (gson == null) {
            gson = SchoolFriendGson.newInstance();
        }
    }

    public LoadData(BaseFragment fragment) {
        mFragment = fragment;
        if (gson == null) {
            gson = SchoolFriendGson.newInstance();
        }
    }

    public LoadData loadCollege() {
        String colleges = mBaseActivity.getSharedPreferences().getString(Constant.COLLEGES, "");
        if (colleges.equals("")) {
            final String url = PathConstant.BASE_URL + PathConstant.COLLEGES_PATH + PathConstant.GET_SCHOOL;
            Log.i(TAG, url);
            String json = QueryJson.emptyBodyToString(mBaseActivity);
            Log.i(TAG, json);
            collegeRequestJson = new PostRequestJson(url, json, getCollegesCallback);
            HttpManager.getInstance().exeRequest(collegeRequestJson);
        }
        return this;
    }

    public LoadData loadVoiceLable() {
        final String url = PathConstant.BASE_URL + PathConstant.ALUMNS_VOICE_PATH + PathConstant.ALUMNS_VOICE_SUB_PATH_GET_LABELS;
        voiceRequest = new GetRequest(url, voicelabelsCallback);
        HttpManager.getInstance().exeRequest(voiceRequest);
        return this;
    }

    public LoadData loadQuestionLable() {
        final String url = PathConstant.BASE_URL + PathConstant.ALUMNIS_QUESTION_PATH + PathConstant.ALUMNIS_QUESTION_SUB_PATH_GET_LABELS;
        askRequest = new GetRequest(url, askLabelsCallback);
        HttpManager.getInstance().exeRequest(askRequest);
        return this;
    }

    public LoadData loadPersonInfo() {
        final FragmentHostActivity activity = mFragment.getHostActivity();
        String personInfo = activity.getSharedPreferences().getString(mFragment.getString(R.string.person_info), "");
        if (personInfo.equals("")) {
            RequestBodyJson<String> bodyJson = new RequestBodyJson<>();
            bodyJson.setAuthorization(activity.token());
            bodyJson.setBody("");
            String body = gson.toJson(bodyJson);
            String url = PathConstant.BASE_URL + PathConstant.USER_DEGREE_INFO_PATH + PathConstant.USER_DEGREE_QUERY;
            PostRequestJson mRequestQueryJson = new PostRequestJson(url, body, new ResponseCallback() {
                @Override
                public void onFail(Exception error) {
                    Log.e(TAG, error.getMessage());
                }

                @Override
                public void onSuccess(String responseBody) {
                    Log.i(TAG, responseBody);
                    ArrayList<DegreeInfo> infos = new ArrayList<>();
                    ParseResponse parseResponse = new ParseResponse();
                    try {
                        Object object = parseResponse.getInfo(responseBody, DegreeInfo.class);
                        if (object != null) {
                            ArrayList list = (ArrayList) object;
                            if (list.size() > 0) {
                                for (Object obj : list) {
                                    DegreeInfo info = (DegreeInfo) obj;
                                    infos.add(info);
                                }
                                activity.getSharedPreferences().edit().putString(mFragment.
                                        getString(R.string.person_info), gson.toJson(infos)).commit();
                                activity.getSharedPreferences().edit().putString(mFragment.getString(R.string.username), infos.get(0).getRealName()).commit();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            Log.e(TAG, url);
            Log.i(TAG, body);
            HttpManager.getInstance().exeRequest(mRequestQueryJson);
        }
        return this;
    }


}
