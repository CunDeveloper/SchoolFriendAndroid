package com.nju.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.adatper.CommentAdapter;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.CommentParam;
import com.nju.http.request.IdParam;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.http.response.QueryJson;
import com.nju.model.ContentComment;
import com.nju.model.RecommendWork;
import com.nju.test.TestData;
import com.nju.util.CommentUtil;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.PathConstant;
import com.nju.util.SchoolFriendGson;
import com.nju.util.SoftInput;
import com.nju.util.StringBase64;
import com.nju.util.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RecommendWorkItemDetailFragment extends BaseFragment {

    private static final String PARAM_KEY = "key";
    public static final String TAG = RecommendWorkItemDetailFragment.class.getSimpleName() ;
    private static RecommendWork mRecommendWork;
    private EditText mContentEditText ;
    private TextView mEmailTV;
    private PostRequestJson mRequestJson,mRequestQueryJson;
    private static final String TEXT_TYPE = "text/plain";
    private ArrayList<ContentComment> mContentComments;
    private CommentAdapter mCommentAdapter;
    private ResponseCallback saveCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(RecommendWorkItemDetailFragment.this)){
                Log.e(TAG,error.getMessage());
            }
        }
        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(RecommendWorkItemDetailFragment.this)){
                Log.i(TAG, responseBody);
                ToastUtil.showShortText(getContext(), getString(R.string.comment_ok));
                queryComment();
            }
        }

    };

    private ResponseCallback queryCommentCallback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(RecommendWorkItemDetailFragment.this)){
                Log.e(TAG, error.getMessage());

            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(RecommendWorkItemDetailFragment.this)){
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCommentAdapter.notifyDataSetChanged();
            }
        }
    };

    private void queryComment(){
        ArrayList<IdParam> idParams = new ArrayList<>();
        IdParam idParam = new IdParam();
        Log.i(TAG,mRecommendWork.getId()+"");
        idParam.setId(mRecommendWork.getId());
        idParams.add(idParam);
        final String json = QueryJson.queryCommentToString(this,idParams);
        String url = PathConstant.BASE_URL+PathConstant.RECOMMEND_WORK_PATH+PathConstant.RECOMMED_WORK_SUB_PATH_GET_ASKS;
        mRequestQueryJson = new PostRequestJson(url,json,queryCommentCallback);
        Log.e(TAG, url);
        Log.i(TAG,json);
        HttpManager.getInstance().exeRequest(mRequestQueryJson);
    }

    private void saveAsk(View view){
        EditText contentET = (EditText) view.findViewById(R.id.comment_edittext);
        CommentParam param = new CommentParam();
        Log.i(TAG,contentET.getText().toString());
        param.setContent(StringBase64.encode(contentET.getText().toString()));
        param.setContentId(mRecommendWork.getId());
        final String json = QueryJson.commentAuthorToString(this,param);
        String url = PathConstant.BASE_URL+PathConstant.RECOMMEND_WORK_PATH+PathConstant.RECOMMEND_WORK_SUB_PATH_ASK;
        mRequestJson = new PostRequestJson(url,json,saveCallback);
        Log.e(TAG,url);
        HttpManager.getInstance().exeRequest(mRequestJson);
    }

    public static RecommendWorkItemDetailFragment newInstance(RecommendWork recommendWork) {
        RecommendWorkItemDetailFragment fragment = new RecommendWorkItemDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_KEY,recommendWork);
        fragment.setArguments(args);
        return fragment;
    }

    public RecommendWorkItemDetailFragment() {
        // Required empty public constructor
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
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getHostActivity().display(6);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommend_work_item_detail, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initView(view);
        mContentEditText = CommentUtil.getCommentEdit(view);
        findJobClick(view);
        askJob(view);
        CommentUtil.hideSoft(getContext(), view);
        CommentUtil.initViewPager(this, view);
        CommentUtil.addViewPageEvent(getContext(), view);
        queryComment();
        return view;
    }

    private void initView(final View view){
        TextView titleTv = (TextView) view.findViewById(R.id.recommend_work_item_detail_title);
        try{
            titleTv.setText(StringBase64.decode(mRecommendWork.getTitle()));
        }catch (IllegalArgumentException e){
            titleTv.setText(Constant.UNKNOWN_CHARACTER);
        }
        TextView detailTv = (TextView) view.findViewById(R.id.recommend_work_item_detail_detail);
        try{
            detailTv.setText(StringBase64.decode(mRecommendWork.getContent()));
        }catch (IllegalArgumentException e){
            detailTv.setText(Constant.UNKNOWN_CHARACTER);
        }
        TextView dateTv = (TextView) view.findViewById(R.id.date_tv);
        dateTv.setText(DateUtil.getRelativeTimeSpanString(mRecommendWork.getDate()));
        TextView nameTV = (TextView) view.findViewById(R.id.name_tv);
        nameTV.setText(mRecommendWork.getAuthor().getAuthorName());
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
        TextView collectTV = (TextView) view.findViewById(R.id.collect_tv);
        collectTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.ShowText(getContext(), getString(R.string.collect_ok));
            }
        });

        mContentComments = TestData.getComments();
        mCommentAdapter = new CommentAdapter(getContext(), mContentComments);
        ListView hotListView = (ListView) view.findViewById(R.id.hot_listview);
        hotListView.setAdapter(mCommentAdapter);
        ListView newListView = (ListView) view.findViewById(R.id.new_comment_listview);
        newListView.setAdapter(mCommentAdapter);
        Button sendBn = (Button) view.findViewById(R.id.activity_school_friend_send_button);
        sendBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAsk(view);
                CommentUtil.closeSoftKey(getContext(),view);
            }
        });
    }

    public void inputEmotion(String text) {
        int selectionCursor = mContentEditText.getSelectionStart();
        mContentEditText.getText().insert(selectionCursor, text);
        mContentEditText.invalidate();
    }

    private void findJobClick(View view){
        TextView textView = (TextView) view.findViewById(R.id.re_work_item_yinpin_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void sendEmail(){
        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        intent.setType(TEXT_TYPE);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
        intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
        intent.setData(Uri.parse("mailto:"+mEmailTV.getText())); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }

    private void askJob(final View view ){
        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.mScrollView);
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.new_comment_layout);
        TextView textView = (TextView) view.findViewById(R.id.re_work_item_ask_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.getHideLayout(view).setVisibility(View.VISIBLE);
                SoftInput.open(getContext());
                scrollView.scrollTo(0, linearLayout.getBottom());
            }
        });
    }
}
