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
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nju.activity.NetworkInfoEvent;
import com.nju.activity.R;
import com.nju.adatper.PersonRecommendAdapter;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.EntryDate;
import com.nju.model.MyRecommend;
import com.nju.model.RecommendWork;
import com.nju.service.RecommendWorkService;
import com.nju.util.CloseRequestUtil;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.ListViewHead;
import com.nju.util.SchoolFriendGson;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MyRecommendFragment extends BaseFragment {
    private static final String TAG = MyRecommendFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static CharSequence mTitle;
    private PostRequestJson mRequestJson;
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<RecommendWork> mRecommendWorks;
    private ArrayList<MyRecommend> mMyRecommends = new ArrayList<>();
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
                                RecommendWork  recommendWork = (RecommendWork) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(recommendWork));
                                if (!mRecommendWorks.contains(recommendWork))
                                    mRecommendWorks.add(recommendWork);
                            }
                            int length = mRecommendWorks.size();
                            if (length>Constant.MAX_ROW){
                                for (int i = length-1;i>Constant.MAX_ROW;i--){
                                    mRecommendWorks.remove(mRecommendWorks.get(i));
                                }
                            }
                            initMap();
                            mPersonRecommendAdapter.notifyDataSetChanged();
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
        mRecommendWorks = new ArrayList<>();
        initMap();
        listView = (ListView) view.findViewById(R.id.listView);
        new ListViewHead(this).setUp(listView);
        mFootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_footer, listView, false);
        mFootView.setVisibility(View.GONE);
        listView.addFooterView(mFootView);
        mPersonRecommendAdapter = new PersonRecommendAdapter(this,mMyRecommends);
        listView.setAdapter(mPersonRecommendAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == (mPersonRecommendAdapter.getCount())
                        && view.getLastVisiblePosition() == Constant.MAX_ROW) {
                    mFootView.setVisibility(View.VISIBLE);
                    mRequestJson = RecommendWorkService.queryMyRecommendWork(MyRecommendFragment.this, callback, Constant.ALL,Constant.PRE,0);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initMap(){
        MyRecommend myRecommend;
        EntryDate entryDate;
        for (RecommendWork recommendWork:mRecommendWorks){
            boolean isContain = true;
            myRecommend = new MyRecommend();
            final long time = DateUtil.getTime(recommendWork.getDate());
            String day = DateFormat.format(Constant.DD, time).toString();
            String month = DateFormat.format(Constant.MM, time).toString();
            entryDate = new EntryDate(month,day);
            for (MyRecommend recommend:mMyRecommends){
                if (entryDate.equals(recommend.getEntryDate())){
                    isContain = false;
                    ArrayList<RecommendWork> tempList = recommend.getRecommendWorks();
                    if (!tempList.contains(recommendWork)){
                        tempList.add(recommendWork);
                        recommend.setRecommendWorks(tempList);
                        break;
                    }
                }
            }
            if (isContain){
                Log.i(TAG,"DAY="+entryDate.getDay()+"==MONTH="+entryDate.getMonth());
                myRecommend.setEntryDate(entryDate);
                ArrayList<RecommendWork> recommendWorks = new ArrayList<>();
                recommendWorks.add(recommendWork);
                myRecommend.setRecommendWorks(recommendWorks);
                mMyRecommends.add(myRecommend);
            }
        }
        Collections.sort(mMyRecommends,Collections.reverseOrder());
    }

    private void setUpOnRefreshListener(View view){
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                mRequestJson = RecommendWorkService.queryMyRecommendWork(MyRecommendFragment.this, callback, Constant.ALL,Constant.PRE,0);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestJson = RecommendWorkService.queryMyRecommendWork(MyRecommendFragment.this, callback, Constant.ALL,Constant.PRE,0);
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
            mRequestJson = RecommendWorkService.queryMyRecommendWork(MyRecommendFragment.this, callback,Constant.ALL,Constant.PRE,0);
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
