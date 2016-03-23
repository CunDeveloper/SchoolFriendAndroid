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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nju.activity.R;
import com.nju.model.ImageWrapper;
import com.nju.util.Divice;
import com.nju.util.InputEmotionUtil;
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
    private EditText mEmailET;
    private String mType;
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
            if (getArguments().getParcelableArrayList(PARAM_UPLOAD_IMAGES) != null){
                mUploadImgPaths = getArguments().getParcelableArrayList(PARAM_UPLOAD_IMAGES);
            }
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
        initView(view);
        initCheckBox(view);
        return view;
    }

    private void initView(View view){
        final int[] contentWordCount = {0};
        final int[] titleWordCount = {0};
        final int[] emailWordCount = {0};
        mContentET = (EditText) view.findViewById(R.id.content_editText);
        mTitleET = (EditText) view.findViewById(R.id.title);
        mContentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                contentWordCount[0] = count;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTitleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                titleWordCount[0] = count;
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEmailET = (EditText) view.findViewById(R.id.email_tv);
        final Button sendBn = getHostActivity().getMenuBn();
        mEmailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (contentWordCount[0]>=1&&titleWordCount[0]>=1 &&emailWordCount[0]>=1){
                    sendBn.setAlpha(1);sendBn.setEnabled(true);
                }else {
                    sendBn.setAlpha(0.7f);sendBn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.emotion_layout);
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout2);
        mEmailET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                }

            }
        });
        mEmailET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            }
        });

    }

    private void initCheckBox(View view){
        final CheckBox shiXiCheckBox = (CheckBox) view.findViewById(R.id.shixi_checkBox);
        final CheckBox xiaoZhaoCheckBox = (CheckBox) view.findViewById(R.id.xiaozhao_checkBox);
        final CheckBox sheZhaoCheckBox = (CheckBox) view.findViewById(R.id.shezhao_checkBox);
        shiXiCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                computeType(shiXiCheckBox,xiaoZhaoCheckBox,sheZhaoCheckBox);
                ToastUtil.showShortText(getContext(),mType);
            }
        });
        xiaoZhaoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                computeType(shiXiCheckBox,xiaoZhaoCheckBox,sheZhaoCheckBox);
                ToastUtil.showShortText(getContext(), mType);
            }
        });
        sheZhaoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                computeType(shiXiCheckBox,xiaoZhaoCheckBox,sheZhaoCheckBox);
                ToastUtil.showShortText(getContext(), mType);
            }
        });

    }

    private void computeType(CheckBox shiXiCheckBox,CheckBox xiaoZhaoCheckBox, CheckBox sheZhaoCheckBox){
        if (shiXiCheckBox.isChecked() && xiaoZhaoCheckBox.isChecked() && sheZhaoCheckBox.isChecked()){
            mType = 6+"";
        } else if (xiaoZhaoCheckBox.isChecked()&&sheZhaoCheckBox.isChecked()){
            mType = 5+"";
        } else if (shiXiCheckBox.isChecked()&&xiaoZhaoCheckBox.isChecked()){
            mType = 4+"";
        } else if (shiXiCheckBox.isChecked()&&sheZhaoCheckBox.isChecked()){
            mType = 3+"";
        } else if (sheZhaoCheckBox.isChecked()){
            mType = 2+"";
        } else if (xiaoZhaoCheckBox.isChecked()){
            mType = 1+"";
        } else  if (shiXiCheckBox.isChecked()){
            mType =0+"";
        }
    }

    public  void setImages(ArrayList<ImageWrapper> images){
        mUploadImgPaths = images;
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
