package com.nju.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.nju.activity.R;
import com.nju.model.ImageWrapper;
import com.nju.util.Divice;
import com.nju.util.InputEmotionUtil;

import java.util.ArrayList;

public class AskPublishFragment extends BaseFragment {

    public static final String TAG = AskPublishFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static final String PARAM_UPLOAD_IMAGES = "paramUploadImage";
    private ArrayList<ImageWrapper> mUploadImgPaths;
    private static String mTitle;
    private EditText mContentET;
    private EditText mTitleET;
    public static AskPublishFragment newInstance(String title,ArrayList<ImageWrapper> uploadImgPaths) {
        AskPublishFragment fragment = new AskPublishFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE,title);
        args.putParcelableArrayList(PARAM_UPLOAD_IMAGES, uploadImgPaths);
        fragment.setArguments(args);
        return fragment;
    }

    public AskPublishFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(PARAM_TITLE);
            if (getArguments().getParcelableArrayList(PARAM_UPLOAD_IMAGES) != null){
                mUploadImgPaths = getArguments().getParcelableArrayList(PARAM_UPLOAD_IMAGES);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ask_publish, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        InputEmotionUtil.initView(this, view, TAG);
        InputEmotionUtil.addViewPageEvent(getContext(), view);
        if (mUploadImgPaths!=null&&mUploadImgPaths.size()>0){
            view.findViewById(R.id.add_pic).setVisibility(View.GONE);
            InputEmotionUtil.setUpGridView(this, view, mUploadImgPaths);
        }
        initView(view);
        return view;
    }

    private void initView(View view){
        final int[] contentWordCount = {0};
        final int[] titleWordCount = {0};
        mContentET = (EditText) view.findViewById(R.id.content_editText);
        mTitleET = (EditText) view.findViewById(R.id.title);
        mContentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contentWordCount[0] = count;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTitleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                titleWordCount[0] = count;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Button sendBn = getHostActivity().getMenuBn();
        if (contentWordCount[0]>=1&&titleWordCount[0]>=1){
            sendBn.setAlpha(1);sendBn.setEnabled(true);
        }else {
            sendBn.setAlpha(0.7f);sendBn.setEnabled(false);
        }
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

    public  void setImages(ArrayList<ImageWrapper> images){
        mUploadImgPaths = images;
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
        Button sendBn = getHostActivity().getMenuBn();
        sendBn.setText(getString(R.string.publish));
        sendBn.setAlpha(0.7f);
        sendBn.setEnabled(false);
        sendBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
     }

}
