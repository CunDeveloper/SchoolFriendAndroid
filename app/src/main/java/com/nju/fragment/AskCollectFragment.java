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
import com.nju.adatper.AskCollectAdapter;
import com.nju.db.db.service.MajorAskCollectDbService;
import com.nju.event.MessageEvent;
import com.nju.event.MessageEventMore;
import com.nju.model.AlumniQuestion;
import com.nju.test.TestData;
import com.nju.util.Divice;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class AskCollectFragment extends BaseFragment {
    private static final String TAG = AskCollectFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static String mTitle;
    private ArrayList<AlumniQuestion> mAlumniQuestions;
    private AskCollectAdapter mAskCollectAdapter;
    private boolean mIsMore = false;
    private int mChoosePosition;
    private RelativeLayout mCollectToolLayout;

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
        new ExeCollectTask(this).execute();
        return view;
    }


    @Subscribe
    public void onMessageEventMore(MessageEventMore eventMore) {
        for (AlumniQuestion alumniQuestion : mAlumniQuestions) {
            alumniQuestion.setCheck(0);
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
            for (AlumniQuestion alumniQuestion : mAlumniQuestions) {
                if (alumniQuestion.getId() == mChoosePosition) {
                    alumniQuestion.setCheck(2);
                } else {
                    alumniQuestion.setCheck(1);
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
        mAlumniQuestions = TestData.getQlumniQuestions();
        ListView listView = (ListView) view.findViewById(R.id.listView);
        mAskCollectAdapter = new AskCollectAdapter(getContext(), mAlumniQuestions);
        listView.setAdapter(mAskCollectAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(MajorAskDetailFragment.newInstance(mAlumniQuestions.get(position)));
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mChoosePosition = mAlumniQuestions.get(position).getId();
                SchoolFriendDialog.listDialog(getContext(), getResources().getStringArray(R.array.collectItem)).show();
                return true;
            }
        });
        mCollectToolLayout = (RelativeLayout) view.findViewById(R.id.collectToolLayout);
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
                    askCollectFragment.mAlumniQuestions.addAll(alumniQuestions);
                    askCollectFragment.mAskCollectAdapter.notifyDataSetChanged();
                }
            }
        }
    }


}
