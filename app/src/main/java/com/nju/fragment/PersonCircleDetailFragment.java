package com.nju.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;

import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.adatper.ContentPicAdater;
import com.nju.adatper.PersonCommentAdapter;
import com.nju.adatper.PraiseHeadAdapter;
import com.nju.adatper.UserCommentItemListAdapter;
import com.nju.event.CommentEvent;
import com.nju.event.MessageDeleteEvent;
import com.nju.event.PraiseEvent;
import com.nju.http.ImageDownloader;
import com.nju.http.ResponseCallback;
import com.nju.http.callback.DeleteCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniTalk;
import com.nju.model.AlumnicTalkPraise;
import com.nju.model.AuthorInfo;
import com.nju.model.ContentComment;
import com.nju.model.RespPraise;
import com.nju.service.AlumniTalkService;
import com.nju.util.CloseRequestUtil;
import com.nju.util.CommentPopupWindow;
import com.nju.util.CommentUtil;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class PersonCircleDetailFragment extends BaseFragment {
    public static final String TAG = PersonCircleDetailFragment.class.getSimpleName();
    private static final String TALK_PARA = "talk_para";
    private AlumniTalk mTalk;
    private EditText mContentEditText;
    private PostRequestJson getCommentsJson,getPraisesJson,mRequestCommentJson
            , mDeleteCommentJson, mDeleteContentJson;
    private PersonCommentAdapter mPersonCommentAdapter;
    private ArrayList<RespPraise> mRespPraises = new ArrayList<>();
    private ArrayList<ContentComment> mComments = new ArrayList<>();
    private PraiseHeadAdapter mPraiseHeadAdapter;
    private int mOtherIndex = 0;
    private int mDeleteIndex = 0;

    private ResponseCallback getCommentCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.i(TAG, error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            if (FragmentUtil.isAttachedToActivity(PersonCircleDetailFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody, ContentComment.class);
                    if (object != null) {
                        ArrayList comments = (ArrayList) object;
                        mComments.clear();
                        if (comments.size() > 0) {
                            for (Object obj : comments) {
                                ContentComment comment = (ContentComment) obj;
                                if (! mComments.contains(comment)){
                                    mComments.add(comment);
                                }
                            }
                        }
                    }
                    Collections.sort(mComments,Collections.reverseOrder());
                    mPersonCommentAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private ResponseCallback getPraiseCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.i(TAG, error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            if (FragmentUtil.isAttachedToActivity(PersonCircleDetailFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody, AlumnicTalkPraise.class);
                    if (object != null) {
                        ArrayList praises = (ArrayList) object;
                        mRespPraises.clear();
                        RespPraise praise;
                        if (praises.size() > 0) {
                            for (Object obj : praises) {
                                praise = new RespPraise();
                                AlumnicTalkPraise talkPraise = (AlumnicTalkPraise) obj;
                                AuthorInfo authorInfo = talkPraise.getPraiseAuthor();
                                praise.setPraiseAuthor(authorInfo);
                                praise.setContentId(talkPraise.getContentId());
                                praise.setDate(talkPraise.getDate());
                                praise.setId(talkPraise.getId());
                                mRespPraises.add(praise);
                            }
                        }
                    }
                    mPraiseHeadAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private ResponseCallback saveCommentCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG, error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            mContentEditText.setHint(Constant.EMPTY_STR);
            mContentEditText.invalidate();
            ToastUtil.showShortText(getContext(), Constant.COMMENT_OK);
            queryPraiseAndComment();
        }
    };

    private ResponseCallback deleteCommentCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG, error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            if (FragmentUtil.isAttachedToActivity(PersonCircleDetailFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    String str = parseResponse.getInfo(responseBody);
                    if (str.equals(Constant.OK_MSG)) {
                        queryPraiseAndComment();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public PersonCircleDetailFragment() {
        // Required empty public constructor
    }

    private void queryPraiseAndComment() {
        ArrayList<AlumniTalk> alumniTalks = new ArrayList<>();
        alumniTalks.add(mTalk);
        getCommentsJson = AlumniTalkService.queryComment(PersonCircleDetailFragment.this,alumniTalks, getCommentCallback);
        getPraisesJson = AlumniTalkService.queryPraise(PersonCircleDetailFragment.this, alumniTalks, getPraiseCallback);
    }

    public static PersonCircleDetailFragment newInstance(AlumniTalk talk) {
        PersonCircleDetailFragment fragment = new PersonCircleDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(TALK_PARA, talk);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
             mTalk = getArguments().getParcelable(TALK_PARA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person_circle_detail, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        mContentEditText = CommentUtil.getCommentEditWithEmotion(this, view);
        CommentUtil.getHideLayout(view).setVisibility(View.VISIBLE);
        initView(view);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (getCommentsJson != null)
            CloseRequestUtil.close(getCommentsJson);
        if (getPraisesJson != null)
            CloseRequestUtil.close(getPraisesJson);
    }

    @Subscribe
    public void onMessagePraiseEvent(PraiseEvent event){
        Log.i(TAG, "event:" + event.getId());
    }

    @Subscribe
    public void onMessageCommentEvent(CommentEvent event){
        Log.i(TAG,"event:" + event.getId());
    }

    private void initView(final View view) {
        TextView contentTV = (TextView) view.findViewById(R.id.school_friend_item_content);
        TextView nameTV = (TextView) view.findViewById(R.id.school_friend_item_name_text);
        TextView labelTV = (TextView) view.findViewById(R.id.school_friend_item_label_text);
        TextView dateTV = (TextView) view.findViewById(R.id.school_friend_item_publish_date);
        TextView deleteTV = (TextView) view.findViewById(R.id.deleteTv);
        TextView locationTV = (TextView) view.findViewById(R.id.locationTV);
        ImageView headImg = (ImageView) view.findViewById(R.id.headIconImg);
        deleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteContentJson = AlumniTalkService.deleteDynamci(PersonCircleDetailFragment.this, mTalk.getId(),
                        new DeleteCallback(TAG,PersonCircleDetailFragment.this));
            }
        });
        if (mTalk != null){
            try{
                contentTV.setText(StringBase64.decode(mTalk.getContent()));
            }catch (IllegalArgumentException e){
                contentTV.setText(Constant.UNKNOWN_CHARACTER);
            }

            AuthorInfo authorInfo = mTalk.getAuthorInfo();
            if (authorInfo != null){
                nameTV.setText(authorInfo.getAuthorName());
                labelTV.setText(authorInfo.getLabel());
                String headUrl = PathConstant.IMAGE_PATH_SMALL + PathConstant.HEAD_ICON_IMG + authorInfo.getHeadUrl();
                ImageDownloader.with(getContext()).download(headUrl,headImg);
                if (getHostActivity().userId() == authorInfo.getAuthorId()){
                    deleteTV.setText(Constant.DELETE);
                } else {
                    deleteTV.setText(Constant.EMPTY_STR);
                }
            }
            dateTV.setText(DateUtil.getRelativeTimeSpanString(mTalk.getDate()));
            locationTV.setText(mTalk.getLocation());
        }

        final TextView commentIconTv = (TextView) view.findViewById(R.id.comment_icon_tv);
        commentIconTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListPopupWindow listPopupWindow = new CommentPopupWindow(getContext(), commentIconTv);
                listPopupWindow.setAdapter(new UserCommentItemListAdapter(getContext(), listPopupWindow,0));
                listPopupWindow.show();
            }
        });

        GridView picGridView = (GridView) view.findViewById(R.id.picsGridview);
        String path = mTalk.getImagePaths();
        if (path != null && !path.equals("")){
            picGridView.setAdapter(new ContentPicAdater(getContext(), PathConstant.ALUMNI_TALK_IMG_PATH, mTalk.getImagePaths().split(",")));
        }

        ListView commentListView = (ListView) view.findViewById(R.id.mListview);
        GridView commentHeadGridView = (GridView) LayoutInflater.from(getContext()).inflate(R.layout.dynamic_comment_head,commentListView,false);
        mPraiseHeadAdapter = new PraiseHeadAdapter(getContext(),PathConstant.ALUMNI_TALK_IMG_PATH,mRespPraises);
        commentHeadGridView.setAdapter(mPraiseHeadAdapter);
        commentHeadGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        commentListView.addHeaderView(commentHeadGridView);
        mPersonCommentAdapter = new PersonCommentAdapter(getContext(), mComments);
        commentListView.setAdapter(mPersonCommentAdapter);
        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOtherIndex = mComments.get(position - 1).getId();
                mContentEditText.setHint(Constant.REPLAY + ":" + mComments.get(position).getCommentAuthor().getAuthorName());
            }
        });

        commentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ContentComment comment = mComments.get(position -1 );
                mDeleteIndex = comment.getId();
                if (comment.getCommentAuthor().getAuthorId() == getHostActivity().userId()) {
                    String[] strings = {getString(R.string.delete)};
                    SchoolFriendDialog.listItemDialog(getContext(), strings).show();
                }
                return true;
            }
        });
        queryPraiseAndComment();

        final Button sendBn = (Button) view.findViewById(R.id.activity_school_friend_send_button);
        sendBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOtherIndex == 0) {
                    mRequestCommentJson = AlumniTalkService.saveComment(PersonCircleDetailFragment.this, view, mTalk.getId(), saveCommentCallback);
                    CommentUtil.closeSoftKeyWithComment(getContext(), view);
                    mContentEditText.setHint("");
                }else {
                    mRequestCommentJson = AlumniTalkService.saveOtherComment(PersonCircleDetailFragment.this, view, mOtherIndex, saveCommentCallback);
                    CommentUtil.closeSoftKeyWithComment(getContext(), view);
                    mContentEditText.setHint("");
                    mOtherIndex = 0;
                }
            }
        });
    }

    @Subscribe
    public void onMessageDeleteComment(MessageDeleteEvent deleteEvent) {
        mDeleteCommentJson = AlumniTalkService.deleteComment(this, mDeleteIndex, deleteCommentCallback);
        mDeleteIndex = 0;
    }

    public void inputEmotion(String text) {
        int selectionCursor = mContentEditText.getSelectionStart();
        mContentEditText.getText().insert(selectionCursor, text);
        mContentEditText.invalidate();
    }

}
