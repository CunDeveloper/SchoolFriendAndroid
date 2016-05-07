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
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nju.activity.R;
import com.nju.adatper.PersonVoiceAdapter;
import com.nju.event.NetworkInfoEvent;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniVoice;
import com.nju.model.EntryDate;
import com.nju.model.MyVoice;
import com.nju.service.AlumniVoiceService;
import com.nju.test.TestData;
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

public class MyVoiceFragment extends BaseFragment {
    private static final String TAG = MyVoiceFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static CharSequence mTitle;
    private PostRequestJson mRequestJson;
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<AlumniVoice> mAlumniVoices;
    private PersonVoiceAdapter mPersonVoiceAdapter;
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
                            for (Object obj : alumniVoices) {
                                AlumniVoice alumniVoice = (AlumniVoice) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(alumniVoice));
                                if (!mAlumniVoices.contains(alumniVoice)) {
                                    mAlumniVoices.add(alumniVoice);
                                }
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

    public static MyVoiceFragment newInstance(String title) {
        MyVoiceFragment fragment = new MyVoiceFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(PARAM_TITLE);
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
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                mRequestJson = AlumniVoiceService.queryMyVoices(MyVoiceFragment.this, callback, Constant.ALL, Constant.PRE, 0);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestJson = AlumniVoiceService.queryMyVoices(MyVoiceFragment.this, callback, Constant.ALL, Constant.PRE, 0);
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
        mAlumniVoices = TestData.getVoicesData();
        mListView = (ListView) view.findViewById(R.id.listView);
        new ListViewHead(this).setUp(mListView);
        mFootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_footer, mListView, false);
        mFootView.setVisibility(View.GONE);
        mListView.addFooterView(mFootView);
        mPersonVoiceAdapter = new PersonVoiceAdapter(this, mMyVoices);
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
    }

    @Subscribe
    public void onNetStateMessageState(NetworkInfoEvent event) {
        if (event.isConnected()) {
            mRequestJson = AlumniVoiceService.queryMyVoices(MyVoiceFragment.this, callback, Constant.ALL, Constant.PRE);
        }
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
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (mRequestJson != null)
            CloseRequestUtil.close(mRequestJson);
    }

}
