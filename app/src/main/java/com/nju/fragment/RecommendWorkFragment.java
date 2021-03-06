package com.nju.fragment;

import android.content.Intent;
import android.os.AsyncTask;
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

import com.nju.activity.BaseActivity;
import com.nju.activity.R;
import com.nju.adatper.RecommendWorkItemAdapter;
import com.nju.db.db.service.RecommendDbService;
import com.nju.event.MessageAuthorImageEvent;
import com.nju.event.MessageComplainEvent;
import com.nju.event.MessageContentIdEvent;
import com.nju.event.MessageDegreeEvent;
import com.nju.event.MessageEvent;
import com.nju.event.NetworkInfoEvent;
import com.nju.event.RecommendWorkTypeEvent;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.RecommendWork;
import com.nju.service.CacheIntentService;
import com.nju.service.RecommendWorkService;
import com.nju.util.BottomToolBar;
import com.nju.util.CloseRequestUtil;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.ListViewHead;
import com.nju.util.SchoolFriendGson;
import com.nju.util.SearchViewUtil;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class RecommendWorkFragment extends BaseFragment {
    private static final String TAG = RecommendWorkFragment.class.getSimpleName();
    private static CharSequence mDegree = Constant.ALL;
    private static CharSequence mType;
    private ArrayList<RecommendWork> mRecommendWorks = new ArrayList<>();
    private PostRequestJson mRequestJson, deleteContentJson;
    private SwipeRefreshLayout mRefreshLayout;
    private RecommendWorkItemAdapter mRecommendWorkItemAdapter;
    private RelativeLayout mFootView;
    private AtomicInteger mRecommendId;
    private View mView;
    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(RecommendWorkFragment.this)) {
                ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
                mRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Log.e(TAG, error.getMessage());
                mFootView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(RecommendWorkFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody, RecommendWork.class);
                    if (object != null) {
                        ArrayList majorAsks = (ArrayList) object;
                        if (majorAsks.size() > 0) {
                            mRecommendWorks.clear();
                            for (Object obj : majorAsks) {
                                RecommendWork recommendWork = (RecommendWork) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(recommendWork));
                                mRecommendWorks.add(recommendWork);
                            }
                            Collections.sort(mRecommendWorks,Collections.reverseOrder());
                            int length = mRecommendWorks.size();
                            if (length > Constant.MAX_ROW) {
                                for (int i = length - 1; i > Constant.MAX_ROW; i--) {
                                    mRecommendWorks.remove(mRecommendWorks.get(i));
                                }
                            }
                        }
                        mRecommendWorkItemAdapter.notifyDataSetChanged();
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

    private ResponseCallback deleteContentCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {

        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            if (FragmentUtil.isAttachedToActivity(RecommendWorkFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    String str = parseResponse.getInfo(responseBody);
                    if (str.equals(Constant.OK_MSG)) {
                        int id = mRecommendId.get();
                        for (RecommendWork recommendWork : mRecommendWorks) {
                            if (id == recommendWork.getId()) {
                                mRecommendWorks.remove(recommendWork);
                                mRecommendWorkItemAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    public RecommendWorkFragment() {
        // Required empty public constructor
        new ExeCacheTask(this).execute(mDegree.toString(), 0 + "");
    }

    public static RecommendWorkFragment newInstance() {
        return new RecommendWorkFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle(mType.toString());
        getHostActivity().display(5);
    }

    private void setTitle(String title) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_recommend_work, container, false);
        mView.setPadding(mView.getPaddingLeft(), Divice.getStatusBarHeight(getActivity()), mView.getPaddingRight(), mView.getPaddingBottom());
        setListView(mView);
        BottomToolBar.showRecommendTool(this, mView);
        setUpOnRefreshListener(mView);
        SearchViewUtil.setUp(this, mView);
        mType = BottomToolBar.getRecommendType(mView);
        return mView;
    }

    private void setUpOnRefreshListener(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                mRequestJson = RecommendWorkService.queryRecommendWork(RecommendWorkFragment.this, callback, mDegree.toString(), Constant.PRE, 0);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestJson = RecommendWorkService.queryRecommendWork(RecommendWorkFragment.this, callback, mDegree.toString(), Constant.PRE, 0);
            }
        });
    }

    private void setListView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.listView);
        new ListViewHead((BaseActivity)getHostActivity()).setUp(listView);
        mFootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_footer, listView, false);
        mFootView.setVisibility(View.GONE);
        listView.addFooterView(mFootView);
        mRecommendWorkItemAdapter = new RecommendWorkItemAdapter(this, mRecommendWorks);
        listView.setAdapter(mRecommendWorkItemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mRecommendWorks.size()>0) {
                    getHostActivity().open(RecommendWorkItemDetailFragment.newInstance(mRecommendWorks.get(position)));
                }
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == (mRecommendWorkItemAdapter.getCount()) &&
                        view.getLastVisiblePosition() == Constant.MAX_ROW) {
                    mFootView.setVisibility(View.VISIBLE);
                    mRequestJson = RecommendWorkService.queryRecommendWork(RecommendWorkFragment.this, callback, Constant.ALL, Constant.NEXT);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        ListViewHead.initView(view, this);
    }

    @Subscribe
    public void onNetStateMessageState(NetworkInfoEvent event) {
        if (event.isConnected()) {
            mRequestJson = RecommendWorkService.queryRecommendWork(RecommendWorkFragment.this, callback, mDegree.toString(), Constant.PRE, 0);
        }
    }

    @Subscribe
    public void onMessageDegree(MessageEvent event) {
        Log.i(TAG, "DEGREE = " + event.getMessage());
        mDegree = event.getMessage();
        mRequestJson = RecommendWorkService.queryRecommendWork(RecommendWorkFragment.this, callback, mDegree.toString(), Constant.PRE, 0);
    }

    @Subscribe
    public void onMessageType(RecommendWorkTypeEvent event) {
        setTitle(event.getType());
        ToastUtil.showShortText(getContext(), "TYPE =" + event.getType());
    }

    @Subscribe
    public void onMessageDeleteContent(MessageContentIdEvent event) {
        mRecommendId = new AtomicInteger();
        mRecommendId.set(event.getId());
        deleteContentJson = RecommendWorkService.deleteMyRecommend(this, event.getId(), deleteContentCallback);
    }

    @Subscribe
    public void onMessageComplainEvent(MessageComplainEvent event) {
        if (event.getMessage().equals(getString(R.string.complain))) {
            ComplainFragment fragment = ComplainFragment.newInstance();
            getHostActivity().open(fragment,fragment);        }
    }

    @Subscribe
    public void onMessageAuthorImageEvent(MessageAuthorImageEvent event) {
        ListViewHead.initView(mView,this);
    }

    @Subscribe
    public void onMessageDegreeEvent(MessageDegreeEvent event){
        BottomToolBar.showRecommendTool(this, mView);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        CloseRequestUtil.close(mRequestJson);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRecommendWorks != null && mRecommendWorks.size() > 0) {
            Intent intent = new Intent(getContext(), CacheIntentService.class);
            intent.putExtra(Constant.LABEL, Constant.RECOMMEND);
            intent.putExtra(Constant.RECOMMEND, mRecommendWorks);
            getContext().startService(intent);
        }
    }

    private static class ExeCacheTask extends AsyncTask<String, Void, ArrayList<RecommendWork>> {
        private final WeakReference<RecommendWorkFragment> mRecommendWorkWeakRef;

        public ExeCacheTask(RecommendWorkFragment recommendFragment) {
            this.mRecommendWorkWeakRef = new WeakReference<>(recommendFragment);
        }

        @Override
        protected ArrayList<RecommendWork> doInBackground(String... params) {
            RecommendWorkFragment recommendFragment = mRecommendWorkWeakRef.get();
            if (recommendFragment != null) {
                String degree = params[0];
                String type = params[1];
                Log.i(TAG, "degree=" + degree + " " + "type = " + type);
                return new RecommendDbService(recommendFragment.getContext()).getRecommendWorksByDegreeAndType(degree, type);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<RecommendWork> recommendWorks) {
            super.onPostExecute(recommendWorks);
            RecommendWorkFragment recommendFragment = mRecommendWorkWeakRef.get();
            if (recommendFragment != null) {
                if (recommendWorks != null && recommendWorks.size() > 0) {
                    Log.i(TAG, "cache json ==" + SchoolFriendGson.newInstance().toJson(recommendWorks));
                    Collections.sort(recommendWorks,Collections.reverseOrder());
                    ArrayList<RecommendWork> source = recommendFragment.mRecommendWorks;
                    ArrayList<RecommendWork> tempArray = new ArrayList<>();
                    RecommendWork work;
                    for (int i = 0; i < source.size(); i++) {
                        work = new RecommendWork();
                        tempArray.add(work);
                    }
                    Collections.copy(tempArray, source);
                    source.removeAll(tempArray);
                    source.addAll(recommendWorks);
                    recommendFragment.mRecommendWorkItemAdapter.notifyDataSetChanged();
                } else {
                    recommendFragment.getHostActivity().getSharedPreferences().edit()
                            .putInt(Constant.RECOMMEND_PRE_ID, 0).apply();
                    recommendFragment.getHostActivity().getSharedPreferences().edit()
                            .putInt(Constant.RECOMMEND_NEXT_ID, 0).apply();
                }
            }
        }
    }
}
