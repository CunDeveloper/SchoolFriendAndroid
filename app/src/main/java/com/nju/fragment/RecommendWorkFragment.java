package com.nju.fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ListCallbackSingleChoice;
import com.nju.View.SchoolFriendDialog;
import com.nju.activity.NetworkInfoEvent;
import com.nju.activity.R;
import com.nju.adatper.RecommendWorkItemAdapter;
import com.nju.db.db.service.RecommendDbService;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.RecommendWork;
import com.nju.service.CacheIntentService;
import com.nju.service.RecommendWorkService;
import com.nju.util.CloseRequestUtil;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.ListViewHead;
import com.nju.util.SchoolFriendGson;
import com.nju.util.SearchViewUtil;
import com.nju.util.SoftInput;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
public class RecommendWorkFragment extends BaseFragment {

    private static final String TAG = RecommendWorkFragment.class.getSimpleName();
    private RelativeLayout mCollegeMainLayout;
    private ArrayList<TextView> mChooseLevelViews = new ArrayList<>();
    private FloatingActionButton mFloatBn;
    private  ArrayList<RecommendWork>  mRecommendWorks = new ArrayList<>();
    private PostRequestJson mRequestJson;
    private SwipeRefreshLayout mRefreshLayout;
    private RecommendWorkItemAdapter mRecommendWorkItemAdapter;
    private RelativeLayout mFootView;
    private static String degreeLabel = Constant.ALL;
    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(RecommendWorkFragment.this)){
                ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
                mRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Log.e(TAG, error.getMessage());
                mFootView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(RecommendWorkFragment.this)){
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody,RecommendWork.class);
                    if (object != null){
                        ArrayList majorAsks = (ArrayList) object;
                        if (majorAsks.size()>0){
                            for (Object obj :majorAsks){
                                RecommendWork   recommendWork = (RecommendWork) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(recommendWork));
                                mRecommendWorks.add(recommendWork);

                            }
                            Collections.sort(mRecommendWorks, new RecommendWorkSort());

                            int length = mRecommendWorks.size();
                            if (length>Constant.MAX_LIST_NUMBER){
                                for (int i = length-1;i>Constant.MAX_LIST_NUMBER;i--){
                                    mRecommendWorks.remove(mRecommendWorks.get(i));
                                }
                            }
                            getHostActivity().getSharedPreferences().edit()
                                    .putInt(Constant.RECOMMEND_PRE_ID,mRecommendWorks.get(0).getId()).apply();
                            getHostActivity().getSharedPreferences().edit()
                                    .putInt(Constant.RECOMMEND_NEXT_ID,mRecommendWorks.get(mRecommendWorks.size()-1).getId()).apply();
                            mRecommendWorkItemAdapter.notifyDataSetChanged();
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


    public static RecommendWorkFragment newInstance( ) {
        return new RecommendWorkFragment();
    }

    public RecommendWorkFragment() {
        // Required empty public constructor
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
        setTitle(getString(R.string.shixi));
        getHostActivity().display(5);
    }

    private void setTitle(String title){
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommend_work, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getActivity()), view.getPaddingRight(), view.getPaddingBottom());
        mCollegeMainLayout = (RelativeLayout) view.findViewById(R.id.college_choose_dialog_relayout);
        mFloatBn =  (FloatingActionButton) view.findViewById(R.id.college_choose_dialog_actionBn);
        setListView(view);
        openChooseDialog();
        hideChooseDialog(view);
        addLevelChooseItem(view);
        setUpOnRefreshListener(view);
        SearchViewUtil.setUp(this,view);
        new ExeCacheTask(this).execute(Constant.ALL,0+"");
        return view;
    }

    private void setUpOnRefreshListener(View view){
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                mRequestJson = RecommendWorkService.queryRecommendWork(RecommendWorkFragment.this, callback, Constant.ALL,Constant.PRE);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestJson = RecommendWorkService.queryRecommendWork(RecommendWorkFragment.this, callback, Constant.ALL,Constant.PRE);
            }
        });
    }



    private void  setListView(View view){
        //mRecommendWorks = TestData.getRecommendWorks();

        ListView listView = (ListView) view.findViewById(R.id.listView);
        ListViewHead.setUp(this, view, listView);
        mFootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_footer, listView, false);
        mFootView.setVisibility(View.GONE);
        listView.addFooterView(mFootView);
        mRecommendWorkItemAdapter = new RecommendWorkItemAdapter(this, mRecommendWorks);
        listView.setAdapter(mRecommendWorkItemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(RecommendWorkItemDetailFragment.newInstance(mRecommendWorks.get(position)));
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == (mRecommendWorkItemAdapter.getCount())) {
                    mFootView.setVisibility(View.VISIBLE);
                    mRequestJson = RecommendWorkService.queryRecommendWork(RecommendWorkFragment.this,callback, Constant.ALL,Constant.NEXT);
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        final int[] position = {0};

//        ListCallbackSingleChoice listCallback= new ListCallbackSingleChoice(){
//
//            @Override
//            public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
//                position[0] = i;
//                mRefreshLayout.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mRefreshLayout.setRefreshing(true);
//                    }
//                });
//                String typeStr = charSequence.toString();
//                String type = 0+"";
//                switch (typeStr) {
//                    case Constant.INTERN:
//                        type = 1 + "";
//                        getHostActivity().getSharedPreferences().edit().putString(Constant.WORK_TYP, type).commit();
//                        break;
//                    case Constant.ALL_JOG:
//                        getHostActivity().getSharedPreferences().edit().putString(Constant.WORK_TYP, type).commit();
//                        type = 2 + "";
//                        break;
//                    case Constant.SCHOOL_JOG:
//                        getHostActivity().getSharedPreferences().edit().putString(Constant.WORK_TYP, type).commit();
//                        type = 3 + "";
//                        break;
//                }
//                String text = getHostActivity().getSharedPreferences().getString(Constant.DEGREE,Constant.ALL);
//                new ExeCacheTask(RecommendWorkFragment.this).execute(text,type);
//                mRequestJson = RecommendWorkService.queryRecommendWorkByType(RecommendWorkFragment.this, callback,text, Integer.valueOf(type));
//                setTitle(charSequence.toString());
//                return true;
//            }
//        };
    }

    @Subscribe
    public void onNetStateMessageState(NetworkInfoEvent event){
        if (event.isConnected()){
            mRequestJson = RecommendWorkService.queryRecommendWork(RecommendWorkFragment.this, callback, Constant.ALL,Constant.PRE);
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
        CloseRequestUtil.close(mRequestJson);
    }

    private void openChooseDialog(){
        mFloatBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.VISIBLE);
                mFloatBn.setVisibility(View.GONE);
            }
        });
    }

    private void hideChooseDialog(View view){
        View mView = view.findViewById(R.id.college_choose_dialog_view);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.GONE);
                mFloatBn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void  addLevelChooseItem(View view){
         LinearLayout input = (LinearLayout) view.findViewById(R.id.college_choose_dialog_choose_layout);
        Set<String> levels = getHostActivity().getSharedPreferences().getStringSet(getString(R.string.level),new HashSet<String>());
        Set<String> collegeSet = getHostActivity().getSharedPreferences()
                .getStringSet(getString(R.string.undergraduateCollege),new HashSet<String>());
        final String[] colleges = collegeSet.toArray(new String[collegeSet.size()]);
        for (String level:levels){
            Log.i(TAG, level);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.bottom_choose_textview,null);
            textView.setLayoutParams(param);
            textView.setText(level);
            if (textView.getText().toString().equals(getString(R.string.undergraduate))){
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            }
            input.addView(textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView mTV = (TextView) v;
                    changeLevelTVColor(mTV);
                    final String text = (String) mTV.getText();
                    String type = getHostActivity().getSharedPreferences().getString(Constant.WORK_TYP,0+"");
                    switch (text) {
                        case Constant.UNDERGRADUATE: {
                            getHostActivity().getSharedPreferences().edit().putString(Constant.DEGREE,text).commit();
                            new ExeCacheTask(RecommendWorkFragment.this).execute(text,type);
                            mRequestJson = RecommendWorkService.queryRecommendWork(RecommendWorkFragment.this, callback, Constant.UNDERGRADUATE,Constant.NEXT);
                            break;
                        }
                        case Constant.MASTER: {
                            new ExeCacheTask(RecommendWorkFragment.this).execute(text,type);
                            getHostActivity().getSharedPreferences().edit().putString(Constant.DEGREE,text).commit();
                            mRequestJson = RecommendWorkService.queryRecommendWork(RecommendWorkFragment.this, callback, Constant.MASTER,Constant.NEXT);
                            break;
                        }
                        case Constant.DOCTOR: {
                            new ExeCacheTask(RecommendWorkFragment.this).execute(text,type);
                            getHostActivity().getSharedPreferences().edit().putString(Constant.DEGREE, text).commit();
                            mRequestJson = RecommendWorkService.queryRecommendWork(RecommendWorkFragment.this, callback, Constant.DOCTOR,Constant.NEXT);
                            break;
                        }
                        case Constant.ALL: {
                            getHostActivity().getSharedPreferences().edit().putString(Constant.DEGREE, text).commit();
                            new ExeCacheTask(RecommendWorkFragment.this).execute(text, type);
                            mRequestJson = RecommendWorkService.queryRecommendWork(RecommendWorkFragment.this, callback, Constant.ALL,Constant.NEXT);
                            break;
                        }
                    }
                    mFloatBn.setVisibility(View.VISIBLE);
                    mCollegeMainLayout.setVisibility(View.GONE);
                }
            });
            mChooseLevelViews.add(textView);
        }
    }

    private void changeLevelTVColor(TextView view){
        for (TextView textView:mChooseLevelViews){
            if (view.getText().toString().equals(textView.getText().toString())){
                view.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
            }else{
                textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (mRecommendWorks != null && mRecommendWorks.size()>0){
            Intent intent = new Intent(getContext(),CacheIntentService.class);
            intent.putExtra(Constant.LABEL,Constant.RECOMMEND);
            intent.putExtra(Constant.RECOMMEND,mRecommendWorks);
            getContext().startService(intent);
        }
    }

    private static class RecommendWorkSort implements Comparator<RecommendWork>{

        @Override
        public int compare(RecommendWork lhs, RecommendWork rhs) {
            final int lhsId = lhs.getId();
            final int rhsId = rhs.getId();
            if (lhsId > rhsId) {
                return -1;
            } else if (lhsId < rhsId) {
                return 1;
            }
            return 0;
        }
    }

    private static class ExeCacheTask extends AsyncTask<String,Void,ArrayList<RecommendWork>>
    {
        private final WeakReference<RecommendWorkFragment> mRecommendWorkWeakRef;
        public ExeCacheTask(RecommendWorkFragment  recommendFragment){
            this.mRecommendWorkWeakRef = new WeakReference<>(recommendFragment);
        }
        @Override
        protected ArrayList<RecommendWork> doInBackground(String... params) {
            RecommendWorkFragment recommendFragment = mRecommendWorkWeakRef.get();
            if (recommendFragment!=null){
                String degree = params[0];
                String type = params[1];
                Log.i(TAG, "degree=" + degree + " " + "type = " + type);
                return new RecommendDbService(recommendFragment.getContext()).getRecommendWorksByDegreeAndType(degree,type);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<RecommendWork> recommendWorks) {
            super.onPostExecute(recommendWorks);
            RecommendWorkFragment recommendFragment = mRecommendWorkWeakRef.get();
            if (recommendFragment!=null){
                if (recommendWorks != null && recommendWorks.size()>0){
                    Log.i(TAG, "cache json =="+SchoolFriendGson.newInstance().toJson(recommendWorks));
                    Collections.sort(recommendWorks, new RecommendWorkSort());
                    ArrayList<RecommendWork> source = recommendFragment.mRecommendWorks;
                    ArrayList<RecommendWork> tempArray = new ArrayList<>();
                    RecommendWork work;
                    for (int i=0;i<source.size();i++){
                        work = new RecommendWork();
                        tempArray.add(work);
                    }
                     Collections.copy(tempArray,source);
                    source.removeAll(tempArray);
                    source.addAll(recommendWorks);
                    recommendFragment.mRecommendWorkItemAdapter.notifyDataSetChanged();
                }else {
                    recommendFragment.getHostActivity().getSharedPreferences().edit()
                            .putInt(Constant.RECOMMEND_PRE_ID,0).apply();
                    recommendFragment.getHostActivity().getSharedPreferences().edit()
                            .putInt(Constant.RECOMMEND_NEXT_ID, 0).apply();
                }
            }
        }
    }
}
