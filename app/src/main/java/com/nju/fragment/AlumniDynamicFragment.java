package com.nju.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nju.View.SchoolFriendDialog;
import com.nju.activity.CommentEvent;
import com.nju.activity.CommentOtherEvent;
import com.nju.activity.DeleteCommentEvent;
import com.nju.activity.MessageContentIdEvent;
import com.nju.activity.MessageDeleteEvent;
import com.nju.activity.MessageEvent;
import com.nju.activity.PersonInfoEvent;
import com.nju.activity.PraiseEvent;
import com.nju.activity.R;
import com.nju.adatper.AlumniTalkAdapter;
import com.nju.db.db.service.AlumniDynamicDbService;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniTalk;
import com.nju.model.AlumnicTalkPraise;
import com.nju.model.ContentComment;
import com.nju.service.AlumniTalkService;
import com.nju.service.CacheIntentService;
import com.nju.util.BottomToolBar;
import com.nju.util.CloseRequestUtil;
import com.nju.util.CommentUtil;
import com.nju.util.Constant;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.ListViewHead;
import com.nju.util.SchoolFriendGson;
import com.nju.util.SoftInput;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

public class AlumniDynamicFragment extends BaseFragment {
    public static final String TAG = AlumniDynamicFragment.class.getSimpleName();
    private ArrayList<AlumniTalk> mAlumniTalks = new ArrayList<>();
    private AlumniTalkAdapter mAlumniTalkAdapter;
    private PostRequestJson mRequestJson,mRequestPraiseJson,mRequestCommentJson
            ,getPraisesJson,getCommentsJson,deleteCommentJson,deleteContentJson;
    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mFootView;
    private View mainView;
    private ListView mListView;
    private EditText mContentEditText;
    private int mIndex;
    private int commentId = 0;
    private AtomicInteger dynamicId;
    private String mDegree = Constant.ALL;
    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(AlumniDynamicFragment.this)){
                ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
                mRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Log.e(TAG, error.getMessage());
                mFootView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(AlumniDynamicFragment.this)){
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody,AlumniTalk.class);
                    if (object != null){
                        ArrayList majorAsks = (ArrayList) object;
                        mAlumniTalks.clear();
                        if (majorAsks.size()>0){
                            for (Object obj :majorAsks){
                                AlumniTalk  alumniTalk = (AlumniTalk) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(alumniTalk));
                                if (!mAlumniTalks.contains(alumniTalk)){
                                    mAlumniTalks.add(alumniTalk);
                                }
                            }
                            Collections.sort(mAlumniTalks, new AlumniTalkSort());
                            int length = mAlumniTalks.size();
                            if (length>Constant.MAX_ROW){
                                for (int i = length-1;i>Constant.MAX_ROW;i--){
                                    mAlumniTalks.remove(mAlumniTalks.get(i));
                                }
                            }
                        }
                        mAlumniTalkAdapter.notifyDataSetChanged();
                    }
                    queryPraiseAndComment();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mRefreshLayout.setRefreshing(false);
                    mFootView.setVisibility(View.GONE);
                }
            }
        }
    };

    private ResponseCallback getPraiseCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.i(TAG,error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            if (FragmentUtil.isAttachedToActivity(AlumniDynamicFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody,AlumnicTalkPraise.class);
                    if (object != null) {
                        ArrayList praises = (ArrayList) object;
                        if (praises.size() > 0) {
                            for (Object obj:praises){
                                AlumnicTalkPraise talkPraise = (AlumnicTalkPraise) obj;
                                int contentId = talkPraise.getContentId();
                                for (AlumniTalk alumniTalk:mAlumniTalks){
                                    if (contentId == alumniTalk.getId()){
                                        ArrayList<AlumnicTalkPraise> talkPraises = alumniTalk.getTalkPraises();
                                        if (! talkPraises.contains(talkPraise)){
                                            alumniTalk.getTalkPraises().add(talkPraise);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private ResponseCallback getCommentCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.i(TAG, error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            if (FragmentUtil.isAttachedToActivity(AlumniDynamicFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody, ContentComment.class);
                    if (object != null) {
                        ArrayList comments = (ArrayList) object;
                        if (comments.size() > 0) {
                            for (AlumniTalk alumniTalk:mAlumniTalks){
                                alumniTalk.setComments(new ArrayList<ContentComment>());
                            }
                            for (Object obj : comments) {
                                ContentComment comment = (ContentComment) obj;
                                int contentId = comment.getAlumnicTalkId();
                                for (AlumniTalk alumniTalk:mAlumniTalks){
                                    if (contentId == alumniTalk.getId()){
                                        ArrayList<ContentComment> talkComments = alumniTalk.getComments();
                                        if (!talkComments.contains(comment)){
                                            alumniTalk.getComments().add(comment);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    mAlumniTalkAdapter.notifyDataSetChanged();
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
            ToastUtil.showShortText(getContext(), Constant.COMMENT_OK);
            queryPraiseAndComment();
        }
    };

    private ResponseCallback savePraiseCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG,error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            ToastUtil.showShortText(getContext(), Constant.PRAISE_OK);
            queryPraiseAndComment();
        }
    };

    private ResponseCallback deleteCommentCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG,error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            if (FragmentUtil.isAttachedToActivity(AlumniDynamicFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    String str = parseResponse.getInfo(responseBody);
                    if (str.equals(Constant.OK_MSG)){
                        queryPraiseAndComment();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private ResponseCallback deleteContentCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            Log.e(TAG,error.getMessage());
        }

        @Override
        public void onSuccess(String responseBody) {
            Log.i(TAG, responseBody);
            if (FragmentUtil.isAttachedToActivity(AlumniDynamicFragment.this)) {
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    String str = parseResponse.getInfo(responseBody);
                    if (str.equals(Constant.OK_MSG)){
                        int id = dynamicId.get();
                        for (AlumniTalk talk :mAlumniTalks){
                            if (id == talk.getId()){
                                mAlumniTalks.remove(talk);
                                mAlumniTalkAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void queryPraiseAndComment(){
        getCommentsJson = AlumniTalkService.queryComment(AlumniDynamicFragment.this,mAlumniTalks,getCommentCallback);
        getPraisesJson = AlumniTalkService.queryPraise(AlumniDynamicFragment.this,mAlumniTalks,getPraiseCallback);
    }

    public static AlumniDynamicFragment newInstance( ) {
        AlumniDynamicFragment fragment = new AlumniDynamicFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AlumniDynamicFragment() {
        // Required empty public constructor
        new ExeCacheTask(this).execute(mDegree);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alumni_dynamice, container, false);
        mainView = view;
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getBottom());
        initListView(view);
        setUpOnRefreshListener(view);
        BottomToolBar.show(this, view);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(R.string.alumn_circle);
        }
        getHostActivity().display(2);
    }

    @Override
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (mRequestJson != null)
            CloseRequestUtil.close(mRequestJson);
        if (mRequestPraiseJson != null)
            CloseRequestUtil.close(mRequestPraiseJson);
        if (mRequestCommentJson != null)
            CloseRequestUtil.close(mRequestCommentJson);
        if (getPraisesJson != null)
            CloseRequestUtil.close(getPraisesJson);
        if (getCommentsJson != null)
            CloseRequestUtil.close(getCommentsJson);
        if (deleteCommentJson != null)
            CloseRequestUtil.close(deleteCommentJson);
        if (deleteContentJson != null)
            CloseRequestUtil.close(deleteContentJson);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (mAlumniTalks != null && mAlumniTalks.size()>0){
            Intent intent = new Intent(getContext(),CacheIntentService.class);
            intent.putExtra(Constant.LABEL,Constant.ALUMNI_DYNAMIC);
            intent.putExtra(Constant.ALUMNI_DYNAMIC,mAlumniTalks);
            getContext().startService(intent);
        }
    }

    @Subscribe
    public void onMessagePraiseEvent(PraiseEvent event){
        mIndex = event.getId();
        mRequestPraiseJson = AlumniTalkService.savePraise(this,mAlumniTalks.get(mIndex).getId(),savePraiseCallback);
    }

    @Subscribe
    public void onMessageCommentEvent(final CommentEvent event){
        mIndex = event.getId();
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mListView.setSelection(event.getId());
            }
        });
        SoftInput.open(getContext());
        CommentUtil.getHideLayout(mainView).setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onMessagePersonEvent(PersonInfoEvent event){
        getHostActivity().open(CircleFragment.newInstance(event.getAuthorInfo()));
    }

    @Subscribe
    public void onMessageCommentOther(CommentOtherEvent event){
        ContentComment comment = event.getComment();
        commentId = comment.getId();
        mContentEditText.setHint("回复" + comment.getCommentAuthor().getAuthorName());
        SoftInput.open(getContext());
        CommentUtil.getHideLayout(mainView).setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onMessageDegree(MessageEvent event){
        mDegree = event.getMessage();
        mRequestJson = AlumniTalkService.queryAlumniTalks(AlumniDynamicFragment.this, callback,mDegree, Constant.PRE,0);
        //new ExeCacheTask(this).execute(event.getMessage());
    }
    @Subscribe
    public void onMessageDeleteComment(DeleteCommentEvent event){
        ContentComment comment = event.getComment();
        commentId = comment.getId();
        if (comment.getCommentAuthor().getAuthorId() == getHostActivity().userId()){
            String[] strings = {getString(R.string.delete)};
            SchoolFriendDialog.listItemDialog(getContext(),strings).show();
        }
    }
    @Subscribe
    public void onMessageDeleteComment(MessageDeleteEvent deleteEvent){
        deleteCommentJson = AlumniTalkService.deleteComment(this,commentId,deleteCommentCallback);
        commentId = 0;
    }
    @Subscribe
    public void onMessageDeleteContent(MessageContentIdEvent event){
        ToastUtil.showShortText(getContext(),"ID="+event.getId());
        dynamicId = new AtomicInteger();
        dynamicId.set(event.getId());
        deleteContentJson = AlumniTalkService.deleteDynamci(this,event.getId(),deleteContentCallback);
    }
    private void initListView(final View view){
        mContentEditText = CommentUtil.getCommentEdit(this,view);
        mListView = (ListView) view.findViewById(R.id.listView);
        ListViewHead.setUp(this,view,mListView);
        mFootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_footer, mListView, false);
        mFootView.setVisibility(View.GONE);
        mListView.addFooterView(mFootView);
        mAlumniTalkAdapter = new AlumniTalkAdapter(this,mAlumniTalks);
        mListView.setAdapter(mAlumniTalkAdapter);
        ImageView mCameraImageView = getHostActivity().getMenuCameraView();
        mCameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(MultiChoosePicFragment.newInstance(PublishDynamicFragment.TAG));
            }
        });

        final Button sendBn = (Button) view.findViewById(R.id.activity_school_friend_send_button);
        sendBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentId != 0) {
                    mRequestCommentJson = AlumniTalkService.saveOtherComment(AlumniDynamicFragment.this, view, commentId, saveCommentCallback);
                    CommentUtil.closeSoftKey(getContext(), view);
                    mContentEditText.setHint("");
                    commentId = 0;
                } else {
                    mRequestCommentJson = AlumniTalkService.saveComment(AlumniDynamicFragment.this, view, mAlumniTalks.get(mIndex).getId(), saveCommentCallback);
                    CommentUtil.closeSoftKey(getContext(), view);
                    mIndex = 0;
                }

            }
        });
    }

    private void setUpOnRefreshListener(View view){
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                mRequestJson = AlumniTalkService.queryAlumniTalks(AlumniDynamicFragment.this, callback, mDegree, Constant.PRE,0);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestJson = AlumniTalkService.queryAlumniTalks(AlumniDynamicFragment.this, callback,mDegree, Constant.PRE,0);

            }
        });
    }

    public void inputEmotion(String text) {
        int selectionCursor = mContentEditText.getSelectionStart();
        mContentEditText.getText().insert(selectionCursor, text);
        mContentEditText.invalidate();
    }

    private static class AlumniTalkSort implements Comparator<AlumniTalk> {
        @Override
        public int compare(AlumniTalk lhs, AlumniTalk rhs) {
            final int lhsId = lhs.getId();
            final int rhsId = rhs.getId();
            if (lhsId > rhsId) {
                return -1;
            } else if (lhsId < rhsId) {
                return 1;
            }
            return 0;
        }
    }

    private static class ExeCacheTask extends AsyncTask<String,Void,ArrayList<AlumniTalk>>
    {
        private final WeakReference<AlumniDynamicFragment> mAlumniDynamicWeakRef;
        private String degree;
        public ExeCacheTask(AlumniDynamicFragment  alumniDynamicFragment){
            this.mAlumniDynamicWeakRef = new WeakReference<>(alumniDynamicFragment);
        }
        @Override
        protected ArrayList<AlumniTalk> doInBackground(String... params) {
            AlumniDynamicFragment alumniDynamicFragment = mAlumniDynamicWeakRef.get();
            degree = params[0];
            if (alumniDynamicFragment != null){
                switch (degree) {
                    case Constant.ALL:
                        return new AlumniDynamicDbService(alumniDynamicFragment.getContext()).getAlumniDynamics(Constant.ALL);
                    case Constant.UNDERGRADUATE:
                        return new AlumniDynamicDbService(alumniDynamicFragment.getContext()).getAlumniDynamics(Constant.UNDERGRADUATE);
                    case Constant.MASTER:
                        return new AlumniDynamicDbService(alumniDynamicFragment.getContext()).getAlumniDynamics(Constant.MASTER);
                    case Constant.DOCTOR:
                        return new AlumniDynamicDbService(alumniDynamicFragment.getContext()).getAlumniDynamics(Constant.DOCTOR);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<AlumniTalk> alumniTalks) {
            super.onPostExecute(alumniTalks);
            AlumniDynamicFragment alumniDynamicFragment = mAlumniDynamicWeakRef.get();
            int preId = 0;
            if (alumniDynamicFragment!=null){
                if (alumniTalks != null && alumniTalks.size()>0){
                    Log.i(TAG, SchoolFriendGson.newInstance().toJson(alumniTalks));
                    Collections.sort(alumniTalks, new AlumniTalkSort());
                    ArrayList<AlumniTalk> source = alumniDynamicFragment.mAlumniTalks;
                    source.clear();
                    source.addAll(alumniTalks);
                    alumniDynamicFragment.mAlumniTalkAdapter.notifyDataSetChanged();
                }
              }
            }
        }
    }

