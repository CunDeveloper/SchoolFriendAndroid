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
import com.nju.activity.PersonInfoEvent;
import com.nju.activity.PraiseEvent;
import com.nju.activity.R;
import com.nju.adatper.AlumniTalkAdapter;
import com.nju.adatper.CollageAdapter;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniTalk;
import com.nju.service.AlumniTalkService;
import com.nju.util.BottomToolBar;
import com.nju.util.CloseRequestUtil;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.ListViewHead;
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
    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mFootView;
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
                            getHostActivity().getSharedPreferences().edit()
                                    .putInt(Constant.DYNAMIC_PRE_ID,mAlumniTalks.get(0).getId()).apply();
                            getHostActivity().getSharedPreferences().edit()
                                    .putInt(Constant.DYNAMIC_NEXT_ID,mAlumniTalks.get(mAlumniTalks.size()-1).getId()).apply();
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
        setUpOnRefreshListener(view);
        BottomToolBar.show(this, view);
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
        CloseRequestUtil.close(mRequestJson);
    }

    @Subscribe
    public void onMessagePraiseEvent(PraiseEvent event){
        ToastUtil.showShortText(getContext(), event.getId() + "praise");
    }

    @Subscribe
    public void onMessageCommentEvent(CommentEvent event){
        ToastUtil.showShortText(getContext(), event.getId() + "comment");
    }

    @Subscribe
    public void onMessagePersonEvent(PersonInfoEvent event){
        getHostActivity().open(CircleFragment.newInstance(event.getAuthorInfo()));
    }
    private void initListView(View view){
        mAlumniTalks = new ArrayList<>();
        ListView listView = (ListView) view.findViewById(R.id.listView);
        ListViewHead.setUp(this,view,listView);
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
                mRequestJson = AlumniTalkService.queryAlumniTalks(AlumniDynamicFragment.this, callback, Constant.ALL,Constant.PRE);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestJson = AlumniTalkService.queryAlumniTalks(AlumniDynamicFragment.this, callback, Constant.ALL,Constant.PRE);
            }
        });
    }


    private static class AlumniTalkSort implements Comparator<AlumniTalk> {
        @Override
        public int compare(AlumniTalk lhs, AlumniTalk rhs) {
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


}
