package com.nju.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.RecommendWork;
import com.nju.util.Divice;
import com.nju.util.SoftInput;
import com.nju.util.ToastUtil;


public class RecommendWorkItemDetailFragment extends BaseFragment {

    private static final String PARAM_KEY = "key";
    private static RecommendWork mRecommendWork;
    private EditText mAckET;
    private LinearLayout mBottomLayout;
    private RelativeLayout hideReLayout;

    public static RecommendWorkItemDetailFragment newInstance(RecommendWork recommendWork) {
        RecommendWorkItemDetailFragment fragment = new RecommendWorkItemDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_KEY,recommendWork);
        fragment.setArguments(args);
        return fragment;
    }

    public RecommendWorkItemDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecommendWork = getArguments().getParcelable(PARAM_KEY);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.shixi);
        }
        getHostActivity().display(5);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommend_work_item_detail, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        TextView titleTv = (TextView) view.findViewById(R.id.recommend_work_item_detail_title);
        titleTv.setText(mRecommendWork.getTitle());
        TextView detailTv = (TextView) view.findViewById(R.id.recommend_work_item_detail_detail);
        detailTv.setText(mRecommendWork.getContent());
        TextView emailTv = (TextView) view.findViewById(R.id.fragment_recommend_work_item_detail_email);
        emailTv.setText(mRecommendWork.getEmail());
       // mAckET = (EditText) view.findViewById(R.id.re_work_item_detail_comment_et);
        mBottomLayout = (LinearLayout) view.findViewById(R.id.re_work_item_detail_layout);
        hideReLayout = (RelativeLayout) view.findViewById(R.id.re_work_item_detail_hide_main_layout);
        findJobClick(view);
        askJob(view);
        collectJob(view);
        hideSoft(view);
        return view;
    }


    private void findJobClick(View view){
        TextView textView = (TextView) view.findViewById(R.id.re_work_item_yinpin_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
                intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
                intent.setData(Uri.parse("mailto:default@recipient.com")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
            }
        });
    }

    private void askJob(View view ){
        TextView textView = (TextView) view.findViewById(R.id.re_work_item_ask_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mAckET.setVisibility(View.VISIBLE);
                //mAckET.setFocusable(true);
                mBottomLayout.setVisibility(View.GONE);
                hideReLayout.setVisibility(View.VISIBLE);
                SoftInput.open(getContext());
               //

            }
        });
    }

    private void hideSoft(View view){
        View mView = view.findViewById(R.id.re_work_item_detail_hide_layout);
        final TextView mEmotionTv = (TextView) view.findViewById(R.id.comment_emotion);
        final LinearLayout mEmotionLineLayout = (LinearLayout) view.findViewById(R.id.comment_input_emotion_main);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftInput.close(getContext(),hideReLayout);
                hideReLayout.setVisibility(View.GONE);
                mBottomLayout.setVisibility(View.VISIBLE);
            }
        });
        mEmotionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmotionLineLayout.getVisibility()==View.GONE){
                    mEmotionLineLayout.setVisibility(View.VISIBLE);
                    SoftInput.close(getContext(), mEmotionTv);

                }else {
                    SoftInput.open(getContext());
                    mEmotionLineLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mEmotionLineLayout.setVisibility(View.GONE);
                        }
                    },400);

                }

            }
        });
    }

    private void collectJob(View view){
        TextView tv = (TextView) view .findViewById(R.id.re_work_item_collect);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
