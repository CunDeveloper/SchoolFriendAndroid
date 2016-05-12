package com.nju.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.nju.View.SchoolFriendDialog;
import com.nju.View.ShareView;
import com.nju.activity.R;
import com.nju.adatper.BigImgAdaper;
import com.nju.adatper.CommentAdapter;
import com.nju.db.db.service.RecommendWorkCollectDbService;
import com.nju.event.MessageEventId;
import com.nju.event.MessageShareEventId;
import com.nju.event.NetworkInfoEvent;
import com.nju.event.PersonInfoEvent;
import com.nju.http.ImageDownloader;
import com.nju.http.ResponseCallback;
import com.nju.http.callback.SaveCollectCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.ContentComment;
import com.nju.model.RecommendWork;
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
import com.nju.util.WeiXinShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

public class RecommendWorkItemDetailFragment extends BaseFragment {

    public static final String TAG = RecommendWorkItemDetailFragment.class.getSimpleName();
    private static final String PARAM_KEY = "key";
    private static final String TEXT_TYPE = "text/plain";
    private static final String EMALIA_SUBJECT = "主题";
    private static final String EMALIA_BODY = "内容";
    private static final String EMALIA_TO = "mailto:";
    private static RecommendWork mRecommendWork;
    private EditText mContentEditText;
    private TextView mEmailTV, mCommentNumberTV;
    private PostRequestJson mRequestJson, mRequestQueryJson, mDeleteContentJson;
    private ArrayList<ContentComment> mContentComments;
    private CommentAdapter mCommentAdapter;
    private View mMainView;
    private int mCommentType = 0;
    private ResponseCallback queryCommentCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(RecommendWorkItemDetailFragment.this)) {
                Log.e(TAG, error.getMessage());
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(RecommendWorkItemDetailFragment.this)) {
                Log.i(TAG, responseBody);
                try {
                    Object object = new ParseResponse().getInfo(responseBody, ContentComment.class);
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
            if (FragmentUtil.isAttachedToActivity(RecommendWorkItemDetailFragment.this)) {
                Log.e(TAG, error.getMessage());
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(RecommendWorkItemDetailFragment.this)) {
                Log.i(TAG, responseBody);
                ToastUtil.showShortText(getContext(), getString(R.string.comment_ok));
                mRequestQueryJson = RecommendWorkService.querySingleComment(RecommendWorkItemDetailFragment.this, mRecommendWork.getId(), queryCommentCallback);
            }
        }

    };
    private ResponseCallback saveOtherCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(RecommendWorkItemDetailFragment.this)) {
                Log.e(TAG, error.getMessage());
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(RecommendWorkItemDetailFragment.this)) {
                Log.i(TAG, responseBody);
                ToastUtil.showShortText(getContext(), getString(R.string.comment_ok));
                mRequestQueryJson = RecommendWorkService.querySingleComment(RecommendWorkItemDetailFragment.this, mRecommendWork.getId(), queryCommentCallback);
            }
        }

    };

    public RecommendWorkItemDetailFragment() {
        // Required empty public constructor
    }

    public static RecommendWorkItemDetailFragment newInstance(RecommendWork recommendWork) {
        RecommendWorkItemDetailFragment fragment = new RecommendWorkItemDetailFragment();
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
        }
        getHostActivity().display(6);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_recommend_work_item_detail, container, false);
        mMainView.setPadding(mMainView.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), mMainView.getPaddingRight(), mMainView.getPaddingBottom());
        initView(mMainView);
        findJobClick(mMainView);
        askJob(mMainView);
        mContentEditText = CommentUtil.getCommentEdit(this, mMainView);
        mRequestQueryJson = RecommendWorkService.querySingleComment(this, mRecommendWork.getId(), queryCommentCallback);
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private void initView(final View view) {
        mCommentNumberTV = (TextView) view.findViewById(R.id.comment_number_tv);
        TextView titleTv = (TextView) view.findViewById(R.id.recommend_work_item_detail_title);
        try {
            titleTv.setText(StringBase64.decode(mRecommendWork.getTitle()));
        } catch (IllegalArgumentException e) {
            titleTv.setText(Constant.UNKNOWN_CHARACTER);
        }
        TextView detailTv = (TextView) view.findViewById(R.id.recommend_work_item_detail_detail);
        try {
            detailTv.setText(StringBase64.decode(mRecommendWork.getContent()));
        } catch (IllegalArgumentException e) {
            detailTv.setText(Constant.UNKNOWN_CHARACTER);
        }
        TextView dateTv = (TextView) view.findViewById(R.id.date_tv);
        dateTv.setText(DateUtil.getRelativeTimeSpanString(mRecommendWork.getDate()));
        final TextView nameTV = (TextView) view.findViewById(R.id.name_tv);
        nameTV.setText(mRecommendWork.getAuthor().getAuthorName());
        nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(CircleFragment.newInstance(mRecommendWork.getAuthorInfo()));
            }
        });
        TextView labelTV = (TextView) view.findViewById(R.id.label_tv);
        labelTV.setText(mRecommendWork.getAuthor().getLabel());
        mEmailTV = (TextView) view.findViewById(R.id.email_tv);
        //mEmailTV.setText(mRecommendWork.getEmail());
        mEmailTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        final TextView collectTV = (TextView) view.findViewById(R.id.collect_tv);

        collectTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RecommendWorkCollectDbService(getContext()).save(mRecommendWork);
                RecommendWorkService.saveCollect(RecommendWorkItemDetailFragment.this, mRecommendWork.getId(),
                        new SaveCollectCallback(TAG,RecommendWorkItemDetailFragment.this,collectTV));
            }
        });

        TextView shareView = (TextView) view.findViewById(R.id.share_tv);
        shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareView.init(RecommendWorkItemDetailFragment.this, view);
            }
        });

        mContentComments = new ArrayList<>();
        mCommentAdapter = new CommentAdapter(getContext(), mContentComments);
        ListView hotListView = (ListView) view.findViewById(R.id.hot_listview);
        hotListView.setAdapter(mCommentAdapter);
        hotListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ContentComment comment = mContentComments.get(position);
                //commentId = comment.getId();
                mContentEditText.setHint("回复" + comment.getCommentAuthor().getAuthorName());
                SoftInput.open(getContext());
                CommentUtil.getHideLayout(view).setVisibility(View.VISIBLE);
            }
        });
        hotListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mRecommendWork.getAuthorInfo().getAuthorId() == getHostActivity().userId()) {
                    String[] strings = {getString(R.string.delete)};
                    SchoolFriendDialog.listItemDialog(getContext(), strings).show();
                }
                return true;
            }
        });

        ListView newListView = (ListView) view.findViewById(R.id.new_comment_listview);
        newListView.setAdapter(mCommentAdapter);
        newListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ContentComment comment = mContentComments.get(position);
                //commentId = comment.getId();
                mContentEditText.setHint("回复" + comment.getCommentAuthor().getAuthorName());
                SoftInput.open(getContext());
                CommentUtil.getHideLayout(view).setVisibility(View.VISIBLE);
            }
        });
        newListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mRecommendWork.getAuthorInfo().getAuthorId() == getHostActivity().userId()) {
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
                if (mCommentType == 0) {
                    mRequestJson = RecommendWorkService.saveAsk(RecommendWorkItemDetailFragment.this, view, mRecommendWork.getId(), saveCallback);
                } else {
                    mRequestJson = RecommendWorkService.saveAskForOther(RecommendWorkItemDetailFragment.this, view, mCommentType, saveOtherCallback);
                    mCommentType = 0;
                }
                CommentUtil.closeSoftKey(getContext(), view);
            }
        });
        ListView listView = (ListView) view.findViewById(R.id.listView);
        if (mRecommendWork.getImgPaths() != null) {
            Log.e(TAG, mRecommendWork.getImgPaths());
            listView.setAdapter(new BigImgAdaper(getContext(), PathConstant.ALUMNI_RECOMMEND_IMG_PATH, mRecommendWork.getImgPaths().split(",")));
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CircleImageViewFragment fragment = CircleImageViewFragment.
                        newInstance(mRecommendWork.getImgPaths().split(","), position, PathConstant.ALUMNI_RECOMMEND_IMG_PATH);
                getHostActivity().open(fragment,fragment);
            }
        });
        TextView deleteTV = (TextView) view.findViewById(R.id.delete_tv);
        deleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteContentJson = RecommendWorkService.deleteMyRecommend(RecommendWorkItemDetailFragment.this, mRecommendWork.getId(), new ResponseCallback() {
                    @Override
                    public void onFail(Exception error) {
                        Log.e(TAG,error.getMessage());
                    }

                    @Override
                    public void onSuccess(String responseBody) {
                        if (FragmentUtil.isAttachedToActivity(RecommendWorkItemDetailFragment.this)) {
                            Log.i(TAG, responseBody);
                            ParseResponse parseResponse = new ParseResponse();
                            try {
                                String str = parseResponse.getInfo(responseBody);
                                getHostActivity().getBackStack().pop();
                                getHostActivity().open(getHostActivity().getBackStack().peek());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

        if (mRecommendWork.getAuthorInfo().getAuthorId() == getHostActivity().userId()) {
            deleteTV.setText(Constant.DELETE);
        }

    }

    public void inputEmotion(String text) {
        int selectionCursor = mContentEditText.getSelectionStart();
        mContentEditText.getText().insert(selectionCursor, text);
        mContentEditText.invalidate();
    }

    private void findJobClick(View view) {
        TextView textView = (TextView) view.findViewById(R.id.re_work_item_yinpin_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType(TEXT_TYPE);
        intent.putExtra(Intent.EXTRA_SUBJECT, EMALIA_SUBJECT);
        intent.putExtra(Intent.EXTRA_TEXT,EMALIA_BODY );
        intent.setData(Uri.parse(EMALIA_TO + mEmailTV.getText()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void askJob(final View view) {
        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.mScrollView);
        final ListView listView = (ListView) view.findViewById(R.id.new_comment_listview);
        TextView textView = (TextView) view.findViewById(R.id.re_work_item_ask_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.getHideLayout(view).setVisibility(View.VISIBLE);
                SoftInput.open(getContext());
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, listView.getBottom());
                    }
                });
            }
        });
    }

    @Subscribe
    public void onNetStateMessageState(NetworkInfoEvent event) {
        if (event.isConnected()) {
            mRequestQueryJson = RecommendWorkService.querySingleComment(RecommendWorkItemDetailFragment.this, mRecommendWork.getId(), queryCommentCallback);
        }
    }

    @Subscribe
    public void onMessagePersonCircle(PersonInfoEvent event) {
        getHostActivity().open(CircleFragment.newInstance(event.getAuthorInfo()));
    }

    @Subscribe
    public void onMessageEvent(MessageEventId event) {
        mCommentType = event.getId();
        final ScrollView scrollView = (ScrollView) mMainView.findViewById(R.id.mScrollView);
        final LinearLayout linearLayout = (LinearLayout) mMainView.findViewById(R.id.new_comment_layout);
        CommentUtil.getHideLayout(mMainView).setVisibility(View.VISIBLE);
        SoftInput.open(getContext());
        scrollView.scrollTo(0, linearLayout.getBottom());
    }

    @Subscribe
    public void onMessageShareEvent(MessageShareEventId eventId) {
        String pageUrl = "http://115.159.186.158:8080/school-friend-service-webapp/SchoolFriendHtml/html/recommedShare.html";
        CharSequence title, description;
        try {
            title = StringBase64.decode(mRecommendWork.getTitle());
            description = StringBase64.decode(mRecommendWork.getContent());
        } catch (IllegalArgumentException e) {
            title = Constant.UNKNOWN_CHARACTER;
            description = Constant.UNKNOWN_CHARACTER;
        }

        if (mRecommendWork.getImgPaths() != null && !mRecommendWork.getImgPaths().equals("")) {
            String url = PathConstant.IMAGE_PATH_SMALL + PathConstant.ALUMNI_RECOMMEND_IMG_PATH + mRecommendWork.getImgPaths().split(",")[0];
            Bitmap bitmap = ImageDownloader.with(getContext()).download(url).bitmap();

            if (eventId.getId() == 0) {
                WeiXinShare.webPage(pageUrl, title.toString(), description.toString(), bitmap, SendMessageToWX.Req.WXSceneSession, getHostActivity().wxApi());
            } else if (eventId.getId() == 1) {
                WeiXinShare.webPage(pageUrl, title.toString(), description.toString(), bitmap, SendMessageToWX.Req.WXSceneTimeline, getHostActivity().wxApi());
            }
        } else {
            if (eventId.getId() == 0) {
                WeiXinShare.webPage(pageUrl, title.toString(), description.toString(), SendMessageToWX.Req.WXSceneSession, getHostActivity().wxApi());
            } else if (eventId.getId() == 1) {
                WeiXinShare.webPage(pageUrl, title.toString(), description.toString(), SendMessageToWX.Req.WXSceneTimeline, getHostActivity().wxApi());
            }
        }


    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (mRequestJson != null)
            CloseRequestUtil.close(mRequestJson);
        if (mRequestQueryJson != null)
            CloseRequestUtil.close(mRequestQueryJson);
        if (mDeleteContentJson != null)
            CloseRequestUtil.close(mDeleteContentJson);
    }
}
