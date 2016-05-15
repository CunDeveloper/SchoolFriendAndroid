package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.nju.adatper.DynamicCollectAdapter;
import com.nju.event.MessageEvent;
import com.nju.event.MessageEventMore;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniDynamicCollect;
import com.nju.model.DynamicCollect;
import com.nju.model.VoiceCollect;
import com.nju.service.AlumniTalkService;
import com.nju.service.AlumniVoiceService;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.SchoolFriendGson;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class DynamicCollectFragment extends BaseFragment {

    private static final String PARAM_TITLE = "paramTitle";
    private static final String TAG = DynamicCollectFragment.class.getSimpleName();
    private static String mTitle;
    private int mChoosePosition;
    private boolean mIsMore = false;
    private ArrayList<AlumniDynamicCollect> mDynamicCollects = new ArrayList<>();
    private DynamicCollectAdapter mDynamicCollectAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mCollectToolLayout;
    private PostRequestJson mQueryCollectsJson;


    private ResponseCallback mQueryCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG,error.getMessage());
            ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
            mRefreshLayout.setRefreshing(false);
            error.printStackTrace();
            Log.e(TAG, error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(DynamicCollectFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody,AlumniDynamicCollect.class);
                    if (object != null) {
                        ArrayList majorAsks = (ArrayList) object;
                        if (majorAsks.size() > 0) {
                            mDynamicCollects.clear();
                            for (Object obj : majorAsks) {
                                AlumniDynamicCollect collect = (AlumniDynamicCollect) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(collect));
                                mDynamicCollects.add(collect);
                            }
                            Collections.sort(mDynamicCollects, Collections.reverseOrder());
                            int length = mDynamicCollects.size();
                            if (length > Constant.MAX_ROW) {
                                for (int i = length - 1; i > Constant.MAX_ROW; i--) {
                                    mDynamicCollects.remove(mDynamicCollects.get(i));
                                }
                            }
                        }
                        mDynamicCollectAdapter.notifyDataSetChanged();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mRefreshLayout.setRefreshing(false);
                }
            }
        }
    };

    public DynamicCollectFragment() {
        // Required empty public constructor
    }

    public static DynamicCollectFragment newInstance(String title) {
        DynamicCollectFragment fragment = new DynamicCollectFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.refresh_listview, container, false);
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
                mQueryCollectsJson = AlumniTalkService.queryCollects(DynamicCollectFragment.this, mQueryCallback);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mQueryCollectsJson = AlumniTalkService.queryCollects(DynamicCollectFragment.this, mQueryCallback);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMessageChoose(MessageEvent event) {
        Log.i(TAG, event.getMessage());
        if (event.getMessage().equals(getString(R.string.more))) {
            mIsMore = true;
            for (AlumniDynamicCollect dynamicCollect : mDynamicCollects) {
                if (dynamicCollect.getId() == mChoosePosition) {
                    dynamicCollect.setCheck(2);
                } else {
                    dynamicCollect.setCheck(1);
                }
            }
            mCollectToolLayout.setVisibility(View.VISIBLE);
            mDynamicCollectAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void onMessageEventMore(MessageEventMore eventMore) {
        for (AlumniDynamicCollect dynamicCollect : mDynamicCollects) {
            dynamicCollect.setCheck(0);
        }
        mCollectToolLayout.setVisibility(View.GONE);
        mDynamicCollectAdapter.notifyDataSetChanged();
        mIsMore = false;
    }

    public boolean isMore() {
        return mIsMore;
    }

    private void initListView(View view) {
        mDynamicCollects = new ArrayList<>();
        ListView listView = (ListView) view.findViewById(R.id.listView);
        mDynamicCollectAdapter = new DynamicCollectAdapter(this, mDynamicCollects);
        listView.setAdapter(mDynamicCollectAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(DynamicCollectDetailFragment.newInstance(mDynamicCollects.get(position)));
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mChoosePosition = mDynamicCollects.get(position).getId();
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

}
