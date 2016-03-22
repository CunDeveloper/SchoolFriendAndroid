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
import com.nju.util.Divice;
import com.nju.util.ShareUtil;
import com.nju.util.SoftInput;
import com.nju.util.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class AlumniVoiceItemDetail extends BaseFragment {

    public static final String TAG = AlumniVoiceItemDetail.class.getSimpleName();
    private static final String PARAM_VOICE= "voiceKey";
    private AlumniVoice mVoice;
    private EditText mContentEditText ;


    public static AlumniVoiceItemDetail newInstance(AlumniVoice voice) {
        AlumniVoiceItemDetail fragment = new AlumniVoiceItemDetail();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_VOICE,voice);
        fragment.setArguments(args);
        return fragment;
    }

    public AlumniVoiceItemDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVoice = getArguments().getParcelable(PARAM_VOICE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getHostActivity().display(6);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alumni_voice_item_detail, container, false);
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
        titleTV.setText(mVoice.getTitle());
        TextView contentTV = (TextView) view.findViewById(R.id.alumni_vo_desc);
        contentTV.setText(mVoice.getContent());
        TextView dateTV = (TextView) view.findViewById(R.id.alumni_vo_date);
        dateTV.setText(mVoice.getDate());
    }



    private void initToolBar(final View view){
        TextView commentTV = (TextView) view.findViewById(R.id.comment);


        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        commentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.scrollTo(0,scrollView.getBottom());
                SoftInput.open(getContext());
                CommentUtil.getHideLayout(view).setVisibility(View.VISIBLE);
            }
        });

        final TextView praiseTV = (TextView) view.findViewById(R.id.praise);
        praiseTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.ShowText(getContext(),getString(R.string.praise_ok));
                praiseTV.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark));
            }
        });

        final TextView collectTV = (TextView) view.findViewById(R.id.collect);
        collectTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.ShowText(getContext(),getString(R.string.collect_ok));
                collectTV.setTextColor(ContextCompat.getColor(getContext(),android.R.color.holo_orange_dark));
            }
        });


        TextView shareTV = (TextView) view.findViewById(R.id.share);
        shareTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShareUtil.share(getContext());
                File imagePath = new File(getContext().getFilesDir(),"images");
                if (!imagePath.exists()){
                    imagePath.mkdirs();
                    File newFile = new File(imagePath,"test.jpg");
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.cheese_1);
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(newFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                File[] files = imagePath.listFiles();

                for (File file:files){

                    Log.e(TAG,file.getName());
                }
            }
        });
    }

}
