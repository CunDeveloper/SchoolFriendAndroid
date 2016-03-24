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
import com.nju.http.request.QueryLimit;
import com.nju.http.request.RequestBodyJson;
import com.nju.model.AlumniVoice;
import com.nju.test.TestData;
import com.nju.test.TestToken;
import com.nju.util.CloseRequestUtil;
import com.nju.util.CryptUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.ParseResponse;
import com.nju.util.PathConstant;
import com.nju.util.SchoolFriendGson;
import com.nju.util.ToastUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;


public class MyVoiceFragment extends BaseFragment {
    private static final String TAG = MyVoiceFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static String mTitle;
    private PostRequestJson mRequestJson;
    private SwipeRefreshLayout mRefreshLayout;
    private final static SchoolFriendGson gson = SchoolFriendGson.newInstance();

    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(MyVoiceFragment.this)){
                ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
                mRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Log.e(TAG, error.getMessage());
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(MyVoiceFragment.this)){
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody,AlumniVoice.class);
                    if (object != null){
                        ArrayList alumniVoices = (ArrayList) object;
                        if (alumniVoices.size()>0){
                            for (Object obj :alumniVoices){
                                AlumniVoice alumniVoice = (AlumniVoice) obj;
                                Log.i(TAG,SchoolFriendGson.newInstance().toJson(alumniVoice));
                            }

                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mRefreshLayout.setRefreshing(false);
                }

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
        RequestBodyJson<QueryLimit> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(CryptUtil.getEncryptiedData(gson.toJson(TestToken.getToken())));
        QueryLimit limit = new QueryLimit();
        limit.setOffset(0);limit.setTotal(20);
        bodyJson.setBody(limit);
        String json = gson.toJson(bodyJson);
        Log.e(TAG,json);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNS_VOICE_PATH+PathConstant.ALUMNS_VOICE_SUB_PATH_VIEW_OWN_VOICE+"?level=所有";
        mRequestJson = new PostRequestJson(url,json,callback);
        Log.e(TAG,url);
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
