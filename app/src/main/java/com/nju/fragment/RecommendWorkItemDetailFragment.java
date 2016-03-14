package com.nju.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.RecommendWork;
import com.nju.util.Divice;
import com.nju.util.ToastUtil;


public class RecommendWorkItemDetailFragment extends BaseFragment {

    private static final String PARAM_KEY = "key";
    private static RecommendWork mRecommendWork;

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
        findJobClick(view);
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


}
