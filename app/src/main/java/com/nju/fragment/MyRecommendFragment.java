package com.nju.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nju.activity.R;
import com.nju.adatper.PersonRecommendAdapter;
import com.nju.adatper.PersonVoiceAdapter;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.model.AlumniVoice;
import com.nju.model.RecommendWork;
import com.nju.test.TestData;
import com.nju.util.Divice;
import com.squareup.okhttp.Call;

import java.util.ArrayList;


public class MyRecommendFragment extends BaseFragment {

    private static final String PARAM_TITLE = "paramTitle";
    private static String mTitle;
    private Call mCall;
    private PostRequestJson mRequestJson;
    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {

        }

        @Override
        public void onSuccess(String responseBody) {

        }
    };

    public static MyRecommendFragment newInstance(String title) {
        MyRecommendFragment fragment = new MyRecommendFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE,title);
        fragment.setArguments(args);
        return fragment;
    }

    public MyRecommendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(PARAM_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_refresh_listview, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()),view.getPaddingRight(),view.getPaddingBottom());
        initListView(view);
        mRequestJson = new PostRequestJson("","",callback);
        HttpManager.getInstance().exeRequest(mRequestJson);
        return view;
    }

    private void initListView(View view){
        final ArrayList<RecommendWork> recommendWorks = TestData.getRecommendWorks();
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new PersonRecommendAdapter(getContext(), recommendWorks));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(PersonRecommendWorkItemDetailFragment.newInstance(recommendWorks.get(position)));
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mTitle);
        }
        getHostActivity().display(5);
    }

    @Override
    public void onStop(){
        super.onStop();
        mCall = mRequestJson.getCall();
        if (mCall != null){
            mCall.cancel();
        }
    }

}
