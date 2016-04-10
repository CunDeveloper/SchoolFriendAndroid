package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nju.activity.NetworkInfoEvent;
import com.nju.activity.R;
import com.nju.adatper.PersonRecommendAdapter;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.http.response.QueryJson;
import com.nju.model.EntryDate;
import com.nju.model.RecommendWork;
import com.nju.service.AlumniVoiceService;
import com.nju.service.RecommendWorkService;
import com.nju.test.TestData;
import com.nju.util.CloseRequestUtil;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.ListViewHead;
import com.nju.util.PathConstant;
import com.nju.util.SchoolFriendGson;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class MyRecommendFragment extends BaseFragment {
    private static final String TAG = MyRecommendFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static String mTitle;
    private PostRequestJson mRequestJson;
    private SwipeRefreshLayout mRefreshLayout;
    private final static SchoolFriendGson gson = SchoolFriendGson.newInstance();
    private ArrayList<RecommendWork> mRecommendWorks;
    private HashMap<EntryDate,ArrayList<RecommendWork>> mRecommendWorkMap = new HashMap<>();
    private ListView listView;
    private PersonRecommendAdapter mPersonRecommendAdapter;
    private RelativeLayout mFootView;
    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(MyRecommendFragment.this)){
                ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
                mRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Log.e(TAG, error.getMessage());
                mFootView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(MyRecommendFragment.this)){
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody,RecommendWork.class);
                    if (object != null){
                        ArrayList majorAsks = (ArrayList) object;
                        if (majorAsks.size()>0){
                            for (Object obj :majorAsks){
                                RecommendWork   recommendWork = (RecommendWork) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(recommendWork));
                                mRecommendWorks.add(recommendWork);
                            }
                            int length = mRecommendWorks.size();
                            if (length>10){
                                for (int i = length-1;i>10;i--){
                                    mRecommendWorks.remove(mRecommendWorks.get(i));
                                }
                            }
                            getHostActivity().getSharedPreferences().edit()
                                    .putInt(Constant.RECOMMEND_PRE_ID,mRecommendWorks.get(0).getId()).apply();
                            getHostActivity().getSharedPreferences().edit()
                                    .putInt(Constant.RECOMMEND_NEXT_ID,mRecommendWorks.get(mRecommendWorks.size()-1).getId()).apply();

                            initMap();
                            listView.setAdapter(new PersonRecommendAdapter(MyRecommendFragment.this, mRecommendWorkMap));
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
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initListView(view);
        setUpOnRefreshListener(view);
        return view;
    }

    private void initListView(View view){
        mRecommendWorks = TestData.getRecommendWorks();
        initMap();
        listView = (ListView) view.findViewById(R.id.listView);
        ListViewHead.setUp(this, view, listView);
        mFootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_footer, listView, false);
        mFootView.setVisibility(View.GONE);
        listView.addFooterView(mFootView);
        mPersonRecommendAdapter = new PersonRecommendAdapter(this,mRecommendWorkMap);
        listView.setAdapter(mPersonRecommendAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == (mPersonRecommendAdapter.getCount())) {
                    mFootView.setVisibility(View.VISIBLE);
                    //mRequestJson = RecommendWorkService.queryMyRecommendWork(MyRecommendFragment.this, callback, Constant.ALL);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initMap(){
        EntryDate entryDate;
        for (RecommendWork recommendWork:mRecommendWorks){
            final long time = DateUtil.getTime(recommendWork.getDate());
            String day = DateFormat.format(Constant.DD, time).toString();
            String month = DateFormat.format(Constant.MM, time).toString();
            entryDate = new EntryDate(month,day);
            if (mRecommendWorkMap.containsKey(entryDate)){
                ArrayList<RecommendWork> tempList = mRecommendWorkMap.get(entryDate);
                tempList.add(recommendWork);
                mRecommendWorkMap.put(entryDate,tempList);
            }else {
                ArrayList<RecommendWork> tempList = new ArrayList<>();
                tempList.add(recommendWork);
                mRecommendWorkMap.put(entryDate,tempList);

            }
        }
    }

    private void setUpOnRefreshListener(View view){
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                mRequestJson = RecommendWorkService.queryMyRecommendWork(MyRecommendFragment.this, callback, Constant.ALL,Constant.PRE);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestJson = RecommendWorkService.queryMyRecommendWork(MyRecommendFragment.this, callback, Constant.ALL,Constant.PRE);
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

    @Subscribe
    public void onNetStateMessageState(NetworkInfoEvent event){
        if (event.isConnected()){
            mRequestJson = RecommendWorkService.queryMyRecommendWork(MyRecommendFragment.this, callback, Constant.ALL,Constant.PRE);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (mRequestJson != null)
            CloseRequestUtil.close(mRequestJson);
    }

}
