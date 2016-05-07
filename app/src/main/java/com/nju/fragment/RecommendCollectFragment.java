package com.nju.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
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
import com.nju.model.RecommendWork;
import com.nju.test.TestData;
import com.nju.util.Divice;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class RecommendCollectFragment extends BaseFragment {
    private static final String TAG = RecommendCollectFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static CharSequence mTitle;
    private ArrayList<RecommendWork> mRecommendWorks;
    private RecommendWorkCollectAdapter mRecommendWorkCollectAdapter;
    private int mChoosePosition;
    private boolean mIsMore = false;
    private RelativeLayout mCollectToolLayout;

    public RecommendCollectFragment() {
        // Required empty public constructor
    }

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
        new ExeCollectTask(this).execute();
        return view;
    }

    private void initListView(View view) {
        mRecommendWorks = TestData.getRecommendWorks();
        ListView listView = (ListView) view.findViewById(R.id.listView);
        mRecommendWorkCollectAdapter = new RecommendWorkCollectAdapter(getContext(), mRecommendWorks);
        listView.setAdapter(mRecommendWorkCollectAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(RecommendWorkItemDetailFragment.newInstance(mRecommendWorks.get(position)));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mChoosePosition = mRecommendWorks.get(position).getId();
                SchoolFriendDialog.listDialog(getContext(), getResources().getStringArray(R.array.collectItem)).show();
                return true;
            }
        });
        mCollectToolLayout = (RelativeLayout) view.findViewById(R.id.collectToolLayout);
    }

    @Subscribe
    public void onMessageChoose(MessageEvent event) {
        Log.i(TAG, event.getMessage());
        if (event.getMessage().equals(getString(R.string.more))) {
            mIsMore = true;
            for (RecommendWork recommendWork : mRecommendWorks) {
                if (recommendWork.getId() == mChoosePosition) {
                    recommendWork.setCheck(2);
                } else {
                    recommendWork.setCheck(1);
                }
            }
            mCollectToolLayout.setVisibility(View.VISIBLE);
            mRecommendWorkCollectAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void onMessageEventMore(MessageEventMore eventMore) {
        for (RecommendWork recommendWork : mRecommendWorks) {
            recommendWork.setCheck(0);
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
                    recommendCollectFragment.mRecommendWorks.addAll(recommendWorks);
                    recommendCollectFragment.mRecommendWorkCollectAdapter.notifyDataSetChanged();
                }
            }
        }
    }

}
