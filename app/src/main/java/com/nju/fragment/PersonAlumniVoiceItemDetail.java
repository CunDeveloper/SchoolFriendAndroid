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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.adatper.BigImgAdaper;
import com.nju.adatper.CommentAdapter;
import com.nju.adatper.PraiseHeadAdapter;
import com.nju.event.MessageDeleteEvent;
import com.nju.event.MessageEventId;
import com.nju.event.NetworkInfoEvent;
import com.nju.http.ImageDownloader;
import com.nju.http.ResponseCallback;
import com.nju.http.callback.DeleteCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniVoice;
import com.nju.model.ContentComment;
import com.nju.model.RespPraise;
import com.nju.service.AlumniTalkService;
import com.nju.service.AlumniVoiceService;
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
import com.nju.util.WhoScanUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IllegalFormatCodePointException;

public class PersonAlumniVoiceItemDetail extends BaseFragment {
    public static final String TAG = PersonAlumniVoiceItemDetail.class.getSimpleName();
    private static final String PARAM_VOICE = "voiceKey";
    private static final String PARAM_TITLE = "title";
    private AlumniVoice mVoice;
    private String mTitle;
    private EditText mContentEditText;
    private ArrayList<ContentComment> mContentComments;
    private ArrayList<RespPraise> mRespPraises = new ArrayList<>();
    private CommentAdapter mCommentAdapter;
    private View mMainView;
    private int commentType = 0;
    private TextView mCommentNumberTV, mPraiseNumberTV;
    private PraiseHeadAdapter mPraiseHeadAdapter;
    private PostRequestJson mRequestSaveJson, mRequestQueryJson, mRequestQueryPraiseJson
            ,mDeleteRequestJson,mDeleteCommentJson;
    private int mDeleteIndex = 0;
    private ResponseCallback queryPraiseCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(PersonAlumniVoiceItemDetail.this)) {
                Log.e(TAG, error.getMessage());
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(PersonAlumniVoiceItemDetail.this)) {
                Log.i(TAG, responseBody);
                try {
                    Object object = new ParseResponse().getInfo(responseBody, RespPraise.class);
                    if (object != null) {
                        ArrayList authors = (ArrayList) object;
                        if (authors.size() > 0) {
                            for (Object obj : authors) {
                                RespPraise respPraise = (RespPraise) obj;
                                mRespPraises.add(respPraise);
                            }
                        }
                    }
                    mPraiseHeadAdapter.notifyDataSetChanged();
                    mPraiseNumberTV.setText(mRespPraises.size() + "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private ResponseCallback queryCommentCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(PersonAlumniVoiceItemDetail.this)) {
                Log.e(TAG, error.getMessage());
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(PersonAlumniVoiceItemDetail.this)) {
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
            if (FragmentUtil.isAttachedToActivity(PersonAlumniVoiceItemDetail.this)) {
                Log.e(TAG, error.getMessage());
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(PersonAlumniVoiceItemDetail.this)) {
                Log.i(TAG, responseBody);
                ToastUtil.showShortText(getContext(), getString(R.string.comment_ok));
                mRequestQueryJson = AlumniVoiceService.queryComment(PersonAlumniVoiceItemDetail.this, mVoice.getId(), queryCommentCallback);
            }
        }
    };


    public PersonAlumniVoiceItemDetail() {
        // Required empty public constructor
    }

    public static PersonAlumniVoiceItemDetail newInstance(AlumniVoice voice, String title) {
        PersonAlumniVoiceItemDetail fragment = new PersonAlumniVoiceItemDetail();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_VOICE, voice);
        args.putString(PARAM_TITLE, title);
        fragment.setArguments(args);
        return fragment;
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
        if (actionBar != null) {
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
        mContentEditText = CommentUtil.getCommentEdit(this, view);
        return view;
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

    private void initView(final View view) {
        mCommentNumberTV = (TextView) view.findViewById(R.id.comment_number_tv);
        mPraiseNumberTV = (TextView) view.findViewById(R.id.praise_number_tv);
        TextView nameTV = (TextView) view.findViewById(R.id.alumni_vo_name);
        nameTV.setText(mVoice.getAuthorInfo().getAuthorName());
        TextView labelTV = (TextView) view.findViewById(R.id.alumni_vo_label);
        labelTV.setText(mVoice.getAuthorInfo().getLabel());
        TextView voiceLabelTV = (TextView) view.findViewById(R.id.labelTV);
        voiceLabelTV.setText(mVoice.getLabel());
        TextView whoScanTV = (TextView) view.findViewById(R.id.whoScanTV);
        int userId = getHostActivity().userId();
        if (userId == mVoice.getAuthorInfo().getAuthorId()){
            whoScanTV.setText(WhoScanUtil.accessStr(mVoice.getWhoScan()));
        }
        TextView titleTV = (TextView) view.findViewById(R.id.alumni_vo_title);
        try {
            titleTV.setText(StringBase64.decode(mVoice.getTitle()));
        } catch (IllegalArgumentException e) {
            titleTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        TextView contentTV = (TextView) view.findViewById(R.id.alumni_vo_desc);
        try {
            contentTV.setText(StringBase64.decode(mVoice.getContent()));
        } catch (IllegalArgumentException e) {
            contentTV.setText(Constant.UNKNOWN_CHARACTER);
        }
        TextView dateTV = (TextView) view.findViewById(R.id.alumni_vo_date);
        dateTV.setText(DateUtil.getRelativeTimeSpanString(mVoice.getDate()));
        GridView gridView = (GridView) view.findViewById(R.id.new_gridview);
        mPraiseHeadAdapter = new PraiseHeadAdapter(getContext(),PathConstant.ALUMNI_VOICE_IMG_PATH,mRespPraises);
        gridView.setAdapter(mPraiseHeadAdapter);
        ListView newListView = (ListView) view.findViewById(R.id.new_comment_listview);
        mContentComments =  new ArrayList<>();
        mCommentAdapter = new CommentAdapter(getContext(), mContentComments);
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

        Button sendBn = (Button) view.findViewById(R.id.activity_school_friend_send_button);
        sendBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentType == 0) {
                    mRequestSaveJson = AlumniVoiceService.saveComment(PersonAlumniVoiceItemDetail.this, view, mVoice.getId(), saveCallback);
                } else {
                    mRequestSaveJson = AlumniVoiceService.saveCommentForOther(PersonAlumniVoiceItemDetail.this, view, commentType, saveCallback);
                    commentType = 0;
                }
                CommentUtil.closeSoftKey(getContext(), view);
            }
        });
        mRequestQueryJson = AlumniVoiceService.queryComment(this, mVoice.getId(), queryCommentCallback);
        mRequestQueryPraiseJson = AlumniVoiceService.queryPraise(this, mVoice.getId(), queryPraiseCallback);

        ListView listView = (ListView) view.findViewById(R.id.listView);
        if (mVoice.getImgPaths() != null) {
            Log.e(TAG, mVoice.getImgPaths());
            listView.setAdapter(new BigImgAdaper(getContext(), PathConstant.ALUMNI_VOICE_IMG_PATH, mVoice.getImgPaths().split(",")));
        }

        ImageView headImg = (ImageView) view.findViewById(R.id.alumni_vo_imageView);
        String url = PathConstant.IMAGE_PATH_SMALL + PathConstant.HEAD_ICON_IMG + mVoice.getAuthorInfo().getHeadUrl();
        ImageDownloader.with(getContext()).download(url,headImg);
    }

    @Subscribe
    public void onNetStateMessageState(NetworkInfoEvent event) {
        if (event.isConnected()) {
            mRequestQueryJson = AlumniVoiceService.queryComment(this, mVoice.getId(), queryCommentCallback);
            mRequestQueryPraiseJson = AlumniVoiceService.queryPraise(this, mVoice.getId(), queryPraiseCallback);
        }
    }

    @Subscribe
    public void onMessageDeleteComment(MessageDeleteEvent deleteEvent) {
        mDeleteCommentJson = AlumniVoiceService.deleteComment(this, mDeleteIndex, new ResponseCallback() {
            @Override
            public void onFail(Exception error) {
                Log.e(TAG, error.getMessage());
            }

            @Override
            public void onSuccess(String responseBody) {
                Log.i(TAG, responseBody);
                if (FragmentUtil.isAttachedToActivity(PersonAlumniVoiceItemDetail.this)) {
                    Log.i(TAG, responseBody);
                    ParseResponse parseResponse = new ParseResponse();
                    try {
                        String str = parseResponse.getInfo(responseBody);
                        if (str != null && str.equals(Constant.OK_MSG)) {
                            mRequestQueryJson = AlumniVoiceService.queryComment(PersonAlumniVoiceItemDetail.this, mVoice.getId(), queryCommentCallback);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mDeleteIndex = 0;
    }

    private void initToolBar(final View view) {
        RelativeLayout deleteLayout = (RelativeLayout) view.findViewById(R.id.delete_reLayout);
        RelativeLayout replayLayout = (RelativeLayout) view.findViewById(R.id.replay_reLayout);
        RelativeLayout closeRelayout = (RelativeLayout) view.findViewById(R.id.close_reLayout);
        closeRelayout.setVisibility(View.GONE);
        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.mScrollView);
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.new_comment_layout);
        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteRequestJson = AlumniVoiceService.deleteVoice(PersonAlumniVoiceItemDetail.this,mVoice.getId(),
                        new DeleteCallback(TAG,PersonAlumniVoiceItemDetail.this));
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

        closeRelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (mRequestSaveJson != null)
            CloseRequestUtil.close(mRequestSaveJson);
        if (mRequestQueryJson != null)
            CloseRequestUtil.close(mRequestQueryJson);
        if (mRequestQueryPraiseJson != null)
            CloseRequestUtil.close(mRequestQueryPraiseJson);
        if (mDeleteRequestJson != null)
            CloseRequestUtil.close(mDeleteRequestJson);
        if (mDeleteCommentJson != null)
            CloseRequestUtil.close(mDeleteCommentJson);
    }
}
