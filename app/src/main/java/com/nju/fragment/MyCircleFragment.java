package com.nju.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nju.activity.R;
import com.nju.util.Divice;

public class MyCircleFragment extends BaseFragment {

    public static MyCircleFragment newInstance( ) {
        MyCircleFragment fragment = new MyCircleFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public MyCircleFragment() {
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
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.my_circle);
        }
        getHostActivity().display(5);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_circle, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initView(view);
        return view;
    }

    private void initView(View view){
        RelativeLayout mDynamicLay = (RelativeLayout) view.findViewById(R.id.my_dynamic);
        mDynamicLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(PersonCircleFragment.newInstance("张小军"));
            }
        });
        RelativeLayout mDynamicCollectLay = (RelativeLayout) view.findViewById(R.id.dynamic_collect);
        mDynamicCollectLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(DynamicCollectFragment.newInstance(getString(R.string.my_collect)));
            }
        });
        RelativeLayout mDynamicPublishLay = (RelativeLayout) view.findViewById(R.id.dynamic_publish);
        mDynamicPublishLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(PublishDynamicFragment.newInstance(getString(R.string.publish_dynamic)));
            }
        });

        RelativeLayout mVoiceLay = (RelativeLayout) view.findViewById(R.id.my_voice);
        mVoiceLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(MyVoiceFragment.newInstance(getString(R.string.my_heart)));
            }
        });
        RelativeLayout mVoiceCollectLay = (RelativeLayout) view.findViewById(R.id.voice_collect);
        mVoiceCollectLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(VoiceCollectFragment.newInstance(getString(R.string.my_collect)));
            }
        });

        RelativeLayout mVoicePublishLay = (RelativeLayout) view.findViewById(R.id.voice_publish);
        mVoicePublishLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(PublishVoiceFragment.newInstance(getString(R.string.publish_voice)));
            }
        });

        RelativeLayout mAskLay = (RelativeLayout) view.findViewById(R.id.my_ask);
        mAskLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(MyAskFragment.newInstance(getString(R.string.my_ask)));
            }
        });
        RelativeLayout mAskCollect = (RelativeLayout) view.findViewById(R.id.ask_collect);
        mAskCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(AskCollectFragment.newInstance(getString(R.string.my_collect)));
            }
        });
        RelativeLayout mAskPublish = (RelativeLayout) view.findViewById(R.id.publish_ask);
        mAskPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(AskPublishFragment.newInstance(getString(R.string.publsh_ask)));
            }
        });
        RelativeLayout mRecommendLay = (RelativeLayout) view.findViewById(R.id.my_recommend);
        mRecommendLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(MyRecommendFragment.newInstance(getString(R.string.my_recommend)));
            }
        });

        RelativeLayout mRecommendCollect = (RelativeLayout) view.findViewById(R.id.recommend_collect);
        mRecommendCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(RecommendCollectFragment.newInstance(getString(R.string.my_collect)));
            }
        });
        RelativeLayout mRecommendPublish = (RelativeLayout) view.findViewById(R.id.recommend_publish);
        mRecommendPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(RecommendPublishFragment.newInstance(getString(R.string.publish_recommend)));
            }
        });

    }


}
