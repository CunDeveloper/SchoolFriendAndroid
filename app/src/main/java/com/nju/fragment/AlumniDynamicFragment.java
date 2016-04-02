package com.nju.fragment;

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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nju.activity.CommentEvent;
import com.nju.activity.PraiseEvent;
import com.nju.activity.R;
import com.nju.adatper.AlumniTalkAdapter;
import com.nju.adatper.CollageAdapter;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniTalk;
import com.nju.service.AlumniTalkService;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.SchoolFriendGson;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class AlumniDynamicFragment extends BaseFragment {
    private static final String TAG = AlumniDynamicFragment.class.getSimpleName();
    private ArrayList<AlumniTalk> mAlumniTalks;
    private AlumniTalkAdapter mAlumniTalkAdapter;
    private PostRequestJson mRequestJson;
    private RelativeLayout mCollegeMainLayout;
    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mFootView;
    private ArrayList<TextView> mChooseLevelViews = new ArrayList<>();
    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(AlumniDynamicFragment.this)){
                ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
                mRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Log.e(TAG, error.getMessage());
                mFootView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(AlumniDynamicFragment.this)){
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody,AlumniTalk.class);
                    if (object != null){
                        ArrayList majorAsks = (ArrayList) object;
                        if (majorAsks.size()>0){
                            for (Object obj :majorAsks){
                                AlumniTalk  alumniTalk = (AlumniTalk) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(alumniTalk));
                                mAlumniTalks.add(alumniTalk);
                            }
                            Collections.sort(mAlumniTalks, new AlumniTalkSort());
                            int length = mAlumniTalks.size();
                            if (length>10){
                                for (int i = length-1;i>10;i--){
                                    mAlumniTalks.remove(mAlumniTalks.get(i));
                                }
                            }
                            mAlumniTalkAdapter.notifyDataSetChanged();
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
    public static AlumniDynamicFragment newInstance( ) {
        AlumniDynamicFragment fragment = new AlumniDynamicFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public AlumniDynamicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alumni_dynamice, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getBottom());
        initListView(view);
        mCollegeMainLayout = (RelativeLayout) view.findViewById(R.id.college_choose_dialog_relayout);
        setUpOnRefreshListener(view);
        addLevelChooseItem(view);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(R.string.alumn_circle);
        }
        getHostActivity().display(2);
    }

    @Override
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onMessagePraiseEvent(PraiseEvent event){
        ToastUtil.showShortText(getContext(), event.getId() + "praise");
    }

    @Subscribe
    public void onMessageCommentEvent(CommentEvent event){
        ToastUtil.showShortText(getContext(), event.getId() + "comment");
    }

    private void initListView(View view){
        mAlumniTalks = new ArrayList<>();
        ListView listView = (ListView) view.findViewById(R.id.listView);
        LinearLayout head = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.listview_header,listView,false);
        listView.addHeaderView(head);
        mFootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_footer, listView, false);
        mFootView.setVisibility(View.GONE);
        listView.addFooterView(mFootView);
        mAlumniTalkAdapter = new AlumniTalkAdapter(this,mAlumniTalks);
        listView.setAdapter(mAlumniTalkAdapter);
        ImageView mCameraImageView = getHostActivity().getMenuCameraView();
        mCameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(MultiChoosePicFragment.newInstance(PublishDynamicFragment.TAG));
            }
        });
    }

    private void setUpOnRefreshListener(View view){
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                mRequestJson = AlumniTalkService.queryAlumniTalks(AlumniDynamicFragment.this, callback, Constant.ALL);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestJson = AlumniTalkService.queryAlumniTalks(AlumniDynamicFragment.this, callback, Constant.ALL);
            }
        });
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
        openChooseDialog(view);
        hideChooseDialog(view);
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
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.primayDark));
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


    private static class AlumniTalkSort implements Comparator<AlumniTalk> {
        @Override
        public int compare(AlumniTalk lhs, AlumniTalk rhs) {
            final long lhsTime = DateUtil.getTime(lhs.getDate());
            final long rhsTime = DateUtil.getTime(rhs.getDate());
            if (lhsTime > rhsTime) {
                return -1;
            } else if (lhsTime < rhsTime) {
                return 1;
            }
            return 0;
        }
    }
}
