package com.nju.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.model.AlumniVoice;
import com.nju.util.CommentUtil;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.ShareUtil;
import com.nju.util.SoftInput;
import com.nju.util.StringBase64;
import com.nju.util.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class PersonAlumniVoiceItemDetail extends BaseFragment {
    public static final String TAG = AlumniVoiceItemDetail.class.getSimpleName();
    private static final String PARAM_VOICE= "voiceKey";
    private static final String PARAM_TITLE = "title";
    private AlumniVoice mVoice;
    private String mTitle;
    private EditText mContentEditText ;


    public static PersonAlumniVoiceItemDetail newInstance(AlumniVoice voice,String title) {
        PersonAlumniVoiceItemDetail fragment = new PersonAlumniVoiceItemDetail();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_VOICE,voice);
        args.putString(PARAM_TITLE,title);
        fragment.setArguments(args);
        return fragment;
    }

    public PersonAlumniVoiceItemDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVoice = getArguments().getParcelable(PARAM_VOICE);
            mTitle = getArguments().getString(PARAM_TITLE);
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
        getHostActivity().display(5);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person_alumni_voice_item_detail, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initView(view);
        initToolBar(view);
        CommentUtil.hideSoft(getContext(), view);
        CommentUtil.initViewPager(this, view);
        CommentUtil.addViewPageEvent(getContext(),view);
        mContentEditText = CommentUtil.getCommentEdit(view);
        return view;
    }

    public void inputEmotion(String text) {
        int selectionCursor = mContentEditText.getSelectionStart();
        mContentEditText.getText().insert(selectionCursor, text);
        mContentEditText.invalidate();
    }

    private void initView(View view){
        TextView nameTV = (TextView) view.findViewById(R.id.alumni_vo_name);
        nameTV.setText(mVoice.getAuthorInfo().getAuthorName());
        TextView labelTV = (TextView) view.findViewById(R.id.alumni_vo_label);
        labelTV.setText(mVoice.getAuthorInfo().getLabel());
        TextView titleTV = (TextView) view.findViewById(R.id.alumni_vo_title);
        try{
            titleTV.setText(StringBase64.decode(mVoice.getTitle()));
        }catch (IllegalArgumentException e){
            titleTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        TextView contentTV = (TextView) view.findViewById(R.id.alumni_vo_desc);
        try{
            contentTV.setText(StringBase64.decode(mVoice.getContent()));
        }catch (IllegalArgumentException e){
            contentTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        TextView dateTV = (TextView) view.findViewById(R.id.alumni_vo_date);
        dateTV.setText(mVoice.getDate());
    }



    private void initToolBar(final View view){

        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollView);
//        commentTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scrollView.scrollTo(0,scrollView.getBottom());
//                SoftInput.open(getContext());
//                CommentUtil.getHideLayout(view).setVisibility(View.VISIBLE);
//            }
//        });
    }
}
