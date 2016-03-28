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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.adatper.AlumniVoiceItemAdapter;
import com.nju.adatper.AlumniVoiceViewPage;
import com.nju.adatper.CollageAdapter;
import com.nju.adatper.RecommendWorkItemAdapter;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.http.response.QueryJson;
import com.nju.model.AlumniVoice;
import com.nju.model.RecommendWork;
import com.nju.test.TestData;
import com.nju.util.CloseRequestUtil;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.PathConstant;
import com.nju.util.SchoolFriendGson;
import com.nju.util.ToastUtil;
import com.squareup.okhttp.Call;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class AlumniVoiceFragment extends BaseFragment {

    private static final String TAG = AlumniVoiceFragment.class.getSimpleName();
    private RelativeLayout mCollegeMainLayout;
    private LinearLayout mChooseLayout;
    private ArrayList<TextView> mChooseLevelViews = new ArrayList<>();
    private PostRequestJson mRequestJson;
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<AlumniVoice> mVoices;
    private RelativeLayout mFootView;
    private AlumniVoiceItemAdapter mAlumniVoiceItemAdapter;
    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(AlumniVoiceFragment.this)){
                ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
                mRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Log.e(TAG, error.getMessage());
                mFootView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(AlumniVoiceFragment.this)){
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody,AlumniVoice.class);
                    if (object != null){
                        ArrayList majorAsks = (ArrayList) object;
                        if (majorAsks.size()>0){
                            for (Object obj :majorAsks){
                                 AlumniVoice   alumniVoice = (AlumniVoice) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(alumniVoice));
                                mVoices.add(alumniVoice);
                            }
                            Collections.sort(mVoices, new Comparator<AlumniVoice>() {
                                @Override
                                public int compare(AlumniVoice lhs, AlumniVoice rhs) {
                                    final long lhsTime = DateUtil.getTime(lhs.getDate());
                                    final long rhsTime = DateUtil.getTime(rhs.getDate());
                                    if (lhsTime > rhsTime) {
                                        return -1;
                                    } else if (lhsTime < rhsTime) {
                                        return 1;
                                    }
                                    return 0;
                                }
                            });
                            int length = mVoices.size();
                            if (length>10){
                                for (int i = length-1;i>10;i--){
                                    mVoices.remove(mVoices.get(i));
                                }
                            }
                            mAlumniVoiceItemAdapter.notifyDataSetChanged();
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

    public static AlumniVoiceFragment newInstance() {
        return  new AlumniVoiceFragment();
    }

    public AlumniVoiceFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tu_cao, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getActivity()), view.getPaddingRight(), view.getPaddingBottom());
        mCollegeMainLayout = (RelativeLayout) view.findViewById(R.id.college_choose_dialog_relayout);
        setListView(view);
        openChooseDialog(view);
        hideChooseDialog(view);
        addLevelChooseItem(view);
        setUpOnRefreshListener(view);
        return view;
    }

    private void  setListView(View view){
        mVoices = TestData.getVoicesData();
        ListView listView = (ListView) view.findViewById(R.id.listView);
        mFootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_footer, listView, false);
        mFootView.setVisibility(View.GONE);
        listView.addFooterView(mFootView);
        mAlumniVoiceItemAdapter = new AlumniVoiceItemAdapter(getContext(), mVoices);
        listView.setAdapter(mAlumniVoiceItemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(AlumniVoiceItemDetail.newInstance(mVoices.get(position)));
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == (mAlumniVoiceItemAdapter.getCount())) {
                    mFootView.setVisibility(View.VISIBLE);
                    updateVoices();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        final int[] position = {0};
        TextView textView = (TextView) getActivity().findViewById(R.id.main_viewpager_menu_more);
        MaterialDialog.ListCallbackSingleChoice listCallback= new MaterialDialog.ListCallbackSingleChoice(){

            @Override
            public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                position[0] = i;
                mRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(true);
                        updateVoices();
                    }
                });
                setTitle(charSequence.toString());
                return true;
            }
        };
        final SchoolFriendDialog dialog  = SchoolFriendDialog.singleChoiceListDialog(getContext(), getString(R.string.chooseType), getResources().getStringArray(R.array.voiceType), listCallback);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setSelectedIndex(position[0]);
                dialog.show();
            }
        });
    }

    private void updateVoices(){
        final String json = QueryJson.queryLimitToString(this);
        String url = PathConstant.BASE_URL+PathConstant.ALUMNS_VOICE_PATH+PathConstant.ALUMNIS_VOICE_SUB_PATH_QUERY+"?level=所有";
        mRequestJson = new PostRequestJson(url,json,callback);
        Log.i(TAG,url);
        HttpManager.getInstance().exeRequest(mRequestJson);
    }

    private void setUpOnRefreshListener(View view){
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                updateVoices();
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateVoices();
            }
        });
    }

    @Override
    public void onStop(){
        super.onStop();
        CloseRequestUtil.close(mRequestJson);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle(getString(R.string.alumni_heart));
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
    private void openChooseDialog(View view){
        FloatingActionButton floatBn = (FloatingActionButton) view.findViewById(R.id.college_choose_dialog_actionBn);
        floatBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void hideChooseDialog(View view){
        View mView = view.findViewById(R.id.college_choose_dialog_view);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.GONE);
            }
        });
    }

    private void addLevelChooseItem(View view){
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.college_choose_dialog_choose_layout);
        final ListView listView = (ListView) view.findViewById(R.id.college_choose_dialog_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCollegeMainLayout.setVisibility(View.GONE);
            }
        });
        Set<String> levels = getHostActivity().getSharedPreferences().getStringSet(getString(R.string.level),new HashSet<String>());
        Set<String> collegeSet = getHostActivity().getSharedPreferences()
                .getStringSet(getString(R.string.undergraduateCollege),new HashSet<String>());
        final String[] colleges = collegeSet.toArray(new String[collegeSet.size()]);

        for (String level:levels){
            Log.i(TAG,level);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.bottom_choose_textview,null);
            textView.setLayoutParams(param);
            textView.setText(level);
            if (textView.getText().toString().equals(getString(R.string.undergraduate))){
                textView.setTextColor(ContextCompat.getColor(getContext(),R.color.primayDark));
                listView.setAdapter(new CollageAdapter(getContext(), colleges));
                listView.setVisibility(View.VISIBLE);
            }
            layout.addView(textView);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView mTV = (TextView) v;
                    if (!mTV.getText().toString().equals(getString(R.string.all))) {
                        listView.setAdapter(new CollageAdapter(getContext(),colleges));
                        listView.setVisibility(View.VISIBLE);
                    } else {
                        mCollegeMainLayout.setVisibility(View.GONE);
                    }
                    changeLevelTVColor(mTV);
                }
            });
            mChooseLevelViews.add(textView);
        }
    }

    private void changeLevelTVColor(TextView view){
        for (TextView textView:mChooseLevelViews){
            if (view.getText().toString().equals(textView.getText().toString())){
                view.setTextColor(ContextCompat.getColor(getContext(),R.color.primayDark));
            }else{
                 textView.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
            }
        }
    }

}
