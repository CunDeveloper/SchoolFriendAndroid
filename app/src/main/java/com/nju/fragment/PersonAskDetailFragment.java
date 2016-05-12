package com.nju.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.adatper.BigImgAdaper;
import com.nju.adatper.CommentAdapter;
import com.nju.event.MessageDeleteEvent;
import com.nju.event.MessageEventId;
import com.nju.event.NetworkInfoEvent;
import com.nju.http.ResponseCallback;
import com.nju.http.callback.DeleteCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniQuestion;
import com.nju.model.ContentComment;
import com.nju.service.AlumniTalkService;
import com.nju.service.MajorAskService;
import com.nju.util.CloseRequestUtil;
import com.nju.util.CommentUtil;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.PathConstant;
import com.nju.util.SchoolFriendGson;
import com.nju.util.SoftInput;
import com.nju.util.SortUtil;
import com.nju.util.StringBase64;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IllegalFormatCodePointException;


public class PersonAskDetailFragment extends BaseFragment {
    public static final String TAG = PersonAskDetailFragment.class.getSimpleName();
    private static final String PARAM_KEY = "questionKey";
    private AlumniQuestion mAlumniQuestion;
    private ArrayList<ContentComment> mContentComments;
    private CommentAdapter mCommentAdapter;
    private EditText mContentEditText;
    private PostRequestJson mRequestSaveJson, mRequestQueryJson
            ,mDelectAskRequestJson,mDeleteCommentJson;
    private View mMainView;
    private int commentType = 0;
    private int mDeleteIndex = 0;
    private ResponseCallback queryCommentCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(PersonAskDetailFragment.this)) {
                Log.e(TAG, error.getMessage());
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(PersonAskDetailFragment.this)) {
                Log.i(TAG, responseBody);
                try {
                    Object object = new ParseResponse().getInfo(responseBody, ContentComment.class);
                    if (object != null) {
                        ArrayList comments = (ArrayList) object;
                        if (comments.size() > 0) {
                            mContentComments.clear();
                            for (Object obj : comments) {
                                ContentComment contentComment = (ContentComment) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(contentComment));
                                mContentComments.add(contentComment);
                            }
                        }
                    }
                    Collections.sort(mContentComments,Collections.reverseOrder());
                    mCommentAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private ResponseCallback saveCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(PersonAskDetailFragment.this)) {
                Log.e(TAG, error.getMessage());
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(PersonAskDetailFragment.this)) {
                Log.i(TAG, responseBody);
                ToastUtil.showShortText(getContext(), getString(R.string.comment_ok));
                mRequestQueryJson = MajorAskService.queryComment(PersonAskDetailFragment.this, mAlumniQuestion.getId(), queryCommentCallback);
            }
        }
    };

    public PersonAskDetailFragment() {
        // Required empty public constructor
    }

    public static PersonAskDetailFragment newInstance(AlumniQuestion alumniQuestion) {
        PersonAskDetailFragment fragment = new PersonAskDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_KEY, alumniQuestion);
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe
    public void onNetStateMessageState(NetworkInfoEvent event) {
        if (event.isConnected()) {
            mRequestQueryJson = MajorAskService.queryComment(PersonAskDetailFragment.this, mAlumniQuestion.getId(), queryCommentCallback);
        }
    }

    @Subscribe
    public void onMessageEvent(MessageEventId event) {
        commentType = event.getId();
        final ScrollView scrollView = (ScrollView) mMainView.findViewById(R.id.mScrollView);
        final LinearLayout linearLayout = (LinearLayout) mMainView.findViewById(R.id.new_comment_layout);
        CommentUtil.getHideLayout(mMainView).setVisibility(View.VISIBLE);
        SoftInput.open(getContext());
        scrollView.scrollTo(0, linearLayout.getBottom());
    }

    @Subscribe
    public void onMessageDeleteComment(MessageDeleteEvent deleteEvent) {
        mDeleteCommentJson = MajorAskService.deleteComment(this, mDeleteIndex, new ResponseCallback() {
            @Override
            public void onFail(Exception error) {
                Log.e(TAG,error.getMessage());
            }

            @Override
            public void onSuccess(String responseBody) {
                Log.i(TAG,responseBody);
                if (FragmentUtil.isAttachedToActivity(PersonAskDetailFragment.this)) {
                    Log.i(TAG, responseBody);
                    ParseResponse parseResponse = new ParseResponse();
                    try {
                        String str = parseResponse.getInfo(responseBody);
                        if (str != null && str.equals(Constant.OK_MSG)) {
                            mRequestQueryJson = MajorAskService.queryComment(PersonAskDetailFragment.this, mAlumniQuestion.getId(), queryCommentCallback);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mDeleteIndex = 0;
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
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.own_major_ask);
        }
        getHostActivity().display(6);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person_ask_detail, container, false);
        mMainView = view;
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initView(view);
        initToolBar(view);
        mContentEditText = CommentUtil.getCommentEdit(this, view);
        return view;
    }

    private void initToolBar(final View view) {
        RelativeLayout deleteLayout = (RelativeLayout) view.findViewById(R.id.delete_reLayout);
        RelativeLayout replayLayout = (RelativeLayout) view.findViewById(R.id.replay_reLayout);
        RelativeLayout closeLayout = (RelativeLayout) view.findViewById(R.id.close_reLayout);
        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.mScrollView);
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.new_comment_layout);
        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDelectAskRequestJson = MajorAskService.deleteQuestion(PersonAskDetailFragment.this,mAlumniQuestion.getId(),
                        new DeleteCallback(TAG,PersonAskDetailFragment.this));
            }
        });
        replayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.getHideLayout(view).setVisibility(View.VISIBLE);
                SoftInput.open(getContext());
                scrollView.scrollTo(0, linearLayout.getBottom());
            }
        });

        closeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initView(final View view) {
        TextView problemTV = (TextView) view.findViewById(R.id.problem_tv);
        try {
            problemTV.setText(StringBase64.decode(mAlumniQuestion.getProblem()));
        } catch (IllegalArgumentException e) {
            problemTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        TextView descTV = (TextView) view.findViewById(R.id.description_tv);

        try {
            descTV.setText(StringBase64.decode(mAlumniQuestion.getDescription()));
        } catch (IllegalArgumentException e) {
            descTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        TextView nameTV = (TextView) view.findViewById(R.id.name_tv);
        nameTV.setText(mAlumniQuestion.getAuthor().getAuthorName());
        TextView dateTV = (TextView) view.findViewById(R.id.date_tv);
        dateTV.setText(DateUtil.getRelativeTimeSpanString(mAlumniQuestion.getDate()));
        final ListView commentListView = (ListView) view.findViewById(R.id.new_comment_listview);
        mContentComments =  new ArrayList<>();
        mCommentAdapter = new CommentAdapter(getContext(), mContentComments);
        commentListView.setAdapter(mCommentAdapter);
        commentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ContentComment comment = mContentComments.get(position);
                mDeleteIndex = comment.getId();
                if (comment.getCommentAuthor().getAuthorId() == getHostActivity().userId()) {
                    String[] strings = {getString(R.string.delete)};
                    SchoolFriendDialog.listItemDialog(getContext(), strings).show();
                }
                return true;
            }
        });

        Button sendBn = (Button) view.findViewById(R.id.activity_school_friend_send_button);
        sendBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentType == 0) {
                    mRequestSaveJson = MajorAskService.saveComment(PersonAskDetailFragment.this, view, mAlumniQuestion.getId(), saveCallback);
                } else {
                    mRequestSaveJson = MajorAskService.saveCommentForOther(PersonAskDetailFragment.this, view, commentType, saveCallback);
                    commentType = 0;
                }
                CommentUtil.closeSoftKey(getContext(), view);
            }
        });
        mRequestQueryJson = MajorAskService.queryComment(PersonAskDetailFragment.this, mAlumniQuestion.getId(), queryCommentCallback);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        if (mAlumniQuestion.getImgPaths() != null) {
            Log.e(TAG, mAlumniQuestion.getImgPaths());
            listView.setAdapter(new BigImgAdaper(getContext(), PathConstant.ALUMNI_QUESTION_IMG_PATH, mAlumniQuestion.getImgPaths().split(",")));
        }
    }

    public void inputEmotion(String text) {
        int selectionCursor = mContentEditText.getSelectionStart();
        mContentEditText.getText().insert(selectionCursor, text);
        mContentEditText.invalidate();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (mRequestSaveJson != null)
            CloseRequestUtil.close(mRequestSaveJson);
        if (mRequestQueryJson != null)
            CloseRequestUtil.close(mRequestQueryJson);
        if (mDelectAskRequestJson != null)
            CloseRequestUtil.close(mDelectAskRequestJson);
        if (mDeleteCommentJson != null)
            CloseRequestUtil.close(mDeleteCommentJson);
    }

}
