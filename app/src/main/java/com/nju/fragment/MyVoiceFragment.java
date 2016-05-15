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
import android.widget.TextView;

import com.nju.activity.BaseActivity;
import com.nju.activity.R;
import com.nju.adatper.PersonVoiceAdapter;
import com.nju.event.NetworkInfoEvent;
import com.nju.http.ImageDownloader;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniVoice;
import com.nju.model.AuthorInfo;
import com.nju.model.EntryDate;
import com.nju.model.MyVoice;
import com.nju.service.AlumniVoiceService;
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

public class MyVoiceFragment extends BaseFragment {
    private static final String TAG = MyVoiceFragment.class.getSimpleName();
    private static final String AUTHOR_PARAM = "authorParam";
    private PostRequestJson mRequestJson;
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<AlumniVoice> mAlumniVoices;
    private PersonVoiceAdapter mPersonVoiceAdapter;
    private AuthorInfo mAuthor;
    private RelativeLayout mFootView;
    private ArrayList<MyVoice> mMyVoices = new ArrayList<>();
    private ListView mListView;

    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(MyVoiceFragment.this)) {
                ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
                mRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Log.e(TAG, error.getMessage());
                mFootView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(MyVoiceFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody, AlumniVoice.class);
                    if (object != null) {
                        ArrayList alumniVoices = (ArrayList) object;
                        if (alumniVoices.size() > 0) {
                            mAlumniVoices.clear();
                            for (Object obj : alumniVoices) {
                                AlumniVoice alumniVoice = (AlumniVoice) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(alumniVoice));
                                mAlumniVoices.add(alumniVoice);
                              
                            }

                            int length = mAlumniVoices.size();
                            if (length > Constant.MAX_ROW) {
                                for (int i = length - 1; i > Constant.MAX_ROW; i--) {
                                    mAlumniVoices.remove(mAlumniVoices.get(i));
                                }
                            }
                            getHostActivity().getSharedPreferences().edit()
                                    .putInt(Constant.MY_VOICE_PRE_ID, mAlumniVoices.get(0).getId()).apply();
                            getHostActivity().getSharedPreferences().edit()
                                    .putInt(Constant.MY_VOICE_NEXT_ID, mAlumniVoices.get(mAlumniVoices.size() - 1).getId()).apply();

                            initMap();
                            mPersonVoiceAdapter.notifyDataSetChanged();
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


    public MyVoiceFragment() {
        // Required empty public constructor
    }

    public static MyVoiceFragment newInstance(AuthorInfo authorInfo) {
        MyVoiceFragment fragment = new MyVoiceFragment();
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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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

    private void setUpOnRefreshListener(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        final int userId = getHostActivity().userId();
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                if (userId == mAuthor.getAuthorId()) {
                    mRequestJson = AlumniVoiceService.queryMyVoices(MyVoiceFragment.this, callback, Constant.ALL, Constant.PRE, 0);
                } else {
                    mRequestJson = AlumniVoiceService.queryOtherAuthorAlumniVoices(MyVoiceFragment.this, callback, Constant.PRE, 0, mAuthor.getAuthorId());
                }
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (userId == mAuthor.getAuthorId()) {
                    mRequestJson = AlumniVoiceService.queryMyVoices(MyVoiceFragment.this, callback, Constant.ALL, Constant.PRE, 0);
                } else {
                    mRequestJson = AlumniVoiceService.queryOtherAuthorAlumniVoices(MyVoiceFragment.this, callback,Constant.PRE, 0,mAuthor.getAuthorId());
                }
            }
        });
    }

    private void initMap() {
        MyVoice myVoice;
        EntryDate entryDate;
        for (AlumniVoice alumniVoice : mAlumniVoices) {
            boolean isContain = true;
            myVoice = new MyVoice();
            final long time = DateUtil.getTime(alumniVoice.getDate());
            String day = DateFormat.format(Constant.DD, time).toString();
            String month = DateFormat.format(Constant.MM, time).toString();
            entryDate = new EntryDate(month, day);
            for (MyVoice voice : mMyVoices) {
                if (entryDate.equals(voice.getEntryDate())) {
                    isContain = false;
                    ArrayList<AlumniVoice> tempList = voice.getAlumniVoices();
                    if (!tempList.contains(alumniVoice)) {
                        tempList.add(alumniVoice);
                        voice.setAlumniVoices(tempList);
                        break;
                    }
                }
            }
            if (isContain) {
                Log.i(TAG, "DAY=" + entryDate.getDay() + "==MONTH=" + entryDate.getMonth());
                myVoice.setEntryDate(entryDate);
                ArrayList<AlumniVoice> alumniVoices = new ArrayList<>();
                alumniVoices.add(alumniVoice);
                myVoice.setAlumniVoices(alumniVoices);
                mMyVoices.add(myVoice);
            }
        }
        Collections.sort(mMyVoices, Collections.reverseOrder());
    }


    private void initListView(View view) {
        mAlumniVoices =  new ArrayList<>();
        mListView = (ListView) view.findViewById(R.id.listView);
        new ListViewHead((BaseActivity)getHostActivity()).setUp(mListView);
        mFootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_footer, mListView, false);
        mFootView.setVisibility(View.GONE);
        mListView.addFooterView(mFootView);
        mPersonVoiceAdapter = new PersonVoiceAdapter(this, mMyVoices,mAuthor.getAuthorId());
        mListView.setAdapter(mPersonVoiceAdapter);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == (mPersonVoiceAdapter.getCount())) {
                    mFootView.setVisibility(View.VISIBLE);
                    mRequestJson = AlumniVoiceService.queryMyVoices(MyVoiceFragment.this, callback, Constant.ALL, Constant.NEXT);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        ListViewHead.initView(view,mAuthor,this);
    }

    @Subscribe
    public void onNetStateMessageState(NetworkInfoEvent event) {
        int userId = getHostActivity().userId();
        if (event.isConnected()) {
            if (userId == mAuthor.getAuthorId()) {
                mRequestJson = AlumniVoiceService.queryMyVoices(MyVoiceFragment.this, callback, Constant.ALL, Constant.PRE, 0);
            } else {
                mRequestJson = AlumniVoiceService.queryOtherAuthorAlumniVoices(MyVoiceFragment.this, callback,Constant.PRE, 0,mAuthor.getAuthorId());
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mAuthor.getAuthorName());
        }
        getHostActivity().display(6);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (mRequestJson != null)
            CloseRequestUtil.close(mRequestJson);
    }

}
