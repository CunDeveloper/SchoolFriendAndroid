package com.nju.util;

import android.util.Log;

import com.nju.activity.BaseActivity;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.GetRequest;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.http.response.QueryJson;

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
    private BaseActivity mBaseActivity;
    private GetRequest voiceRequest,askRequest;
    private PostRequestJson collegeRequestJson;
    private static SchoolFriendGson gson;
    public LoadData(BaseActivity baseActivity){
        mBaseActivity = baseActivity;
        if (gson == null){
            gson = SchoolFriendGson.newInstance();
        }
    }
    private ResponseCallback getCollegesCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG, "error" + error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            ParseResponse parseResponse = new ParseResponse();
            try{
                String info = parseResponse.getInfo(responseBody);
                Log.i(TAG,info);
                Map<String,ArrayList<String>> colleges = gson.fromJsonToMap(info);
                for (Map.Entry<String,ArrayList<String>> entry:colleges.entrySet()){
                    Log.i(TAG,entry.getKey());
                    for (String str:entry.getValue()){
                        Log.i(TAG,str);
                    }
                }
                mBaseActivity.getSharedPreferences().edit()
                        .putString(Constant.COLLEGES,info).commit();
            }catch (IOException e){

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
                            Log.i(TAG,"voice label = "+label);
                            labels.add(label);
                        }
                    }
                }
                Set<String> askLabelSet = new HashSet<>(labels);
                mBaseActivity.getSharedPreferences().edit()
                        .putStringSet(Constant.VOICE_LABEL,askLabelSet).commit();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private ResponseCallback  askLabelsCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG,"error"+error.getMessage());
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
                            Log.i(TAG,"ask label = "+label);
                            labels.add(label);
                        }
                    }
                }
                Set<String> askLabelSet = new HashSet<>(labels);
                mBaseActivity.getSharedPreferences().edit()
                        .putStringSet(Constant.ASK_LABEL,askLabelSet).commit();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public LoadData loadCollege(){
        final String url = PathConstant.BASE_URL+PathConstant.COLLEGES_PATH+PathConstant.GET_SCHOOL;
        Log.i(TAG,url);
        String json = QueryJson.emptyBodyToString(mBaseActivity);
        Log.i(TAG,json);
        collegeRequestJson = new PostRequestJson(url,json,getCollegesCallback);
        HttpManager.getInstance().exeRequest(collegeRequestJson);
        return this;
    }

    public  LoadData loadVoiceLable(){
        final String url = PathConstant.BASE_URL+PathConstant.ALUMNS_VOICE_PATH+PathConstant.ALUMNS_VOICE_SUB_PATH_GET_LABELS;
        voiceRequest = new GetRequest(url,voicelabelsCallback);
        HttpManager.getInstance().exeRequest(voiceRequest);
        return this;
    }

    public  LoadData loadQuestionLable(){
        final String url = PathConstant.BASE_URL+PathConstant.ALUMNIS_QUESTION_PATH+PathConstant.ALUMNIS_QUESTION_SUB_PATH_GET_LABELS;
        askRequest = new GetRequest(url,askLabelsCallback);
        HttpManager.getInstance().exeRequest(askRequest);
        return this;
    }

}
