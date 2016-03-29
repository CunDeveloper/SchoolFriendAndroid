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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nju.activity.NetworkInfoEvent;
import com.nju.activity.R;
import com.nju.adatper.CommentAdapter;
import com.nju.event.MessageEventId;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniQuestion;
import com.nju.model.ContentComment;
import com.nju.service.AlumniVoiceService;
import com.nju.service.MajorAskService;
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
import com.nju.util.StringBase64;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;


public class MajorAskDetailFragment extends BaseFragment {
    public static final String TAG = MajorAskDetailFragment.class.getSimpleName();
    private static final String PARAM_KEY = "questionKey";
    private AlumniQuestion mAlumniQuestion;
    private ArrayList<ContentComment> mContentComments;
    private CommentAdapter mCommentAdapter;
    private EditText mContentEditText ;
    private PostRequestJson mRequestSaveJson,mRequestQueryJson;
    private View mMainView;
    private int commentType = 0;

    private ResponseCallback saveCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(MajorAskDetailFragment.this)){
                Log.e(TAG, error.getMessage());
            }
        }
        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(MajorAskDetailFragment.this)){
                Log.i(TAG, responseBody);
                ToastUtil.showShortText(getContext(), getString(R.string.comment_ok));
                mRequestQueryJson = MajorAskService.queryComment(MajorAskDetailFragment.this, mAlumniQuestion.getId(), queryCommentCallback);
            }
        }
    };


    private ResponseCallback queryCommentCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(MajorAskDetailFragment.this)){
                Log.e(TAG, error.getMessage());
            }
        }
        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(MajorAskDetailFragment.this)){
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public static MajorAskDetailFragment newInstance(AlumniQuestion alumniQuestion) {
        MajorAskDetailFragment fragment = new MajorAskDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_KEY,alumniQuestion);
        fragment.setArguments(args);
        return fragment;
    }

    public MajorAskDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAlumniQuestion = getArguments().getParcelable(PARAM_KEY);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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
        View view = inflater.inflate(R.layout.fragment_major_ask_detail, container, false);
        mMainView = view;
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initView(view);
        initToolBar(view);
        mContentEditText = CommentUtil.getCommentEdit(this,view);
        return view;
    }

    public void inputEmotion(String text) {
        int selectionCursor = mContentEditText.getSelectionStart();
        mContentEditText.getText().insert(selectionCursor, text);
        mContentEditText.invalidate();
    }

    @Subscribe
    public void onNetStateMessageState(NetworkInfoEvent event){
        if (event.isConnected()){
            mRequestQueryJson = MajorAskService.queryComment(MajorAskDetailFragment.this, mAlumniQuestion.getId(), queryCommentCallback);        }
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

    private void initView(final View view){
        TextView problemTV = (TextView) view.findViewById(R.id.problem_tv);
        try{
            problemTV.setText(StringBase64.decode(mAlumniQuestion.getProblem()));
        }catch (IllegalArgumentException e){
            problemTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        TextView descTV = (TextView) view.findViewById(R.id.description_tv);

        try{
            descTV.setText(StringBase64.decode(mAlumniQuestion.getDescription()));
        }catch (IllegalArgumentException e){
            descTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        TextView nameTV = (TextView) view.findViewById(R.id.name_tv);
        nameTV.setText(mAlumniQuestion.getAuthor().getAuthorName());
        TextView dateTV = (TextView) view.findViewById(R.id.date_tv);
        dateTV.setText(DateUtil.getRelativeTimeSpanString(mAlumniQuestion.getDate()));
        final ListView commentListView = (ListView) view.findViewById(R.id.new_comment_listview);
        mContentComments = TestData.getComments();
        mCommentAdapter = new CommentAdapter(getContext(), mContentComments);
        commentListView.setAdapter(mCommentAdapter);
        Button sendBn = (Button) view.findViewById(R.id.activity_school_friend_send_button);
        sendBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentType == 0){
                    mRequestSaveJson = MajorAskService.saveComment(MajorAskDetailFragment.this, view, mAlumniQuestion.getId(), saveCallback);
                }else {
                    mRequestSaveJson = MajorAskService.saveCommentForOther(MajorAskDetailFragment.this, view, commentType, saveCallback);
                    commentType = 0;
                }
                CommentUtil.closeSoftKey(getContext(), view);
            }
        });
        mRequestQueryJson = MajorAskService.queryComment(MajorAskDetailFragment.this, mAlumniQuestion.getId(), queryCommentCallback);
    }

    private void initToolBar(final View view){
        TextView commentTV = (TextView) view.findViewById(R.id.comment);
        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.myScrollView);
        final ListView commentListView = (ListView) view.findViewById(R.id.new_comment_listview);
        commentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.scrollTo(0,commentListView.getBottom());
                SoftInput.open(getContext());
                CommentUtil.getHideLayout(view).setVisibility(View.VISIBLE);
            }
        });

        final TextView collectTV = (TextView) view.findViewById(R.id.collect);
        collectTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    @Override
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (mRequestSaveJson != null)
            CloseRequestUtil.close(mRequestSaveJson);
        if (mRequestQueryJson != null)
            CloseRequestUtil.close(mRequestQueryJson);
    }
}
