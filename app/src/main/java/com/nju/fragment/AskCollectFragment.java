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
import com.nju.adatper.AskCollectAdapter;
import com.nju.db.db.service.MajorAskCollectDbService;
import com.nju.event.MessageEvent;
import com.nju.event.MessageEventMore;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniQuestion;
import com.nju.model.QuestionCollect;
import com.nju.model.RecommendCollect;
import com.nju.service.MajorAskService;
import com.nju.service.RecommendWorkService;
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


public class AskCollectFragment extends BaseFragment {
    private static final String TAG = AskCollectFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static String mTitle;
    private ArrayList<QuestionCollect> mQuestionCollects = new ArrayList<>();
    private PostRequestJson mQueryCollectsJson;
    private SwipeRefreshLayout mRefreshLayout;
    private AskCollectAdapter mAskCollectAdapter;
    private boolean mIsMore = false;
    private int mChoosePosition;
    private RelativeLayout mCollectToolLayout;

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
            if (FragmentUtil.isAttachedToActivity(AskCollectFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody, QuestionCollect.class);
                    if (object != null) {
                        ArrayList majorAsks = (ArrayList) object;
                        if (majorAsks.size() > 0) {
                            mQuestionCollects.clear();
                            for (Object obj : majorAsks) {
                                QuestionCollect collect = (QuestionCollect) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(collect));
                                mQuestionCollects.add(collect);
                            }
                            Collections.sort(mQuestionCollects, Collections.reverseOrder());
                            int length = mQuestionCollects.size();
                            if (length > Constant.MAX_ROW) {
                                for (int i = length - 1; i > Constant.MAX_ROW; i--) {
                                    mQuestionCollects.remove(mQuestionCollects.get(i));
                                }
                            }
                        }
                        mAskCollectAdapter.notifyDataSetChanged();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mRefreshLayout.setRefreshing(false);
                }
            }
        }
    };

    public AskCollectFragment() {
        // Required empty public constructor
    }

    public static AskCollectFragment newInstance(String title) {
        AskCollectFragment fragment = new AskCollectFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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
        View view = inflater.inflate(R.layout.refresh_listview, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initListView(view);
        //new ExeCollectTask(this).execute();
        setUpOnRefreshListener(view);
        return view;
    }


    @Subscribe
    public void onMessageEventMore(MessageEventMore eventMore) {
        for (QuestionCollect collect : mQuestionCollects) {
            collect.setCheck(0);
        }
        mCollectToolLayout.setVisibility(View.GONE);
        mAskCollectAdapter.notifyDataSetChanged();
        mIsMore = false;
    }

    @Subscribe
    public void onMessageChoose(MessageEvent event) {
        Log.i(TAG, event.getMessage());
        if (event.getMessage().equals(getString(R.string.more))) {
            mIsMore = true;
            for (QuestionCollect collect : mQuestionCollects) {
                if (collect.getId() == mChoosePosition) {
                    collect.setCheck(2);
                } else {
                    collect.setCheck(1);
                }
            }
            mCollectToolLayout.setVisibility(View.VISIBLE);
            mAskCollectAdapter.notifyDataSetChanged();
        }
    }

    public boolean isMore() {
        return mIsMore;
    }

    private void initListView(View view) {
        mQuestionCollects = new ArrayList<>();
        ListView listView = (ListView) view.findViewById(R.id.listView);
        mAskCollectAdapter = new AskCollectAdapter(getContext(),mQuestionCollects);
        listView.setAdapter(mAskCollectAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(MajorAskDetailFragment.newInstance(mQuestionCollects.get(position).getAlumniQuestion()));
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mChoosePosition = mQuestionCollects.get(position).getId();
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
                mQueryCollectsJson = MajorAskService.queryCollects(AskCollectFragment.this, mQueryCallback);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mQueryCollectsJson = MajorAskService.queryCollects(AskCollectFragment.this, mQueryCallback);
            }
        });
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

    private static class ExeCollectTask extends AsyncTask<Void, Void, ArrayList<AlumniQuestion>> {
        private final WeakReference<AskCollectFragment> mAskCollectWeakReference;

        public ExeCollectTask(AskCollectFragment askCollectFragment) {
            this.mAskCollectWeakReference = new WeakReference<>(askCollectFragment);
        }

        @Override
        protected ArrayList<AlumniQuestion> doInBackground(Void... params) {
            AskCollectFragment askCollectFragment = mAskCollectWeakReference.get();
            if (askCollectFragment != null) {
                return new MajorAskCollectDbService(askCollectFragment.getContext()).getCollects();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<AlumniQuestion> alumniQuestions) {
            super.onPostExecute(alumniQuestions);
            AskCollectFragment askCollectFragment = mAskCollectWeakReference.get();
            if (askCollectFragment != null) {
                if (alumniQuestions != null) {
                    //askCollectFragment.mQuestionCollects.addAll(alumniQuestions);
                    askCollectFragment.mAskCollectAdapter.notifyDataSetChanged();
                }
            }
        }
    }


}
