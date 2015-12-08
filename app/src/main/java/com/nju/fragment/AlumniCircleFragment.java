package com.nju.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nju.activity.R;
import com.nju.adatper.FriendContentAdapter;
import com.nju.model.Comment;
import com.nju.model.FriendWeibo;
import com.nju.test.WeiBoData;
import com.nju.util.Divice;
import com.nju.util.SchoolFriendLayoutParams;
import com.nju.util.SoftInput;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.nju.adatper.UserCommentItemListAdapter.COMMENT_OK;
import static com.nju.adatper.UserCommentItemListAdapter.PRAISE_OK;

public class AlumniCircleFragment extends BaseFragment {

    public static final String TAG = AlumniCircleFragment.class.getSimpleName();
    private ListView mListView;
    private EditText mCommentEditText;
    private Button mSendButton;
    private List<FriendWeibo> weibos;
    private List<FriendWeibo> sumWeiBos;
    private FriendContentAdapter mFriendContentAdapter;
    private LinearLayout mMainLayout;
    private RelativeLayout mInputLayout;
    private SchoolFriendLayoutParams schoolFriendLayoutParams;
    private ImageView mCameraImageView;
    private AppBarLayout mAppBarLayout;
    private boolean label = false;
    private int mPosition = 1;

    private Handler mHandler = new MyHandler(this);
    public static AlumniCircleFragment newInstance() {
        return new AlumniCircleFragment();
    }

    public AlumniCircleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppBarLayout = (AppBarLayout) getActivity().findViewById(R.id.main_viewpager_appbar);
        mCameraImageView = (ImageView) getActivity().findViewById(R.id.main_viewpager_camera_imageView);
        mCameraImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHostActivity().getToolBar().setTitle(getString(R.string.alumn_circle));
        getHostActivity().getMenuCameraView().setVisibility(View.VISIBLE);
        getHostActivity().getMenuDeleteView().setVisibility(View.GONE);
        getHostActivity().getMenuBn().setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_alumni_circle, container, false);
        mListView = (ListView)view.findViewById(R.id.school_friend_listview);
        mMainLayout = (LinearLayout)view.findViewById(R.id.activity_school_friend_main);
        mInputLayout = (RelativeLayout)view.findViewById(R.id.activity_school_friend_input_layout);
        mCommentEditText = (EditText)view.findViewById(R.id.activity_school_friend_comment_edittext);
        mSendButton = (Button)view.findViewById(R.id.activity_school_friend_send_button);
        schoolFriendLayoutParams = new SchoolFriendLayoutParams(getActivity());
        weibos = WeiBoData.weiBos(getActivity());
        sumWeiBos = WeiBoData.weiBos(getActivity());
        mFriendContentAdapter = new FriendContentAdapter(weibos,getActivity(),mHandler,mListView);
        mListView.setAdapter(mFriendContentAdapter);
        mListView.setPadding(mListView.getPaddingLeft(),Divice.getStatusBarHeight(getActivity()),mListView.getPaddingRight(),mListView.getPaddingBottom());
        initOnGlobalListener();
        initSendListener();
        initChooseImage();
        return view;
    }

    private void initOnGlobalListener(){
        mMainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int rootHeight = mMainLayout.getRootView().getHeight();
                int subHeight = mMainLayout.getHeight();
                if ((rootHeight - subHeight) < (rootHeight / 3)) {
                    if (Divice.isPhone()) {
                        mListView.setLayoutParams(schoolFriendLayoutParams.noSoftInputParams(subHeight));
                        mListView.setTranscriptMode(ListView.ITEM_VIEW_TYPE_IGNORE);
                    } else {
                        mListView.setLayoutParams(schoolFriendLayoutParams.noSoftInputParams(subHeight));
                    }
                    mInputLayout.setVisibility(View.GONE);
                    if (mInputLayout.getVisibility() == View.GONE && label) {
                        label = false;
                        for (int i = mPosition + 1; i < sumWeiBos.size(); i++) {
                            weibos.add(sumWeiBos.get(i));
                        }
                        mFriendContentAdapter.notifyDataSetChanged();
                        mListView.setTranscriptMode(ListView.ITEM_VIEW_TYPE_IGNORE);
                    }
                } else {
                    if (Divice.isPhone()) {
                        mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                        mListView.setLayoutParams(schoolFriendLayoutParams.softInputParams(subHeight, 60));
                    } else {
                        mListView.setLayoutParams(schoolFriendLayoutParams.softInputParams(subHeight, 80));
                    }
                    mInputLayout.setVisibility(View.VISIBLE);
                    mCommentEditText.requestFocus();
                    if (mInputLayout.getVisibility() == View.VISIBLE) {
                        label = true;
                    }
                }
            }
        });
    }

    private void initSendListener(){
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mCommentEditText.getText().toString();
                Comment comment = new Comment();
                comment.setContent(text);
                comment.setcUserName("ZhangXiaojun");
                weibos.get(mPosition).getComments().add(comment);
                mCommentEditText.setText("");
                SoftInput.close(getActivity(), mSendButton);
            }
        });
    }

    private void initChooseImage() {
        mCameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHostActivity().open(MultiChoosedPicFragment.newInstance());
            }
        });
        mCameraImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getHostActivity().open(PublishTextFragment.newInstance());
                return true;
            }
        });
    }

    private static class MyHandler extends  Handler {

        private final WeakReference<AlumniCircleFragment> mAlumniCircleFragment;

        private MyHandler(AlumniCircleFragment alumniCircleFragment) {
            this.mAlumniCircleFragment = new WeakReference<>(alumniCircleFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            AlumniCircleFragment fragment = mAlumniCircleFragment.get();
            if (fragment != null) {
                super.handleMessage(msg);
                if(msg.what == PRAISE_OK) {
                    fragment.mFriendContentAdapter.notifyDataSetChanged();
                }
                if(msg.what == COMMENT_OK) {
                    fragment.mFriendContentAdapter.notifyDataSetChanged();
                    fragment.mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    SoftInput.open(fragment.getActivity());
                    fragment.mPosition = (int) msg.obj;
                }
            }
        }
    }

}
