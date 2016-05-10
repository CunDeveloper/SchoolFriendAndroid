package com.nju.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.adatper.UserCommentItemListAdapter;
import com.nju.event.CommentEvent;
import com.nju.event.PraiseEvent;
import com.nju.http.ImageDownloader;
import com.nju.model.AlumniTalk;
import com.nju.model.AuthorInfo;
import com.nju.util.CommentPopupWindow;
import com.nju.util.CommentUtil;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.PathConstant;
import com.nju.util.StringBase64;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PersonCircleDetailFragment extends BaseFragment {
    public static final String TAG = PersonCircleDetailFragment.class.getSimpleName();
    private static final String TALK_PARA = "talk_para";
    private AlumniTalk mTalk;
    private EditText mContentEditText;

    public PersonCircleDetailFragment() {
        // Required empty public constructor
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
    }

    @Subscribe
    public void onMessagePraiseEvent(PraiseEvent event){
        Log.i(TAG, "event:" + event.getId());
    }

    @Subscribe
    public void onMessageCommentEvent(CommentEvent event){
        Log.i(TAG,"event:" + event.getId());
    }

    private void initView(View view) {
        TextView contentTV = (TextView) view.findViewById(R.id.school_friend_item_content);
        TextView nameTV = (TextView) view.findViewById(R.id.school_friend_item_name_text);
        TextView labelTV = (TextView) view.findViewById(R.id.school_friend_item_label_text);
        TextView dateTV = (TextView) view.findViewById(R.id.school_friend_item_publish_date);
        ImageView headImg = (ImageView) view.findViewById(R.id.headIconImg);
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
            }
            dateTV.setText(DateUtil.getRelativeTimeSpanString(mTalk.getDate()));
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

    }

    public void inputEmotion(String text) {
        int selectionCursor = mContentEditText.getSelectionStart();
        mContentEditText.getText().insert(selectionCursor, text);
        mContentEditText.invalidate();
    }

}
