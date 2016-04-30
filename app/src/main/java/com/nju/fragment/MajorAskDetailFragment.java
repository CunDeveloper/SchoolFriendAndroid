package com.nju.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
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
import com.nju.activity.CommentOtherEvent;
import com.nju.activity.DeleteCommentEvent;
import com.nju.activity.NetworkInfoEvent;
import com.nju.activity.PersonInfoEvent;
import com.nju.activity.R;
import com.nju.adatper.BigImgAdaper;
import com.nju.adatper.CommentAdapter;
import com.nju.db.db.service.MajorAskCollectDbService;
import com.nju.event.MessageEventId;
import com.nju.event.MessageShareEventId;
import com.nju.http.ImageDownloader;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniQuestion;
import com.nju.model.ContentComment;
import com.nju.service.MajorAskService;
import com.nju.test.TestData;
import com.nju.util.CloseRequestUtil;
import com.nju.util.CommentUtil;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.PathConstant;
import com.nju.util.SchoolFriendGson;
import com.nju.util.ShareUtil;
import com.nju.util.SoftInput;
import com.nju.util.SortUtil;
import com.nju.util.StringBase64;
import com.nju.util.ToastUtil;
import com.nju.util.WeiXinShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;

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
                            mContentComments = SortUtil.softByDate(mContentComments);
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
        initListView(view);
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
    @Subscribe
    public void onMessagePersonEvent(PersonInfoEvent event){
        getHostActivity().open(CircleFragment.newInstance(event.getAuthorInfo()));
    }

    @Subscribe
    public void onMessageShareEvent(MessageShareEventId eventId){
        AlumniQuestion test = new AlumniQuestion();
        test.setId(mAlumniQuestion.getId());
        test.setDescription(StringBase64.decode(mAlumniQuestion.getDescription()));
        test.setProblem(StringBase64.decode(mAlumniQuestion.getProblem()));
        test.setAuthor(mAlumniQuestion.getAuthor());
        test.setImgPaths(mAlumniQuestion.getImgPaths());
        Log.i(TAG,SchoolFriendGson.newInstance().toJson(test));
        String pageUrl = "http://115.159.186.158:8080/school-friend-service-webapp/SchoolFriendHtml/html/recommedShare.html?content="+SchoolFriendGson.newInstance().toJson(test);
        Log.i(TAG,pageUrl);
        CharSequence title,description;
        try{
            title = StringBase64.decode(mAlumniQuestion.getProblem());
            description = StringBase64.decode(mAlumniQuestion.getDescription());
        }catch (IllegalArgumentException e){
            title = Constant.UNKNOWN_CHARACTER;
            description = Constant.UNKNOWN_CHARACTER;
        }

        if (mAlumniQuestion.getImgPaths() != null && !mAlumniQuestion.getImgPaths().equals("")){
            String url = PathConstant.IMAGE_PATH_SMALL+PathConstant.ALUMNI_QUESTION_IMG_PATH+mAlumniQuestion.getImgPaths().split(",")[0];
            Bitmap bitmap = ImageDownloader.with(getContext()).download(url).bitmap();

            if (eventId.getId()== 0){
                WeiXinShare.webPage(pageUrl, title.toString(), description.toString(), bitmap, SendMessageToWX.Req.WXSceneSession,getHostActivity().wxApi());
            }else if (eventId.getId() == 1){
                WeiXinShare.webPage(pageUrl, title.toString(), description.toString(), bitmap, SendMessageToWX.Req.WXSceneTimeline,getHostActivity().wxApi());
            }
        }else {
            if (eventId.getId()== 0){
                WeiXinShare.webPage(pageUrl, title.toString(), description.toString(),SendMessageToWX.Req.WXSceneSession,getHostActivity().wxApi());
            }else if (eventId.getId() == 1){
                WeiXinShare.webPage(pageUrl, title.toString(), description.toString(),SendMessageToWX.Req.WXSceneTimeline,getHostActivity().wxApi());
            }
        }


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
        nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(CircleFragment.newInstance(mAlumniQuestion.getAuthor()));
            }
        });
        nameTV.setText(mAlumniQuestion.getAuthor().getAuthorName());
        TextView dateTV = (TextView) view.findViewById(R.id.date_tv);
        dateTV.setText(DateUtil.getRelativeTimeSpanString(mAlumniQuestion.getDate()));
        mContentComments = TestData.getComments();
        final ListView commentListView = (ListView) view.findViewById(R.id.new_comment_listview);
        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    ContentComment comment = mContentComments.get(position);
                    //commentId = comment.getId();
                    mContentEditText.setHint("回复" + comment.getCommentAuthor().getAuthorName());
                    SoftInput.open(getContext());
                    CommentUtil.getHideLayout(view).setVisibility(View.VISIBLE);
            }
        });
        commentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ContentComment comment = mContentComments.get(position);
                if (comment.getCommentAuthor().getAuthorId() == getHostActivity().userId()){
                    String[] strings = {getString(R.string.delete)};
                    SchoolFriendDialog.listItemDialog(getContext(), strings).show();
                }
                return true;
            }
        });

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

        TextView deleteTV = (TextView) view.findViewById(R.id.delete_tv);
        if (mAlumniQuestion.getAuthor().getAuthorId() == this.getHostActivity().userId()){
            deleteTV.setText(Constant.DELETE);
        }
        deleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mRequestQueryJson = MajorAskService.queryComment(MajorAskDetailFragment.this, mAlumniQuestion.getId(), queryCommentCallback);
    }

    private void initListView(View view){
        ListView listView = (ListView) view.findViewById(R.id.listView);
        if (mAlumniQuestion.getImgPaths()!=null){
            Log.e(TAG,mAlumniQuestion.getImgPaths());
            listView.setAdapter(new BigImgAdaper(getContext(), PathConstant.ALUMNI_QUESTION_IMG_PATH,mAlumniQuestion.getImgPaths().split(",")));
        }

    }

    private void initToolBar(final View view) {
        TextView commentTV = (TextView) view.findViewById(R.id.comment);
        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.myScrollView);
        final ListView commentListView = (ListView) view.findViewById(R.id.new_comment_listview);
        commentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.scrollTo(0, commentListView.getBottom());
                SoftInput.open(getContext());
                CommentUtil.getHideLayout(view).setVisibility(View.VISIBLE);
            }
        });

        final TextView collectTV = (TextView) view.findViewById(R.id.collect);
        collectTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MajorAskCollectDbService(getContext()).save(mAlumniQuestion);
                MajorAskService.saveCollect(MajorAskDetailFragment.this, mAlumniQuestion.getId(), new ResponseCallback() {
                    @Override
                    public void onFail(Exception error) {
                        Log.e(TAG, error.getMessage());
                    }

                    @Override
                    public void onSuccess(String responseBody) {
                        Log.i(TAG, responseBody);
                        if (FragmentUtil.isAttachedToActivity(MajorAskDetailFragment.this)) {
                            Log.i(TAG, responseBody);
                            ParseResponse parseResponse = new ParseResponse();
                            try {
                                String str = parseResponse.getInfo(responseBody);
                                if (str != null && str.equals(Constant.OK_MSG)) {
                                    ToastUtil.ShowText(getContext(), getString(R.string.collect_ok));
                                    collectTV.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            }
        });

        TextView shareTV = (TextView) view.findViewById(R.id.share);
         shareTV.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ShareView.init(MajorAskDetailFragment.this, view);
             }
         });

         final TextView parise = (TextView) view.findViewById(R.id.praise);
         parise.setVisibility(View.GONE);
    }


         @Override
         public void onStop() {
             EventBus.getDefault().unregister(this);
             super.onStop();
             if (mRequestSaveJson != null)
                 CloseRequestUtil.close(mRequestSaveJson);
             if (mRequestQueryJson != null)
                 CloseRequestUtil.close(mRequestQueryJson);
         }
     }
