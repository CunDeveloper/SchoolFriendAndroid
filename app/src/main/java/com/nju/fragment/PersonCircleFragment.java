package com.nju.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nju.activity.R;
import com.nju.adatper.PersonCircleAdapter;
import com.nju.http.HttpClient;
import com.nju.http.HttpMethod;
import com.nju.http.SchoolFriendOkRep;
import com.nju.http.SchoolFriendRequest;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.SchoolFriendGson;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Content;

public class PersonCircleFragment extends BaseFragment {
    public static final String TAG = PersonCircleFragment.class.getSimpleName();
    private String mUserName;
    private static final String USERNAME = "username";
    private ListView mListView;

    private SchoolFriendOkRep okRep = new SchoolFriendOkRep(){
        @Override
        public void onResponse(Object response) {
            super.onResponse(response);
            String result = response.toString();
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Content>>(){}.getType();
            ArrayList<Content> posts = (ArrayList<Content>) gson.fromJson(result, listType);
            mListView.setAdapter(new PersonCircleAdapter(getContext(),posts));
            Log.e(TAG, posts.size() + "==");

        }
    };

    public static PersonCircleFragment newInstance(String userName) {
        PersonCircleFragment fragment = new PersonCircleFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USERNAME,userName);
        fragment.setArguments(bundle);
        return fragment;
    }

    public PersonCircleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mUserName = getArguments().getString(USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new Thread(){
            @Override
            public void run(){
                SchoolFriendRequest request = new SchoolFriendRequest(HttpMethod.POST(),Constant.BASE_URL+Constant.PERSON_CIRCLE_URL,okRep);
                HashMap paras = new HashMap();
                paras.put(Constant.USER_ID,String.valueOf(51));
                paras.put(Constant.LABEL,Constant.QUERY_ALL);
                request.setParams(paras);
                HttpClient.getInstance(getContext()).addToRequestQueue(request);
            }
        }.start();
        View view = inflater.inflate(R.layout.fragment_person_circle, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()),view.getPaddingRight(),view.getPaddingBottom());
        mListView = (ListView) view.findViewById(R.id.fragment_person_circle_listview);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHostActivity().hideAllMenuView();
        getHostActivity().getToolBar().setTitle(mUserName);
    }


}
