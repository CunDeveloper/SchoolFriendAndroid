package com.nju.fragment;

import android.os.AsyncTask;
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
import android.widget.RelativeLayout;

import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.adatper.RecommendWorkCollectAdapter;
import com.nju.db.db.service.RecommendWorkCollectDbService;
import com.nju.event.MessageEvent;
import com.nju.event.MessageEventMore;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.RecommendCollect;
import com.nju.model.RecommendWork;
import com.nju.service.RecommendWorkService;
import com.nju.util.CloseRequestUtil;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.SchoolFriendGson;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;


public class RecommendCollectFragment extends BaseFragment {
    private static final String TAG = RecommendCollectFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static CharSequence mTitle;
    private ArrayList<RecommendCollect> mRecommendCollects = new ArrayList<>();
    private RecommendWorkCollectAdapter mRecommendWorkCollectAdapter;
    private int mChoosePosition;
    private PostRequestJson mQueryCollectsJson;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean mIsMore = false;
    private RelativeLayout mCollectToolLayout;

    public RecommendCollectFragment() {
        // Required empty public constructor
    }

    private ResponseCallback mQueryCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG,error.getMessage());
            ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
            mRefreshLayout.setRefreshing(false);
            error.printStackTrace();
            Log.e(TAG, error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(RecommendCollectFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody, RecommendCollect.class);
                    if (object != null) {
                        ArrayList majorAsks = (ArrayList) object;
                        if (majorAsks.size() > 0) {
                            mRecommendCollects.clear();
                            for (Object obj : majorAsks) {
                                RecommendCollect collect = (RecommendCollect) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(collect));
                                mRecommendCollects.add(collect);
                            }
                            Collections.sort(mRecommendCollects, Collections.reverseOrder());
                            int length = mRecommendCollects.size();
                            if (length > Constant.MAX_ROW) {
                                for (int i = length - 1; i > Constant.MAX_ROW; i--) {
                                    mRecommendCollects.remove(mRecommendCollects.get(i));
                                }
                            }
                        }
                        mRecommendWorkCollectAdapter.notifyDataSetChanged();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mRefreshLayout.setRefreshing(false);
                }
            }
        }
    };

    public static RecommendCollectFragment newInstance(String title) {
        RecommendCollectFragment fragment = new RecommendCollectFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(PARAM_TITLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.refresh_listview, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initListView(view);
        //new ExeCollectTask(this).execute();
        setUpOnRefreshListener(view);
        return view;
    }

    private void initListView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.listView);
        mRecommendWorkCollectAdapter = new RecommendWorkCollectAdapter(getContext(), mRecommendCollects);
        listView.setAdapter(mRecommendWorkCollectAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(RecommendWorkItemDetailFragment.newInstance(mRecommendCollects.get(position).getRecommendWork()));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mChoosePosition = mRecommendCollects.get(position).getId();
                SchoolFriendDialog.listDialog(getContext(), getResources().getStringArray(R.array.collectItem)).show();
                return true;
            }
        });
        mCollectToolLayout = (RelativeLayout) view.findViewById(R.id.collectToolLayout);
    }

    private void setUpOnRefreshListener(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                mQueryCollectsJson = RecommendWorkService.queryCollects(RecommendCollectFragment.this, mQueryCallback);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mQueryCollectsJson = RecommendWorkService.queryCollects(RecommendCollectFragment.this, mQueryCallback);
            }
        });
    }

    @Subscribe
    public void onMessageChoose(MessageEvent event) {
        Log.i(TAG, event.getMessage());
        if (event.getMessage().equals(getString(R.string.more))) {
            mIsMore = true;
            for (RecommendCollect collect : mRecommendCollects) {
                if (collect.getId() == mChoosePosition) {
                    collect.setCheck(2);
                } else {
                    collect.setCheck(1);
                }
            }
            mCollectToolLayout.setVisibility(View.VISIBLE);
            mRecommendWorkCollectAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void onMessageEventMore(MessageEventMore eventMore) {
        for (RecommendCollect collect : mRecommendCollects) {
            collect.setCheck(0);
        }
        mCollectToolLayout.setVisibility(View.GONE);
        mRecommendWorkCollectAdapter.notifyDataSetChanged();
        mIsMore = false;
    }

    public boolean isMore() {
        return mIsMore;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mTitle);
        }
        getHostActivity().display(6);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (mQueryCollectsJson != null)
            CloseRequestUtil.close(mQueryCollectsJson);
    }

    private static class ExeCollectTask extends AsyncTask<Void, Void, ArrayList<RecommendWork>> {
        private final WeakReference<RecommendCollectFragment> mRecommendWorkWeakRef;

        public ExeCollectTask(RecommendCollectFragment recommendCollectFragment) {
            this.mRecommendWorkWeakRef = new WeakReference<>(recommendCollectFragment);
        }

        @Override
        protected ArrayList<RecommendWork> doInBackground(Void... params) {
            RecommendCollectFragment recommendCollectFragment = mRecommendWorkWeakRef.get();
            if (recommendCollectFragment != null) {
                return new RecommendWorkCollectDbService(recommendCollectFragment.getContext()).getCollects();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<RecommendWork> recommendWorks) {
            super.onPostExecute(recommendWorks);
            RecommendCollectFragment recommendCollectFragment = mRecommendWorkWeakRef.get();
            if (recommendCollectFragment != null) {
                if (recommendWorks != null) {
                   // recommendCollectFragment.mRecommendCollects.addAll(recommendWorks);
                    recommendCollectFragment.mRecommendWorkCollectAdapter.notifyDataSetChanged();
                }
            }
        }
    }

}
