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
import com.nju.model.ContentComment;
import com.nju.model.RecommendWork;
import com.nju.service.AlumniTalkService;
import com.nju.service.RecommendWorkService;
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


public class PersonRecommendWorkItemDetailFragment extends BaseFragment {
    public static final String TAG = PersonRecommendWorkItemDetailFragment.class.getSimpleName();
    private static final String PARAM_KEY = "key";
    private static RecommendWork mRecommendWork;
    private EditText mContentEditText;
    private TextView mCommentNumberTV;
    private PostRequestJson mRequestDeleteJson, mRequestQueryJson, mRequestSaveJson,mDeleteCommentJson;
    private ArrayList<ContentComment> mContentComments;
    private CommentAdapter mCommentAdapter;
    private int commentType = 0;
    private View mMainView;
    private int mDeleteIndex = 0;

    private ResponseCallback queryCommentCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(PersonRecommendWorkItemDetailFragment.this)) {
                Log.e(TAG, error.getMessage());
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(PersonRecommendWorkItemDetailFragment.this)) {
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
                    Collections.sort(mContentComments, Collections.reverseOrder());
                    mCommentAdapter.notifyDataSetChanged();
                    mCommentNumberTV.setText(mContentComments.size() + "");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private ResponseCallback saveCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(PersonRecommendWorkItemDetailFragment.this)) {
                Log.e(TAG, error.getMessage());
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(PersonRecommendWorkItemDetailFragment.this)) {
                Log.i(TAG, responseBody);
                ToastUtil.showShortText(getContext(), getString(R.string.comment_ok));
                mRequestQueryJson = RecommendWorkService.querySingleComment(PersonRecommendWorkItemDetailFragment.this, mRecommendWork.getId(), queryCommentCallback);
            }
        }

    };

    private ResponseCallback saveOtherCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(PersonRecommendWorkItemDetailFragment.this)) {
                Log.e(TAG, error.getMessage());
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(PersonRecommendWorkItemDetailFragment.this)) {
                Log.i(TAG, responseBody);
                ToastUtil.showShortText(getContext(), getString(R.string.comment_ok));
                mRequestQueryJson = RecommendWorkService.querySingleComment(PersonRecommendWorkItemDetailFragment.this,
                        mRecommendWork.getId(), queryCommentCallback);
            }
        }

    };


    public PersonRecommendWorkItemDetailFragment() {
        // Required empty public constructor
    }

    public static PersonRecommendWorkItemDetailFragment newInstance(RecommendWork recommendWork) {
        PersonRecommendWorkItemDetailFragment fragment = new PersonRecommendWorkItemDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_KEY, recommendWork);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecommendWork = getArguments().getParcelable(PARAM_KEY);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getType(mRecommendWork.getType()));
        }
        getHostActivity().display(6);
    }

    private String getType(int type) {
        if (type == 0)
            return getString(R.string.shixi);
        if (type == 1)
            return getString(R.string.xiaozhao);
        if (type == 2)
            return getString(R.string.shezhao);
        return getString(R.string.recommend_work);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person_recommend_work_item_detail, container, false);
        mMainView = view;
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initView(view);
        mContentEditText = CommentUtil.getCommentEdit(this, view);
        initToolBar(view);
        mRequestQueryJson = RecommendWorkService.querySingleComment(this, mRecommendWork.getId(), queryCommentCallback);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private void initToolBar(final View view) {
        RelativeLayout deleteReLayout = (RelativeLayout) view.findViewById(R.id.delete_reLayout);
        deleteReLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequestDeleteJson = RecommendWorkService.deleteMyRecommend(PersonRecommendWorkItemDetailFragment.this, mRecommendWork.getId(),
                        new DeleteCallback(TAG,PersonRecommendWorkItemDetailFragment.this));
            }
        });
        RelativeLayout replayReLayout = (RelativeLayout) view.findViewById(R.id.replay_reLayout);
        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.mScrollView);
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.new_comment_layout);
        replayReLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.getHideLayout(view).setVisibility(View.VISIBLE);
                SoftInput.open(getContext());
                scrollView.scrollTo(0, linearLayout.getBottom());
            }
        });
        RelativeLayout closeReLayout = (RelativeLayout) view.findViewById(R.id.close_reLayout);
        closeReLayout.setVisibility(View.GONE);
        closeReLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initView(final View view) {
        TextView titleTv = (TextView) view.findViewById(R.id.title_tv);
        try {
            titleTv.setText(StringBase64.decode(mRecommendWork.getTitle()));
        } catch (IllegalArgumentException e) {
            titleTv.setText(Constant.UNKNOWN_CHARACTER);
        }
        TextView detailTv = (TextView) view.findViewById(R.id.description_tv);
        try {
            detailTv.setText(StringBase64.decode(mRecommendWork.getContent()));
        } catch (IllegalArgumentException e) {
            detailTv.setText(Constant.UNKNOWN_CHARACTER);
        }
        TextView emailTV = (TextView) view.findViewById(R.id.email_tv);
        emailTV.setText(mRecommendWork.getEmail());
        TextView dateTV = (TextView) view.findViewById(R.id.date_tv);
        dateTV.setText(DateUtil.getRelativeTimeSpanString(mRecommendWork.getDate()));
        mCommentNumberTV = (TextView) view.findViewById(R.id.comment_number_tv);
        TextView nameTV = (TextView) view.findViewById(R.id.name_tv);
        nameTV.setText(mRecommendWork.getAuthor().getAuthorName());
        TextView labelTV = (TextView) view.findViewById(R.id.label_tv);
        labelTV.setText(mRecommendWork.getAuthor().getLabel());
        mContentComments =  new ArrayList<>();
        mCommentAdapter = new CommentAdapter(getContext(), mContentComments);
        ListView newListView = (ListView) view.findViewById(R.id.new_comment_listview);
        newListView.setAdapter(mCommentAdapter);
        newListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

        final Button sendBn = (Button) view.findViewById(R.id.activity_school_friend_send_button);
        sendBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentType == 0) {
                    mRequestSaveJson = RecommendWorkService.saveAsk(PersonRecommendWorkItemDetailFragment.this, view, mRecommendWork.getId(), saveCallback);
                } else {
                    mRequestSaveJson = RecommendWorkService.saveAskForOther(PersonRecommendWorkItemDetailFragment.this, view, commentType, saveOtherCallback);
                    commentType = 0;
                }
                CommentUtil.closeSoftKey(getContext(), view);
            }
        });

        ListView listView = (ListView) view.findViewById(R.id.listView);
        if (mRecommendWork.getImgPaths() != null) {
            Log.e(TAG, mRecommendWork.getImgPaths());
            listView.setAdapter(new BigImgAdaper(getContext(), PathConstant.ALUMNI_RECOMMEND_IMG_PATH, mRecommendWork.getImgPaths().split(",")));
        }
    }

    @Subscribe
    public void onNetStateMessageState(NetworkInfoEvent event) {
        if (event.isConnected()) {
            mRequestQueryJson = RecommendWorkService.querySingleComment(this, mRecommendWork.getId(), queryCommentCallback);
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
        mDeleteCommentJson = RecommendWorkService.deleteComment(this, mDeleteIndex, new ResponseCallback() {
            @Override
            public void onFail(Exception error) {
                Log.e(TAG, error.getMessage());
            }

            @Override
            public void onSuccess(String responseBody) {
                Log.i(TAG, responseBody);
                if (FragmentUtil.isAttachedToActivity(PersonRecommendWorkItemDetailFragment.this)) {
                    Log.i(TAG, responseBody);
                    ParseResponse parseResponse = new ParseResponse();
                    try {
                        String str = parseResponse.getInfo(responseBody);
                        ToastUtil.showShortText(getContext(),str);
                        if (str != null && str.equals(Constant.OK_MSG)) {
                            mRequestQueryJson = RecommendWorkService.querySingleComment(PersonRecommendWorkItemDetailFragment.this,
                                    mRecommendWork.getId(), queryCommentCallback);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mDeleteIndex = 0;
    }

    public void inputEmotion(String text) {
        int selectionCursor = mContentEditText.getSelectionStart();
        mContentEditText.getText().insert(selectionCursor, text);
        mContentEditText.invalidate();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRequestDeleteJson != null)
            CloseRequestUtil.close(mRequestDeleteJson);
        if (mRequestQueryJson != null)
            CloseRequestUtil.close(mRequestQueryJson);
        if (mRequestSaveJson != null)
            CloseRequestUtil.close(mRequestSaveJson);

    }
}
