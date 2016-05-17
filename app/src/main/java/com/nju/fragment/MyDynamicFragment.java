package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nju.activity.BaseActivity;
import com.nju.activity.R;
import com.nju.adatper.PersonDynamicAdapter;
import com.nju.event.NetworkInfoEvent;
import com.nju.http.ImageDownloader;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniTalk;
import com.nju.model.AuthorInfo;
import com.nju.model.EntryDate;
import com.nju.model.MyDynamic;
import com.nju.service.AlumniTalkService;
import com.nju.util.CloseRequestUtil;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.ListViewHead;
import com.nju.util.PathConstant;
import com.nju.util.SchoolFriendGson;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class MyDynamicFragment extends BaseFragment {
    private static final String TAG = MyDynamicFragment.class.getSimpleName();
    private static final String AUTHOR_PARAM = "authorParam";
    private ListView mListView;
    private RelativeLayout mFootView;
    private PersonDynamicAdapter mDynamiceAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private PostRequestJson mRequestJson;
    private AuthorInfo mAuthor;
    private ArrayList<MyDynamic> mMyDynamics = new ArrayList<>();
    private ArrayList<AlumniTalk> mAlumniTalks = new ArrayList<>();
    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG, error.getMessage());
            if (FragmentUtil.isAttachedToActivity(MyDynamicFragment.this)) {
                ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
                mRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Log.e(TAG, error.getMessage());
                mFootView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            if (FragmentUtil.isAttachedToActivity(MyDynamicFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody, AlumniTalk.class);
                    if (object != null) {
                        ArrayList majorAsks = (ArrayList) object;
                        mAlumniTalks.clear();
                        if (majorAsks.size() > 0) {
                            for (Object obj : majorAsks) {
                                AlumniTalk alumniTalk = (AlumniTalk) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(alumniTalk));
                                mAlumniTalks.add(alumniTalk);
                            }
                            int length = mAlumniTalks.size();
                            if (length > Constant.MAX_ROW) {
                                for (int i = length - 1; i > Constant.MAX_ROW; i--) {
                                    mAlumniTalks.remove(mAlumniTalks.get(i));
                                }
                            }
//                            getHostActivity().getSharedPreferences().edit()
//                                    .putInt(Constant.MY_DYNAMIC_PRE_ID, mAlumniTalks.get(0).getId()).apply();
//                            getHostActivity().getSharedPreferences().edit()
//                                    .putInt(Constant.MY_DYNAMIC_NEXT_ID, mAlumniTalks.get(mAlumniTalks.size() - 1).getId()).apply();
                            initMap();
                            mDynamiceAdapter.notifyDataSetChanged();
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

    public MyDynamicFragment() {
        // Required empty public constructor
    }

    public static MyDynamicFragment newInstance(AuthorInfo authorInfo) {
        MyDynamicFragment fragment = new MyDynamicFragment();
        Bundle args = new Bundle();
        args.putParcelable(AUTHOR_PARAM,authorInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAuthor = getArguments().getParcelable(AUTHOR_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.person_refresh_listview, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initListView(view);
        setUpOnRefreshListener(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mAuthor.getAuthorName()+Constant.DYNAMIC);
        }
        getHostActivity().display(6);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (mRequestJson != null) {
            CloseRequestUtil.close(mRequestJson);
        }
    }


    private void initListView(View view) {
        initMap();
        mListView = (ListView) view.findViewById(R.id.listView);
        new ListViewHead((BaseActivity)getHostActivity()).setUp(mListView);
        mFootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_footer, mListView, false);
        mFootView.setVisibility(View.GONE);
        mListView.addFooterView(mFootView);
        mDynamiceAdapter = new PersonDynamicAdapter(this, mMyDynamics,mAuthor.getAuthorId());
        mListView.setAdapter(mDynamiceAdapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == (mDynamiceAdapter.getCount())) {
                    mFootView.setVisibility(View.VISIBLE);
                    // mRequestJson = MajorAskService.queryMyAsk(MyAskFragment.this, callback, Constant.ALL,Constant.NEXT);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void setUpOnRefreshListener(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        final int userId = getHostActivity().userId();
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                if (userId == mAuthor.getAuthorId()) {
                    mRequestJson = AlumniTalkService.queryOwnAlumniTalks(MyDynamicFragment.this, callback, Constant.ALL, Constant.PRE, 0);
                } else {
                    mRequestJson = AlumniTalkService.queryOtherAuthorAlumniTalks(MyDynamicFragment.this, callback, Constant.PRE, 0,mAuthor.getAuthorId());
                }
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (userId == mAuthor.getAuthorId()) {
                    mRequestJson = AlumniTalkService.queryOwnAlumniTalks(MyDynamicFragment.this, callback, Constant.ALL, Constant.PRE, 0);
                } else {
                    mRequestJson = AlumniTalkService.queryOtherAuthorAlumniTalks(MyDynamicFragment.this, callback, Constant.PRE, 0,mAuthor.getAuthorId());
                }
            }
        });

        ListViewHead.initView(view, mAuthor,this);
    }

    @Subscribe
    public void onNetStateMessageState(NetworkInfoEvent event) {
        if (event.isConnected()) {
            int userId = getHostActivity().userId();
            if (userId == mAuthor.getAuthorId()) {
                mRequestJson = AlumniTalkService.queryOwnAlumniTalks(MyDynamicFragment.this, callback, Constant.ALL, Constant.PRE, 0);
            } else {
                mRequestJson = AlumniTalkService.queryOtherAuthorAlumniTalks(MyDynamicFragment.this, callback, Constant.PRE, 0,mAuthor.getAuthorId());
            }
        }
    }

    private void initMap() {
        MyDynamic myDynamic;
        EntryDate entryDate;
        for (AlumniTalk alumniTalk : mAlumniTalks) {
            boolean isContain = true;
            myDynamic = new MyDynamic();
            final long time = DateUtil.getTime(alumniTalk.getDate());
            String day = DateFormat.format(Constant.DD, time).toString();
            String month = DateFormat.format(Constant.MM, time).toString();
            entryDate = new EntryDate(month, day);
            for (MyDynamic dynamic : mMyDynamics) {
                if (entryDate.equals(dynamic.getEntryDate())) {
                    isContain = false;
                    ArrayList<AlumniTalk> tempList = dynamic.getAlumniTalks();
                    if (!tempList.contains(alumniTalk)) {
                        tempList.add(alumniTalk);
                        dynamic.setAlumniTalks(tempList);
                        break;
                    }
                }
            }
            if (isContain) {
                Log.i(TAG, "DAY=" + entryDate.getDay() + "==MONTH=" + entryDate.getMonth());
                myDynamic.setEntryDate(entryDate);
                ArrayList<AlumniTalk> alumniTalks = new ArrayList<>();
                alumniTalks.add(alumniTalk);
                myDynamic.setAlumniTalks(alumniTalks);
                mMyDynamics.add(myDynamic);
            }
        }
        Collections.sort(mMyDynamics, Collections.reverseOrder());
    }

}
