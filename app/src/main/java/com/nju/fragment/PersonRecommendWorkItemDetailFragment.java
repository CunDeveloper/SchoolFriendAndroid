package com.nju.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.RecommendWork;
import com.nju.util.CommentUtil;
import com.nju.util.Divice;
import com.nju.util.SoftInput;
import com.nju.util.ToastUtil;


public class PersonRecommendWorkItemDetailFragment extends BaseFragment {
    private static final String PARAM_KEY = "key";
    public static final String TAG = RecommendWorkItemDetailFragment.class.getSimpleName() ;
    private static RecommendWork mRecommendWork;
    private EditText mContentEditText ;
    private TextView mEmailTV;
    private static final String TEXT_TYPE = "text/plain";

    public static PersonRecommendWorkItemDetailFragment newInstance(RecommendWork recommendWork) {
        PersonRecommendWorkItemDetailFragment fragment = new PersonRecommendWorkItemDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_KEY,recommendWork);
        fragment.setArguments(args);
        return fragment;
    }

    public PersonRecommendWorkItemDetailFragment() {
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
        View view = inflater.inflate(R.layout.fragment_person_recommend_work_item_detail, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initView(view);
        mContentEditText = CommentUtil.getCommentEdit(view);
        CommentUtil.hideSoft(getContext(), view);
        CommentUtil.initViewPager(this, view);
        CommentUtil.addViewPageEvent(getContext(), view);
        return view;
    }

    private void initView(View view){
        TextView titleTv = (TextView) view.findViewById(R.id.recommend_work_item_detail_title);
        titleTv.setText(mRecommendWork.getTitle());
        TextView detailTv = (TextView) view.findViewById(R.id.recommend_work_item_detail_detail);
        detailTv.setText(mRecommendWork.getContent());
        TextView emailTV = (TextView) view.findViewById(R.id.email_tv);
        emailTV.setText(mRecommendWork.getEmail());
    }

    public void inputEmotion(String text) {
        int selectionCursor = mContentEditText.getSelectionStart();
        mContentEditText.getText().insert(selectionCursor, text);
        mContentEditText.invalidate();
    }







}
