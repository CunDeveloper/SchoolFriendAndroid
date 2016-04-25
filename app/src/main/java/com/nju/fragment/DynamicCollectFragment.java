package com.nju.fragment;

import android.os.Bundle;
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
import com.nju.activity.MessageEvent;
import com.nju.activity.R;
import com.nju.adatper.DynamicCollectAdapter;
import com.nju.event.MessageEventMore;
import com.nju.model.DynamicCollect;
import com.nju.test.TestData;
import com.nju.util.Divice;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;


public class DynamicCollectFragment extends BaseFragment {

    private static final String PARAM_TITLE = "paramTitle";
    private static final String TAG = DynamicCollectFragment.class.getSimpleName();
    private static String mTitle;
    private int mChoosePosition;
    private boolean mIsMore = false;
    private ArrayList<DynamicCollect> mDynamicCollects;
    private DynamicCollectAdapter mDynamicCollectAdapter;
    private RelativeLayout mCollectToolLayout;
    public static DynamicCollectFragment newInstance(String title) {
        DynamicCollectFragment fragment = new DynamicCollectFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE,title);
        fragment.setArguments(args);
        return fragment;
    }

    public DynamicCollectFragment() {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.refresh_listview, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initListView(view);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMessageChoose(MessageEvent event){
        Log.i(TAG, event.getMessage());
        if (event.getMessage().equals(getString(R.string.more))){
            mIsMore = true;
            for (DynamicCollect dynamicCollect:mDynamicCollects){
                if (dynamicCollect.getId() == mChoosePosition){
                    dynamicCollect.setCheck(2);
                }else {
                    dynamicCollect.setCheck(1);
                }
            }
            mCollectToolLayout.setVisibility(View.VISIBLE);
            mDynamicCollectAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void onMessageEventMore(MessageEventMore eventMore){
        for (DynamicCollect dynamicCollect:mDynamicCollects){
            dynamicCollect.setCheck(0);
        }
        mCollectToolLayout.setVisibility(View.GONE);
        mDynamicCollectAdapter.notifyDataSetChanged();
        mIsMore = false;
    }

    public boolean isMore(){
        return mIsMore;
    }

    private void initListView(View view){
        mDynamicCollects = TestData.getDynamicCollects();
        ListView listView = (ListView) view.findViewById(R.id.listView);
        mDynamicCollectAdapter = new DynamicCollectAdapter(getContext(),mDynamicCollects);
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
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mTitle);
        }
        getHostActivity().display(6);
    }

}
