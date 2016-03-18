package com.nju.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nju.activity.R;
import com.nju.util.Divice;
import com.nju.util.InputEmotionUtil;

public class PublishVoiceFragment extends BaseFragment {
    public static final String TAG = PublishVoiceFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private String mTitle;
    private EditText mTitleET;
    private EditText mContentET;
    public static PublishVoiceFragment newInstance(String title) {
        PublishVoiceFragment fragment = new PublishVoiceFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE,title);
        fragment.setArguments(args);
        return fragment;
    }

    public PublishVoiceFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_publish_voice, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()),view.getPaddingRight(),view.getPaddingBottom());
        InputEmotionUtil.initView(this, view, TAG);
        InputEmotionUtil.addViewPageEvent(getContext(), view);
        mContentET = (EditText) view.findViewById(R.id.content_editText);
        mTitleET = (EditText) view.findViewById(R.id.title);
        return view;
    }

    public void inputEmotion(String text) {
        String label = InputEmotionUtil.getLabel();
        if (label != null){
            if (label.equals(getString(R.string.content))){
                int selectionCursor = mContentET.getSelectionStart();
                mContentET.getText().insert(selectionCursor, text);
                mContentET.invalidate();
            }else if (label.equals(getString(R.string.title))){
                int selectionCursor = mTitleET.getSelectionStart();
                mTitleET.getText().insert(selectionCursor, text);
                mTitleET.invalidate();
            }
        }
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
        getHostActivity().display(0);
        getHostActivity().getMenuBn().setText(getString(R.string.publish));
    }

}
