package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nju.activity.R;
import com.nju.adatper.PersonAskAdapter;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.request.QueryLimit;
import com.nju.http.request.RequestBodyJson;
import com.nju.model.AlumniQuestion;
import com.nju.model.MajorAsk;
import com.nju.test.TestData;
import com.nju.test.TestToken;
import com.nju.util.CloseRequestUtil;
import com.nju.util.CryptUtil;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.ParseResponse;
import com.nju.util.PathConstant;
import com.nju.util.SchoolFriendGson;
import com.nju.util.ToastUtil;
import com.squareup.okhttp.Call;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyAskFragment extends BaseFragment {

    private static final String TAG = MyAskFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static String mTitle;
    private PostRequestJson mRequestJson;
    private SwipeRefreshLayout mRefreshLayout;
    private final static SchoolFriendGson gson = SchoolFriendGson.newInstance();
    private ArrayList<AlumniQuestion> alumniQuestions;
    private PersonAskAdapter askAdapter;
    private RelativeLayout mFootView;
    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(MyAskFragment.this)){
                ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
                mRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Log.e(TAG, error.getMessage());
                mFootView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(MyAskFragment.this)){
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody,AlumniQuestion.class);
                    if (object != null){
                        ArrayList majorAsks = (ArrayList) object;
                        if (majorAsks.size()>0){
                            for (Object obj :majorAsks){
                                AlumniQuestion  majorAsk = (AlumniQuestion) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(majorAsk));
                                alumniQuestions.add(majorAsk);
                            }
                            Collections.sort(alumniQuestions, new Comparator<AlumniQuestion>() {
                                @Override
                                public int compare(AlumniQuestion lhs, AlumniQuestion rhs) {
                                    final long lhsTime = DateUtil.getTime(lhs.getDate());
                                    final long rhsTime = DateUtil.getTime(rhs.getDate());
                                    if (lhsTime>rhsTime){
                                        return -1;
                                    }else if (lhsTime<rhsTime)
                                    {
                                        return 1;
                                    }
                                    return 0;
                                }
                            });
                            int length = alumniQuestions.size();
                            if (length>10){
                                for (int i = length-1;i>10;i--){
                                    alumniQuestions.remove(alumniQuestions.get(i));
                                }
                            }
                            askAdapter.notifyDataSetChanged();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mRefreshLayout.setRefreshing(false);
                    mFootView.setVisibility(View.GONE);
                }

            }
        }
    };

    public static MyAskFragment newInstance(String title) {
        MyAskFragment fragment = new MyAskFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE,title);
        fragment.setArguments(args);
        return fragment;
    }

    public MyAskFragment() {
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
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initListView(view);
        setUpOnRefreshListener(view);
        return view;
    }

    private void setUpOnRefreshListener(View view){
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                updateAsk();
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateAsk();
            }
        });
    }

    private void updateAsk(){
        RequestBodyJson<QueryLimit> bodyJson = new RequestBodyJson<>();
        bodyJson.setAuthorization(CryptUtil.getEncryptiedData(gson.toJson(TestToken.getToken())));
        QueryLimit limit = new QueryLimit();
        limit.setOffset(0);limit.setTotal(20);
        bodyJson.setBody(limit);
        String json = gson.toJson(bodyJson);
        Log.e(TAG,json);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNIS_QUESTION_PATH+PathConstant.ALUMNIS_QUESTION_SUB_PATH_VIEW_OWN+"?level=所有";
        mRequestJson = new PostRequestJson(url,json,callback);
        Log.e(TAG,url);
        HttpManager.getInstance().exeRequest(mRequestJson);

    }

    private void initListView(View view) {
        alumniQuestions = TestData.getQlumniQuestions();
        final ListView listView = (ListView) view.findViewById(R.id.listView);
        mFootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_footer, listView, false);
        mFootView.setVisibility(View.GONE);
        listView.addFooterView(mFootView);
        askAdapter = new PersonAskAdapter(getContext(), alumniQuestions);
        listView.setAdapter(askAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(PersonAskDetailFragment.newInstance(alumniQuestions.get(position)));
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == (askAdapter.getCount())){
                    mFootView.setVisibility(View.VISIBLE);
                    updateAsk();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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
