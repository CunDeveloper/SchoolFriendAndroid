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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nju.activity.NetworkInfoEvent;
import com.nju.activity.R;
import com.nju.adatper.MajorAskAdapter;
import com.nju.db.db.service.MajorAskDbService;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniQuestion;
import com.nju.service.CacheIntentService;
import com.nju.service.MajorAskService;
import com.nju.util.CloseRequestUtil;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
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
import java.util.Comparator;


public class MajorAskFragment extends BaseFragment {
    private static final String TAG = MajorAskFragment.class.getSimpleName();
    private SwipeRefreshLayout mRefreshLayout;
    private PostRequestJson mRequestJson;
    private ArrayList<AlumniQuestion> mAlumniQuestions;
    private RelativeLayout mFootView;
    private MajorAskAdapter mMajorAskAdapter ;
    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(MajorAskFragment.this)){
                ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
                mRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Log.e(TAG, error.getMessage());
                mFootView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(MajorAskFragment.this)){
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody,AlumniQuestion.class);
                    if (object != null){
                        ArrayList majorAsks = (ArrayList) object;
                        if (majorAsks.size()>0){
                            for (Object obj :majorAsks){
                                AlumniQuestion   alumniQuestion = (AlumniQuestion) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(alumniQuestion));
                                mAlumniQuestions.add(alumniQuestion);
                            }
                            Collections.sort(mAlumniQuestions,new MajorAskSort());
                            int length = mAlumniQuestions.size();
                            if (length>10){
                                for (int i = length-1;i>10;i--){
                                    mAlumniQuestions.remove(mAlumniQuestions.get(i));
                                }
                            }
                            mMajorAskAdapter.notifyDataSetChanged();
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

    public static MajorAskFragment newInstance( ) {
        MajorAskFragment fragment = new MajorAskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MajorAskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.own_major_ask);
        }
        getHostActivity().display(2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_major_ask, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initListView(view);
        initCameraView();
        setUpOnRefreshListener(view);
        return view;
    }

    private void setUpOnRefreshListener(View view){
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestJson = MajorAskService.queryMajorAsk(MajorAskFragment.this, callback, Constant.ALL);
            }
        });
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                mRequestJson = MajorAskService.queryMajorAsk(MajorAskFragment.this, callback, Constant.ALL);
            }
        });
    }



    private void initCameraView(){
        ImageView  mCameraImageView = getHostActivity().getMenuCameraView();
        mCameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(MultiChoosePicFragment.newInstance(AskPublishFragment.TAG));
            }
        });
    }

    private void initListView(View view){
        mAlumniQuestions = new ArrayList<>();
        new ExeCacheTask(this).execute();
        ListView listView = (ListView) view.findViewById(R.id.listView);
        LinearLayout head = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.listview_header, listView, false);
        listView.addHeaderView(head);
        mFootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_footer, listView, false);
        mFootView.setVisibility(View.GONE);
        listView.addFooterView(mFootView);
        mMajorAskAdapter = new MajorAskAdapter(this, mAlumniQuestions);
        listView.setAdapter(mMajorAskAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(MajorAskDetailFragment.newInstance(mAlumniQuestions.get(position)));
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == (mMajorAskAdapter.getCount())) {
                    mFootView.setVisibility(View.VISIBLE);
                    mRequestJson = MajorAskService.queryMajorAsk(MajorAskFragment.this, callback, Constant.ALL);
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
            mRequestJson = MajorAskService.queryMajorAsk(MajorAskFragment.this, callback, Constant.ALL);
        }
    }


    @Override
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (mRequestJson != null)
            CloseRequestUtil.close(mRequestJson);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Intent intent = new Intent(getContext(),CacheIntentService.class);
        intent.putExtra(Constant.LABEL,Constant.MAJOR_ASK);
        intent.putExtra(Constant.MAJOR_ASK,mAlumniQuestions);
        getContext().startService(intent);
    }

    private static class MajorAskSort implements Comparator<AlumniQuestion> {
        @Override
        public int compare(AlumniQuestion lhs, AlumniQuestion rhs) {
            final long lhsTime = DateUtil.getTime(lhs.getDate());
            final long rhsTime = DateUtil.getTime(rhs.getDate());
            if (lhsTime > rhsTime) {
                return -1;
            } else if (lhsTime < rhsTime) {
                return 1;
            }
            return 0;
        }

        @Override
        public boolean equals(Object object) {
            return false;
        }
    }

    private static class ExeCacheTask extends AsyncTask<Void,Void,ArrayList<AlumniQuestion>>
    {
        private final WeakReference<MajorAskFragment> mMajorAskWeakRef;
        public ExeCacheTask(MajorAskFragment  majorAskFragment){
            this.mMajorAskWeakRef = new WeakReference<>(majorAskFragment);
        }
        @Override
        protected ArrayList<AlumniQuestion> doInBackground(Void... params) {
            MajorAskFragment majorAskFragment = mMajorAskWeakRef.get();
            if (majorAskFragment!=null){
                return new MajorAskDbService(majorAskFragment.getContext()).getMajorAsks();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<AlumniQuestion> alumniQuestions) {
            super.onPostExecute(alumniQuestions);
            MajorAskFragment majorAskFragment = mMajorAskWeakRef.get();
            if (majorAskFragment!=null){
                if (alumniQuestions != null){
                    Log.i(TAG,SchoolFriendGson.newInstance().toJson(alumniQuestions));
                    Collections.sort(alumniQuestions, new MajorAskSort());
                    majorAskFragment.mAlumniQuestions.addAll(alumniQuestions);
                    majorAskFragment.mMajorAskAdapter.notifyDataSetChanged();
                }
            }
        }
    }

}
