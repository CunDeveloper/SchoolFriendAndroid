package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nju.activity.R;
import com.nju.adatper.PersonVoiceAdapter;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.model.AlumniVoice;
import com.nju.test.TestData;
import com.nju.util.CloseRequestUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.ToastUtil;
import com.squareup.okhttp.Call;

import java.util.ArrayList;


public class MyVoiceFragment extends BaseFragment {
    private static final String TAG = MyVoiceFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static String mTitle;
    private Call mCall;
    private PostRequestJson mRequestJson;
    private SwipeRefreshLayout mRefreshLayout;
    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(MyVoiceFragment.this)){
                ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
                mRefreshLayout.setRefreshing(false);
                error.printStackTrace();
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(MyVoiceFragment.this)){
                Log.i(TAG, responseBody);
                mRefreshLayout.setRefreshing(false);
            }
        }
    };
    public static MyVoiceFragment newInstance(String title) {
        MyVoiceFragment fragment = new MyVoiceFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE,title);
        fragment.setArguments(args);
        return fragment;
    }

    public MyVoiceFragment() {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.person_refresh_listview, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initListView(view);
        setUpOnRefreshListener(view);
        return view;
    }

    private void updateMyVoices(){
        mRequestJson = new PostRequestJson("https://api.myjson.com/bins/3ucpf","",callback);
        HttpManager.getInstance().exeRequest(mRequestJson);
    }

    private void setUpOnRefreshListener(View view){
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                updateMyVoices();
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateMyVoices();
            }
        });
    }

    private void initListView(View view){
        final ArrayList<AlumniVoice> alumniVoices = TestData.getVoicesData();
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new PersonVoiceAdapter(getContext(),alumniVoices));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(PersonAlumniVoiceItemDetail.newInstance(alumniVoices.get(position), "学习"));
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
        getHostActivity().display(6);
    }

    @Override
    public void onStop(){
        super.onStop();
        CloseRequestUtil.close(mRequestJson);
    }

}
