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
import com.nju.adatper.PersonAskAdapter;
import com.nju.adatper.PersonRecommendAdapter;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniQuestion;
import com.nju.model.EntryDate;
import com.nju.model.MyAsk;
import com.nju.model.MyRecommend;
import com.nju.model.RecommendWork;
import com.nju.service.MajorAskService;
import com.nju.test.TestData;
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
import java.util.Comparator;
import java.util.HashMap;

public class MyAskFragment extends BaseFragment {

    private static final String TAG = MyAskFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static CharSequence mTitle;
    private PostRequestJson mRequestJson;
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<AlumniQuestion> alumniQuestions;
    private PersonAskAdapter askAdapter;
    private RelativeLayout mFootView;
    private ArrayList<MyAsk>  mMyAsks = new ArrayList<>();
    private ListView mListView;
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
                                if (!alumniQuestions.contains(majorAsk))
                                    alumniQuestions.add(majorAsk);
                            }

                            int length = alumniQuestions.size();
                            if (length>Constant.MAX_ROW){
                                for (int i = length-1;i>Constant.MAX_ROW;i--){
                                    alumniQuestions.remove(alumniQuestions.get(i));
                                }
                            }

                            initMap();
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
                mRequestJson = MajorAskService.queryMyAsk(MyAskFragment.this, callback, Constant.ALL,Constant.PRE,0);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestJson = MajorAskService.queryMyAsk(MyAskFragment.this, callback, Constant.ALL,Constant.PRE,0);
            }
        });
    }

    private void initMap(){
        MyAsk myAsk;
        EntryDate entryDate;
        for (AlumniQuestion alumniQuestion:alumniQuestions){
            boolean isContain = true;
            myAsk = new MyAsk();
            final long time = DateUtil.getTime(alumniQuestion.getDate());
            String day = DateFormat.format(Constant.DD, time).toString();
            String month = DateFormat.format(Constant.MM, time).toString();
            entryDate = new EntryDate(month,day);
            for (MyAsk ask:mMyAsks){
                if (entryDate.equals(ask.getEntryDate())){
                    isContain = false;
                    ArrayList<AlumniQuestion> tempList = ask.getAlumniQuestions();
                    if (!tempList.contains(alumniQuestion)){
                        tempList.add(alumniQuestion);
                        ask.setAlumniQuestions(tempList);
                        break;
                    }
                }
            }
            if (isContain){
                Log.i(TAG, "DAY=" + entryDate.getDay() + "==MONTH=" + entryDate.getMonth());
                myAsk.setEntryDate(entryDate);
                ArrayList<AlumniQuestion> alumniQuestions = new ArrayList<>();
                alumniQuestions.add(alumniQuestion);
                myAsk.setAlumniQuestions(alumniQuestions);
                mMyAsks.add(myAsk);
            }
        }
        Collections.sort(mMyAsks,Collections.reverseOrder());
    }


    private void initListView(View view) {
        alumniQuestions = TestData.getQlumniQuestions();
        initMap();
        mListView = (ListView) view.findViewById(R.id.listView);
        new ListViewHead(this).setUp(mListView);
        mFootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_footer, mListView, false);
        mFootView.setVisibility(View.GONE);
        mListView.addFooterView(mFootView);
        askAdapter = new PersonAskAdapter(this,mMyAsks);
        mListView.setAdapter(askAdapter);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == (askAdapter.getCount())) {
                    mFootView.setVisibility(View.VISIBLE);
                   // mRequestJson = MajorAskService.queryMyAsk(MyAskFragment.this, callback, Constant.ALL,Constant.NEXT);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Subscribe
    public void onNetStateMessageState(NetworkInfoEvent event){
        if (event.isConnected()){
            mRequestJson = MajorAskService.queryMyAsk(MyAskFragment.this, callback, Constant.ALL,Constant.PRE);
        }
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
