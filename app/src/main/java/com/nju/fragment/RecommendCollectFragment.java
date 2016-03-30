package com.nju.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.adatper.RecommendWorkCollectAdapter;
import com.nju.adatper.VoiceCollectAdapter;
import com.nju.db.db.service.MajorAskCollectDbService;
import com.nju.db.db.service.RecommendWorkCollectDbService;
import com.nju.model.AlumniQuestion;
import com.nju.model.AlumniVoice;
import com.nju.model.RecommendWork;
import com.nju.test.TestData;
import com.nju.util.Divice;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class RecommendCollectFragment extends BaseFragment {

    private static final String PARAM_TITLE = "paramTitle";
    private static String mTitle;
    private ArrayList<RecommendWork>  mRecommendWorks;
    private RecommendWorkCollectAdapter mRecommendWorkCollectAdapter;
    public static RecommendCollectFragment newInstance(String title) {
        RecommendCollectFragment fragment = new RecommendCollectFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE,title);
        fragment.setArguments(args);
        return fragment;
    }

    public RecommendCollectFragment() {
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
        View view = inflater.inflate(R.layout.refresh_listview, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initListView(view);
        new ExeCollectTask(this).execute();
        return view;
    }

    private void initListView(View view){
        mRecommendWorks = TestData.getRecommendWorks();
        ListView listView = (ListView) view.findViewById(R.id.listView);
        mRecommendWorkCollectAdapter = new RecommendWorkCollectAdapter(getContext(),mRecommendWorks);
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
                SchoolFriendDialog.listDialog(getContext(), getResources().getStringArray(R.array.collectItem), null).show();
                return true;
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

    private static class ExeCollectTask extends AsyncTask<Void,Void,ArrayList<RecommendWork>>
    {
        private final WeakReference<RecommendCollectFragment> mRecommendWorkWeakRef;
        public ExeCollectTask(RecommendCollectFragment  recommendCollectFragment){
            this.mRecommendWorkWeakRef = new WeakReference<>(recommendCollectFragment);
        }
        @Override
        protected ArrayList<RecommendWork> doInBackground(Void... params) {
            RecommendCollectFragment recommendCollectFragment = mRecommendWorkWeakRef.get();
            if (recommendCollectFragment!=null){
                return new RecommendWorkCollectDbService(recommendCollectFragment.getContext()).getCollects();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<RecommendWork> recommendWorks) {
            super.onPostExecute(recommendWorks);
            RecommendCollectFragment recommendCollectFragment = mRecommendWorkWeakRef.get();
            if (recommendCollectFragment!=null){
                if (recommendWorks != null){
                    recommendCollectFragment.mRecommendWorks.addAll(recommendWorks);
                    recommendCollectFragment.mRecommendWorkCollectAdapter.notifyDataSetChanged();
                }
            }
        }
    }

}
