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
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nju.activity.BaseActivity;
import com.nju.activity.R;
import com.nju.adatper.MajorAskAdapter;
import com.nju.db.db.service.MajorAskDbService;
import com.nju.event.MessageAuthorImageEvent;
import com.nju.event.MessageComplainEvent;
import com.nju.event.MessageContentIdEvent;
import com.nju.event.MessageDegreeEvent;
import com.nju.event.MessageEvent;
import com.nju.event.MessageLabelEvent;
import com.nju.event.NetworkInfoEvent;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniQuestion;
import com.nju.service.CacheIntentService;
import com.nju.service.MajorAskService;
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


public class MajorAskFragment extends BaseFragment {
    private static final String TAG = MajorAskFragment.class.getSimpleName();
    private SwipeRefreshLayout mRefreshLayout;
    private PostRequestJson mRequestJson, delectAskRequestJson;
    private ArrayList<AlumniQuestion> mAlumniQuestions = new ArrayList<>();
    private RelativeLayout mFootView;
    private MajorAskAdapter mMajorAskAdapter;
    private AtomicInteger mAskId;
    private CharSequence mLabel = "";
    private CharSequence mDegree = Constant.ALL;
    private View mView;
    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(MajorAskFragment.this)) {
                ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
                mRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Log.e(TAG, error.getMessage());
                mFootView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(MajorAskFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody, AlumniQuestion.class);
                    if (object != null) {
                        ArrayList majorAsks = (ArrayList) object;
                        if (majorAsks.size() > 0) {
                            mAlumniQuestions.clear();
                            for (Object obj : majorAsks) {
                                AlumniQuestion alumniQuestion = (AlumniQuestion) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(alumniQuestion));
                                mAlumniQuestions.add(alumniQuestion);
                            }
                            Collections.sort(mAlumniQuestions, Collections.reverseOrder());
                            int length = mAlumniQuestions.size();
                            if (length > Constant.MAX_ROW) {
                                for (int i = length - 1; i > Constant.MAX_ROW; i--) {
                                    mAlumniQuestions.remove(mAlumniQuestions.get(i));
                                }
                            }
                        }
                        mMajorAskAdapter.notifyDataSetChanged();
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
            Log.e(TAG, error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            if (FragmentUtil.isAttachedToActivity(MajorAskFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    String str = parseResponse.getInfo(responseBody);
                    if (str.equals(Constant.OK_MSG)) {
                        int id = mAskId.get();
                        for (AlumniQuestion alumniQuestion : mAlumniQuestions) {
                            if (id == alumniQuestion.getId()) {
                                synchronized (MajorAskFragment.this) {
                                    mAlumniQuestions.remove(alumniQuestion);
                                }
                               mMajorAskAdapter.notifyDataSetChanged();
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

    public MajorAskFragment() {
        // Required empty public constructor
        new ExeCacheTask(this).execute(mDegree.toString(), mLabel.toString());
    }

    public static MajorAskFragment newInstance() {
        MajorAskFragment fragment = new MajorAskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.own_major_ask);
        }
        getHostActivity().display(5);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_major_ask, container, false);
        mView.setPadding(mView.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), mView.getPaddingRight(), mView.getPaddingBottom());
        initListView(mView);
        initCameraView();
        BottomToolBar.showMajorTool(this, mView);
        setUpOnRefreshListener(mView);
        SearchViewUtil.setUp(this, mView);
        return mView;
    }

    private void setUpOnRefreshListener(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                mRequestJson = MajorAskService.queryMajorAsk(MajorAskFragment.this, callback, Constant.ALL, Constant.PRE, 0);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestJson = MajorAskService.queryMajorAsk(MajorAskFragment.this, callback, Constant.ALL, Constant.PRE, 0);
            }
        });
    }


    private void initCameraView() {
        ImageView mCameraImageView = getHostActivity().getMenuCameraView();
        mCameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(MultiChoosePicFragment.newInstance(AskPublishFragment.TAG));
            }
        });
    }

    private void initListView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.listView);
        new ListViewHead((BaseActivity)getHostActivity()).setUp(listView);
        mFootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_footer, listView, false);
        mFootView.setVisibility(View.GONE);
        listView.addFooterView(mFootView);
        mMajorAskAdapter = new MajorAskAdapter(this, mAlumniQuestions);
        listView.setAdapter(mMajorAskAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAlumniQuestions.size()>0) {
                    MajorAskDetailFragment fragment = MajorAskDetailFragment.newInstance(mAlumniQuestions.get(position));
                    getHostActivity().open(fragment, fragment);
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == (mMajorAskAdapter.getCount())) {
                    mFootView.setVisibility(View.VISIBLE);
                    // mRequestJson = MajorAskService.queryMajorAsk(MajorAskFragment.this, callback, Constant.ALL, Constant.NEXT);
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
            if (mAlumniQuestions.size() > 0) {
                mRequestJson = MajorAskService.queryMajorAsk(MajorAskFragment.this, callback, Constant.ALL, Constant.PRE, mAlumniQuestions.get(0).getId());
            } else {
                mRequestJson = MajorAskService.queryMajorAsk(MajorAskFragment.this, callback, Constant.ALL, Constant.PRE, 0);
            }
        }
    }

    @Subscribe
    public void onMessageDeleteContent(MessageContentIdEvent event) {
        mAskId = new AtomicInteger();
        mAskId.set(event.getId());
        delectAskRequestJson = MajorAskService.deleteQuestion(this, event.getId(), deleteContentCallback);
    }

    @Subscribe
    public void onMessageDegree(MessageEvent event) {
        mDegree = event.getMessage();
        mRequestJson = MajorAskService.queryMajorAsk(MajorAskFragment.this, callback, event.getMessage(), Constant.PRE, 0);
        // new ExeCacheTask(this).execute(event.getMessage(),mLabel.toString());
    }

    @Subscribe
    public void onMessageDegreeEvent(MessageDegreeEvent event){
        BottomToolBar.showMajorTool(this, mView);
    }

    @Subscribe
    public void onMessageLabel(MessageLabelEvent event) {
        ToastUtil.showShortText(getContext(), event.getLabel());
        mLabel = event.getLabel();
        // new ExeCacheTask(this).execute(mDegree.toString(),mLabel.toString());
        mRequestJson = MajorAskService.queryMajorAskByLabel(this, callback, mDegree.toString(), event.getLabel(), Constant.PRE, 0);
    }

    @Subscribe
    public void onMessageAuthorImageEvent(MessageAuthorImageEvent event) {
        ListViewHead.initView(mView,this);
    }

    @Override
    public void onStop() {
        Log.i(TAG, "EXE STOP");
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (mRequestJson != null)
            CloseRequestUtil.close(mRequestJson);
        if (delectAskRequestJson != null)
            CloseRequestUtil.close(delectAskRequestJson);
    }

    @Subscribe
    public void onMessageComplainEvent(MessageComplainEvent event) {
        if (event.getMessage().equals(getString(R.string.complain))) {
            ComplainFragment fragment = ComplainFragment.newInstance();
            getHostActivity().open(fragment,fragment);        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAlumniQuestions != null && mAlumniQuestions.size() > 0) {
            Intent intent = new Intent(getContext(), CacheIntentService.class);
            intent.putExtra(Constant.LABEL, Constant.MAJOR_ASK);
            intent.putExtra(Constant.MAJOR_ASK, mAlumniQuestions);
            getContext().startService(intent);
        }
    }


    private static class ExeCacheTask extends AsyncTask<String, Void, ArrayList<AlumniQuestion>> {
        private final WeakReference<MajorAskFragment> mMajorAskWeakRef;
        private String degree;
        private String label;

        public ExeCacheTask(MajorAskFragment majorAskFragment) {
            this.mMajorAskWeakRef = new WeakReference<>(majorAskFragment);
        }

        @Override
        protected ArrayList<AlumniQuestion> doInBackground(String... params) {
            MajorAskFragment majorAskFragment = mMajorAskWeakRef.get();
            degree = params[0];
            label = params[1];
            if (majorAskFragment != null) {
                return new MajorAskDbService(majorAskFragment.getContext()).getMajorAsksByDegreeAndLabel(degree, label);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<AlumniQuestion> alumniQuestions) {
            super.onPostExecute(alumniQuestions);
            MajorAskFragment majorAskFragment = mMajorAskWeakRef.get();
            if (majorAskFragment != null) {
                synchronized (mMajorAskWeakRef.get()) {
                    ArrayList<AlumniQuestion> source = majorAskFragment.mAlumniQuestions;
                    ArrayList<AlumniQuestion> tempArray = new ArrayList<>();
                    AlumniQuestion question;
                    for (int i = 0; i < source.size(); i++) {
                        question = new AlumniQuestion();
                        tempArray.add(question);
                    }
                    Collections.copy(tempArray, source);
                    source.removeAll(tempArray);
                    if (alumniQuestions != null && alumniQuestions.size() > 0) {
                        Log.i(TAG, SchoolFriendGson.newInstance().toJson(alumniQuestions));
                        Collections.sort(alumniQuestions, Collections.reverseOrder());
                        source.addAll(alumniQuestions);
                        for (AlumniQuestion alumniQuestion : alumniQuestions) {
                            Log.i(TAG, alumniQuestion.getLabel());
                        }
                        majorAskFragment.mMajorAskAdapter.notifyDataSetChanged();
                     }
                }
            }
        }
    }

}
