package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.adatper.EmotionPageAdapter;
import com.nju.model.ImageWrapper;
import com.nju.util.Divice;
import com.nju.util.InputEmotionUtil;
import com.nju.util.SoftInput;
import com.nju.util.ToastUtil;

import java.util.ArrayList;

public class RecommendPublishFragment extends BaseFragment {

    public static final String TAG = RecommendPublishFragment.class.getSimpleName();
    private static final String PARAM_TITLE = "paramTitle";
    private static final String PARAM_UPLOAD_IMAGES = "paramUploadImage";
    private ArrayList<ImageWrapper> mUploadImgPaths;
    private static String mTitle;
    private EditText mTitleET;
    private EditText mContentET;
    public static RecommendPublishFragment newInstance(String title,ArrayList<ImageWrapper> uploadImgPaths) {
        RecommendPublishFragment fragment = new RecommendPublishFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE, title);
        args.putParcelableArrayList(PARAM_UPLOAD_IMAGES, uploadImgPaths);
        fragment.setArguments(args);
        return fragment;
    }


    public RecommendPublishFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(PARAM_TITLE);
            mUploadImgPaths = getArguments().getParcelableArrayList(PARAM_UPLOAD_IMAGES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommend_publish, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        InputEmotionUtil.initView(this, view, TAG);
        InputEmotionUtil.addViewPageEvent(getContext(), view);
        if (mUploadImgPaths!=null&&mUploadImgPaths.size()>0){
            view.findViewById(R.id.add_pic).setVisibility(View.GONE);
            InputEmotionUtil.setUpGridView(this, view, mUploadImgPaths);
        }
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
