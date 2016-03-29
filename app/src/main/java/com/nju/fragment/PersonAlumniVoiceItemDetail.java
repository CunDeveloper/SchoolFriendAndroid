package com.nju.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nju.activity.NetworkInfoEvent;
import com.nju.activity.R;
import com.nju.adatper.CommentAdapter;
import com.nju.adatper.PraiseHeadAdapter;
import com.nju.event.MessageEventId;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniVoice;
import com.nju.model.ContentComment;
import com.nju.model.RespPraise;
import com.nju.service.AlumniVoiceService;
import com.nju.service.RecommendWorkService;
import com.nju.test.TestData;
import com.nju.util.CloseRequestUtil;
import com.nju.util.CommentUtil;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.SchoolFriendGson;
import com.nju.util.SoftInput;
import com.nju.util.StringBase64;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

public class PersonAlumniVoiceItemDetail extends BaseFragment {
    public static final String TAG = PersonAlumniVoiceItemDetail.class.getSimpleName();
    private static final String PARAM_VOICE= "voiceKey";
    private static final String PARAM_TITLE = "title";
    private AlumniVoice mVoice;
    private String mTitle;
    private EditText mContentEditText ;
    private ArrayList<ContentComment> mContentComments;
    private ArrayList<RespPraise> mPraiseAuthors = new ArrayList<>();
    private CommentAdapter mCommentAdapter;
    private View mMainView;
    private int commentType = 0;
    private TextView mCommentNumberTV,mPraiseNumberTV;
    private PostRequestJson mRequestSaveJson,mRequestQueryJson,mRequestQueryPraiseJson;
    private ResponseCallback saveCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(PersonAlumniVoiceItemDetail.this)){
                Log.e(TAG,error.getMessage());
            }
        }
        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(PersonAlumniVoiceItemDetail.this)){
                Log.i(TAG, responseBody);
                ToastUtil.showShortText(getContext(), getString(R.string.comment_ok));
                mRequestQueryJson = AlumniVoiceService.queryComment(PersonAlumniVoiceItemDetail.this, mVoice.getId(), queryCommentCallback);
            }
        }
    };

    private ResponseCallback queryPraiseCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(PersonAlumniVoiceItemDetail.this)){
                Log.e(TAG,error.getMessage());
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(PersonAlumniVoiceItemDetail.this)){
                Log.i(TAG, responseBody);
                try {
                    Object object = new ParseResponse().getInfo(responseBody,RespPraise.class);
                    if (object != null) {
                        ArrayList authors = (ArrayList) object;
                        if (authors.size() > 0) {
                            for (Object obj : authors) {
                                RespPraise authorInfo = (RespPraise) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(authorInfo));
                                mPraiseAuthors.add(authorInfo);
                            }
                        }
                    }
                    mPraiseNumberTV.setText(mPraiseAuthors.size() + "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private ResponseCallback queryCommentCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(PersonAlumniVoiceItemDetail.this)){
                Log.e(TAG, error.getMessage());
            }
        }
        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(PersonAlumniVoiceItemDetail.this)){
                Log.i(TAG, responseBody);
                try {
                    Object object = new ParseResponse().getInfo(responseBody,ContentComment.class);
                    if (object != null) {
                        ArrayList comments = (ArrayList) object;
                        if (comments.size() > 0) {
                            for (Object obj : comments) {
                                ContentComment contentComment = (ContentComment) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(contentComment));
                                mContentComments.add(contentComment);
                            }
                        }
                    }
                    mCommentAdapter.notifyDataSetChanged();
                    mCommentNumberTV.setText(mContentComments.size()+"");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };


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
        getHostActivity().display(6);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person_alumni_voice_item_detail, container, false);
        mMainView = view;
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initView(view);
        initToolBar(view);
        mContentEditText = CommentUtil.getCommentEdit(this,view);
        return view;
    }

    @Subscribe
    public void onMessageEvent(MessageEventId event){
        commentType = event.getId();
        final ScrollView scrollView = (ScrollView) mMainView.findViewById(R.id.mScrollView);
        final LinearLayout linearLayout = (LinearLayout) mMainView.findViewById(R.id.new_comment_layout);
        CommentUtil.getHideLayout(mMainView).setVisibility(View.VISIBLE);
        SoftInput.open(getContext());
        scrollView.scrollTo(0, linearLayout.getBottom());
    }



    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void inputEmotion(String text) {
        int selectionCursor = mContentEditText.getSelectionStart();
        mContentEditText.getText().insert(selectionCursor, text);
        mContentEditText.invalidate();
    }

    private void initView(final View view){
        mCommentNumberTV = (TextView) view.findViewById(R.id.comment_number_tv);
        mPraiseNumberTV = (TextView) view.findViewById(R.id.praise_number_tv);
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
        dateTV.setText(DateUtil.getRelativeTimeSpanString(mVoice.getDate()));
        GridView gridView = (GridView) view.findViewById(R.id.new_gridview);
        gridView.setAdapter(new PraiseHeadAdapter(getContext()));
        ListView newListView = (ListView) view.findViewById(R.id.new_comment_listview);
        mContentComments = TestData.getComments();
        mCommentAdapter = new CommentAdapter(getContext(),mContentComments);
        newListView.setAdapter(mCommentAdapter);
        Button sendBn = (Button) view.findViewById(R.id.activity_school_friend_send_button);
        sendBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentType == 0){
                    mRequestSaveJson = AlumniVoiceService.saveComment(PersonAlumniVoiceItemDetail.this, view, mVoice.getId(), saveCallback);
                }else {
                    mRequestSaveJson = AlumniVoiceService.saveCommentForOther(PersonAlumniVoiceItemDetail.this, view, commentType, saveCallback);
                    commentType = 0;
                }
                CommentUtil.closeSoftKey(getContext(), view);
            }
        });
        mRequestQueryJson = AlumniVoiceService.queryComment(this,mVoice.getId(),queryCommentCallback);
        mRequestQueryPraiseJson = AlumniVoiceService.queryPraise(this,mVoice.getId(),queryPraiseCallback);
    }

    @Subscribe
    public void onNetStateMessageState(NetworkInfoEvent event){
        if (event.isConnected()){
            mRequestQueryJson = AlumniVoiceService.queryComment(this,mVoice.getId(),queryCommentCallback);
            mRequestQueryPraiseJson = AlumniVoiceService.queryPraise(this, mVoice.getId(), queryPraiseCallback);
        }
    }

    private void initToolBar(final View view){
    }

    @Override
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (mRequestSaveJson != null)
            CloseRequestUtil.close(mRequestSaveJson);
        if (mRequestQueryJson != null)
            CloseRequestUtil.close(mRequestQueryJson);
        if (mRequestQueryPraiseJson != null)
            CloseRequestUtil.close(mRequestQueryPraiseJson);
    }
}
