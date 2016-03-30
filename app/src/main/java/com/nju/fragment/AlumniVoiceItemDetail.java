package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.nju.db.db.service.AlumniVoiceCollectDbService;
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
import com.nju.util.ShareUtil;
import com.nju.util.SoftInput;
import com.nju.util.SortUtil;
import com.nju.util.StringBase64;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;


public class AlumniVoiceItemDetail extends BaseFragment {

    public static final String TAG = AlumniVoiceItemDetail.class.getSimpleName();
    private static final String PARAM_VOICE= "voiceKey";
    private AlumniVoice mVoice;
    private EditText mContentEditText ;
    private TextView mCommentNumberTV,mPraiseNumberTV,mPraiseTV;
    private ArrayList<ContentComment> mContentComments;
    private ArrayList<RespPraise> mPraiseAuthors = new ArrayList<>();
    private CommentAdapter mCommentAdapter;
    private View mMainView;
    private int commentType = 0;
    private boolean isPraise;
    private PostRequestJson mRequestSaveJson,mRequestQueryJson,mRequestSavePraiseJson
            ,mRequestQueryPraiseJson;
    private ResponseCallback saveCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(AlumniVoiceItemDetail.this)){
                Log.e(TAG,error.getMessage());
            }
        }
        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(AlumniVoiceItemDetail.this)){
                Log.i(TAG, responseBody);
                ToastUtil.showShortText(getContext(), getString(R.string.comment_ok));
                mRequestQueryJson = AlumniVoiceService.queryComment(AlumniVoiceItemDetail.this,mVoice.getId(),queryCommentCallback);
            }
        }
    };

    private ResponseCallback savePraiseCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(AlumniVoiceItemDetail.this)){
                Log.e(TAG,error.getMessage());
            }
        }
        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(AlumniVoiceItemDetail.this)){
                Log.i(TAG, responseBody);
                ToastUtil.ShowText(getContext(), getString(R.string.praise_ok));
                mRequestQueryPraiseJson = AlumniVoiceService.queryPraise(AlumniVoiceItemDetail.this,mVoice.getId(),queryPraiseCallback);
            }
        }
    };

    private ResponseCallback queryPraiseCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(AlumniVoiceItemDetail.this)){
                Log.e(TAG,error.getMessage());
            }
        }
        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(AlumniVoiceItemDetail.this)){
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
                    mPraiseNumberTV.setText(mPraiseAuthors.size()+"");
                    changePraiseColor();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private ResponseCallback queryCommentCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(AlumniVoiceItemDetail.this)){
                Log.e(TAG, error.getMessage());
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(AlumniVoiceItemDetail.this)){
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
                            mContentComments = SortUtil.softByDate(mContentComments);
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
        mMainView = view;
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initView(view);
        initToolBar(view);
        mContentEditText = CommentUtil.getCommentEdit(this,view);
        return view;
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
                    mRequestSaveJson = AlumniVoiceService.saveComment(AlumniVoiceItemDetail.this, view, mVoice.getId(), saveCallback);
                }else {
                    mRequestSaveJson = AlumniVoiceService.saveCommentForOther(AlumniVoiceItemDetail.this, view, commentType, saveCallback);
                    commentType = 0;
                }
                 CommentUtil.closeSoftKey(getContext(), view);
            }
        });
        mRequestQueryJson = AlumniVoiceService.queryComment(this,mVoice.getId(),queryCommentCallback);
        mRequestQueryPraiseJson = AlumniVoiceService.queryPraise(this,mVoice.getId(),queryPraiseCallback);
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
        mPraiseTV= (TextView) view.findViewById(R.id.praise);

        mPraiseTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPraise){
                    mRequestSavePraiseJson = AlumniVoiceService.praise(AlumniVoiceItemDetail.this,mVoice.getId(),savePraiseCallback);
                    isPraise = false;
                }else {
                    ToastUtil.showShortText(getContext(),getString(R.string.you_have_praised));
                }
            }
        });

        final TextView collectTV = (TextView) view.findViewById(R.id.collect);
        collectTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlumniVoiceCollectDbService(getContext()).save(mVoice);
                ToastUtil.ShowText(getContext(), getString(R.string.collect_ok));
                collectTV.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark));
            }
        });


        TextView shareTV = (TextView) view.findViewById(R.id.share);
        shareTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil.share(getContext());
            }
        });
    }

    private void changePraiseColor(){
        int authorId = getHostActivity().getSharedPreferences().getInt(getString(R.string.authorId),0);
        for (RespPraise respPraise:mPraiseAuthors){
            Log.i(TAG,respPraise.getPraiseAuthor().getAuthorId()+"");
            if (respPraise.getPraiseAuthor().getAuthorId() == authorId){
                isPraise = false;
                mPraiseTV.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark));
            }
        }
    }

    @Subscribe
    public void onNetStateMessageState(NetworkInfoEvent event){
        if (event.isConnected()){
            mRequestQueryJson = AlumniVoiceService.queryComment(AlumniVoiceItemDetail.this, mVoice.getId(), queryCommentCallback);
            mRequestQueryPraiseJson = AlumniVoiceService.queryPraise(AlumniVoiceItemDetail.this, mVoice.getId(), queryPraiseCallback);
        }
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
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (mRequestSaveJson != null)
            CloseRequestUtil.close(mRequestSaveJson);
        if (mRequestQueryJson != null)
            CloseRequestUtil.close(mRequestQueryJson);
        if (mRequestSavePraiseJson != null)
            CloseRequestUtil.close(mRequestSavePraiseJson);
        if (mRequestQueryPraiseJson != null)
            CloseRequestUtil.close(mRequestQueryPraiseJson);
    }
}
