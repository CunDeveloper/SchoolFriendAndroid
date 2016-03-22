package com.nju.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ListCallbackSingleChoice;
import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.adatper.RecommendWorkAdapter;
import com.nju.adatper.RecommendWorkItemAdapter;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.model.RecommendWork;
import com.nju.test.TestData;
import com.nju.util.CloseRequestUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class RecommendWorkFragment extends BaseFragment {

    private static final String TAG = RecommendWorkFragment.class.getSimpleName();
    private RelativeLayout mCollegeMainLayout;
    private ArrayList<TextView> mChooseLevelViews = new ArrayList<>();
    private FloatingActionButton mFloatBn;
    private  ArrayList<RecommendWork>  recommendWorks;

    private PostRequestJson mRequestJson;
    private SwipeRefreshLayout mRefreshLayout;
    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(RecommendWorkFragment.this)){
                ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
                mRefreshLayout.setRefreshing(false);
                error.printStackTrace();
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(RecommendWorkFragment.this)){
                Log.i(TAG, responseBody);
                mRefreshLayout.setRefreshing(false);
            }
        }
    };

    public static RecommendWorkFragment newInstance( ) {
        RecommendWorkFragment fragment = new RecommendWorkFragment();

        return fragment;
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
        return view;
    }

    private void setUpOnRefreshListener(View view){
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                updateRecommendWork();
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateRecommendWork();
            }
        });
    }

    private void updateRecommendWork() {
        mRequestJson = new PostRequestJson("https://api.myjson.com/bins/3ucpf","",callback);
        HttpManager.getInstance().exeRequest(mRequestJson);
    }

    private void  setListView(View view){
        recommendWorks = TestData.getRecommendWorks();
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new RecommendWorkItemAdapter(getContext(), recommendWorks));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(RecommendWorkItemDetailFragment.newInstance(recommendWorks.get(position)));
            }
        });
        final int[] position = {0};
        TextView textView = (TextView) getActivity().findViewById(R.id.main_viewpager_menu_more);
        ListCallbackSingleChoice listCallback= new ListCallbackSingleChoice(){

            @Override
            public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                position[0] = i;
                mRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(true);
                        updateRecommendWork();
                    }
                });
                setTitle(charSequence.toString());
                return true;
            }
        };
        final SchoolFriendDialog dialog  = SchoolFriendDialog.singleChoiceListDialog(getContext(), getString(R.string.chooseType), getResources().getStringArray(R.array.recommdWorkType), listCallback);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setSelectedIndex(position[0]);
                dialog.show();
            }
        });
    }



    @Override
    public void onStop(){
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

    private void addLevelChooseItem(View view){
         LinearLayout input = (LinearLayout) view.findViewById(R.id.college_choose_dialog_choose_layout);
        Set<String> levels = getHostActivity().getSharedPreferences().getStringSet(getString(R.string.level),new HashSet<String>());
        Set<String> collegeSet = getHostActivity().getSharedPreferences()
                .getStringSet(getString(R.string.undergraduateCollege),new HashSet<String>());
        final String[] colleges = (String[]) collegeSet.toArray(new String[collegeSet.size()]);

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
                    if (mTV.getText().toString().equals(getString(R.string.all))) {
                        mFloatBn.setVisibility(View.VISIBLE);
                    }
                    changeLevelTVColor(mTV);
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



}
